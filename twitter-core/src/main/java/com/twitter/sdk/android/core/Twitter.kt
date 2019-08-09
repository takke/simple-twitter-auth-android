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
import android.content.Context
import com.twitter.sdk.android.core.internal.CommonUtils

/**
 * The [Twitter] class stores common configuration and state for TwitterKit SDK.
 */
class Twitter private constructor(config: TwitterConfig) {

    private val context: Context = config.context

    /**
     * @return the global [TwitterAuthConfig].
     */
    val twitterAuthConfig: TwitterAuthConfig

    private val logger: Logger = config.logger ?: DEFAULT_LOGGER

    private val debug: Boolean = config.debug ?: false

    init {
        twitterAuthConfig = if (config.twitterAuthConfig == null) {
            val key = CommonUtils.getStringResourceValue(context, CONSUMER_KEY, "")
            val secret = CommonUtils.getStringResourceValue(context, CONSUMER_SECRET, "")
            TwitterAuthConfig(key, secret)
        } else {
            config.twitterAuthConfig
        }
    }

    companion object {
        const val TAG = "Twitter"
        private const val CONSUMER_KEY = "com.twitter.sdk.android.CONSUMER_KEY"
        private const val CONSUMER_SECRET = "com.twitter.sdk.android.CONSUMER_SECRET"
        private const val NOT_INITIALIZED_MESSAGE = "Must initialize Twitter before using getInstance()"
        internal val DEFAULT_LOGGER: Logger = DefaultLogger()

        @SuppressLint("StaticFieldLeak")
        @Volatile
        internal var instance: Twitter? = null

        /**
         * Entry point to initialize the TwitterKit SDK.
         *
         *
         * Only the Application context is retained.
         * See http://developer.android.com/resources/articles/avoiding-memory-leaks.html
         *
         *
         * Should be called from `OnCreate()` method of custom `Application` class.
         * <pre>
         * public class SampleApplication extends Application {
         *   &#64;Override
         *   public void onCreate() {
         *   final TwitterConfig config = new TwitterConfig.Builder(this).build();
         *     Twitter.initialize(config);
         *   }
         * }
         * </pre>
         *
         * @param config [TwitterConfig] user for initialization
         */
        fun initialize(config: TwitterConfig) {
            createTwitter(config)
        }

        @Synchronized
        internal fun createTwitter(config: TwitterConfig): Twitter? {
            if (instance == null) {
                instance = Twitter(config)
                return instance
            }

            return instance
        }

        private fun checkInitialized() {
            if (instance == null) {
                throw IllegalStateException(NOT_INITIALIZED_MESSAGE)
            }
        }

        /**
         * @return Single instance of the [Twitter].
         */
        fun getInstance(): Twitter {
            checkInitialized()
            return instance!!
        }

        /**
         * @return the global value for debug mode.
         */
        val isDebug: Boolean
            get() = instance?.debug ?: false

        /**
         * @return the global [Logger].
         */
        fun getLogger(): Logger {
            return instance?.logger ?: DEFAULT_LOGGER
        }
    }
}
