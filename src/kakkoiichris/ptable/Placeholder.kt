package kakkoiichris.ptable

import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke

class Placeholder private constructor(
    private val symbol: String,
    override val xPos: Double,
    override val yPos: Double
) : TableCell() {
    companion object {
        fun of(element: Element) =
            Placeholder(element.symbol ?: "?", element.xPos, element.yPos)
                .apply { setBounds(element) }
    }

    override val isLaOrAc
        get() = false

    override fun render(view: View, renderer: Renderer) {
        if (hidden) return

        with(renderer) {
            color = fgDark

            stroke = BasicStroke(2F)

            drawRect(this@Placeholder)

            color = fgLight

            font = symbolFont

            val fm = fontMetrics

            val text = symbol
            val tw = fm.stringWidth(text)
            val th = fm.leading + fm.ascent

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + (height / 2) + (th / 2)).toInt())
        }
    }
}