package org.triman.robotsimulator.gui

import scala.swing.Action

object ToolbarCommands {
	def init() : Unit = {
		val pauseAction = Action(""){
			// ToDo: define simulation RoombaSimulatorApplication.simulatedRoomba.pause
			MainWindow.pauseButton.enabled = false
			MainWindow.startButton.enabled = true
		}
		pauseAction.icon = MainWindow.pauseButton.icon
		MainWindow.pauseButton.action = pauseAction
		
		val startAction = Action(""){
			// ToDo: define simulation RoombaSimulatorApplication.simulatedRoomba.unPause
			MainWindow.pauseButton.enabled = true
			MainWindow.startButton.enabled = false
		}
		startAction.enabled = false
		startAction.icon = MainWindow.startButton.icon
		MainWindow.startButton.action = startAction
	}
	
	val resetAction = Action(""){
		// ToDo: define simulation RoombaSimulatorApplication.simulatedRoomba.reset
	}
	resetAction.icon = MainWindow.resetButton.icon
	MainWindow.resetButton.action = resetAction
	
}