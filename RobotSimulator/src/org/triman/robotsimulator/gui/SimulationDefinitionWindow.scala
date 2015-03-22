package org.triman.robotsimulator.gui

import org.triman.robotsimulator.viewmodels.SimulationViewModel
import scala.swing.Frame
import javax.swing.JDialog

class SimulationDefinitionWindow(val viewModel : SimulationViewModel) extends Frame {
	def initComponents() {
		title = "Simulation definition"
		// environment
		// robots
		// dialog buttons
	}
	
	initComponents()
	
	def showDialog(){
		val dialog = new JDialog(this.peer, "Simulation definition")
    dialog.setBounds(350, 350, 200, 200)
    dialog.setVisible(true)
	}
	
}