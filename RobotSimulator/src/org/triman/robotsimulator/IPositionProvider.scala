package org.triman.robotsimulator

trait IPositionProvider {
	def computeNextPosition(initial : Position, speed : Double, radius : Double, dt : Double) : Position
}