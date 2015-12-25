package org.triman.robotsimulator.gui

import scala.swing.Action
import org.triman.robotsimulator.Simulation
import org.triman.robotsimulator.roomba.RoombaProperties
import org.triman.robotsimulator.RealtimeClock
import org.triman.robotsimulator.StandardPositionProvider

object SimulationToolbarCommands {
	
	private var currentSimulation : Simulation with StandardPositionProvider with RealtimeClock with RoombaProperties = null;
	
	def init() : Unit = {
		val pauseAction = Action(""){
			this.synchronized{
				if(currentSimulation != null){
					currentSimulation stop
				}
			}
			MainWindow.pauseButton.enabled = false
			MainWindow.startButton.enabled = true
		}
		pauseAction.icon = MainWindow.pauseButton.icon
		MainWindow.pauseButton.action = pauseAction
		
		val startAction = Action(""){
			this.synchronized{
				if(currentSimulation == null){
					currentSimulation = new Simulation(MainWindow.viewModel.robots(), MainWindow.viewModel.environment()._2) with StandardPositionProvider with RealtimeClock with RoombaProperties
					// for debug purpose
					currentSimulation.robots(1).speed = 100
				}
				currentSimulation start
			}
			MainWindow.pauseButton.enabled = true
			MainWindow.startButton.enabled = false
		}
		startAction.enabled = true
		pauseAction.enabled = false;
		startAction.icon = MainWindow.startButton.icon
		MainWindow.startButton.action = startAction
	}
	
	val resetAction = Action(""){
		this.synchronized{
				if(currentSimulation != null){
					currentSimulation stop
				}
				currentSimulation = null
				MainWindow.viewModel.robots().foreach(r => r.position.update(r.initialPosition))
			}
	}
	resetAction.icon = MainWindow.resetButton.icon
	MainWindow.resetButton.action = resetAction
	
}