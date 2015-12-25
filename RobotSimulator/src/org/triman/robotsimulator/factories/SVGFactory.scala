package org.triman.robotsimulator.factories

import org.triman.robotsimulator.Robot
import org.triman.robotsimulator.SensorDefinition
import org.triman.graphics.Drawable
import org.triman.robotsimulator.NamedArea
import java.net.URL
import org.triman.graphics.SVGUtils
import org.triman.xml.EmbeddedXml
import org.triman.xml.XmlUtils
import org.triman.graphics.DrawableUtils
import org.triman.robotsimulator.RobotDefinition
import java.io.File
import scala.xml.Node
import java.util.UUID
import org.triman.robotsimulator.xml.RobotDefinitionXml
import scala.xml.Elem

/**
 * Extract Data from SVG objects
 * The Drawable returned'll be used in order to display the objects in the GUI.
 */
object SVGFactory {
	
	/**
	 * Extract the robot from the svg resource
	 * @parameter cml The XML data to extract
	 */
	def getRobotDefinition(xml : Elem) : RobotDefinition  with EmbeddedXml = {
		val d = SVGUtils.svg2Drawable(xml)
		val definitionId = xml \ "@definitionId"
		val r = new RobotDefinition(if(definitionId.isEmpty)UUID.randomUUID.toString else definitionId.head.toString,d.id,SensorDefinition.extractAll(d),d) with RobotDefinitionXml
		r.embeddedXml = xml
		
		r
	}
	
	/**
	 * Extract the robot from the svg resource
	 * @parameter path Path of the resource to load
	 */
	def getRobotDefinition(path : String) : RobotDefinition  with EmbeddedXml = getRobotDefinition(XmlUtils.readXML(path))
		
	
	def getEnvironment(xml: Elem) : Tuple2[Drawable, List[NamedArea]]  with EmbeddedXml = {
		val d = SVGUtils.svg2Drawable(xml)
		
		val r = new Tuple2(d,DrawableUtils.extractNamedDrawables(d).map(n => new NamedArea(n._1, n._2.shape)).toList) with EmbeddedXml
		r.embeddedXml = xml
		r
	}
	
	/**
	 * Extract the environment from the svg resource
	 * @parameter path Path of the resource to load
	 */
	def getEnvironment(path : String) : Tuple2[Drawable, List[NamedArea]]  with EmbeddedXml = {
		val xml = XmlUtils.readXML(path)
		getEnvironment(xml)
	}
}