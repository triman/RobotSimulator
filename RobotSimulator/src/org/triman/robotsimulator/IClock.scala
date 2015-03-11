package org.triman.robotsimulator

import org.triman.utils.Notifier

trait IClock {
    /**
     * Time (in sec) since the beginning of the simulation
     */
	lazy val time = new Notifier[Double, Symbol](0.0){val id='Time}
	
	def start() : Unit
	def stop() : Unit
	
}