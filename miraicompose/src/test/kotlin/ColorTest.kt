package com.youngerhousea.miraicompose

import androidx.compose.ui.graphics.Color
import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ColorSerializer
import com.youngerhousea.miraicompose.theme.InternalAppTheme
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml

@Serializable
data class C(
    @Serializable(with = ColorSerializer::class)
    val color:Color
)

fun main() {
    val a = Yaml {}.encodeToString(AppTheme)
    println(a)
    val d: InternalAppTheme = Yaml { }.decodeFromString(a)
    println(Yaml {}.encodeToString(d))
}