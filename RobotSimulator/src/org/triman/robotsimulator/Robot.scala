package org.triman.robotsimulator

import org.triman.utils.Notifier

class Robot(val name : String, val sensorModel : List[SensorDefinition]) {
	val position = new Notifier[Position, Symbol](new Position(0,0,0)){val id = 'Position}
	val sensors = new Notifier[Map[String, AnyVal], Symbol](Map.empty){val id = 'Sensors}
		
	var speed : Double = 0;
	var radius : Double = 0;
	
}