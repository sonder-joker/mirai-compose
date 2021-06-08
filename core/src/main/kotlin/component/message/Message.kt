package com.youngerhousea.miraicompose.core.component.message

import net.mamoe.mirai.Bot


/**
 * 在线bot的页面
 *
 */
interface Message {
    val botList: List<Bot>
}