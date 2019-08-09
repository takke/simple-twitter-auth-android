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

package com.twitter.sdk.android.core.identity

import android.app.Activity

import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore

import java.util.concurrent.atomic.AtomicReference


/**
 * The state of an authorization request. This class is thread safe.
 */
internal class AuthState {

    private val authHandlerRef = AtomicReference<AuthHandler>(null)

    val isAuthorizeInProgress: Boolean
        get() = authHandlerRef.get() != null

    val authHandler: AuthHandler?
        get() = authHandlerRef.get()

    fun beginAuthorize(activity: Activity, authHandler: AuthHandler): Boolean {
        var result = false
        if (isAuthorizeInProgress) {
            Twitter.getLogger().w(TwitterCore.TAG, "Authorize already in progress")
        } else if (authHandler.authorize(activity)) {
            result = authHandlerRef.compareAndSet(null, authHandler)
            if (!result) {
                Twitter.getLogger().w(TwitterCore.TAG, "Failed to update authHandler, authorize" + " already in progress.")
            }
        }
        return result
    }

    fun endAuthorize() {
        authHandlerRef.set(null)
    }
}
