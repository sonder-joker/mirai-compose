package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.youngerhousea.miraicompose.core.component.setting.Setting

@Composable
fun SettingUi(setting: Setting) {
    Children(setting.state) {
        when (val ch = it.instance) {
            is Setting.Child.AutoLogin -> AutoLoginSettingUi(ch.autoLoginSetting)
            is Setting.Child.LogColor -> LogColorSettingUi(ch.logColorSetting)
            is Setting.Child.LogLevel -> LoggerLevelSettingUi(ch.logLevelSetting)
            is Setting.Child.Main -> MainSettingUi(ch.main)
        }
    }
}





