package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke

object Table : Renderable {
    private lateinit var elements: List<Element>

    private lateinit var metalloidLineElements: List<Element>

    val expanding get() = elements.any(Element::expanding)

    fun init() {
        val data = resources.getFolder("json").getJSON("elements")

        val elementsData = data["elements"]?.asObjectArrayOrNull() ?: error("Elements not found!")

        elements = elementsData.reversed().map {
            it.create(Element::class) ?: error("Couldn't create element!")
        }.toMutableList().apply {
            add(0, Element.Placeholder("La").apply {
                target = Vector(
                    3.0 * ELEMENT_SIZE,
                    6.0 * ELEMENT_SIZE
                )
            })

            add(0, Element.Placeholder("Ac").apply {
                target = Vector(
                    3.0 * ELEMENT_SIZE,
                    7.0 * ELEMENT_SIZE
                )
            })
        }.toList()

        val metalloidLineElementList = mutableListOf<Element>()

        for (number in arrayOf(5, 13, 14, 32, 33, 51, 52, 84, 85, 117)) {
            metalloidLineElementList += elements.first { it.number == number }
        }

        metalloidLineElements = metalloidLineElementList.toList()
    }

    fun expand() {
        elements.forEach { it.expanding = true }
    }

    fun slideDown() {
        elements.forEach { it.slideDown() }
    }

    fun slideOut() {
        elements.forEach { it.slideOut() }
    }

    fun selectElement(mouse: Vector) =
        elements.firstOrNull { it.category != "Placeholder" && mouse in it }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        for (element in elements) {
            element.update(view, manager, time, input)
        }
    }

    override fun render(view: View, renderer: Renderer) {
        for (element in elements) {
            element.render(view, renderer)
        }

        renderer.color = fgLight
        renderer.stroke = BasicStroke(5F)

        for (i in 0 until metalloidLineElements.size - 1) {
            val a = metalloidLineElements[i]
            val b = metalloidLineElements[i + 1]

            renderer.drawLine(a.position, b.position)
        }
    }
}