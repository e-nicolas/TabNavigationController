package io.github.enicolas.tabnavigationcontrollerexample

import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.navigation.fragment.NavHostFragment

internal data class NavigationModel(
	val navHost: NavHostFragment,
	val tag: String,
	@IdRes val startDestination: Int,
	@NavigationRes val navigationGraphId: Int
)
