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

package com.twitter.sdk.android.core.internal

import android.net.Uri
import android.os.Build

import java.text.Normalizer

class TwitterApi @JvmOverloads constructor(val baseHostUrl: String = BASE_HOST_URL) {

    /**
     * Builds upon the base host url by appending paths to the url.
     *
     * @param paths the paths to append
     * @return [Uri.Builder] that can be used to further build the url.
     */
    fun buildUponBaseHostUrl(vararg paths: String): Uri.Builder {
        val builder = Uri.parse(baseHostUrl).buildUpon()
        for (p in paths) {
            builder.appendPath(p)
        }
        return builder
    }

    companion object {

        private const val BASE_HOST = "api.twitter.com"
        const val BASE_HOST_URL = "https://$BASE_HOST"

        /**
         * @return User-Agent string that looks like:
         * client_name/client_version (client_version_code) model/os_version (manufacturer;device;brand;product;client_source;preload;on_wifi)
         *
         *
         * Example: TwitterAndroidSDK/1.1.0.dev HTC One/4.1.2 (HTC;HTC One;tmous;m7)
         *
         *
         * See go/ooua for more information.
         */
        fun buildUserAgent(clientName: String, version: String): String {
            val ua = StringBuilder(clientName)
                    .append('/').append(version)
                    // NOTE: We currently do not provide client_version_code information.
                    .append(' ')
                    .append(Build.MODEL).append('/').append(Build.VERSION.RELEASE)
                    .append(" (")
                    .append(Build.MANUFACTURER).append(';')
                    .append(Build.MODEL).append(';')
                    .append(Build.BRAND).append(';')
                    .append(Build.PRODUCT)
                    // NOTE: We do not add client_source, preload, or wifi information.
                    .append(')')
            return normalizeString(ua.toString())
        }

        private fun normalizeString(str: String): String {
            val normalizedString = Normalizer.normalize(str, Normalizer.Form.NFD)
            return stripNonAscii(normalizedString)
        }

        private fun stripNonAscii(str: String): String {
            val sb = StringBuilder(str.length)
            for (i in 0 until str.length) {
                val c = str[i]
                if (c in ' '..'~') {
                    sb.append(c)
                }
            }

            return sb.toString()
        }
    }
}
