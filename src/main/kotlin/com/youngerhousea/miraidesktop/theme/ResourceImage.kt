package com.youngerhousea.miraidesktop.theme

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.loadVectorXmlResource
import androidx.compose.ui.unit.Density
import org.xml.sax.InputSource
import java.awt.image.BufferedImage
import java.net.URL
import javax.imageio.ImageIO

object ResourceImage {
    val mirai = loadImageVector("ic_mirai.xml")
    val icon = loadImageResource("mirai.png")
}

@Suppress("SameParameterValue")
private fun loadImageVector(path: String): ImageVector {
    val resource: URL? = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    val r = resource.openStream()
    return loadVectorXmlResource(InputSource(r), Density(1f))
}

@Suppress("SameParameterValue")
private fun loadImageResource(path: String): BufferedImage {
    val resource = Thread.currentThread().contextClassLoader.getResource(path)
    requireNotNull(resource) { "Resource $path not found" }
    return resource.openStream().use(ImageIO::read)
}