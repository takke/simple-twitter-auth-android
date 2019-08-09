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
import android.content.Intent
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.OAuthResult
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterAuthException
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException

/**
 * Client for requesting authorization and email from the user.
 */
class TwitterAuthClient internal constructor(
        private val authConfig: TwitterAuthConfig,
        private val authState: AuthState
) {

    val requestCode: Int
        get() = authConfig.requestCode

    private object AuthStateLazyHolder {
        internal val INSTANCE = AuthState()
    }

    /**
     * Constructor.
     *
     * @throws IllegalStateException if called before starting TwitterKit with
     * Twitter.initialize()
     */
    constructor() : this(TwitterCore.getInstance().authConfig, AuthStateLazyHolder.INSTANCE)

    /**
     * Requests authorization.
     *
     * @param activity The [Activity] context to use for the authorization flow.
     * @param callback The callback interface to invoke when authorization completes.
     * @throws IllegalArgumentException if activity or callback is null.
     */
    fun authorize(activity: Activity?, callback: Callback<OAuthResult>?) {
        activity ?: throw IllegalArgumentException("Activity must not be null.")
        callback ?: throw IllegalArgumentException("Callback must not be null.")

        if (activity.isFinishing) {
            Twitter.getLogger()
                    .e(TwitterCore.TAG, "Cannot authorize, activity is finishing.", null)
        } else {
            handleAuthorize(activity, callback)
        }
    }

    private fun handleAuthorize(activity: Activity, callback: Callback<OAuthResult>) {
        val callbackWrapper = CallbackWrapper(callback)
        if (!authorizeUsingSSO(activity, callbackWrapper) && !authorizeUsingOAuth(activity, callbackWrapper)) {
            callbackWrapper.failure(TwitterAuthException("Authorize failed."))
        }
    }

    /**
     * Cancels any pending authorization request
     */
    @Suppress("unused")
    fun cancelAuthorize() {
        authState.endAuthorize()
    }

    private fun authorizeUsingSSO(activity: Activity, callbackWrapper: CallbackWrapper): Boolean {
        return if (SSOAuthHandler.isAvailable(activity)) {
            Twitter.getLogger().d(TwitterCore.TAG, "Using SSO")
            authState.beginAuthorize(activity,
                    SSOAuthHandler(authConfig, callbackWrapper, authConfig.requestCode))
        } else {
            false
        }
    }

    private fun authorizeUsingOAuth(activity: Activity, callbackWrapper: CallbackWrapper): Boolean {
        Twitter.getLogger().d(TwitterCore.TAG, "Using OAuth")
        return authState.beginAuthorize(activity,
                OAuthHandler(authConfig, callbackWrapper, authConfig.requestCode))
    }

    /**
     * Call this method when [Activity.onActivityResult]
     * is called to complete the authorization flow.
     *
     * @param requestCode the request code used for SSO
     * @param resultCode the result code returned by the SSO activity
     * @param data the result data returned by the SSO activity
     */
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        Twitter.getLogger().d(TwitterCore.TAG,
                "onActivityResult called with $requestCode $resultCode")
        if (!authState.isAuthorizeInProgress) {
            Twitter.getLogger().e(TwitterCore.TAG, "Authorize not in progress", null)
        } else {
            val authHandler = authState.authHandler
            if (authHandler != null && authHandler.handleOnActivityResult(requestCode, resultCode, data)) {
                authState.endAuthorize()
            }
        }
    }

    private class CallbackWrapper(private val callback: Callback<OAuthResult>) : Callback<OAuthResult>() {

        override fun success(result: OAuthResult) {
            Twitter.getLogger().d(TwitterCore.TAG, "Authorization completed successfully")
            callback.success(result)
        }

        override fun failure(exception: TwitterException) {
            Twitter.getLogger().e(TwitterCore.TAG, "Authorization completed with an error",
                    exception)
            callback.failure(exception)
        }
    }
}
