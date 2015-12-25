package org.triman.robotsimulator

import org.triman.graphics.Drawable
import org.triman.graphics.DrawableUtils
object SensorDefinition{
	def getRegexForSensor(sensor : String) = {
		"""^(\w+):(\w+)(?:,(\w+))*( |_)""" + sensor + """$"""
	}
	
	def extractAll(drawable : Drawable) = {
		val regex = getRegexForSensor("""(\w+)""")
		DrawableUtils.extractNamedDrawables(drawable).filter(n => n._1 != null && n._1.matches(regex)).map(n => {
			val parts = n._1.split("""(\W|_)+""")
			new SensorDefinition(parts.last, n._2, parts.toList.slice(1, parts.length-2), parts.head.toLowerCase() == "invertedsensor")
		}).toList
	}
}
class SensorDefinition(val name : String, val drawable : Drawable, val layers : List[String], val isInverted : Boolean){
	override def toString() = name
}