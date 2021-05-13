package io.github.enicolas.tabnavigationcontrollerexample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.setupActionBarWithNavController
import io.github.enicolas.tabnavigationcontrollerexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val tabList = listOf(
        R.id.navigation_home,
        R.id.navigation_dashboard,
        R.id.navigation_notifications
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupTabNavigation()
        setupBottomNavigation()
        setupActionBar()
    }

    private fun setupTabNavigation() {
        binding.tabNavigationController.setupTabs(supportFragmentManager, listOf(
            R.navigation.mobile_navigation to R.id.navigation_home,
            R.navigation.mobile_navigation to R.id.navigation_dashboard,
            R.navigation.mobile_navigation to R.id.navigation_notifications,
        ))
        binding.tabNavigationController.delegate = tabNavigationDelegate
    }

    private fun setupBottomNavigation() {
        binding.navView.setOnNavigationItemSelectedListener {
            val index = tabList.indexOf(it.itemId)
            binding.tabNavigationController.switchTab(index)
            true
        }

        binding.navView.setOnNavigationItemReselectedListener {
            binding.tabNavigationController.clearStack()
        }
    }

    private fun setupActionBar() {
        binding.tabNavigationController.currentController?.let {
            setupActionBarWithNavController(it, binding.tabNavigationController.appBarConfiguration)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return binding.tabNavigationController.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if(binding.tabNavigationController.onBackPressed()) {
            moveTaskToBack(true)
        }
    }

    /**
     * Tab Navigation Controller delegate
     */
    private val tabNavigationDelegate = object : TabNavigationControllerDelegate {
        override fun onChangeTab(
            navigation: TabNavigationController,
            tabIndex: Int,
            destination: NavDestination?
        ) {
            setupActionBar()
            tabList.getOrNull(tabIndex)?.let { tabId ->
                binding.navView.menu.findItem(tabId)?.isChecked = true
            }
        }

        override fun onDestinationChange(
            navigation: TabNavigationController,
            controller: NavController,
            destination: NavDestination,
            bundle: Bundle?
        ) {

        }
    }
}