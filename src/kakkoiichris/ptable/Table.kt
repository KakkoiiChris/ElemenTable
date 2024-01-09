package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke
import java.awt.Color

object Table : Renderable {
    private lateinit var cells: List<TableCell>

    private lateinit var metalloidLineElements: List<TableCell>

    val expanding get() = cells.any(TableCell::expanding)

    fun init() {
        val data = resources.getFolder("json").getJSON("elements")

        val elementsData = data["elements"]
            ?.asObjectArrayOrNull()
            ?: error("Elements not found!")

        val elements = elementsData
            .reversed()
            .map { it.create(Element::class) ?: error("Couldn't create element!") }

        val la = elements.first { it.symbol == "La" }

        val laPlaceholder = Placeholder.of(la).apply {
            target = Vector(
                3.0 * ELEMENT_SIZE,
                6.0 * ELEMENT_SIZE
            )
        }

        val ac = elements.first { it.symbol == "Ac" }

        val acPlaceholder = Placeholder.of(ac).apply {
            target = Vector(
                3.0 * ELEMENT_SIZE,
                7.0 * ELEMENT_SIZE
            )
        }

        cells = listOf(
            laPlaceholder,
            acPlaceholder,
            *elements.toTypedArray()
        )

        val metalloidLineElementList = mutableListOf<TableCell>()

        for (number in arrayOf(13, 14, 32, 33, 51, 52, 84, 85, 117)) {
            metalloidLineElementList += elements.first { it.number == number }
        }

        metalloidLineElements = metalloidLineElementList.toList()
    }

    fun expand() {
        cells.forEach { it.expanding = true }
    }

    fun slideDown() {
        cells.forEach { it.slideDown() }
    }

    fun slideOut() {
        cells.forEach { it.slideOut() }
    }

    fun selectElement(mouse: Vector) =
        cells
            .filterIsInstance<Element>()
            .firstOrNull { mouse in it }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        for (element in cells) {
            element.update(view, manager, time, input)
        }
    }

    override fun render(view: View, renderer: Renderer) {
        for (element in cells) {
            element.render(view, renderer)
        }

        renderer.color = Color.YELLOW
        renderer.stroke = BasicStroke(5F)

        for (i in 0 until metalloidLineElements.size - 1) {
            val a = metalloidLineElements[i]
            val b = metalloidLineElements[i + 1]

            renderer.drawLine(a.position, b.position)
        }
    }
}