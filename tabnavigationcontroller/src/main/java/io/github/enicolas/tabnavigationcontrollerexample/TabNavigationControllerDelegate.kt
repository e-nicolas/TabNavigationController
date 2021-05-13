package io.github.enicolas.tabnavigationcontrollerexample

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavDestination

interface TabNavigationControllerDelegate {
	/**
	 * When the tab was changed
	 * @param navigation: [TabNavigationController]
	 * @param tabIndex: the new tabIndex
	 * @param destination: the new [NavDestination]
	 */
	fun onChangeTab(navigation: TabNavigationController, tabIndex: Int, destination: NavDestination?)

	/**
	 * When the navController navigates
	 * @param navigation: [TabNavigationController]
	 * @param controller: the current [NavController]
	 * @param destination: the new [NavDestination]
	 */
	fun onDestinationChange(navigation: TabNavigationController, controller: NavController, destination: NavDestination, bundle: Bundle?)
}
