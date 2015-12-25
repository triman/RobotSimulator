package org.triman.robotsimulator.xml

import org.triman.xml.EmbeddedXml
import org.triman.robotsimulator.RobotDefinition
import org.triman.xml.XmlUtils._
import scala.xml.Elem


trait RobotDefinitionXml extends EmbeddedXml {
	self : RobotDefinition =>
	override def toXml() : Elem = {
		super.toXml.setAttribute("definitionId", id)
	}
}