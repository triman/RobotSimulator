package org.triman.robotsimulator.gui

import scala.swing.Action
import org.triman.graphics.CompositeDrawableShape
import java.awt.geom.Point2D

object ViewToolbarCommands {
	def init(){
		val selectAction = Action(""){
			MainWindow.canvas.selectionEnabled = MainWindow.selectButton.selected
		}
		selectAction.icon = MainWindow.selectButton.icon
		MainWindow.selectButton.action = selectAction
		
		val expandAction = Action(""){
			
			if(MainWindow.canvas.shapes.isEmpty){
				return
			}
			
			// get bounds of drawing
			val fullShape = new CompositeDrawableShape(MainWindow.canvas.shapes : _*)
			val bounds = fullShape.shape().getBounds()
			
			MainWindow.canvas.origin = new Point2D.Double(-(bounds.x + bounds.width/2), -(bounds.y + bounds.height/2))
			
			val zoom = (Math.floor(Math.min(MainWindow.canvas.size.width.toDouble / bounds.width, MainWindow.canvas.size.height.toDouble / bounds.height)/MainWindow.canvas.zoomTicks))*MainWindow.canvas.zoomTicks
			MainWindow.canvas.zoom.update(zoom)
		}
		expandAction.icon = MainWindow.expandButton.icon
		MainWindow.expandButton.action = expandAction
	}
}