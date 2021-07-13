package com.youngerhousea.miraicompose.core.data

import kotlinx.serialization.Serializable

@Serializable
data class LogColor(
    val debug: String = "0xFF00FFFF",
    val verbose: String = "0xFFFF00FF",
    val info: String = "0xFF019d4E",
    val warning: String = "0xFFf2A111",
    val error: String = "0xFFEA3C5B",
    val highLight:String = "0xFFFFFF00"
)

//    val debug: Long = 0xFF00FFFF,
//    val verbose: Long = 0xFFFF00FF,
//    val info: Long = 0xFF019d4E,
//    val warning: Long = 0xFFf2A111,
//    val error: Long = 0xFFEA3C5B
