package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.media.Sprite
import kakkoiichris.hypergame.state.State
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.Color
import java.awt.Font

object IntroState : State {
    private enum class SubState {
        FadeInAtom {
            override val next get() = FadeInTitle
        },

        FadeInTitle {
            override val next get() = Idle
        },

        Idle {
            override val next get() = FadeOutTitle
        },

        FadeOutTitle {
            override val next get() = FadeOutAtom
        },

        FadeOutAtom {
            override val next get() = FadeOutAtom
        };

        companion object {
            var current = FadeInAtom

            fun next() {
                current = current.next
            }
        }

        abstract val next: SubState
    }

    private val atom = Sprite.load("/resources/img/icon.png")
    private val titleFont = Font("Chemical Reaction A BRK", Font.BOLD, 100)
    private val versionFont = Font("Chemical Reaction B BRK", Font.PLAIN, 50)

    private const val ATOM_THETA_DELTA = 0.01
    private var atomTheta = 0.0

    private const val ATOM_ALPHA_DELTA = 0.01
    private var atomAlpha = 0.0

    private const val TITLE_ALPHA_DELTA = 0.01
    private var titleAlpha = 0.0

    private const val IDLE_TIME = 3.0
    private var idleTimer = 0.0

    override fun swapTo(view: View) {
    }

    override fun swapFrom(view: View) {
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        atomTheta += ATOM_THETA_DELTA

        when (SubState.current) {
            SubState.FadeInAtom   -> {
                atomAlpha += time.delta * ATOM_ALPHA_DELTA

                if (atomAlpha >= 1.0) {
                    atomAlpha = 1.0

                    SubState.next()
                }
            }

            SubState.FadeInTitle  -> {
                titleAlpha += time.delta * TITLE_ALPHA_DELTA

                if (titleAlpha >= 1.0) {
                    titleAlpha = 1.0

                    SubState.next()
                }
            }

            SubState.Idle         -> {
                idleTimer += time.seconds

                if (idleTimer >= IDLE_TIME) {
                    SubState.next()
                }
            }

            SubState.FadeOutTitle -> {
                titleAlpha -= time.delta * TITLE_ALPHA_DELTA

                if (titleAlpha <= 0.0) {
                    titleAlpha = 0.0

                    SubState.next()
                }
            }

            SubState.FadeOutAtom  -> {
                atomAlpha -= time.delta * ATOM_ALPHA_DELTA

                if (atomAlpha <= 0.0) {
                    atomAlpha = 0.0

                    manager.goto(TableState)
                }
            }
        }
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            color = Color.BLACK

            fillRect(0, 0, view.width, view.height)

            push()

            translate(view.width / 2, view.height / 2)
            rotate(atomTheta)

            drawImage(atom, -atom.width / 2, -atom.height / 2)

            pop()

            when (SubState.current) {
                SubState.FadeInAtom, SubState.FadeOutAtom                  -> {
                    color = Color(0, 0, 0, 255 - (atomAlpha * 255).toInt())

                    fillRect(0, 0, view.width, view.height)
                }

                SubState.FadeInTitle, SubState.Idle, SubState.FadeOutTitle -> {
                    font = titleFont

                    val titleWidth = fontMetrics.stringWidth(TITLE)

                    color = Color(0, 0, 0, (titleAlpha * 255).toInt())

                    drawString(TITLE, (view.width - titleWidth) / 2, (view.height - fontMetrics.height) / 2)
                }
            }
        }
    }

    override fun halt(view: View) {
    }
}