package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.math.Box
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.view.View
import java.awt.Font

sealed class TableCell : Box(
    (DISPLAY_WIDTH - ELEMENT_SIZE) / 2.0,
    (DISPLAY_HEIGHT - ELEMENT_SIZE) / 2.0,
    ELEMENT_SIZE.toDouble() - 1,
    ELEMENT_SIZE.toDouble() - 1
), Renderable {
    companion object {
        val numberFont = Font("Courier New", Font.PLAIN, 16)
        val symbolFont = Font("Courier New", Font.BOLD, 25)
        val massFont = Font("Courier New", Font.ITALIC, 12)
    }

    abstract val xPos: Double
    abstract val yPos: Double
    abstract val isLaOrAc: Boolean

    var target = Vector()

    var hidden = false
    var expanding = false
    var highlighted = false

    fun slideDown() {
        if (isLaOrAc) {
            target.y = yPos * ELEMENT_SIZE

            expanding = true
        }
    }

    fun slideOut() {
        if (isLaOrAc) {
            target.x = xPos * ELEMENT_SIZE

            expanding = true
        }
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        if (expanding) {
            position += (target - position) * 0.1

            if (position.distanceTo(target) <= 0.1) {
                position = target

                expanding = false
            }

            return
        }

        highlighted = input.mouse in this
    }
}