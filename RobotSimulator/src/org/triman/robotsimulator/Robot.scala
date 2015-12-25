package org.triman.robotsimulator

import org.triman.utils.Notifier

class Robot(val name : String, var initialPosition : Position ,  val definition : RobotDefinition) {
	val position = new Notifier[Position, Symbol](initialPosition){val id = 'Position}
	val sensors = new Notifier[Map[String, AnyVal], Symbol](Map.empty){val id = 'Sensors}
		
	var speed : Double = 0;
	var radius : Double = 0;
	
}