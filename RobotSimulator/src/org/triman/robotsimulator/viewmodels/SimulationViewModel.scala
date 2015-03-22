package org.triman.robotsimulator.viewmodels

import org.triman.utils.Notifier

class SimulationViewModel {
	val environmentFile = new Notifier[String, Symbol](""){val id = 'environmentFile}
	val robotDefinition = new Notifier[List[Tuple2[String, Int]], Symbol](List(("", 1))){val id = 'robotDefinitionFiles}
}