package kakkoiichris.ptable

import kakkoiichris.hypergame.util.filesystem.ResourceManager
import kakkoiichris.hypergame.view.Display
import java.awt.Color
import javax.imageio.ImageIO

const val ELEMENT_SIZE = 64
const val BORDER = ELEMENT_SIZE / 2
const val COLUMNS = 18
const val ROWS = 10
const val DISPLAY_WIDTH = (ELEMENT_SIZE * COLUMNS) + (BORDER * 3)
const val DISPLAY_HEIGHT = (ELEMENT_SIZE * ROWS) + (BORDER * 5)
const val TITLE = "ElemenTable"
const val VERSION = "Beta 3.8"

val fgLight = Color(234, 230, 216)
val fgDark = Color(48, 43, 43)
val bgColor = Color(61, 60, 57)

val resources = ResourceManager("/resources")

fun main() {
    Table.init()

    val icon = ImageIO.read(Table::class.java.getResource("/resources/img/icon.png"))

    val display = Display(DISPLAY_WIDTH, DISPLAY_HEIGHT, title = TITLE, icon = icon)

    display.manager.goto(IntroState)

    display.open()
}