package org.triman.robotsimulator.viewmodels

import org.triman.utils.Notifier
import org.triman.robotsimulator.NamedArea
import org.triman.robotsimulator.SensorDefinition
import org.triman.robotsimulator.Robot
import java.net.URI
import scala.xml.XML
import org.triman.xml.EmbeddedXml
import org.triman.robotsimulator.xml.RobotXml
import org.triman.graphics.Drawable
import org.triman.robotsimulator.RobotDefinition
import org.triman.xml.XmlUtils
import org.triman.robotsimulator.factories.SVGFactory
import scala.xml.Elem
import org.triman.robotsimulator.Position

class SimulationViewModel {
	val environment = new Notifier[Tuple2[Drawable, List[NamedArea]]  with EmbeddedXml, Symbol](null){val id = 'Environent}
	val robotDefinitions = new Notifier[List[RobotDefinition with EmbeddedXml], Symbol](List.empty){val id = 'RobotDefinitions}
	val robots = new Notifier[List[Robot with RobotXml], Symbol](List.empty){val id = 'Robots}
	/**
	 * Load from the given file
	 * @param file Path to the file to load
	 */
	def loadFromFile(file : String){
		var xml = XmlUtils.readXML(file)
		environment.update(SVGFactory.getEnvironment((xml \ "Environment").flatMap(e => e.child).head.asInstanceOf[Elem]))
		
		robotDefinitions.update((xml \ "RobotDefinitions").flatMap(e => e.child).map(x => SVGFactory.getRobotDefinition(x.asInstanceOf[Elem])).toList)
		val r = (xml \ "Robots").flatMap(e => e.child).map(x => {
			val definition = robotDefinitions().filter(d => d.id == (x \ "@definition").head.toString).head.copy
			val r = new Robot((x \ "@name").head.toString, new Position((x \ "@x").head.toString.toDouble, (x \ "@y").head.toString.toDouble, (x \ "@orientation").head.toString.toDouble), definition) with RobotXml
			r.embeddedXml = x.asInstanceOf[Elem]
			r
		}).toList
		robots.update(r)
	}
	
	/**
	 * Create an XML file with the current viewmodel
	 * @param file Path to the file where the simulation should be saved.
	 */
	def saveToFile(file : String){
		// create an XML definition of the current viewmodel
		val e = <Environment>{if(environment() != null) environment().toXml else ""}</Environment>
		val d = <RobotDefinitions>{robotDefinitions().map(rd => rd.toXml)}</RobotDefinitions>
		val r = <Robots>{robots().map(ro => ro.toXml)}</Robots>
		var xml = <RobotSimulation>{e ++ d ++ r}</RobotSimulation>
		XML.save(file, xml, "UTF-8", true, null)
	}
	
	
}