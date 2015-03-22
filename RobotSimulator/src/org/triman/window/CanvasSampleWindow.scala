package org.triman.window

import scala.swing.Frame
import java.awt.Dimension
import javax.swing.JFrame
import org.triman.graphics._
import java.awt.geom.RoundRectangle2D
import java.awt.Color
import java.awt.BasicStroke
import scala.swing.BorderPanel
import scala.swing.Button
import org.triman.window.Toolbar

object CanvasSampleWindow extends Frame{

	def main(args: Array[String]): Unit = {
			visible = true
			peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
			val panel = new BorderPanel
			val canvas = new Canvas
			panel.preferredSize = new Dimension(1000,500)
			contents = panel
			panel.layout(canvas) = BorderPanel.Position.Center
			
			
			val toolbar = new Toolbar
			toolbar.add(new Button("hello"))
			toolbar.add(new Button("world"))
			val toolbar2 = new Toolbar
			toolbar2.add(new Button("foo"))
			toolbar2.add(new Button("bar"))
			panel.layout(toolbar) = BorderPanel.Position.North
			panel.layout(toolbar2) = BorderPanel.Position.North
			
			var s = new CompositeDrawableShape(
					new ColoredDrawableShape(DrawableShapeCompanion.Shape2DrawableShape(new RoundRectangle2D.Double(0,0,10,10,3,3)),null,Color.RED,
							new BasicStroke(3.0f, BasicStroke.CAP_BUTT,
							BasicStroke.JOIN_MITER, 10.0f, Array[Float](10), 0.0f)),
					new ColoredDrawableShape(DrawableShapeCompanion.Shape2DrawableShape(new RoundRectangle2D.Double(20,20,5,10,3,3)),Color.YELLOW,Color.GREEN)
					)
			
			canvas.shapes += s
	}

}