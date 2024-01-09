package kakkoiichris.ptable

import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.util.data.json.JSONMember
import kakkoiichris.hypergame.util.math.Vector
import kakkoiichris.hypergame.view.View
import kakkoiichris.ptable.ElementColor.Category.Actinide
import kakkoiichris.ptable.ElementColor.Category.Lanthanide
import java.awt.BasicStroke

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
    category: String? = null,

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
    override val xPos: Double,

    @JSONMember("ypos")
    override val yPos: Double,

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
) : TableCell() {
    val category = ElementColor.Category[category ?: ""]

    private var elementColor = this.category

    override val isLaOrAc
        get() = category == Lanthanide || category == Actinide

    init {
        target = when (this.category) {
            Lanthanide -> Vector(
                3.0 * ELEMENT_SIZE,
                6.0 * ELEMENT_SIZE
            )

            Actinide   -> Vector(
                3.0 * ELEMENT_SIZE,
                7.0 * ELEMENT_SIZE
            )

            else       -> Vector(
                xPos * ELEMENT_SIZE,
                yPos * ELEMENT_SIZE
            )
        }
    }

    override fun render(view: View, renderer: Renderer) {
        if (hidden) return

        with(renderer) {
            color = elementColor.back

            if (highlighted) {
                color = color.brighter()
            }

            fillRect(this@Element)

            color = fgDark

            stroke = BasicStroke(2F)

            drawRect(this@Element)

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
            th = fm.leading + fm.ascent

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + (height / 2) + (th / 2)).toInt())

            font = massFont

            fm = fontMetrics

            text = String.format("%.3f", atomicMass)
            tw = fm.stringWidth(text)

            drawString(text, (x + ((width - tw) / 2)).toInt(), (y + height - fm.descent).toInt())
        }
    }
}