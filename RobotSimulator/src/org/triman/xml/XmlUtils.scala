package org.triman.xml

import scala.xml.XML
import scala.io.Source
import scala.xml.Elem
import scala.xml.UnprefixedAttribute
import scala.xml.Null

object XmlUtils {
	/**
	 * Reads an XML file
	 * @param filePath Path to the file which should be read.
	 */
	def readXML(filePath: String) = {
		XML.withSAXParser(NonValidatingSAXParserFactory.getInstance)
			.loadString(Source.fromFile(
				filePath).getLines().reduce(_ + _))
	}
	
	implicit def elem2ExtendedElem(elem: Elem) = new {
		def setAttribute(key: String, value: String) = {
			elem % new UnprefixedAttribute(key, value, Null)
		}
	}
}