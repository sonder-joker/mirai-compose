package com.youngerhousea.miraicompose.component.message

import com.youngerhousea.miraicompose.console.ComposeBot


/**
 * 在线bot的页面
 *
 */
interface Message {
    val botList: List<ComposeBot>
}