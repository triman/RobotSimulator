package org.triman.utils.geometry

import java.awt.geom.AffineTransform
/**
 * Extensions for Affine Transform
 * @see java.awt.geom.AffineTransform
 */
object AffineTransformExtensions {
  /**
   * Identity
   */
  lazy val identity = {
    val a = new AffineTransform
    a.setToIdentity
    a
  }
}