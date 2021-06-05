package com.youngerhousea.miraicompose.component.about

import androidx.compose.ui.text.AnnotatedString


/**
 * 关于MiraiCompose的信息
 *
 */
interface About {
    val frontend: AnnotatedString

    val backend: AnnotatedString

    val miraiForum:AnnotatedString
}