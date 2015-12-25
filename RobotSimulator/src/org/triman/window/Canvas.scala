package org.triman.window

import scala.swing.Panel
import java.awt.Graphics2D
import java.awt.geom.Point2D
import java.awt.geom.AffineTransform
import scala.collection.mutable.MutableList
import scala.swing.event.MousePressed
import scala.swing.event.MouseDragged
import scala.swing.event.MouseReleased
import scala.swing.event.MouseWheelMoved
import java.awt.Point
import java.awt.geom.Ellipse2D
import java.awt.geom.NoninvertibleTransformException
import java.awt.RenderingHints
import org.triman.graphics.Drawable
import java.awt.AlphaComposite
import org.triman.utils.Notifier
import org.triman.window.events.CanvasSelectionChanged
import org.triman.window.events.CanvasSelectionDragged
import org.triman.graphics.ColoredDrawableShape
import java.awt.Color
import org.triman.graphics.DrawableShape
import java.awt.BasicStroke

/**
 * Defines a Canvas as a surface where Drawable objects can be drawn, zoomed using
 * the mouse wheel and panned (via drag&drop).
 * @see org.github.triman.Drawable
 */
class Canvas extends Panel{
	
	/**
	 * The shapes that will be drawn on the Canvas
	 */
	val shapes = new MutableList[Drawable]()
	var selectableShapes = new MutableList[Drawable]()
	val zoom = new Notifier[Double, Symbol](1.0){def id='Zoom}
	var zoomTicks = 0.02
	var backgroundDrawable : Drawable = null;
	
	var selectionEnabled = false
	
	var currentSelection : Drawable = null
	
	private var currentX = 0.0
	private var currentY = 0.0
	private var previousX = 0.0
	private var previousY = 0.0
	
	listenTo(mouse.clicks, mouse.moves, mouse.wheel)
	reactions += {
      case e: MousePressed  => {
    	  previousX = e.point.getX()
    	  previousY = e.point.getY()
    	  
    	  if(selectionEnabled){
	    	  // check if the click was made on a drawable
	    	  var tp = getTranslatedPoint(previousX, previousY)
	    	  val selection = selectableShapes.filter(s => s.shape.contains(tp.getX, tp.getY())).take(1);
    	  	val old = currentSelection
	    	  if(!selection.isEmpty){
	    	  	currentSelection = selection.head
	    	  }else{
	    	  	currentSelection = null
	    	  }
    	  	if(old != currentSelection){
    	  		repaint
	    	  	publish(new CanvasSelectionChanged(this, old, currentSelection))
    	  	}
    	  }
      }
      
      case e: MouseDragged  => {
			// Determine the old and new mouse coordinates based on the translated coordinate space.
     	val adjPreviousPoint = getTranslatedPoint(previousX, previousY)
			val adjNewPoint = getTranslatedPoint(e.point.getX(), e.point.getY())
      val newX = adjNewPoint.getX() - adjPreviousPoint.getX();
      val newY = adjNewPoint.getY() - adjPreviousPoint.getY();
 
      previousX = e.point.getX();
      previousY = e.point.getY();
      if(!selectionEnabled || currentSelection == null){
			  currentX += newX;
        currentY += newY;
	    }else{
	     	publish(new CanvasSelectionDragged(this, currentSelection, adjPreviousPoint, adjNewPoint))
	    }
      
      	repaint();
      }
      case e : MouseWheelMoved => {
      	val z = zoom() + zoomTicks * -e.rotation
    	  zoom.update(Math.max(0.00001, z))
    	  repaint()
      }
    }
	
	/**
	 * Get the current transform
	 */
	def currentTransform() = {
		val tx = new AffineTransform();
         
        val centerX = size.width.asInstanceOf[Double] / 2
        val centerY = size.height.asInstanceOf[Double] / 2
         
        tx.translate(centerX, centerY)
        tx.scale(zoom(), zoom())
        tx.translate(currentX, currentY)
        
        tx
	}
	
	def origin = new Point2D.Double(currentX, currentY)
	def origin_= (p : Point2D.Double): Unit = {
		currentX = p.getX
		currentY = p.getY
		
		repaint()
	} 
	
	/**
	 * Computes the transform of a point on the surface, using the current transform
	 */
	def getTranslatedPoint(x : Double, y : Double) = {
		val tx = currentTransform
        val point2d = new Point2D.Double(x, y)
        try {
            tx.inverseTransform(point2d, null);
        } catch {
        	case e : NoninvertibleTransformException => {
        		e.printStackTrace()
        		null
        	}
        }
	}
	
	def paintOnBackground(g: Graphics2D) = {}
	
	override def paint(g: Graphics2D) = {
		super.paint(g)
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON)
		// draw background
		if(backgroundDrawable != null){
			val t = new AffineTransform
			val size = backgroundDrawable.shape.getBounds2D()
			for(i <- 0 until (g.getClipBounds().width/size.getWidth()).ceil.toInt){
				for(j <- 0 until (g.getClipBounds().height/size.getHeight()).ceil.toInt){
				t.setToIdentity()
				t.translate(i*size.getWidth(), j*size.getHeight())
					backgroundDrawable.draw(g, t)
					backgroundDrawable.fill(g, t)
				}
			}
		}
		
		paintOnBackground(g)
		
		shapes.foreach(s => {
				if(s != null){
					s.fill(g, currentTransform)
					s.draw(g, currentTransform)
				}
			})
		if(currentSelection != null){
			var d = new ColoredDrawableShape(new DrawableShape(currentSelection.shape.getBounds2D()), new Color(0,0,255,64), new Color(0,0,255), new BasicStroke(2))
			d.fill(g, currentTransform)
			d.draw(g, currentTransform)
		}
	}
}