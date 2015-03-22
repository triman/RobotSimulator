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
	val zoom = new Notifier[Double, Symbol](1.0){def id='Zoom}
	var backgroundDrawable : Drawable = null;
	
	private var currentX = 0.0
	private var currentY = 0.0
	private var previousX = 0.0
	private var previousY = 0.0
	
	listenTo(mouse.clicks, mouse.moves, mouse.wheel)
	reactions += {
      case e: MousePressed  => {
    	  previousX = e.point.getX()
    	  previousY = e.point.getY()
      }
      
      case e: MouseDragged  => {
			// Determine the old and new mouse coordinates based on the translated coordinate space.
			val adjPreviousPoint = getTranslatedPoint(previousX, previousY)
			val adjNewPoint = getTranslatedPoint(e.point.getX(), e.point.getY())
                 
                val newX = adjNewPoint.getX() - adjPreviousPoint.getX();
                val newY = adjNewPoint.getY() - adjPreviousPoint.getY();
 
                previousX = e.point.getX();
                previousY = e.point.getY();
                 
                currentX += newX;
                currentY += newY;
                 
                repaint();
      }
      case e : MouseWheelMoved => {
      	val z = zoom() + .02 * -e.rotation
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
	
	/**
	 * Computes the transform of a point on the surface, using the current transform
	 */
	private def getTranslatedPoint(x : Double, y : Double) = {
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
				s.fill(g, currentTransform)
				s.draw(g, currentTransform)
			})
    }
}