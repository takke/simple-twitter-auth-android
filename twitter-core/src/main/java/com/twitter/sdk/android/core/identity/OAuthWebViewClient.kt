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

import android.net.http.SslError
import android.os.Bundle
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.twitter.sdk.android.core.internal.network.UrlUtils
import java.net.URI

internal class OAuthWebViewClient(
        private val completeUrl: String,
        private val listener: Listener
) : WebViewClient() {

    internal interface Listener {
        fun onPageFinished(webView: WebView, url: String)
        fun onSuccess(bundle: Bundle)
        fun onError(exception: WebViewException)
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        listener.onPageFinished(view, url)
    }

    override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
        if (url.startsWith(completeUrl)) {
            val params = UrlUtils.getQueryParams(URI.create(url), false)
            val bundle = Bundle(params.size)
            for ((key, value) in params) {
                bundle.putString(key, value)
            }
            listener.onSuccess(bundle)
            return true
        }
        @Suppress("DEPRECATION")
        return super.shouldOverrideUrlLoading(view, url)
    }

    override fun onReceivedError(view: WebView, errorCode: Int,
                                 description: String, failingUrl: String) {
        @Suppress("DEPRECATION")
        super.onReceivedError(view, errorCode, description, failingUrl)
        listener.onError(WebViewException(errorCode, description, failingUrl))
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        super.onReceivedSslError(view, handler, error)
        listener.onError(WebViewException(error.primaryError, null, null))
    }
}
