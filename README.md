# Espresso steps
## 1. Add Espresso
* Add repository and dependencies as described on
 https://developer.android.com/training/testing/espresso/setup.html
 https://developer.android.com/topic/libraries/testing-support-library/packages.html
* Force versions used in the app with `resolutionStrategy`
See:
 https://sites.google.com/a/android.com/tools/tech-docs/new-build-system/user-guide#TOC-Resolving-conflicts-between-main-and-test-APK
 https://docs.gradle.org/current/dsl/org.gradle.api.artifacts.ResolutionStrategy.html

* Define and use separate `espresso` build type based on debug (`testBuildType`, `buildTypes`)

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
