package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.data.json.JSONMember
import kakkoiichris.hypergame.util.math.Box
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.util.math.tween
import kakkoiichris.hypergame.view.View
import java.awt.BasicStroke
import java.awt.Color
import java.awt.Font

class Element(
    @JSONMember("name")
    val name: String? = null,

    @JSONMember("appearance")
    val appearance: String? = null,

    @JSONMember("atomic_mass")
    val atomicMass: Double? = null,

    @JSONMember("boil")
    val boil: Double? = null,

    @JSONMember("category")
    val category: String? = null,

    @JSONMember("density")
    val density: Double? = null,

    @JSONMember("discovered_by")
    val discoveredBy: String? = null,

    @JSONMember("melt")
    val melt: Double? = null,

    @JSONMember("molar_heat")
    val molarHeat: Double? = null,

    @JSONMember("named_by")
    val namedBy: String? = null,

    @JSONMember("number")
    val number: Int? = null,

    @JSONMember("period")
    val period: Double? = null,

    @JSONMember("phase")
    val phase: String? = null,

    @JSONMember("source")
    val source: String? = null,

    @JSONMember("spectral_img")
    val spectralImage: String? = null,

    @JSONMember("summary")
    val summary: String? = null,

    @JSONMember("symbol")
    val symbol: String? = null,

    @JSONMember("xpos")
    val xPos: Double? = null,

    @JSONMember("ypos")
    val yPos: Double? = null,

    @JSONMember("shells")
    val shells: DoubleArray? = null,

    @JSONMember("electron_configuration")
    val electronConfiguration: String? = null,

    @JSONMember("electron_affinity")
    val electronAffinity: Double? = null,

    @JSONMember("electronegativity_pauling")
    val electronegativityPauling: Double? = null,

    @JSONMember("ionization_energies")
    val ionizationEnergies: DoubleArray? = null,
) : Box(
    (DISPLAY_WIDTH - ELEMENT_SIZE) / 2.0,
    (DISPLAY_HEIGHT - ELEMENT_SIZE) / 2.0,
    ELEMENT_SIZE.toDouble() - 1,
    ELEMENT_SIZE.toDouble() - 1
), Renderable {
    var target = when (category) {
        "lanthanide" -> Vector(
            3.0 * ELEMENT_SIZE,
            6.0 * ELEMENT_SIZE
        )

        "actinide"   -> Vector(
            3.0 * ELEMENT_SIZE,
            7.0 * ELEMENT_SIZE
        )

        else         -> Vector(
            (xPos ?: -1.0) * ELEMENT_SIZE,
            (yPos ?: -1.0) * ELEMENT_SIZE
        )
    }

    private var elementColor = Category[category ?: ""]

    private var highlightScale = 1.0

    var expanding = false
    var highlighted = false

    private val isLaOrAc get() = category in arrayOf("lanthanide", "actinide")

    fun slideDown() {
        if (isLaOrAc) {
            target.y = (yPos ?: -1.0) * ELEMENT_SIZE

            expanding = true
        }
    }

    fun slideOut() {
        if (isLaOrAc) {
            target.x = (xPos ?: -1.0) * ELEMENT_SIZE

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

        highlightScale = highlightScale.tween(if (highlighted) 1.2 else 1.0, 0.3, 0.001)
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            push()

            translate(center)
            scale(highlightScale, highlightScale)
            translate(-dimensions / 2.0)

            color = elementColor.back

            if (highlighted) {
                color = color.brighter()
            }

            val localBounds = copy(x = 0.0, y = 0.0)

            val (x, y, width, height) = localBounds

            fillRect(localBounds)

            color = fgDark

            stroke = BasicStroke(2F)

            drawRect(localBounds)

            color = elementColor.fore

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

            pop()
        }
    }

    companion object {
        private val numberFont = Font("Consolas", Font.PLAIN, 12)
        private val symbolFont = Font("Chemical Reaction A BRK", Font.BOLD, 24)
        private val massFont = Font("Consolas", Font.ITALIC, 10)

        fun Placeholder(symbol: String) =
            Element(symbol = symbol, category = "Placeholder")
    }

    private interface ElementColor {
        val fore: Color
        val back: Color
    }

    enum class Category(override val fore: Color, override val back: Color) : ElementColor {
        AlkaliMetal(fgDark, Color(100, 185, 186)),
        AlkalineEarthMetal(fgDark, Color(204, 125, 49)),
        Lanthanide(fgLight, Color(103, 78, 167)),
        TransitionMetal(fgDark, Color(139, 143, 21)),
        PostTransitionMetal(fgDark, Color(239, 211, 216)),
        Actinide(fgDark, Color(218, 144, 169)),
        PolyatomicNonmetal(fgDark, Color(228, 187, 1)),
        DiatomicNonmetal(fgLight, Color(117, 17, 1)),
        NobleGas(fgDark, Color(228, 230, 104)),
        Metalloid(fgLight, Color(1, 110, 139)),
        Unknown(fgDark, Color(182, 188, 170)),
        Placeholder(fgLight, Color(0, 0, 0, 0));

        companion object {
            operator fun get(name: String): Category {
                val entryName = name
                    .split("(\\s+|-)".toRegex())
                    .joinToString(separator = "") { it.capitalized() }

                return entries.firstOrNull { it.name == entryName } ?: Unknown
            }
        }
    }

    enum class State(override val fore: Color, override val back: Color) : ElementColor {
        Solid(Color.BLACK, Color(255, 63, 63)),
        Liquid(Color.BLACK, Color(63, 255, 63)),
        Gas(Color.BLACK, Color(63, 63, 255)),
        Unknown(Color.BLACK, Color(127, 127, 127))
    }

    enum class Block(override val fore: Color, override val back: Color) : ElementColor {
        S(Color.BLACK, Color(255, 0, 127)),
        P(Color.BLACK, Color(255, 127, 0)),
        D(Color.BLACK, Color(127, 255, 0)),
        F(Color.BLACK, Color(0, 127, 255))
    }
}