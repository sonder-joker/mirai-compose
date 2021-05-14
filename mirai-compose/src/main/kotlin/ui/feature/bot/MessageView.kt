package com.youngerhousea.miraicompose.ui.feature.bot

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arkivanov.decompose.ComponentContext
import com.youngerhousea.miraicompose.future.inject
import com.youngerhousea.miraicompose.model.ComposeBot
import com.youngerhousea.miraicompose.ui.common.VerticalSplittableSimple
import kotlinx.coroutines.InternalCoroutinesApi
import net.mamoe.mirai.Bot
import net.mamoe.mirai.event.events.BotEvent
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.BotLeaveEvent
import net.mamoe.mirai.event.events.MessageEvent
import java.lang.management.ManagementFactory
import java.lang.management.MemoryMXBean
import java.lang.management.MemoryUsage

/**
 * 在线bot的页面
 *
 */
class Message(
    componentContext: ComponentContext,
    val botList: List<ComposeBot>
) : ComponentContext by componentContext {
    internal data class MUsage(
        val committed: Long,
        val init: Long,
        val used: Long,
        val max: Long,
    )

    internal interface MemoryUsageGet {
        val heapMemoryUsage: MUsage
        val nonHeapMemoryUsage: MUsage
        val objectPendingFinalizationCount: Int
    }

    internal val memoryUsageGet: MemoryUsageGet = kotlin.runCatching {
        ByMemoryMXBean
    }.getOrElse { ByRuntime }

    internal object ByMemoryMXBean : MemoryUsageGet {
        val memoryMXBean: MemoryMXBean = ManagementFactory.getMemoryMXBean()
        val MemoryUsage.m: MUsage
            get() = MUsage(
                committed, init, used, max
            )
        override val heapMemoryUsage: MUsage
            get() = memoryMXBean.heapMemoryUsage.m
        override val nonHeapMemoryUsage: MUsage
            get() = memoryMXBean.nonHeapMemoryUsage.m
        override val objectPendingFinalizationCount: Int
            get() = memoryMXBean.objectPendingFinalizationCount
    }

    internal object ByRuntime : MemoryUsageGet {
        override val heapMemoryUsage: MUsage
            get() {
                val runtime: Runtime = Runtime.getRuntime()
                return MUsage(
                    committed = 0,
                    init = 0,
                    used = runtime.maxMemory() - runtime.freeMemory(),
                    max = runtime.maxMemory()
                )
            }
        override val nonHeapMemoryUsage: MUsage
            get() = MUsage(-1, -1, -1, -1)
        override val objectPendingFinalizationCount: Int
            get() = -1
    }
}

@Composable
fun MessageUi(message: Message) {
    Column(Modifier.fillMaxSize()) {
        message.botList.forEach {
            Text(it.messageSpeed.toString())
        }
    }
}

@Composable
fun TopView(modifier: Modifier) =
    Row(
        modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Events",
            color = LocalContentColor.current.copy(alpha = 0.60f),
            fontSize = 12.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }
