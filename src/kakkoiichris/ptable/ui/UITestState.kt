package kakkoiichris.ptable.ui

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.input.Key
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.State
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.Color

object UITestState : State {
    private val main = Panel()
    
    override fun swapTo(view: View) {
        main.setBounds(0.0, 0.0, view.width.toDouble(), view.height.toDouble())
        main.layout = Layout.Fixed
        main.padding = 20U
        
        for (i in 0..5) {
            val component = Button()
            component.text = "Button ${i + 1}"
            component.setBounds(i * 50.0, i * 50.0, main.width / 2, main.height / 2)
//            subPanel.marginTop = (randomDouble() * 50).toUInt()
//            subPanel.marginRight = (randomDouble() * 50).toUInt()
//            subPanel.marginBottom = (randomDouble() * 50).toUInt()
//            subPanel.marginLeft = (randomDouble() * 50).toUInt()
            val color = Color((Math.random() * 0xFFFFFF).toInt())
            component.background = color
            component.accent = color.brighter()
            component.eventListener = { event -> println((event.source as Button).text) }
            main.add(component)
        }
    }

    override fun swapFrom(view: View) {
    }
    
    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        if (input.keyDown(Key.SPACE)) {
            main.layout = Layout.Border()
        }
        
        //main.top += time.delta
        
        main.update(view, manager, time, input)
    }
    
    override fun render(view: View, renderer: Renderer) {
        main.render(view, renderer)
    }
    
    override fun halt(view: View) {
    }
}