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

    private const val FADE_ALPHA_DELTA = 0.01
    private var fadeAlpha = 1.0

    private var state = SubState.FadeIn

    private lateinit var screenShot: BufferedImage

    private var selected: Element? = null

    override fun swapTo(view: View) {
        screenShot = view.getScreenshot()
    }

    override fun swapFrom(view: View) {
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        when (state) {
            SubState.FadeIn    -> {
                fadeAlpha -= time.delta * FADE_ALPHA_DELTA

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
                    Labels.nextMode = Labels.Mode.Numerals
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