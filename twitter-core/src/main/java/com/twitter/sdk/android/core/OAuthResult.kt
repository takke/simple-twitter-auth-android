package com.twitter.sdk.android.core

data class OAuthResult(
        val token: String,
        val tokenSecret: String,
        val userId: Long,
        val screenName: String
)
