package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Button
import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.media.Sprite
import kakkoiichris.hypergame.media.withAlpha
import kakkoiichris.hypergame.state.State
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.AlphaComposite
import java.awt.Font
import java.awt.RenderingHints

object IntroState : State {
    private const val ATOM_THETA_DELTA = 0.01
    private const val ALPHA_DELTA = 0.025
    private const val IDLE_TIME = 3.0

    private enum class SubState {
        FadeInAtom,
        FadeInTitle,
        FadeInVersion,
        Pause,
        FadeOut
    }

    private var state = SubState.FadeInAtom

    private val atom = Resources.icon

    private val titleFont = Font(Resources.boogie, Font.BOLD, 150)
    private val versionFont = Font("Courier New", Font.BOLD, 75)

    private var atomTheta = 0.0
    private var atomAlpha = 0.0
    private var titleAlpha = 0.0
    private var versionAlpha = 0.0
    private var idleTimer = 0.0

    override fun swapTo(view: View) {
        view.renderer.addRenderingHints(
            mapOf(
                RenderingHints.KEY_ANTIALIASING to RenderingHints.VALUE_ANTIALIAS_ON,
                RenderingHints.KEY_TEXT_ANTIALIASING to RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB,
            )
        )
    }

    override fun swapFrom(view: View) {
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        if (input.buttonDown(Button.LEFT)) {
            manager.goto(MainState)

            return
        }

        atomTheta += ATOM_THETA_DELTA * time.delta

        when (state) {
            SubState.FadeInAtom    -> {
                atomAlpha += ALPHA_DELTA * time.delta

                if (atomAlpha >= 1.0) {
                    atomAlpha = 1.0

                    state = SubState.FadeInTitle
                }
            }

            SubState.FadeInTitle   -> {
                titleAlpha += ALPHA_DELTA * time.delta

                if (titleAlpha >= 1.0) {
                    titleAlpha = 1.0

                    state = SubState.FadeInVersion
                }
            }

            SubState.FadeInVersion -> {
                versionAlpha += ALPHA_DELTA * time.delta

                if (versionAlpha >= 1.0) {
                    versionAlpha = 1.0

                    state = SubState.Pause
                }
            }

            SubState.Pause         -> {
                idleTimer += time.seconds

                if (idleTimer >= IDLE_TIME) {
                    state = SubState.FadeOut
                }
            }

            SubState.FadeOut       -> {
                titleAlpha -= ALPHA_DELTA * time.delta
                versionAlpha -= ALPHA_DELTA * time.delta

                if (titleAlpha <= 0.0 || versionAlpha <= 0.0) {
                    titleAlpha = 0.0
                    versionAlpha = 0.0

                    manager.goto(MainState)
                }
            }
        }
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            color = bgColor

            fillRect(0, 0, view.width, view.height)

            push()
            translate(view.width / 2, view.height / 2)
            rotate(atomTheta)

            composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, atomAlpha.toFloat())

            drawImage(atom, -view.height / 2, -view.height / 2, view.height, view.height)

            pop()

            when (state) {
                SubState.FadeInTitle, SubState.FadeInVersion, SubState.Pause, SubState.FadeOut -> {
                    font = titleFont

                    val titleWidth = fontMetrics.stringWidth(TITLE)

                    color = fgDark.withAlpha(titleAlpha)

                    drawString(TITLE, (view.width - titleWidth) / 2, (view.height - fontMetrics.height) / 2)

                    font = versionFont

                    val versionWidth = fontMetrics.stringWidth(VERSION)

                    color = fgDark.withAlpha(versionAlpha)

                    drawString(VERSION, (view.width - versionWidth) / 2, view.height / 2)
                }

                else                                                                           -> Unit
            }
        }
    }

    override fun halt(view: View) {
    }
}