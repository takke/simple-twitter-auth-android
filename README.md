# 本リポジトリについて

- [Twitter Kit for Android](https://dev.twitter.com/twitterkit/android/overview) の認証部分を抽出したものです。
- TwitPane 用に作りました。
- Retrofit, Gson への依存を除去し、代わりに Twitter4J を利用するように変更しています。
- 主な使い方は [Log In with Twitter · twitter\-archive/twitter\-kit\-android Wiki](https://github.com/twitter-archive/twitter-kit-android/wiki/Log-In-with-Twitter) を参照。


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

To build the entire project run

```
./gradlew assemble
```

Deploy to ```~/.m2/repository/com/twitter/sdk/android/```.
```
$ ./gradlew publishToMavenLocal
```


## License

Copyright 2019 Panecraft, Inc.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
