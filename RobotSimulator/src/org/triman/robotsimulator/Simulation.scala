package org.triman.robotsimulator

import scala.concurrent.Future

class Simulation(val robots : List[Robot]) {
	self : ITimeProvider with IPositionProvider =>
	
	private var currentTime = 0.0
	  
	def computeRobotPositions() = {
	  val ct = getTime
	  val dt = ct - currentTime
	  robots.foreach(r => r.position.update(computeNextPosition(r.position(), r.speed, r.radius, dt)))
	  currentTime = ct
	  
	  computeSensors
	}
	
	def computeSensors() : List[Future[Any]] = {
	  robots.map(r => computeSensors(r))
	}
	
	private def computeSensors(robot : Robot) : Future[Any] = {
	  // get transform
	  
	  // transform all sensors and apply them to the objects
	  Future.failed[Any](new Exception("This is not implemented... yet"))
	}
}