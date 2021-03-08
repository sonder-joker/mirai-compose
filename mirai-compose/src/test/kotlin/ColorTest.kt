package com.youngerhousea.miraicompose

import com.youngerhousea.miraicompose.theme.AppTheme
import com.youngerhousea.miraicompose.theme.ComposeSetting
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class ColorTest {

    val yaml = Yaml { }

    @Test
    fun appThemeTest() {
        val theme = yaml.encodeToString(ComposeSetting.AppTheme)
        val data = yaml.decodeFromString<AppTheme>(theme)
        assertEquals(ComposeSetting.AppTheme, data)
    }


}

