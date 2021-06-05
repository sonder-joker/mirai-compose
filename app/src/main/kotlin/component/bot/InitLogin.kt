package com.youngerhousea.miraicompose.component.bot

import androidx.compose.material.SnackbarHostState
import androidx.compose.ui.text.input.TextFieldValue


/**
 * bot的登录的界面
 */
interface InitLogin{

    val account: TextFieldValue

    val password: TextFieldValue

    val isLoading: Boolean

    val state: SnackbarHostState

    fun onLogin()

    fun onAccountTextChange(textFieldValue: TextFieldValue)

    fun onPasswordTextChange(textFieldValue: TextFieldValue)
}