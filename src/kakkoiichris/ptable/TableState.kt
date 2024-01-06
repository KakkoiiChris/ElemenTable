package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.input.Key
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.State
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View

object TableState : State {
    private enum class SubState {
        FadeIn {
            override val next get() = Expanding
        },

        Expanding {
            override val next get() = SlideDown
        },

        SlideDown {
            override val next get() = SlideOut
        },

        SlideOut {
            override val next get() = Main
        },

        Main {
            override val next get() = Main
        };

        companion object {
            var current = FadeIn

            fun next() {
                current = current.next
            }
        }

        abstract val next: SubState
    }

    private const val FADE_ALPHA_DELTA = 0.01
    private var fadeAlpha = 1.0

    override fun swapTo(view: View) {
    }

    override fun swapFrom(view: View) {
    }

    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        when (SubState.current) {
            SubState.FadeIn    -> {
                fadeAlpha -= time.delta * FADE_ALPHA_DELTA

                if (fadeAlpha <= 0.0) {
                    fadeAlpha = 0.0

                    Table.expand()

                    SubState.next()
                }
            }

            SubState.Expanding -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    Table.slideDown()

                    SubState.next()
                }
            }

            SubState.SlideDown -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    Table.slideOut()

                    SubState.next()
                }
            }

            SubState.SlideOut  -> {
                Table.update(view, manager, time, input)

                if (!Table.expanding) {
                    SubState.next()
                }
            }

            SubState.Main      -> {
                if (input.keyDown(Key.SPACE)) {
                    Labels.nextMode = Labels.Mode.Numerals
                }

                Table.update(view, manager, time, input)

                Labels.update(view, manager, time, input)
            }
        }
    }

    override fun render(view: View, renderer: Renderer) {
        with(renderer) {
            color = bgColor

            fillRect(0, 0, view.width, view.height)

            Table.render(view, renderer)

            when (SubState.current) {
                SubState.Main -> Labels.render(view, renderer)

                else          -> Unit
            }
        }
    }

    override fun halt(view: View) {
    }
}