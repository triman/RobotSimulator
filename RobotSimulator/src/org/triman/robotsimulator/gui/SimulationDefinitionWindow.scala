package org.triman.robotsimulator.gui

import org.triman.robotsimulator.viewmodels.SimulationViewModel
import scala.swing.BoxPanel
import scala.swing.Orientation
import scala.swing.Label
import javax.swing.JOptionPane
import org.triman.window.FileInput
import scala.swing.Swing
import java.awt.Dimension
import scala.swing.ScrollPane


class SimulationDefinitionWindow(val viewModel : SimulationViewModel) extends ScrollPane {
	def initComponents() {
	  verticalScrollBarPolicy = ScrollPane.BarPolicy.AsNeeded
	  horizontalScrollBarPolicy = ScrollPane.BarPolicy.Never
	  
	  val panel = new BoxPanel(Orientation.Vertical)
	  
	  
	  // environment
	  panel.contents += new FileInput()
	  panel.contents += new FileInput()
	  panel.contents += new FileInput()
	  panel.contents += new FileInput()
	  panel.contents += new FileInput()
	  panel.contents += new FileInput()
	  // robots
	  
	  // glue
	  val glue = Swing.VGlue
	  glue.preferredSize = new Dimension(0, Int.MaxValue)
	  //panel.contents += glue
	  preferredSize = new Dimension(1000, 0)
	  contents = panel
	  
	}
	
	initComponents()
	
	def showDialog(){
		JOptionPane.showOptionDialog(MainWindow.peer, this.peer, "New simulation", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE, null, Array("Create", "Cancel") , null);
		
	}
	
}