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

import android.os.Parcel
import android.os.Parcelable

/**
 * Authorization configuration details.
 */
class TwitterAuthConfig : Parcelable {

    /**
     * @return the consumer key
     */
    val consumerKey: String?
    /**
     * @return the consumer secret
     */
    val consumerSecret: String?

    /**
     * @return The request code to use for Single Sign On. This code will
     * be returned in [android.app.Activity.onActivityResult]
     * when the activity exits.
     */
    val requestCode: Int
        get() = DEFAULT_AUTH_REQUEST_CODE

    /**
     * @param consumerKey    The consumer key.
     * @param consumerSecret The consumer secret.
     *
     * @throws IllegalArgumentException if consumer key or consumer secret is null.
     */
    constructor(consumerKey: String?, consumerSecret: String?) {
        if (consumerKey == null || consumerSecret == null) {
            throw IllegalArgumentException(
                    "TwitterAuthConfig must not be created with null consumer key or secret.")
        }
        this.consumerKey = sanitizeAttribute(consumerKey)
        this.consumerSecret = sanitizeAttribute(consumerSecret)
    }

    private constructor(`in`: Parcel) {
        consumerKey = `in`.readString()
        consumerSecret = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(out: Parcel, flags: Int) {
        out.writeString(consumerKey)
        out.writeString(consumerSecret)
    }

    companion object CREATOR : Parcelable.Creator<TwitterAuthConfig> {
        override fun createFromParcel(parcel: Parcel): TwitterAuthConfig {
            return TwitterAuthConfig(parcel)
        }

        override fun newArray(size: Int): Array<TwitterAuthConfig?> {
            return arrayOfNulls(size)
        }

        /**
         * The default request code to use for Single Sign On. This code will
         * be returned in [android.app.Activity.onActivityResult]
         */
        private const val DEFAULT_AUTH_REQUEST_CODE = 140

        private fun sanitizeAttribute(input: String?): String? {
            return input?.trim { it <= ' ' }
        }
    }
}
