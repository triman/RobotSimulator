package org.triman.robotsimulator.xml

import org.triman.xml.EmbeddedXml
import java.util.UUID
import org.triman.robotsimulator.Robot
import scala.xml.Elem

trait RobotXml extends EmbeddedXml{
	self: Robot =>
	override def toXml(): Elem = {
		<Robot name={name} definition={definition.id} x={initialPosition.x.toString} y={initialPosition.y.toString} orientation={initialPosition.orientation.toString}/>
	}

}