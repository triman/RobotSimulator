package org.triman.robotsimulator.factories

import org.triman.robotsimulator.Robot
import org.triman.robotsimulator.SensorDefinition
import org.triman.graphics.Drawable
import org.triman.robotsimulator.NamedArea
import java.net.URL
import org.triman.graphics.SVGUtils

/**
 * Extract Data from SVG objects
 * The Drawable returned'll be used in order to display the objects in the GUI.
 */
object SVGFactory {
	
	/**
	 * Extract the robot from the svg resource
	 * @parameter url URL of the resource to load
	 */
	def getRobot(url : URL) : Tuple2[Drawable, List[SensorDefinition]] = {
		null
	}
	
	/**
	 * Extract the environment from the svg resource
	 * @parameter url URL of the resource to load
	 */
	def getEnvironment(url : URL) : Tuple2[Drawable, List[NamedArea]] = {
		null
	}
}