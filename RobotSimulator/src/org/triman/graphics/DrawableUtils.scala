package org.triman.graphics

import org.triman.utils.StringUtils

/**
 * utility functions around Drawables
 */
object DrawableUtils {
	
	/**
	 * Extract all the drawables withan ID from the given Drawable.
	 * @param d the Drawable which the named element will be extracted from.
	 */
	def extractNamedDrawables(d : Drawable) : Map[String, Drawable] = {
		extractDrawablePart(d, dr => !StringUtils.isNullOrEmpty(dr.id))
			.map(dr => (dr.id, dr))
			.toMap
	}
	
	/**
	 * Extracts the parts of a Drawable that satisfies a given predicate
	 * @param d the Drawable
	 * @param p the predicate that has to be satisfied
	 */
	def extractDrawablePart(d : Drawable, p : (Drawable) => Boolean) : List[Drawable] = d match {
			case c : CompositeDrawableShape => if(p(c)){List(c)} else {List()} ++ c.shapes.flatMap(s => extractDrawablePart(s, p))
			case d => if(p(d)){List(d)} else {List()}
		}
}