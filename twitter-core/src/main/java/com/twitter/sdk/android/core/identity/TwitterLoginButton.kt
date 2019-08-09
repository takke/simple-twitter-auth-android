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

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.Button
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.OAuthResult
import com.twitter.sdk.android.core.R
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.internal.CommonUtils
import java.lang.ref.WeakReference

/**
 * Log in button for logging into Twitter. When the button is clicked, an authorization request
 * is started and the user is presented with a screen requesting access to the user's Twitter
 * account. If successful, a [OAuthResult] is provided
 * in the [com.twitter.sdk.android.core.Callback.success]
 */
@SuppressLint("AppCompatCustomView")
class TwitterLoginButton internal constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyle: Int,
        @field:Volatile internal var authClient: TwitterAuthClient?
) : Button(context, attrs, defStyle) {

    internal val activityRef: WeakReference<Activity?> = WeakReference(activity)

    internal var onClickListener: OnClickListener? = null
    lateinit var callback: Callback<OAuthResult>

    /**
     * Gets the activity. Override this method if this button was created with a non-Activity
     * context.
     */
    private val activity: Activity?
        get() {
            val context = context
            return when {
                context is ContextThemeWrapper && context.baseContext is Activity ->
                    context.baseContext as Activity
                context is Activity -> context
                isInEditMode -> null
                else -> throw IllegalStateException(ERROR_MSG_NO_ACTIVITY)
            }
        }

    internal val twitterAuthClient: TwitterAuthClient?
        get() {
            if (authClient == null) {
                synchronized(TwitterLoginButton::class.java) {
                    if (authClient == null) {
                        authClient = TwitterAuthClient()
                    }
                }
            }
            return authClient
        }

    @JvmOverloads
    constructor(context: Context,
                attrs: AttributeSet? = null,
                defStyle: Int = android.R.attr.buttonStyle
    ) : this(context, attrs, defStyle, null)

    init {
        setupButton()

        checkTwitterCoreAndEnable()
    }

    @Suppress("DEPRECATION")
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun setupButton() {
        val res = resources

        super.setCompoundDrawablesWithIntrinsicBounds(
                res.getDrawable(R.drawable.tw__ic_logo_default), null, null, null)
        super.setCompoundDrawablePadding(res.getDimensionPixelSize(R.dimen.tw__login_btn_drawable_padding))
        super.setText(R.string.tw__login_btn_txt)
        super.setTextColor(res.getColor(R.color.tw__solid_white))
        super.setTextSize(TypedValue.COMPLEX_UNIT_PX, res.getDimensionPixelSize(R.dimen.tw__login_btn_text_size).toFloat())
        super.setTypeface(Typeface.DEFAULT_BOLD)
        super.setPadding(res.getDimensionPixelSize(R.dimen.tw__login_btn_left_padding), 0, res.getDimensionPixelSize(R.dimen.tw__login_btn_right_padding), 0)
        super.setBackgroundResource(R.drawable.tw__login_btn)
        super.setOnClickListener(LoginClickListener())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.setAllCaps(false)
        }
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
        if (requestCode == twitterAuthClient?.requestCode) {
            twitterAuthClient?.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun setOnClickListener(onClickListener: OnClickListener?) {
        this.onClickListener = onClickListener
    }

    private inner class LoginClickListener : OnClickListener {

        override fun onClick(view: View) {
            if (!::callback.isInitialized) {
                CommonUtils.logOrThrowIllegalStateException(TwitterCore.TAG,
                        "Callback must not be null, did you call setCallback?")
            }
            checkActivity(activityRef.get())

            twitterAuthClient?.authorize(activityRef.get(), callback)

            onClickListener?.onClick(view)
        }

        private fun checkActivity(activity: Activity?) {
            if (activity == null || activity.isFinishing) {
                CommonUtils.logOrThrowIllegalStateException(TwitterCore.TAG,
                        ERROR_MSG_NO_ACTIVITY)
            }
        }
    }

    private fun checkTwitterCoreAndEnable() {
        //Default (Enabled) in edit mode
        if (isInEditMode) return

        try {
            TwitterCore.getInstance()
        } catch (ex: IllegalStateException) {
            //Disable if TwitterCore hasn't started
            Twitter.getLogger().e(TAG, ex.message)
            isEnabled = false
        }

    }

    companion object {
        internal const val TAG = TwitterCore.TAG
        internal const val ERROR_MSG_NO_ACTIVITY = "TwitterLoginButton requires an activity." +
                " Override getActivity to provide the activity for this button."
    }
}
