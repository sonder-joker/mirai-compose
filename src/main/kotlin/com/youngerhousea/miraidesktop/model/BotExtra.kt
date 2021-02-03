package com.youngerhousea.miraidesktop.model

import androidx.compose.ui.graphics.ImageBitmap
import com.youngerhousea.miraidesktop.utils.toAvatarImage
import net.mamoe.mirai.contact.Contact

private val avatarCache = mutableMapOf<Long, ImageBitmap>()

suspend fun Contact.avatar(): ImageBitmap {
    return avatarUrl.toAvatarImage()
}