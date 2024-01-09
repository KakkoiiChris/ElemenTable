package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Button
import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.input.Key
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.State
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.AlphaComposite
import java.awt.image.BufferedImage

object MainState : State {
    private const val FADE_ALPHA_DELTA = 0.025
    private const val ATOM_THETA_DELTA = 0.01

    private enum class SubState {
        FadeIn,
        Expand,
        SlideDown,
        SlideOut,
        Table,
        ZoomIn,
        ZoomOut,
        Element
    }

    private var fadeAlpha = 1.0
    private var atomTheta = 0.0

    private var state = SubState.FadeIn

    private lateinit var screenShot: BufferedImage

    private val atom = Resources.icon

    private var selected: Element? = null

    override fun swapTo(view: View) {
        screenShot = view.getScreenshot()
    }

    override fun swapFrom(view: View) {
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        atomTheta += ATOM_THETA_DELTA * time.delta

        when (state) {
            SubState.FadeIn    -> {
                fadeAlpha -= FADE_ALPHA_DELTA * time.delta

                if (fadeAlpha <= 0.0) {
                    fadeAlpha = 0.0

                    Table.expand()

                    state = SubState.Expand
                }
            }

            SubState.Expand    -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    Table.slideDown()

                    state = SubState.SlideDown
                }
            }

            SubState.SlideDown -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    Table.slideOut()

                    state = SubState.SlideOut
                }
            }

            SubState.SlideOut  -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    state = SubState.Table
                }
            }

            SubState.Table     -> {
                if (input.keyDown(Key.SPACE)) {
                    Labels.toggleMode()
                }

                Table.update(view, manager, time, input)

                Labels.update(view, manager, time, input)

                if (input.buttonDown(Button.LEFT)) {
                    selected?.hidden = false

                    selected = Table.selectElement(input.mouse)

                    selected?.hidden = true
                }
            }

            SubState.ZoomIn    -> {

            }

            SubState.ZoomOut   -> {}

            SubState.Element   -> {}
        }
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            color = bgColor

            fillRect(0, 0, view.width, view.height)

            push()
            translate(ELEMENT_SIZE / 2, ELEMENT_SIZE / 2)
            rotate(atomTheta)

            drawImage(atom, -ELEMENT_SIZE / 2, -ELEMENT_SIZE / 2, ELEMENT_SIZE, ELEMENT_SIZE)

            pop()

            Table.render(view, renderer)

            when (state) {
                SubState.FadeIn -> {
                    push()

                    composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, fadeAlpha.toFloat())

                    drawImage(screenShot, 0, 0)

                    pop()
                }

                SubState.Table  -> Labels.render(view, renderer)

                else            -> Unit
            }
        }
    }

    override fun halt(view: View) {
    }
}