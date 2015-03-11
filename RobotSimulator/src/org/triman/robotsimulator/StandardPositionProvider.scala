package org.triman.robotsimulator

/**
 * Implements a standard position provider
 */
trait StandardPositionProvider extends IPositionProvider {
  self: IRobotProperties =>
  
  
	def computeNextPosition(initial: Position, speed: Double, radius: Double, dt: Double): Position = {
    // a 0 radius or infinite radius means we drive straight...
    import Math._
    val d = dt * speed;
    if (d == 0) {
      initial
    } else if (radius == 0 || radius == Double.PositiveInfinity || radius == Double.NegativeInfinity) { // driving straight
      new Position(initial.x + d * cos(initial.orientation),
          initial.y + d * sin(initial.orientation),
          initial.orientation
      )
    } else { // driving along an arc
      val da = (d.toDouble * min(1.0, abs(2 * radius / wheelDistance))) / radius
      var t1 = initial.orientation + da
      t1 -= 2 * PI * (floor(t1 / 2 * PI))
      new Position(
          initial.x - radius * sin(initial.orientation) + radius * sin(initial.orientation + da),
          initial.y + radius * cos(initial.orientation) - radius * cos(initial.orientation + da),
          initial.orientation + da
      )
    }
  }
}