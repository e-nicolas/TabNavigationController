package io.github.enicolas.tabnavigationcontrollerexample

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.widget.FrameLayout
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController

/**
 * Class inspired on this example:
 * https://github.com/android/architecture-components-samples/tree/master/NavigationAdvancedSample
 */
class TabNavigationController @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

	companion object {
		private const val KEY_TAB_HISTORY = "key_tab_history"
	}

	/**
	 * Private properties
	 */
	private var currentTabIndex: Int = 0
	private var tabHistory = TabControllerHistory()
	private var fragmentManager: FragmentManager? = null
	private var navHostList: ArrayList<NavigationModel> = arrayListOf()
	private var toolbar: Toolbar? = null

	val appBarConfiguration by lazy {
		AppBarConfiguration(
			topLevelDestinationIds = navHostList.map { it.startDestination }.toSet(),
			fallbackOnNavigateUpListener = ::onSupportNavigateUp
		)
	}

	/**
	 * Public properties
	 */
	var delegate: TabNavigationControllerDelegate? = null

	val currentController: NavController?
	get() {
		return navHostList.getOrNull(currentTabIndex)?.navHost?.navController
	}

	val isRootView: Boolean
	get() {
		return currentController?.currentDestination == null ||
				currentController?.currentDestination?.id == navHostList[currentTabIndex].startDestination
	}

	/**
	 * Create all the navHosts for each tab
	 * @param fragmentManager: The fragment manager to attach all [NavHostFragment]
	 * @param startDestinations: A pair to build each tab. First is the graph navigation id and second is the startDestination fragment id
	 * @param startTabIndex: The initial tab index
	 */
	fun setupTabs(fragmentManager: FragmentManager, startDestinations: List<Pair<Int, Int>>, startTabIndex: Int = 0) {
		this.fragmentManager = fragmentManager
		this.currentTabIndex = startTabIndex

		startDestinations.forEachIndexed { index, (navigationKey, startDestinationValue) ->
			val hostFragment = createNavHost(navigationKey, startDestinationValue, index)
			if(index != startTabIndex) {
				detachNavHostFragment(fragmentManager, hostFragment)
			} else {
				attachNavHostFragment(fragmentManager, hostFragment)
			}
		}
		tabHistory.push(startTabIndex)
		updateNavController()
	}

	/**
	 * Create a navigation host with navController
	 * @param navigationId: id of navigation
	 * @param startDestinationValue: the id of the first fragment
	 * @param index: the index of tab
	 */
	private fun createNavHost(@NavigationRes navigationId: Int, @IdRes startDestinationValue: Int, index: Int): NavHostFragment {
		val fragmentTag = "bottomNav$index"

		// If the Nav Host fragment exists, return it
		(fragmentManager?.findFragmentByTag(fragmentTag) as? NavHostFragment)?.let { existingFragment ->
			navHostList.add(
				NavigationModel(
					navHost = existingFragment,
					tag = fragmentTag,
					startDestination = startDestinationValue,
					navigationGraphId = navigationId
				)
			)
			return existingFragment
		}

		val hostFragment = NavHostFragment.create(navigationId)
		navHostList.add(
			NavigationModel(
				navHost = hostFragment,
				tag = fragmentTag,
				startDestination = startDestinationValue,
				navigationGraphId = navigationId
			)
		)

		fragmentManager?.beginTransaction()
			?.add(this.id, hostFragment, fragmentTag)
			?.commitNow()

		hostFragment.navController.apply {
			graph = navInflater.inflate(navigationId).apply {
				startDestination = startDestinationValue
			}
			addOnDestinationChangedListener { controller, destination, arguments ->
				delegate?.onDestinationChange(this@TabNavigationController, controller, destination, arguments)
			}
		}

		return hostFragment
	}

	/**
	 * Setup a toolbar to work with the current navigation controller
	 * @param toolbar - The [Toolbar] to be attached
	 */
	fun setupToolbar(toolbar: Toolbar) {
		this.toolbar = toolbar
		setupToolbarWithConfiguration()
	}

	private fun setupToolbarWithConfiguration() {
		currentController?.let {
			toolbar?.setupWithNavController(it, appBarConfiguration)
		}
	}

	/**
	 * Switch tab
	 * @param tabIndex: The index to be changed
	 * @param addToHistory: Add index to tab history
	 */
	fun switchTab(tabIndex: Int, addToHistory: Boolean = true) {
		if(currentTabIndex != tabIndex) {
			currentTabIndex = tabIndex
			updateNavController()
			setupToolbarWithConfiguration()

			if (addToHistory) { tabHistory.push(tabIndex) }
			delegate?.onChangeTab(this, currentTabIndex, currentController?.currentDestination)
		}
	}

	/**
	 * Attach the new nav host and detach the old ones
	 */
	private fun updateNavController() {
		fragmentManager?.let { fragmentManager ->
			if (fragmentManager.isStateSaved) return@let

			val firstFragment = navHostList[0]
			val newFragment = navHostList[currentTabIndex]

			// Pop everything above the first fragment (the "fixed start destination")
			fragmentManager.popBackStack(firstFragment.tag, FragmentManager.POP_BACK_STACK_INCLUSIVE)

			// Exclude the first fragment tag because it's always in the back stack.
			if(firstFragment.tag != newFragment.tag) {
				fragmentManager.beginTransaction()
					.attach(newFragment.navHost)
					.setPrimaryNavigationFragment(newFragment.navHost)
					.apply {
						// Detach all other Fragments
						navHostList.forEach {
							if (it.tag != newFragment.tag) {
								detach(firstFragment.navHost)
							}
						}
					}
					.addToBackStack(firstFragment.tag)
					.setReorderingAllowed(true)
					.commit()
			}
		}
	}

	fun onSaveInstanceState(outState: Bundle?) {
		outState?.putSerializable(KEY_TAB_HISTORY, tabHistory)
	}

	fun onRestoreInstanceState(savedInstanceState: Bundle?, restoredTabIndex: Int? = null) {
		savedInstanceState?.let {
			tabHistory = it.getSerializable(KEY_TAB_HISTORY) as TabControllerHistory
		}
		restoredTabIndex?.let { index ->
			switchTab(index, false)
		}
	}

	fun onSupportNavigateUp(): Boolean {
		return currentController?.navigateUp() ?: true
	}

	/**
	 * Clear the stack from the current index
	 */
	fun clearStack() {
		val rootFragment = navHostList[currentTabIndex].startDestination
		currentController?.popBackStack(rootFragment, false)
	}

	/**
	 * If is not the root view, switch the tab based on it's history
	 * if clear all backstack return true
	 */
	fun onBackPressed(): Boolean {
		currentController?.let {
			if (isRootView) {
				if (tabHistory.size > 1) {
					val tabIndex = tabHistory.popPrevious()
					switchTab(tabIndex, false)
				} else {
					return true
				}
			} else {
				it.popBackStack()
			}
			return false
		} ?: run {
			return true
		}
	}

	/**
	 * Detach [NavHostFragment] from [FragmentManager]
	 */
	private fun detachNavHostFragment(
		fragmentManager: FragmentManager,
		navHostFragment: NavHostFragment
	) {
		fragmentManager.beginTransaction()
			.detach(navHostFragment)
			.commitNow()
	}

	/**
	 * Attach [NavHostFragment] from [FragmentManager] and set it to "app:defaultNavHost=true"
	 */
	private fun attachNavHostFragment(
		fragmentManager: FragmentManager,
		navHostFragment: NavHostFragment
	) {
		fragmentManager.beginTransaction()
			.attach(navHostFragment)
			.setPrimaryNavigationFragment(navHostFragment)
			.commitNow()
	}
}
