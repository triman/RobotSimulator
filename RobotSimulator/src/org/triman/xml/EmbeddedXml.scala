package org.triman.xml

import scala.xml.Elem

trait EmbeddedXml {
	var embeddedXml : Elem = null;
	
	def toXml() = {
		embeddedXml
	}
}