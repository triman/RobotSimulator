package org.triman.utils

object StringUtils {
	def isNullOrEmpty(s: String) = s match{
		case "" | null => true
		case _ => false
	}
	
	
}