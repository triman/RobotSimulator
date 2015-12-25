package org.triman.window.events

import scala.swing.event.Event
import org.triman.graphics.Drawable
import scala.swing.Component
import scala.swing.event.SelectionChanged

class CanvasSelectionChanged(val component: Component, val oldSelection : Drawable, val newSlection : Drawable) extends SelectionChanged(component) {
}