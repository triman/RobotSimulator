package org.triman.robotsimulator.gui

import org.triman.window.Canvas
import org.triman.graphics.Drawable
import java.awt.geom.AffineTransform
import org.triman.robotsimulator.Robot
import org.triman.graphics.TransformableDrawable

class RobotListener(val robot : Robot, val canvas : Canvas, robotDrawable : Drawable) {
	private val currentTransform = new AffineTransform()
	currentTransform.setToIdentity()
	
	private val transformedRobot = new TransformableDrawable(robotDrawable, currentTransform)
	// create listeners
	robot.position.attend(_ => onRoombaPositionChange)
	// ToDo: cet sensor state event simulatedRoomba.onSensorsStateComputed += onRoombaSensorsStateComputed
	
	// get the status display
	//val roombaStatus = RoombaStatusDrawable
	// add the roomba and status display on the canvas
	if(canvas != null){
		canvas.shapes += transformedRobot
		// ToDo: current state for sensors canvas.shapes += roombaStatus
	}
	
	def onRoombaPositionChange(){
		// move the roomba
		val position = robot.position()
		
		currentTransform.setToIdentity()
		currentTransform.translate(position.x, -position.y)
		currentTransform.rotate(-position.orientation)
		
		canvas.repaint()
	}
	/*
	def onRoombaSensorsStateComputed(s : SensorsState){
		// bumpers
		if(s.leftBump.isDefined && s.leftBump.get){
			roombaStatus.setOff('BumperLeft)
		}else{
			roombaStatus.setOn('BumperLeft)
		}
		if(s.rightBump.isDefined && s.rightBump.get){
			roombaStatus.setOff('BumperRight)
		}else{
			roombaStatus.setOn('BumperRight)
		}
		// cliffs
		if(s.cliffLeft.isDefined && s.cliffLeft.get){
			roombaStatus.setOff('CliffLeft)
		}else{
			roombaStatus.setOn('CliffLeft)
		}
		if(s.cliffFrontLeft.isDefined && s.cliffFrontLeft.get){
			roombaStatus.setOff('CliffFrontLeft)
		}else{
			roombaStatus.setOn('CliffFrontLeft)
		}
		if(s.cliffFrontRight.isDefined && s.cliffFrontRight.get){
			roombaStatus.setOff('CliffFrontRight)
		}else{
			roombaStatus.setOn('CliffFrontRight)
		}
		if(s.cliffRight.isDefined && s.cliffRight.get){
			roombaStatus.setOff('CliffRight)
		}else{
			roombaStatus.setOn('CliffRight)
		}
		
		// drops
		if(s.casterWheeldrop.isDefined && s.casterWheeldrop.get){
			roombaStatus.setOff('CasterWheel)
		}else{
			roombaStatus.setOn('CasterWheel)
		}
		if(s.leftWheelDrop.isDefined && s.leftWheelDrop.get){
			roombaStatus.setOff('WheelLeft)
		}else{
			roombaStatus.setOn('WheelLeft)
		}
		if(s.rightWheeldrop.isDefined && s.rightWheeldrop.get){
			roombaStatus.setOff('WheelRight)
		}else{
			roombaStatus.setOn('WheelRight)
		}
		
		if(s.wall.isDefined && s.wall.get){
			roombaStatus.setOn('Wall)
		}else{
			roombaStatus.setOff('Wall)
		}
		
		if(s.virtualWall.isDefined && s.virtualWall.get){
			roombaStatus.setOff('VirtualWall)
		}else{
			roombaStatus.setOn('VirtualWall)
		}
		
		canvas.repaint()
	}
	* 
	*/
	
	
}