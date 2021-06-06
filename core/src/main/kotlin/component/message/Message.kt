package com.youngerhousea.miraicompose.core.component.message

import com.youngerhousea.miraicompose.core.console.ComposeBot


/**
 * 在线bot的页面
 *
 */
interface Message {
    val botList: List<ComposeBot>
}