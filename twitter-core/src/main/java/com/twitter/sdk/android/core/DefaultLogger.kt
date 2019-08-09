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

import android.util.Log

/**
 * Default logger that logs to android.util.Log.
 */
class DefaultLogger : Logger {
    override var logLevel: Int = 0

    constructor(logLevel: Int) {
        this.logLevel = logLevel
    }

    constructor() {
        this.logLevel = Log.INFO
    }

    override fun isLoggable(tag: String, level: Int): Boolean {
        return logLevel <= level
    }

    override fun d(tag: String, text: String?, throwable: Throwable?) {
        if (isLoggable(tag, Log.DEBUG)) {
            Log.d(tag, text, throwable)
        }
    }

    override fun v(tag: String, text: String?, throwable: Throwable?) {
        if (isLoggable(tag, Log.VERBOSE)) {
            Log.v(tag, text, throwable)
        }
    }

    override fun i(tag: String, text: String?, throwable: Throwable?) {
        if (isLoggable(tag, Log.INFO)) {
            Log.i(tag, text, throwable)
        }
    }

    override fun w(tag: String, text: String?, throwable: Throwable?) {
        if (isLoggable(tag, Log.WARN)) {
            Log.w(tag, text, throwable)
        }
    }

    override fun e(tag: String, text: String?, throwable: Throwable?) {
        if (isLoggable(tag, Log.ERROR)) {
            Log.e(tag, text, throwable)
        }
    }

    override fun d(tag: String, text: String?) {
        d(tag, text, null)
    }

    override fun v(tag: String, text: String?) {
        v(tag, text, null)
    }

    override fun i(tag: String, text: String?) {
        i(tag, text, null)
    }

    override fun w(tag: String, text: String?) {
        w(tag, text, null)
    }

    override fun e(tag: String, text: String?) {
        e(tag, text, null)
    }

    override fun log(priority: Int, tag: String, msg: String) {
        log(priority, tag, msg, false)
    }

    override fun log(priority: Int, tag: String, msg: String, forceLog: Boolean) {
        if (forceLog || isLoggable(tag, priority)) {
            Log.println(priority, tag, msg)
        }
    }
}
