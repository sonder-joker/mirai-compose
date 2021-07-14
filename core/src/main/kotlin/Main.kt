package com.youngerhousea.miraicompose.core

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.youngerhousea.miraicompose.core.component.NavHost
import com.youngerhousea.miraicompose.core.component.impl.NavHostImpl
import java.nio.file.Files
import java.nio.file.LinkOption
import java.nio.file.Path
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.createDirectories
import kotlin.io.path.div

fun navHost(componentContext: ComponentContext = DefaultComponentContext(LifecycleRegistry())): NavHost =
    NavHostImpl(componentContext)

val root: Path = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().createDirectories()

val dataPath = (root / "data").createDirectories()

val configPath = (root / "config").createDirectories()

val composePath = (root / "compose").createDirectories()

val pluginPath = (root / "plugins").createDirectories()

val autoLoginFile = (composePath / "autoLogin.yml").createFiles()

val themeFile = (composePath / "theme.yml").createFiles()

val loginPriorityFile = (composePath / "loginPriority.yml").createFiles()

private val fileNameFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss")

val logFileName = (root / "log" / LocalDateTime.now().format(fileNameFormat)).createFiles()

@Suppress("NOTHING_TO_INLINE")
inline fun Path.createFiles(): Path =
    if (!Files.exists(this, LinkOption.NOFOLLOW_LINKS))
        Files.createFile(this)
    else
        this

