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

import android.content.Context

/**
 * Configurable Twitter options
 */
class TwitterConfig private constructor(
        val context: Context,
        val logger: Logger?,
        val twitterAuthConfig: TwitterAuthConfig?,
        val debug: Boolean?
) {

    /**
     * Builder for creating [TwitterConfig] instances.
     */
    class Builder(context: Context?) {
        private val context: Context
        private var logger: Logger? = null
        private var twitterAuthConfig: TwitterAuthConfig? = null
        private var debug: Boolean? = null

        init {
            if (context == null) {
                throw IllegalArgumentException("Context must not be null.")
            }

            this.context = context.applicationContext
        }

        /**
         * Sets the [Logger] to build with.
         */
        fun logger(logger: Logger?): Builder {
            if (logger == null) {
                throw IllegalArgumentException("Logger must not be null.")
            }

            this.logger = logger

            return this
        }

        /**
         * Sets the [TwitterAuthConfig] to build with.
         */
        fun twitterAuthConfig(authConfig: TwitterAuthConfig?): Builder {
            if (authConfig == null) {
                throw IllegalArgumentException("TwitterAuthConfig must not be null.")
            }

            this.twitterAuthConfig = authConfig

            return this
        }

        /**
         * Enable debug mode
         */
        fun debug(debug: Boolean): Builder {
            this.debug = debug

            return this
        }

        /**
         * Build the [TwitterConfig] instance
         */
        fun build(): TwitterConfig {
            return TwitterConfig(context, logger, twitterAuthConfig, debug)
        }
    }
}
