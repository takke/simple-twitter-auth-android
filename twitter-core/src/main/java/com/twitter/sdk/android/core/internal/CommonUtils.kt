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

import android.content.Context
import com.twitter.sdk.android.core.Twitter

object CommonUtils {

    /**
     * Uses the given context's application icon to retrieve the package name for the resources for the context
     * This package name only differs from context.getPackageName() when using aapt parameter --rename-manifest-package
     * @param context Context to get resource package name from
     * @return String representing the package name of the resources for the given context
     */
    private fun getResourcePackageName(context: Context): String {
        // There should always be an icon
        // http://developer.android.com/guide/topics/manifest/application-element.html#icon
        // safety check anyway to prevent exceptions
        val iconId = context.applicationContext.applicationInfo.icon
        return if (iconId > 0) {
            context.resources.getResourcePackageName(iconId)
        } else {
            context.packageName
        }
    }

    private fun getResourcesIdentifier(context: Context, key: String, resourceType: String): Int {
        val resources = context.resources
        return resources.getIdentifier(key, resourceType, getResourcePackageName(context))
    }

    /**
     *
     *
     * Gets a value for a string resource by its name. If a key is not present, the provided default value
     * will be returned.
     *
     *
     * @param context [Context] to use when accessing resources
     * @param key [String] name of the boolean value to look up
     * @param defaultValue value to be returned if the specified resource could be not be found.
     * @return [String] value of the specified property, or an empty string if it could not be found.
     */
    fun getStringResourceValue(context: Context?, key: String, defaultValue: String): String {
        if (context != null) {
            val resources = context.resources

            if (resources != null) {
                val id = getResourcesIdentifier(context, key, "string")

                if (id > 0) {
                    return resources.getString(id)
                }
            }
        }

        return defaultValue
    }

    /**
     * If [Twitter.isDebug], throws an IllegalStateException,
     * else logs a warning.
     *
     * @param logTag the log tag to use for logging
     * @param errorMsg the error message
     */
    fun logOrThrowIllegalStateException(logTag: String, errorMsg: String) {
        if (Twitter.isDebug) {
            throw IllegalStateException(errorMsg)
        } else {
            Twitter.getLogger().w(logTag, errorMsg)
        }
    }
}
