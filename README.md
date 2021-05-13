# TabNavigationController

TabNavigationController to manage multiples backstacks using BottomNavigation

## Preview
![Preview](https://media.giphy.com/media/SK7odxK3RxoMj1ar0M/giphy.gif)

## Installation

#### Gradle
Step 1. Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Step 2. Add the dependency
```groovy
'com.github.e-nicolas:TabNavigationController:v.1.0.0'
```

#### Manual
1. Download the project and copy TabNavigationController directory to your project
2. Add the following line to your app's gradle:

```groovy
implementation project(':genericadapter')
```

# Usage

### Setup tabs
```kotlin
binding.tabNavigationController.setupTabs(
                fragmentManager = supportFragmentManager,
                startDestinations = listOf(
                        R.navigation.mobile_navigation to R.id.navigation_home,
                        R.navigation.mobile_navigation to R.id.navigation_dashboard,
                        R.navigation.mobile_navigation to R.id.navigation_notifications,
                ))
```
### Delegate
```kotlin
 private val tabNavigationDelegate = object : TabNavigationControllerDelegate {
      override fun onChangeTab(
          navigation: TabNavigationController,
          tabIndex: Int,
          destination: NavDestination?
      ) {
          // Update Toolbar titles and BottomNavigation selected item
      }

      override fun onDestinationChange(
          navigation: TabNavigationController,
          controller: NavController,
          destination: NavDestination,
          bundle: Bundle?
      ) {
        // Update Toolbar titles and BottomNavigation selected item
      }
  }
```
## Contributions
Please contribute! I will gladly review any pull requests.

## License

```
Copyright 2021 Emmanouil Nicolas.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
