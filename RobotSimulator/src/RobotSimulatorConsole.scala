import org.triman.robotsimulator._
import org.triman.robotsimulator.roomba.RoombaProperties

object RobotSimulatorConsole {

  def main(args: Array[String]): Unit = {
    // create a new simulation in an empty environment
    
    var robot = new Robot("robot", List.empty)
    robot.speed = 1000
    robot.radius = 1000
    robot.position.attend(p => {
    	println("robot at: " + p toString)
    })
    
    val sim = new Simulation(robot :: List.empty, List.empty) with StandardPositionProvider with RealtimeClock with RoombaProperties
    
    
    
    sim start
   
    
    println("sim started")
    
    Thread.sleep(5000)
    
    sim stop
    
    println("sim ended")
    
  }

}