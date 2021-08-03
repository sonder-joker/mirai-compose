package com.youngerhousea.miraicompose.core.viewmodel

import com.arkivanov.decompose.instancekeeper.InstanceKeeper
import com.youngerhousea.miraicompose.core.autoLoginFile
import com.youngerhousea.miraicompose.core.data.LoginCredential
import com.youngerhousea.miraicompose.core.utils.replace
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.immutableListOf
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.decodeFromString
import net.mamoe.yamlkt.Yaml
import okhttp3.internal.immutableListOf
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

class AutoLoginViewModel(
    val path: Path = autoLoginFile
) : InstanceKeeper.Instance {

    val data: StateFlow<List<LoginCredential>> get() = _data

    private val _data: MutableStateFlow<List<LoginCredential>> =
        MutableStateFlow(Yaml.decodeFromString(path.readText()) ?: listOf())

    fun addAutoLoginAccount(loginCredential: LoginCredential) {
        _data.value += loginCredential
    }

    fun updateLoginCredential(index: Int, loginCredential: LoginCredential) {
        _data.value = _data.value.replace(index, loginCredential)

    }

    override fun onDestroy() {
        path.writeText(Yaml.encodeToString(_data.value))
    }
}