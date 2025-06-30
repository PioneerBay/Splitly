# Splitly

Splitly is a Kotlin Multiplatform application that helps users manage shared expenses with friends and family in a simple and convenient way. Rather than transferring real money back and forth each time, users can track payments virtually and only pay the outstanding balance when needed.

The app is built using Jetpack Compose Multiplatform, enabling a shared codebase across Android, iOS, and desktop. This ensures a consistent experience while simplifying development across platforms.

## Running the App
```cmd
git clone https://github.com/PioneerBay/Splitly.git
./gradlew runDistributable
```

## Technical Details

* `/composeApp` is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
  - `commonMain` is for code that’s common for all targets.
  - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
    For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
    `iosMain` would be the right folder for such calls.

* `/iosApp` contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform, 
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.


Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)…
