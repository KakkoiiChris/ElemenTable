package kakkoiichris.ptable

import kakkoiichris.hypergame.util.filesystem.ResourceManager

object Resources {
    private val resources = ResourceManager("/resources")

    private val dat = resources.getFolder("dat")

    val elements = dat.getJSON("elements")

    private val img = resources.getFolder("img")

    val icon = img.getSprite("icon")
    val gear = img.getSprite("gear")

    private val ttf = resources.getFolder("ttf")

    val boogie = ttf.getFont("boogieBoys")
}