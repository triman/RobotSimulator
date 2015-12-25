package org.triman.robotsimulator.gui

import scala.swing.Frame
import scala.swing.BorderPanel
import scala.swing.Button
import scala.swing.Action
import scala.swing.ComboBox
import scala.swing.FileChooser
import org.triman.robotsimulator.factories.SVGFactory
import scala.swing.FlowPanel
import org.triman.robotsimulator.Robot
import org.triman.robotsimulator.Position
import org.triman.robotsimulator.xml.RobotXml
import java.awt.Dimension

object AddRobotWindow extends Frame{
	title = "Add Robot..."

	init
	
	
	def init(){
		val panel = new BorderPanel
		contents = panel
		
		val viewModel = MainWindow.viewModel
		
		// combobox with all defined types
		val combobox = new ComboBox(viewModel.robotDefinitions()) 
		panel.layout(combobox) = BorderPanel.Position.Center
		// button to load new class
		val addButton = new Button(
				Action("+") {
					val chooser = new FileChooser
	          	val r = chooser.showOpenDialog(panel)
	          	r match{
	          		case FileChooser.Result.Approve => {
	          			viewModel.robotDefinitions.update(viewModel.robotDefinitions() :+ SVGFactory.getRobotDefinition(chooser.selectedFile.getAbsolutePath()))
	          			combobox.peer.setModel(ComboBox.newConstantModel(viewModel.robotDefinitions()))
	          		}
	          		case _ => {}
	          	}
		})
		panel.layout(addButton) = BorderPanel.Position.East
		
		val dialogButtonsPanel = new FlowPanel(FlowPanel.Alignment.Right)(
				new Button(
					Action("Add robot") {
						if(combobox.selection.item != null){
							viewModel.robots.update(viewModel.robots() :+ new Robot(combobox.selection.item.name + " " + (viewModel.robots().count(r => r.definition == combobox.selection.item) +1), new Position(0,0,0), combobox.selection.item) with RobotXml)
						}
						visible = false
					}
				),
			new Button(
					Action("Cancel") {
						visible = false
					}
				)
		)
		
		panel.layout(dialogButtonsPanel) = BorderPanel.Position.South
		
		size = new Dimension(200, 100)
	}
	
	
}