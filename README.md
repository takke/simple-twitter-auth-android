**本リポジトリは [Twitter Kit for Android](https://dev.twitter.com/twitterkit/android/overview)
 を TwitPane 用に認証部分に特化したものです。ご利用はおすすめしません。**

**Twitter4J を利用するように変更し、Retrofit, Gson への依存を除去しています**


# SimpleTwitterAuth for Android

SimpleTwitterAuth is a simple SSO wrapper for Twitter login authentication
based on [Twitter Kit for Android](https://github.com/twitter-archive/twitter-kit-android)

- If installed Twitter app, use Twitter app SSO (like TwitterKit)
- If not installed Twitter app, use in app browser for authentication.


## Features

* Log in with Twitter

## Getting Started

* Generate your Twitter API keys through the [Twitter developer apps dashboard](https://apps.twitter.com/).
* Install Twitter Kit using instructions below.
* For extensive documentation, please see the [wiki](https://github.com/twitter/twitter-kit-android/wiki).

### Install using JitPack

Add twitter dependency to your build.gradle:
```groovy

repositories {
    maven {
        url 'https://jitpack.io'
        content {
            includeGroup "com.github.takke"
        }
    }
}

dependencies {
    implementation 'com.github.takke:simple-twitter-auth-android:-SNAPSHOT'
}

```

[takke / simple\-twitter\-auth\-android](https://jitpack.io/#takke/simple-twitter-auth-android/-SNAPSHOT)

### Building from source

Rename samples/app/twitter.properties.sample to samples/app/twitter.properties and populate the consumer key and secret.

To build the entire project run

```
./gradlew assemble
```

Run all automated tests on device to verify.

```
./gradlew test connectedCheck
```

To run the sample app

```
./gradlew :samples:app:installDebug
```


Deploy to ```~/.m2/repository/com/twitter/sdk/android/```.
```
$ ./gradlew publishToMavenLocal
```


<!--
## Contributing

The master branch of this repository contains the latest stable release of Twitter Kit. See [CONTRIBUTING.md](https://github.com/twitter/twitter-kit-android/blob/master/CONTRIBUTING.md) for more details about how to contribute.

## Code of Conduct

This, and all github.com/twitter projects, are under the [Twitter Open Source Code of Conduct](https://github.com/twitter/code-of-conduct/blob/master/code-of-conduct.md). Additionally, see the [Typelevel Code of Conduct](http://typelevel.org/conduct) for specific examples of harassing behavior that are not tolerated.

## Contact

For usage questions post on [Twitter Community](https://twittercommunity.com/tags/c/publisher/twitter/android).

Please report any bugs as [issues](https://github.com/twitter/twitter-kit-android/issues).

Follow [@TwitterDev](http://twitter.com/twitterdev) on Twitter for updates.
-->

## License

Copyright 2017 Twitter, Inc.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
