package org.triman.window.events

import scala.swing.Component
import scala.swing.event.Event
import java.awt.geom.Point2D
import org.triman.graphics.Drawable

class CanvasSelectionDragged(val component:Component, val selection : Drawable ,val from: Point2D, val to:Point2D) extends Event {

}