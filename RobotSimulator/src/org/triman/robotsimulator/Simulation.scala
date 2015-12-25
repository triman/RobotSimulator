package org.triman.robotsimulator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.awt.geom.AffineTransform
import java.awt.geom.Area
import scala.util.Success
import scala.util.Failure

class Simulation(val robots: List[Robot], val environment: List[NamedArea]) {
  self: IClock with IPositionProvider =>

  private var currentTime = 0.0

  // each time the clock ticks, we recompute the robot's position
  time.attend(computeRobotPositions _)

  def computeRobotPositions(time: Double): Unit = {
    val dt = time - currentTime
    robots.foreach(r => r.position.update(computeNextPosition(r.position(), r.speed, r.radius, dt)))
    currentTime = time

    computeSensors
  }

  def computeSensors(): Unit = {
    robots.map(r => computeSensors(r).onComplete({
		    case Success(res) => {
		      r.sensors.update(res)
		    }
		    case Failure(res) => {
		    	println(r.name + " sensor computation failed")
		    }
		  })
	  )
  }

  private def computeSensors(robot: Robot): Future[Map[String, AnyVal]] = Future {
    // get transform
    val transform = new AffineTransform()
    // set to represent the roomba position
    transform.translate(robot.position().x, -robot.position().y)	// negative since the vertical axis is pointing downward
    transform.rotate(-robot.position().orientation)
   	val bounds = robot.definition.drawable.shape.getBounds()
    transform.translate(- bounds.width/2, - bounds.height/2)
    
    // transform all sensors and apply them to the objects
    robot.definition.sensors.map(s => {	// transform sensors
    					val t = new Area(s.drawable.shape)
    					t.transform(transform)
    					(s.name, s.layers,t, s.isInverted)
    				 })
    				 .map(s => s._1 -> environment
        						.filter(a => s._2.isEmpty || s._2.map(s => s.toLowerCase()).contains(a.name.toLowerCase()))
        						.exists(a => {
        						  val i = new Area(a.shape);
        						  i.intersect(s._3)
        						  i.isEmpty ^ s._4
        						})) toMap
  }
}