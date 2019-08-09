/*
 * Copyright (C) 2015 Twitter, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.twitter.sdk.android.core

import android.annotation.SuppressLint

/**
 * The TwitterCore Kit provides Login with Twitter and the Twitter API.
 */
class TwitterCore internal constructor(
        val authConfig: TwitterAuthConfig
) {

    val version: String
        get() = BuildConfig.VERSION_NAME + "." + BuildConfig.BUILD_NUMBER

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        internal var instance: TwitterCore? = null
        const val TAG = "Twitter"

        fun getInstance(): TwitterCore {
            if (instance == null) {
                synchronized(TwitterCore::class.java) {
                    if (instance == null) {
                        instance = TwitterCore(Twitter.getInstance().twitterAuthConfig)
                    }
                }
            }
            return instance!!
        }
    }
}
