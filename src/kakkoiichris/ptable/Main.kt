package kakkoiichris.ptable

import kakkoiichris.hypergame.util.filesystem.ResourceManager
import kakkoiichris.hypergame.view.Display
import kakkoiichris.ptable.ui.UITestState
import javax.imageio.ImageIO

const val ELEMENT_SIZE = 58
const val BORDER = ELEMENT_SIZE / 2
const val COLUMNS = 18
const val ROWS = 10
const val DISPLAY_WIDTH = (ELEMENT_SIZE * COLUMNS) + (BORDER * 3)
const val DISPLAY_HEIGHT = (ELEMENT_SIZE * ROWS) + (BORDER * 5)
const val TITLE = "pTable"
const val VERSION = "BETA 3.8"

const val STATE_INTRO = "intro"
const val STATE_TABLE = "table"

val resources = ResourceManager("/resources")

fun main() {
    Table.init()

    val icon = ImageIO.read(Table::class.java.getResource("/resources/img/icon.png"))

    val display = Display(DISPLAY_WIDTH, DISPLAY_HEIGHT, title = TITLE, icon = icon)

    display.manager += IntroState
    display.manager += TableState
    display.manager += UITestState

    display.open()
}