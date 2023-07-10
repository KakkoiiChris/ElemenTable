package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.data.json.JSONMember
import kakkoiichris.hypergame.util.math.Box
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font

class Element(
    @JSONMember("name")
    val name: String?,

    @JSONMember("appearance")
    val appearance: String?,

    @JSONMember("atomic_mass")
    val atomicMass: Double?,

    @JSONMember("boil")
    val boil: Double?,

    @JSONMember("category")
    val category: String?,

    @JSONMember("density")
    val density: Double?,

    @JSONMember("discovered_by")
    val discoveredBy: String?,

    @JSONMember("melt")
    val melt: Double?,

    @JSONMember("molar_heat")
    val molarHeat: Double?,

    @JSONMember("named_by")
    val namedBy: String?,

    @JSONMember("number")
    val number: Int?,

    @JSONMember("period")
    val period: Int?,

    @JSONMember("phase")
    val phase: String?,

    @JSONMember("source")
    val source: String?,

    @JSONMember("spectral_img")
    val spectralImage: String?,

    @JSONMember("summary")
    val summary: String?,

    @JSONMember("symbol")
    val symbol: String?,

    @JSONMember("xpos")
    val xPos: Int?,

    @JSONMember("ypos")
    val yPos: Int?,

    @JSONMember("shells")
    val shells: IntArray?,

    @JSONMember("electron_configuration")
    val electronConfiguration: String?,

    @JSONMember("electron_affinity")
    val electronAffinity: Double?,

    @JSONMember("electronegativity_pauling")
    val electronegativityPauling: Double?,

    @JSONMember("ionization_energies")
    val ionizationEnergies: DoubleArray?,
) : Box(
    (DISPLAY_WIDTH - ELEMENT_SIZE) / 2.0,
    (DISPLAY_HEIGHT - ELEMENT_SIZE) / 2.0,
    ELEMENT_SIZE.toDouble() - 1,
    ELEMENT_SIZE.toDouble() - 1
), Renderable {
    companion object {
        private val numberFont = Font("Consolas", Font.PLAIN, 12)
        private val symbolFont = Font("Chemical Reaction A BRK", Font.BOLD, 24)
        private val massFont = Font("Consolas", Font.ITALIC, 10)
    }

    private var target: Vector

    private var elementColor = Category[category ?: ""]

    var expanding = false
    var highlighted = false

    private val isLaOrAc get() = category in arrayOf("lanthanide", "actinide")

    init {
        target = when (category) {
            "lanthanide" -> Vector(
                3.0 * ELEMENT_SIZE,
                6.0 * ELEMENT_SIZE
            )

            "actinide"   -> Vector(
                3.0 * ELEMENT_SIZE,
                7.0 * ELEMENT_SIZE
            )

            else         -> Vector(
                (xPos?.toDouble() ?: -1.0) * ELEMENT_SIZE,
                (yPos?.toDouble() ?: -1.0) * ELEMENT_SIZE
            )
        }
    }

    fun slideDown() {
        if (isLaOrAc) {
            target.y = (yPos?.toDouble() ?: -1.0) * ELEMENT_SIZE

            expanding = true
        }
    }

    fun slideOut() {
        if (isLaOrAc) {
            target.x = (xPos?.toDouble() ?: -1.0) * ELEMENT_SIZE

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
        }
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            color = elementColor.value

            fill(rectangle)

            color = Color.BLACK

            if (highlighted) {
                color = color.brighter()
            }

            stroke = BasicStroke(2F)

            draw(rectangle)

            font = numberFont

            var fm = fontMetrics

            var text = number?.toString() ?: "???"
            var tw = fm.stringWidth(text)
            var th = fm.descent + fm.leading + fm.ascent

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + th).toInt())

            font = symbolFont

            fm = fontMetrics

            text = symbol ?: "???"
            tw = fm.stringWidth(text)
            th = fm.descent + fm.leading + fm.ascent

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + (height / 2) + (th / 2)).toInt())

            font = massFont

            fm = fontMetrics

            text = String.format("%.3f", atomicMass)
            tw = fm.stringWidth(text)
            th = fm.descent + fm.leading + fm.ascent

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + height - fm.descent).toInt())
        }
    }

    private interface ElementColor {
        val value: Color
    }

    enum class Category(override val value: Color) : ElementColor {
        AlkaliMetal(Color(0, 191, 255)),
        AlkalineEarthMetal(Color(255, 127, 0)),
        Lanthanide(Color(191, 127, 255)),
        TransitionMetal(Color(0, 255, 0)),
        PostTransitionMetal(Color(255, 127, 127)),
        Actinide(Color(255, 0, 127)),
        PolyatomicNonmetal(Color(255, 255, 63)),
        DiatomicNonmetal(Color(255, 0, 0)),
        NobleGas(Color(0, 255, 127)),
        Metalloid(Color(127, 127, 255)),
        Unknown(Color(255, 255, 255));

        companion object {
            operator fun get(name: String): Category {
                val entryName = name
                    .split("(\\s+|-)".toRegex())
                    .joinToString(separator = "") { it.capitalized() }

                return values().firstOrNull { it.name == entryName } ?: Unknown
            }
        }
    }

    enum class State(override val value: Color) : ElementColor {
        Solid(Color(255, 63, 63)),
        Liquid(Color(63, 255, 63)),
        Gas(Color(63, 63, 255)),
        Unknown(Color(127, 127, 127))
    }

    enum class Block(override val value: Color) : ElementColor {
        S(Color(255, 0, 127)),
        P(Color(255, 127, 0)),
        D(Color(127, 255, 0)),
        F(Color(0, 127, 255))
    }
}