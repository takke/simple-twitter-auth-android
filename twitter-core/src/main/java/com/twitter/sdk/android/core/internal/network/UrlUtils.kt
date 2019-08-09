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

package com.twitter.sdk.android.core.internal.network

import android.text.TextUtils
import java.io.UnsupportedEncodingException
import java.net.URI
import java.net.URLDecoder
import java.util.*

object UrlUtils {

    private const val UTF8 = "UTF8"

    fun getQueryParams(uri: URI, decode: Boolean): TreeMap<String, String> {
        return getQueryParams(uri.rawQuery, decode)
    }

    private fun getQueryParams(paramsString: String?, decode: Boolean): TreeMap<String, String> {
        val params = TreeMap<String, String>()
        if (paramsString == null) {
            return params
        }
        for (nameValuePairString in paramsString.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()) {
            val nameValuePair = nameValuePairString.split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (nameValuePair.size == 2) {
                if (decode) {
                    params[urlDecode(nameValuePair[0])] = urlDecode(nameValuePair[1])
                } else {
                    params[nameValuePair[0]] = nameValuePair[1]
                }
            } else if (!TextUtils.isEmpty(nameValuePair[0])) {
                if (decode) {
                    params[urlDecode(nameValuePair[0])] = ""
                } else {
                    params[nameValuePair[0]] = ""
                }
            }
        }
        return params
    }

    private fun urlDecode(s: String?): String {
        if (s == null) {
            return ""
        }
        try {
            return URLDecoder.decode(s, UTF8)
        } catch (unlikely: UnsupportedEncodingException) {
            throw RuntimeException(unlikely.message, unlikely)
        }

    }

}
