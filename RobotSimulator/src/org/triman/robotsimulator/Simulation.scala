package org.triman.robotsimulator

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import java.awt.geom.AffineTransform
import java.awt.geom.Area

class Simulation(val robots: List[Robot], val environment: List[NamedArea]) {
  self: IClock with IPositionProvider =>

  private var currentTime = 0.0

  time.attend(computeRobotPositions _)

  def computeRobotPositions(time: Double): Unit = {
    val dt = time - currentTime
    robots.foreach(r => r.position.update(computeNextPosition(r.position(), r.speed, r.radius, dt)))
    currentTime = time

    computeSensors
  }

  def computeSensors(): List[Future[Map[String, AnyVal]]] = {
    robots.map(r => computeSensors(r))
  }

  private def computeSensors(robot: Robot): Future[Map[String, AnyVal]] = Future {
    // get transform
    val transform = new AffineTransform()
    // set to represent the roomba position
    transform.translate(robot.position().x, -robot.position().y)	// negative since the vertical axis is pointing downward
    transform.rotate(-robot.position().orientation)

    // transform all sensors and apply them to the objects
    robot.sensorModel.map(s => {	// transform sensors
    					val t = new Area(s.area)
    					t.transform(transform)
    					(s.name, s.layers,t)
    				 })
    				 .map(s => s._1 -> environment
        						.filter(a => s._2.isEmpty || s._2.contains(a.name))
        						.exists(a => {
        						  val i = new Area(a.area);
        						  i.intersect(s._3)
        						  i.isEmpty
        						})) toMap
  }
}