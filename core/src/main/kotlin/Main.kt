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
import kotlin.io.path.createDirectories
import kotlin.io.path.div

fun navHost(componentContext: ComponentContext = DefaultComponentContext(LifecycleRegistry())): NavHost = NavHostImpl(componentContext)

val root: Path = Paths.get(System.getProperty("user.dir", ".")).toAbsolutePath().createDirectories()

val configFile = (root / "compose").createDirectories()

val autoLogin = (configFile / "autoLogin.yml").createFiles()

val themePath = (configFile / "theme.yml").createFiles()

val loginPriorityPath =  (configFile / "loginPriority.yml").createFiles()

@Suppress("NOTHING_TO_INLINE")
inline fun Path.createFiles(): Path =
     if (!Files.exists(this, LinkOption.NOFOLLOW_LINKS))
        Files.createFile(this)
    else
        this
