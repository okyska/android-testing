# Espresso steps
## 1. Configure Espresso dependencies
* Add repository and dependencies as described on
 https://developer.android.com/training/testing/espresso/setup.html
 https://developer.android.com/topic/libraries/testing-support-library/packages.html
* Force versions used in the app with `resolutionStrategy`
See:
 https://sites.google.com/a/android.com/tools/tech-docs/new-build-system/user-guide#TOC-Resolving-conflicts-between-main-and-test-APK
 https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html

* Define and use separate `espresso` build type based on debug (`testBuildType`, `buildTypes`)
* Switch to `espresso` build variant

## 2. Basic Espresso test
* Implement basic test with @RunWith, @Test, ActivityTestRule, basic actions and checks

## 3. Use string from resources
* Instrumented tests have access to AUT resources and objects
* InstrumentationRegistry has two similarly named methods:
  - getTargetContext() - returns the AUT context - use it operate on the app and its resources
  - getContext() - return the context of instrumentation package - use it access resources in test APK

## 4. Refactor duplicate code and introduce back button
* Move commonly used code to methods to avoid copy-paste and ease maintanance
* Caveats with Espress.pressBack():
  * Close soft keyboard before clicking it to make sure back button click is applied to an activity or fragment
  * Back button click on main activity will throw an exception (and AUT may be terminated at this time)

## 5. Introduce pressKey with complex keys
* Show that pressKey can be used both with simple keycode (KEYCODE_*) and complex keys via EspressKey.Builder class

## *. Access objects from the app
* Accessing objects from the app can be useful to reset AUT state

## *. Testcases for practice

===== Day 2
## 1. onData to select interests at the end of the list
* onData allows to select interests at the end of the list without failure
(ex. Football and Astronomy will failure with old implementation)

# Android Testing

This project is a fake Android app that is intended to be used in workshop training to learn Android testing practices.

The Android app is based on the concept of a personal financial tracking/analysis app.  The app supports the following user journeys:

 * Login
 * Signup - personal info, selecting personal interests, and selecting credentials
 * Accounts Overview - Shows transaction lists for each of your credit cards
 * Linked Accounts - Shows a list of all bank accounts you've linked to the app
 * Link Account - Links a bank account to the app (that account will then appear in the accounts overview)
 * Credit Cards - Shows aggregate credit information across all of your linked cards
 * Analysis - Shows the amount you spend per category across all of your linked cards

The project comes with a number of pre-written tests.  These tests are mostly Espresso integration tests, but some unit tests are included as well.

## User Journey Screenshots

![User Journey Screenshots](/docs/screenshots/user-journeys.jpg)
