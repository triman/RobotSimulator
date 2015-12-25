package org.triman.robotsimulator

import org.triman.graphics.Drawable

class RobotDefinition(val id: String, var name : String, val sensors : List[SensorDefinition], val drawable : Drawable){
	override def toString() = name + " " + sensors.length
	def copy() = {
		val drawableCopy = drawable.copy
		new RobotDefinition(id, name, SensorDefinition.extractAll(drawableCopy), drawableCopy)
	}
}