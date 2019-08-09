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
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterAuthException

/**
 * Abstract class for handling authorization requests.
 */
abstract class AuthHandler
/**
 * @param authConfig  The [TwitterAuthConfig].
 * @param callback    The listener to callback when authorization completes.
 * @param requestCode The request code.
 */
internal constructor(internal val authConfig: TwitterAuthConfig,
                     internal val callback: Callback<OAuthResult>,
                     protected val requestCode: Int) {

    /**
     * Called to request authorization.
     *
     * @return true if authorize request was successfully started.
     */
    abstract fun authorize(activity: Activity): Boolean

    /**
     * Called when [Activity.onActivityResult]
     * is called to complete the authorization flow.
     *
     * @param requestCode the request code used for SSO
     * @param resultCode  the result code returned by the SSO activity
     * @param data        the result data returned by the SSO activity
     */
    fun handleOnActivityResult(requestCode: Int, resultCode: Int, data: Intent?): Boolean {
        if (this.requestCode != requestCode) {
            return false
        }

        val callback = callback
        if (resultCode == Activity.RESULT_OK) {
            val token = data!!.getStringExtra(EXTRA_TOKEN)
            val tokenSecret = data.getStringExtra(EXTRA_TOKEN_SECRET)
            val screenName = data.getStringExtra(EXTRA_SCREEN_NAME)
            val userId = data.getLongExtra(EXTRA_USER_ID, 0L)

            val accessToken = OAuthResult(token, tokenSecret, userId, screenName)

            callback.success(accessToken)
        } else if (data != null && data.hasExtra(EXTRA_AUTH_ERROR)) {
            callback.failure(
                    data.getSerializableExtra(EXTRA_AUTH_ERROR) as TwitterAuthException)
        } else {
            callback.failure(TwitterAuthException("Authorize failed."))
        }
        return true
    }

    companion object {
        internal const val EXTRA_TOKEN = "tk"
        internal const val EXTRA_TOKEN_SECRET = "ts"
        internal const val EXTRA_SCREEN_NAME = "screen_name"
        internal const val EXTRA_USER_ID = "user_id"
        internal const val EXTRA_AUTH_ERROR = "auth_error"

        internal const val RESULT_CODE_ERROR = Activity.RESULT_FIRST_USER
    }
}
