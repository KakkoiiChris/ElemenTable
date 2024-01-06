package kakkoiichris.ptable

import kakkoiichris.hypergame.input.Input
import kakkoiichris.hypergame.media.Renderable
import kakkoiichris.hypergame.media.Renderer
import kakkoiichris.hypergame.state.StateManager
import kakkoiichris.hypergame.util.Time
import kakkoiichris.hypergame.view.View
import java.awt.AlphaComposite
import java.awt.Color
import java.awt.Font

object Labels : Renderable {
    enum class Mode {
        Numbers, Numerals
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
    
    private const val DELTA_ALPHA = 0.05F
    
    private val font = Font("Monospaced", Font.BOLD, BORDER - 13)
    
    private var mode = Mode.Numbers
    var nextMode: Mode? = null
    
    private var mainAlpha = 0F
    private var groupAlpha = 1F
    
    var visible = true
    
    override fun update(view: View, manager: StateManager, time: Time, input: Input) {
        if (visible) {
            if (mainAlpha < 1F) {
                mainAlpha += (DELTA_ALPHA * time.delta).toFloat()
                
                if (mainAlpha >= 1F) {
                    mainAlpha = 1F
                }
            }
            
            if (nextMode != null) {
                if (groupAlpha >= 0) {
                    groupAlpha -= DELTA_ALPHA
                    
                    if (groupAlpha < 0F) {
                        groupAlpha = 0F
                        mode = nextMode!!
                        nextMode = null
                    }
                }
            }
            else {
                if (groupAlpha < 255) {
                    groupAlpha += DELTA_ALPHA
                    
                    if (groupAlpha >= 1F) {
                        groupAlpha = 1F
                    }
                }
            }
        }
    }
    
    override fun render(view: View, renderer: Renderer) {
        val lastComposite = renderer.composite
        
        renderer.composite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, mainAlpha)
        
        renderer.color = Color.WHITE
        renderer.font = font
        
        val fm = renderer.getFontMetrics(font)
        
        var labelWidth = fm.stringWidth("Groups")
        
        val x = ELEMENT_SIZE * 18 / 2 + BORDER * 2 - labelWidth / 2
        
        renderer.drawString("Groups", x, 20)
        
        labelWidth = fm.stringWidth("Periods")
        
        val y = ELEMENT_SIZE * 7 / 2 + BORDER * 2 + labelWidth / 2
        
        renderer.translate(20, y)
        renderer.rotate(-Math.PI / 2)
        renderer.drawString("Periods", 0, 0)
        renderer.rotate(Math.PI / 2)
        renderer.translate(-20, -y)
        
        for (i in 0..7) {
            renderer.drawRect(BORDER, BORDER * 2 + i * ELEMENT_SIZE, BORDER, ELEMENT_SIZE)
            
            val label = "${i + 1}"
            
            labelWidth = fm.stringWidth(label)
            
            renderer.drawString(label,
                BORDER + (BORDER - labelWidth) / 2,
                BORDER * 2 + i * ELEMENT_SIZE + ELEMENT_SIZE / 2 + 7)
        }
        
        renderer.color = Color(255, 255, 255, (groupAlpha * 255).toInt())
        
        when (mode) {
            Mode.Numbers  -> {
                for (i in 0 until 18) {
                    renderer.drawRect(BORDER * 2 + i * ELEMENT_SIZE, BORDER, ELEMENT_SIZE,
                        BORDER)
                    
                    val label = "${i + 1}"
                    
                    labelWidth = fm.stringWidth(label)
                    
                    renderer.drawString(label,
                        BORDER * 2 + i * ELEMENT_SIZE + (ELEMENT_SIZE - labelWidth) / 2,
                        BORDER * 2 - 8)
                }
            }
            
            Mode.Numerals -> {
                var xOffset = 0
                
                for (i in numerals.indices) {
                    if (numerals[i] == "VIIIB") {
                        renderer.drawRect(BORDER * 2 + i * ELEMENT_SIZE + xOffset, BORDER,
                            ELEMENT_SIZE * 3, BORDER)
                        
                        labelWidth = fm.stringWidth(numerals[i])
                        
                        renderer.drawString(numerals[i], BORDER * 2 + i * ELEMENT_SIZE
                            + (ELEMENT_SIZE * 3 - labelWidth) / 2 + xOffset, BORDER * 2 - 8)
                        
                        xOffset = ELEMENT_SIZE * 2
                    }
                    else {
                        renderer.drawRect(BORDER * 2 + i * ELEMENT_SIZE + xOffset, BORDER,
                            ELEMENT_SIZE, BORDER)
                        
                        labelWidth = fm.stringWidth(numerals[i])
                        
                        renderer.drawString(numerals[i], BORDER * 2 + i * ELEMENT_SIZE
                            + (ELEMENT_SIZE - labelWidth) / 2 + xOffset, BORDER * 2 - 8)
                    }
                }
            }
        }
        
        renderer.composite = lastComposite
    }
}