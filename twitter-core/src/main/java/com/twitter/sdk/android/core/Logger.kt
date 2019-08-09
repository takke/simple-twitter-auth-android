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

/**
 * Interface to support custom logger.
 */
interface Logger {
    var logLevel: Int

    fun isLoggable(tag: String, level: Int): Boolean

    fun d(tag: String, text: String?, throwable: Throwable?)
    fun v(tag: String, text: String?, throwable: Throwable?)
    fun i(tag: String, text: String?, throwable: Throwable?)
    fun w(tag: String, text: String?, throwable: Throwable?)
    fun e(tag: String, text: String?, throwable: Throwable?)

    fun d(tag: String, text: String?)
    fun v(tag: String, text: String?)
    fun i(tag: String, text: String?)
    fun w(tag: String, text: String?)
    fun e(tag: String, text: String?)

    fun log(priority: Int, tag: String, msg: String)
    fun log(priority: Int, tag: String, msg: String, forceLog: Boolean)
}
