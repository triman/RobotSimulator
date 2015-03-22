package org.triman.window

import java.awt.Dimension
import scala.swing.BorderPanel
import java.awt.SystemColor
import scala.swing.Component
import java.awt.Font

class StatusBar extends BorderPanel {
    preferredSize = new Dimension(10, 18)

    val rightPanel = new BorderPanel
    rightPanel.opaque = false
    
    add(rightPanel, BorderPanel.Position.East);
    background = SystemColor.control
    def add(c : Component) = {
    	var f = new Font(c.font.getName, c.font.getStyle, 9)
 			c.font = f
    	rightPanel.layout(c) = BorderPanel.Position.East
    }
}