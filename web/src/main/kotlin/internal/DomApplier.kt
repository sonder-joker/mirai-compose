package org.jetbrains.compose.web

import androidx.compose.runtime.AbstractApplier
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.NodeList
import org.w3c.dom.html.HTMLElement

class DomApplier(
    root: Node
) : AbstractApplier<Node>(root) {

    override fun insertTopDown(index: Int, instance: Node) {
        // ignored. Building tree bottom-up
    }

    override fun insertBottomUp(index: Int, instance: Node) {
        current.insert(index, instance)
    }

    override fun remove(index: Int, count: Int) {
        current.remove(index, count)
    }

    override fun move(from: Int, to: Int, count: Int) {
        current.move(from, to, count)
    }

    override fun onClear(node: Node) {

        // or current.node.clear()?; in all examples it calls 'clear' on the root
            root.node.clear()
    }
}


open class DomNodeWrapper(open val node: Node) {
    private var currentListeners = emptyList<WrappedEventListener<*>>()

    fun updateEventListeners(list: List<WrappedEventListener<*>>) {
        val htmlElement = node as? HTMLElement ?: return
        currentListeners.forEach {
            htmlElement.removeEventListener(it.event, it)
        }

        currentListeners = list

        currentListeners.forEach {
            htmlElement.addEventListener(it.event, it)
        }
    }

    fun insert(index: Int, nodeWrapper: DomNodeWrapper) {
        val length = node.childNodes.length
        if (index < length) {
            node.insertBefore(nodeWrapper.node, node.childNodes[index])
        } else {
            node.appendChild(nodeWrapper.node)
        }
    }

    fun remove(index: Int, count: Int) {
        repeat(count) {
            node.removeChild(node.childNodes.item(index))
        }
    }

    fun move(from: Int, to: Int, count: Int) {
        if (from == to) {
            return // nothing to do
        }

        for (i in 0 until count) {
            // if "from" is after "to," the from index moves because we're inserting before it
            val fromIndex = if (from > to) from + i else from
            val toIndex = if (from > to) to + i else to + count - 2

            val child = node.removeChild(node.childNodes[fromIndex])
            node.insertBefore(child, node.childNodes[toIndex])
        }
    }
}

operator fun NodeList.get(index: Int): Node = item(index)

