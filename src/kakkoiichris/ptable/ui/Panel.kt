package kakkoiichris.ptable.ui

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View

open class Panel(name: String = "") : Component(name) {
    var layout: Layout = Layout.None
    
    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        layout(this, componentList)
        
        super.update(view, manager, time, input)
    }
}