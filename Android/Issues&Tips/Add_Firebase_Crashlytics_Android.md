# Add Firebase Crashlitics to Android project

[Firebase Android Release Notes](https://firebase.google.com/support/release-notes/android)

### Project level build.gradle
```
buildscript {
    repositories {
        ...
        maven { url 'https://maven.fabric.io/public'}
        ...

    }
    dependencies {
        ...
        classpath 'io.fabric.tools:gradle:1.31.0'
        ...
    }
}

allprojects {
    repositories {
        ...
        google()
    }
}

```

### App level build.gradle
```
apply plugin: 'io.fabric'

dependencies {
    implementation 'com.crashlytics.sdk.android:crashlytics:2.10.1'
}

```
