package com.youngerhousea.miraicompose.app.ui.setting

import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.*
import com.youngerhousea.miraicompose.core.component.setting.LogLevelSetting



@Composable
fun LoggerLevelSettingUi(setting: LogLevelSetting) {
    Column {
        Text("Log level", Modifier.weight(4f))

        val node by setting.priorityNode.collectAsState()

//        EnumTabRowWithContent(node,
//            rowModifier = Modifier.width(400.dp),
//            onClick = {
//                setting.setLogConfigLevel(it)
//            }) {
//            Text(it.name)
//
//        }
    }
}


//----------------------------------------------------------------------------
//     *                             *
//    / \                           / \
// 'a'   'c'                     'a'   'c'
//  |   / | \                     |      \
//  | /   |  \                    |       \
// 'e'  'f'  'g' =>             'e'      'e'
//  | \  |   /
//  |  \ | /
// 'b' 'd'
//

interface Node {
    val parent: Node

    val childNode: MutableList<Node>

    fun connect(node: Node) = childNode.add(node)
}

interface Layer {
    val node: List<Node>
}

class NodeImpl : Node {
    override val parent: Node
        get() = TODO("Not yet implemented")

    override val childNode: MutableList<Node>
        get() = TODO("Not yet implemented")

}

object Root : Node {
    override val parent: Node
        get() = TODO("Not yet implemented")
    override val childNode: MutableList<Node>
        get() = TODO("Not yet implemented")

}

@Composable
fun Node(node: Node) {
    Column {
        var now by remember { mutableStateOf(0) }
        while (node.childNode.isNotEmpty()) {

            now++
        }
    }
}

@Composable
fun SingleNode() {
    var isClick by remember { mutableStateOf(false) }
    var buttonPlace by remember { mutableStateOf(false) }
    Button({
        isClick = !isClick
    }, Modifier.pointerInput(Unit) {
        viewConfiguration
        forEachGesture {
            awaitPointerEventScope {
                val a  = awaitEventFirstDown().also {
                    it.changes.forEach { it.consumeDownChange() }
                }.mouseEvent
                a?.let {

                }
            }
        }
    }) {

    }

    if (isClick) {

    }
}

private suspend fun AwaitPointerEventScope.awaitEventFirstDown(): PointerEvent {
    var event: PointerEvent
    do {
        event = awaitPointerEvent()
    } while (
        !event.changes.all { it.changedToDown() }
    )
    return event
}
