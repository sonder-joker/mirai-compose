package com.youngerhousea.mirai.compose.model

import kotlinx.serialization.Serializable

@Serializable
data class LoginCredential(
    val account: String = "",
    val password: String = "",
    val passwordKind: PasswordKind = PasswordKind.PLAIN,
    val protocolKind: ProtocolKind = ProtocolKind.ANDROID_PHONE,
) {

    @Serializable
    enum class ProtocolKind {
        ANDROID_PHONE,
        ANDROID_PAD,
        ANDROID_WATCH
    }

    @Serializable
    enum class PasswordKind {
        PLAIN,
        MD5
    }
}