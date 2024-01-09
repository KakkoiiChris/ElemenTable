package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.media.withAlpha
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.Font

object Labels : Renderable {
    private const val DELTA_ALPHA = 0.1F

    enum class Mode {
        Numbers {
            override val other get() = Numerals
        },

        Numerals {
            override val other get() = Numbers
        };

        abstract val other: Mode
    }

    enum class SubState {
        Hidden,
        FadeInMain,
        Idle,
        FadeOutMain,
        FadeInGroup,
        FadeOutGroup
    }

    private val numerals = arrayOf(
        "IA",
        "IIA",
        "IIIB",
        "IVB",
        "VB",
        "VIB",
        "VIIB",
        "VIIIB",
        "IB",
        "IIB",
        "IIIA",
        "IVA",
        "VA",
        "VIA",
        "VIIA",
        "VIIIA"
    )

    private val font = Font("Monospaced", Font.BOLD, BORDER - 13)

    private var state = SubState.FadeInMain

    private var mode = Mode.Numbers
    var nextMode: Mode? = null

    private var mainAlpha = 0.0
    private var groupAlpha = 1.0

    fun toggleMode() {
        state = SubState.FadeOutGroup
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        when (state) {
            SubState.Hidden, SubState.Idle -> Unit

            SubState.FadeInMain            -> {
                mainAlpha += DELTA_ALPHA * time.delta

                if (mainAlpha >= 1.0) {
                    mainAlpha = 1.0

                    state = SubState.Idle
                }
            }

            SubState.FadeOutMain           -> {
                mainAlpha -= DELTA_ALPHA * time.delta

                if (mainAlpha <= 0.0) {
                    mainAlpha = 0.0

                    state = SubState.Hidden
                }
            }

            SubState.FadeInGroup           -> {
                groupAlpha += DELTA_ALPHA * time.delta

                if (groupAlpha >= 1.0) {
                    groupAlpha = 1.0

                    state = SubState.Idle
                }
            }

            SubState.FadeOutGroup          -> {
                groupAlpha -= DELTA_ALPHA * time.delta

                if (groupAlpha <= 0.0) {
                    groupAlpha = 0.0

                    mode = mode.other

                    state = SubState.FadeInGroup
                }
            }
        }
    }

    override fun render(view: View, renderer: Renderer) {
        renderer.color = fgLight.withAlpha(mainAlpha)
        renderer.font = font

        val fm = renderer.getFontMetrics(font)

        var labelWidth = fm.stringWidth("Groups")

        val groupsX = ELEMENT_SIZE * 9 + BORDER * 2 - labelWidth / 2

        renderer.drawString("Groups", groupsX, 20)

        labelWidth = fm.stringWidth("Periods")

        val periodsY = ELEMENT_SIZE * 7 / 2 + BORDER * 2 + labelWidth / 2

        renderer.push()
        renderer.translate(20, periodsY)
        renderer.rotate(-Math.PI / 2)

        renderer.drawString("Periods", 0, 0)

        renderer.pop()

        for (i in 0..7) {
            renderer.drawRect(BORDER, BORDER * 2 + i * ELEMENT_SIZE, BORDER, ELEMENT_SIZE)

            val label = "${i + 1}"

            labelWidth = fm.stringWidth(label)

            renderer.drawString(
                label,
                BORDER + (BORDER - labelWidth) / 2,
                BORDER * 2 + i * ELEMENT_SIZE + ELEMENT_SIZE / 2 + 7
            )
        }

        renderer.color = fgLight.withAlpha(groupAlpha)

        when (mode) {
            Mode.Numbers  -> {
                for (i in 0 until 18) {
                    renderer.drawRect(BORDER * 2 + i * ELEMENT_SIZE, BORDER, ELEMENT_SIZE, BORDER)

                    val label = "${i + 1}"

                    labelWidth = fm.stringWidth(label)

                    renderer.drawString(
                        label,
                        BORDER * 2 + i * ELEMENT_SIZE + (ELEMENT_SIZE - labelWidth) / 2,
                        BORDER * 2 - 8
                    )
                }
            }

            Mode.Numerals -> {
                var xOffset = 0

                for (i in numerals.indices) {
                    if (numerals[i] == "VIIIB") {
                        renderer.drawRect(
                            BORDER * 2 + i * ELEMENT_SIZE + xOffset, BORDER,
                            ELEMENT_SIZE * 3, BORDER
                        )

                        labelWidth = fm.stringWidth(numerals[i])

                        renderer.drawString(
                            numerals[i], BORDER * 2 + i * ELEMENT_SIZE
                                + (ELEMENT_SIZE * 3 - labelWidth) / 2 + xOffset, BORDER * 2 - 8
                        )

                        xOffset = ELEMENT_SIZE * 2
                    }
                    else {
                        renderer.drawRect(
                            BORDER * 2 + i * ELEMENT_SIZE + xOffset, BORDER,
                            ELEMENT_SIZE, BORDER
                        )

                        labelWidth = fm.stringWidth(numerals[i])

                        renderer.drawString(
                            numerals[i], BORDER * 2 + i * ELEMENT_SIZE
                                + (ELEMENT_SIZE - labelWidth) / 2 + xOffset, BORDER * 2 - 8
                        )
                    }
                }
            }
        }
    }
}