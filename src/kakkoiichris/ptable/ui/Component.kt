package kakkoiichris.ptable.ui

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.util.math.Box
import kakkoiichris.hypergame.view.View
import java.awt.Color

open class Component(var name: String = "") : Box(0.0, 0.0, 0.0, 0.0), Renderable {
    var parent: Component? = null
    
    private val components = mutableListOf<Component>()
    
    val componentList get() = components.toList()
    
    var padding = 0U
        set(value) {
            field = value
            paddingTop = value
            paddingRight = value
            paddingBottom = value
            paddingLeft = value
        }
    
    val paddingVertical get() = paddingTop + paddingBottom
    val paddingHorizontal get() = paddingLeft + paddingRight
    
    var paddingTop = 0U
    var paddingRight = 0U
    var paddingBottom = 0U
    var paddingLeft = 0U
    
    var margin = 0U
        set(value) {
            field = value
            marginTop = value
            marginRight = value
            marginBottom = value
            marginLeft = value
        }
    
    val marginVertical get() = marginTop + marginBottom
    val marginHorizontal get() = marginLeft + marginRight
    
    var marginTop = 0U
    var marginRight = 0U
    var marginBottom = 0U
    var marginLeft = 0U
    
    var foreground = Color.BLACK
    var background = Color.WHITE
    var accent = Color.GRAY
    
    fun setPaddings(top: UInt = 0U, right: UInt = 0U, bottom: UInt = 0U, left: UInt = 0U) {
        paddingTop = top
        paddingRight = right
        paddingBottom = bottom
        paddingLeft = left
    }
    
    fun setMargins(top: UInt = 0U, right: UInt = 0U, bottom: UInt = 0U, left: UInt = 0U) {
        marginTop = top
        marginRight = right
        marginBottom = bottom
        marginLeft = left
    }
    
    fun add(component: Component, option: UInt = 0U) {
        component.parent = this
        
        if (option == 0U) {
            components += component
        }
        else {
            components[option.toInt() - 1] = component
        }
    }
    
    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        for (component in components) {
            component.update(view, manager, time, input)
        }
    }
    
    override fun render(view: View, renderer: Renderer) {
        renderer.color = background
        
        renderer.fill(rectangle)
        
        renderer.color = foreground
        
        renderer.drawRect(this)
        
        renderer.color = accent
        
        renderer.drawRect(copy().apply { resize(-25.0) })
        
        for (component in components) {
            component.render(view, renderer)
        }
    }
}