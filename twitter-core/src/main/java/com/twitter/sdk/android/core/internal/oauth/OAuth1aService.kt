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

package com.twitter.sdk.android.core.internal.oauth

import android.net.Uri
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.TwitterApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import twitter4j.auth.AccessToken
import twitter4j.auth.OAuthAuthorization
import twitter4j.auth.RequestToken
import twitter4j.conf.ConfigurationBuilder

/**
 * OAuth1.0a service. Provides methods for requesting request tokens, access tokens, and signing
 * requests.
 */
class OAuth1aService(private val twitterCore: TwitterCore,
                     private val api: TwitterApi
) {

    private lateinit var oAuth: OAuthAuthorization

    /**
     * Requests a temp token to start the Twitter sign-in flow.
     */
    @Throws(twitter4j.TwitterException::class)
    suspend fun requestTempToken(): RequestToken {

        val config = twitterCore.authConfig

        return withContext(Dispatchers.Default) {

            val builder = ConfigurationBuilder()

            builder.setOAuthConsumerKey(config.consumerKey)
            builder.setOAuthConsumerSecret(config.consumerSecret)

            val conf = builder.build()
            oAuth = OAuthAuthorization(conf)

            oAuth.getOAuthRequestToken(buildCallbackUrl(config))
        }
    }

    /**
     * Builds a callback url that is used to receive a request containing the oauth_token and
     * oauth_verifier parameters.
     *
     * @param authConfig The auth config
     * @return the callback url
     */
    fun buildCallbackUrl(authConfig: TwitterAuthConfig): String {
        return Uri.parse(CALLBACK_URL).buildUpon()
                .appendQueryParameter("version", twitterCore.version)
                .appendQueryParameter("app", authConfig.consumerKey)
                .build()
                .toString()
    }

    /**
     * Requests a Twitter access token to act on behalf of a user account.
     */
    @Throws(twitter4j.TwitterException::class)
    suspend fun requestAccessToken(requestToken: RequestToken,
                                   verifier: String): AccessToken {

        return withContext(Dispatchers.Default) {

            oAuth.getOAuthAccessToken(requestToken, verifier)
        }
    }

    /**
     * @param requestToken The request token.
     * @return authorization url that can be used to get a verifier code to get access token.
     */
    fun getAuthorizeUrl(requestToken: RequestToken): String {
        // https://api.twitter.com/oauth/authorize?oauth_token=%s
        return api.buildUponBaseHostUrl(RESOURCE_OAUTH, "authorize")
                .appendQueryParameter(OAuthConstants.PARAM_TOKEN, requestToken.token)
                .build()
                .toString()
    }

    companion object {

        private const val RESOURCE_OAUTH = "oauth"
        private const val CALLBACK_URL = "twittersdk://callback"
    }
}
