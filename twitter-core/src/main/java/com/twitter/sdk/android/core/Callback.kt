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

/**
 * Communicates responses from a server or offline requests. One and only one method will be
 * invoked in response to a given request.
 *
 * @param <T> expected response type</T>
 */
abstract class Callback<T> {

    /**
     * Called when call completes successfully.
     *
     * @param result the parsed result.
     */
    abstract fun success(result: T)

    /**
     * Unsuccessful call due to network failure, non-2XX status code, or unexpected
     * exception.
     */
    abstract fun failure(exception: TwitterException)
}
