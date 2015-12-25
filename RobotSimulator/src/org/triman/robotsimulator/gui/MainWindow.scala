package org.triman.robotsimulator.gui

import scala.xml.XML
import org.triman.graphics._
import org.triman.window._
import scala.io.Source
import scala.swing.Frame
import java.awt.Dimension
import javax.swing.JFrame
import java.awt.geom.Ellipse2D
import java.awt.Color
import java.awt.geom.AffineTransform
import scala.xml.Elem
import scala.swing.BorderPanel
import scala.swing.Label
import scala.swing.Alignment
import scala.swing.MenuBar
import scala.swing.Menu
import scala.swing.MenuItem
import scala.swing.Action
import scala.swing.FileChooser
import scala.swing.Button
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import org.triman.robotsimulator.viewmodels.SimulationViewModel
import scala.swing.Dialog
import org.triman.robotsimulator.factories.SVGFactory
import java.net.URI
import java.net.URL
import org.triman.xml.EmbeddedXml
import java.io.File
import scala.swing.Separator
import javax.swing.JToolBar
import javax.swing.SwingConstants
import scala.collection.mutable.HashMap
import org.triman.robotsimulator.Robot
import org.triman.robotsimulator.Position
import org.triman.robotsimulator.SensorDefinition
import org.triman.window.events.CanvasSelectionDragged
import scala.swing.ToggleButton
import scala.swing.Panel
import scala.swing.FlowPanel
import scala.swing.event.MousePressed
import javax.swing.border.EmptyBorder
import java.awt.Insets
/**
 * http://www.jasperpotts.com/blog/2007/07/svg-shape-2-java2d-code/
 */
object MainWindow extends Frame {
	
	private var _room: Drawable = null

	def room = _room
	def room_=(d: Drawable): Unit = {
		// the room is ALWAYS the head of the shapes list -> it's drawed on the bottom layer
		if (_room != null) {
			canvas.shapes.update(0, d)
		}
		else {
			d +=: canvas.shapes
		}
		_room = d
		canvas.repaint()
	}

	private val container = new BorderPanel
	contents = container
	title = "Robot simulator"
	peer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
	val canvas = new Canvas

	size = new Dimension(1000, 500)
	canvas.background = Color.GRAY
	//canvas.backgroundDrawable = RoombaSimGuiElements.canvasBackground
	container.layout(canvas) = BorderPanel.Position.Center

	val statusBar = new StatusBar
	container.layout(statusBar) = BorderPanel.Position.South
	val statusLabel = new Label("")
	val zoomLevelLabel = new Label("zoom: " + (canvas.zoom() * 100).round.toString() + "%")
	zoomLevelLabel.preferredSize = new Dimension(70, 15)
	zoomLevelLabel.horizontalAlignment = Alignment.Left
	canvas.zoom.attend(z => { zoomLevelLabel.text = "zoom: " + (z * 100).round.toString() + "%" })
	statusBar.add(statusLabel)
	statusBar.add(zoomLevelLabel)
	
	val viewModel = new SimulationViewModel()
	
	val drawableEnvironment = new DrawableContainer(null)
	val drawableRobots = new DrawableContainer(null)
	canvas.shapes += drawableEnvironment
	canvas.shapes += drawableRobots
	
	// register changes on view model
	viewModel.environment.attend(t => {
		drawableEnvironment.drawable = t._1
		canvas.repaint
	})
	
	val robotsDrawables = new HashMap[Robot, DrawableContainer]()
	
	private def refrestRobotPosition(r : Robot){
		// register listener
		val l : Position => Unit = p => {
			val transform = new AffineTransform
			val bounds = r.definition.drawable.shape.getBounds()
			transform.setToIdentity
			transform.translate(p.x, -p.y)	// negative since the vertical axis is pointing downward
			transform.rotate(-p.orientation)
			transform.translate(- bounds.width/2, - bounds.height/2)
			robotsDrawables(r).drawable = new TransformableDrawable(r.definition.drawable, transform)
			
			canvas.repaint()
		}
		
		val s : Map[String, AnyVal] => Unit = m => {
			println("[ sim ] sensors for '" + r.name + "' updated")
			m.foreach(kvp => kvp._2 match {
				case b : Boolean => {
					println("\t" + kvp._1 + " : " + kvp._2)
					val regex =  """^.*(\W|_)"""+ kvp._1 + "$"
					DrawableUtils.extractNamedDrawables(robotsDrawables(r))
						.filter(n => n._1 != null && n._1.matches(regex))
						.foreach(d =>{ 
							val c = if(b) GuiConstants.sensorDeactivated else GuiConstants.sensorActivated
							DrawableUtils.setColor(d._2, c, c, null)
						})
				}
			})
		}
		
		if(!r.position.isAttending(l)){
			r.position.attend(l)
		}
		
		if(!r.sensors.isAttending(s)){
			r.sensors.attend(s)
		}
		
		l(r.position())
		canvas.repaint
	}
	
	viewModel.robots.attend(l => {
		robotsDrawables.empty
		// register robot changes
		l.foreach(r => {
			robotsDrawables(r) = new DrawableContainer(null)
			refrestRobotPosition(r)
		})
		drawableRobots.drawable = new CompositeDrawableShape(robotsDrawables.map(_._2).toList:_*)
		
		canvas.selectableShapes.clear
		canvas.selectableShapes ++= robotsDrawables.map(_._2)
	})
	
	canvas.reactions += {
      case e: CanvasSelectionDragged  => {
      	var robot = robotsDrawables.filter(d => d._2 == e.selection).head._1
      	var bounds = robot.definition.drawable.shape.getBounds()
      	// move the robot to the new position
      	robot.position.update(new Position(e.to.getX(), -e.to.getY(), robot.position().orientation))
      	robot.initialPosition = robot.position()
      }
	}
	
	
	// menu
	menuBar = new MenuBar
    {
       contents += new Menu("File")
       {
      	contents += new MenuItem(new Action("New")
         {
          def apply
          {
          	viewModel.robots.update(List.empty)
          	viewModel.robotDefinitions.update(List.empty)
          	viewModel.environment.update(null)
          }
         })
	      contents += new MenuItem(new Action("Open..."){
	      	def apply
	          {
	          	// ToDo: Read saved simulation (from file)
	          	val chooser = new FileChooser
	          	val r = chooser.showOpenDialog(container)
	          	r match{
	          		case FileChooser.Result.Approve => {
	          			viewModel.loadFromFile(chooser.selectedFile.getAbsolutePath())
	          			}
	          		case _ => {}
	          	}
	          }
	      })
	      contents += new MenuItem(new Action("Save..."){
	      	def apply
	          {
	          	// ToDo: Read saved simulation (from file)
	          	val chooser = new FileChooser
	          	val r = chooser.showSaveDialog(container)
	          	r match{
	          		case FileChooser.Result.Approve => {
	          			viewModel.saveToFile(chooser.selectedFile.getAbsolutePath())
	          			}
	          		case _ => {}
	          	}
	          }
	      })
	      contents += new Separator()
	    }
      contents += new Menu("Simulation"){
      	contents += new MenuItem(new Action("Add robot..."){
      		def apply
      		{
      			AddRobotWindow.visible = true
      		}
      	})
      	contents += new MenuItem(new Action("Set environment...")
	         {
	          def apply
	          {
	          	// ToDo: Read saved simulation (from file)
	          	val chooser = new FileChooser
	          	val r = chooser.showOpenDialog(container)
	          	r match{
	          		case FileChooser.Result.Approve => {
	          			viewModel.environment.update(SVGFactory.getEnvironment(chooser.selectedFile.getAbsolutePath()))
	          			}
	          		case _ => {}
	          	}
	          }
	         })
      } 
    }
  // toolbar
	val simulationToolbar = new Toolbar
	// add buttons to the toolbar
	val startButton = new ImageButton(getClass().getResource("play.png"))
	
	val pauseButton = new ImageButton(getClass().getResource("pause.png"))
	val resetButton = new ImageButton(getClass().getResource("reset.png"))
	
	simulationToolbar.add(startButton)
	simulationToolbar.add(pauseButton)
	simulationToolbar.peer.addSeparator
	simulationToolbar.add(resetButton)
	SimulationToolbarCommands.init
	
	val viewToolbar = new Toolbar
	// display buttons
	val selectButton = new ImageToggleButton(getClass().getResource("selector.png"))
	selectButton.border = new EmptyBorder(new Insets(0,3,0,3))
	viewToolbar.add(selectButton)
	val expandButton = new ImageButton(getClass().getResource("vertical_arrow.png"))
	expandButton.border = new EmptyBorder(new Insets(0,3,0,3))
	viewToolbar.add(expandButton)
	
	ViewToolbarCommands.init
	
	val toolbarPanel = new FlowPanel(FlowPanel.Alignment.Left)(simulationToolbar, viewToolbar)
	
	container.layout(toolbarPanel) = BorderPanel.Position.North
	
	
	
	// add a toolbar on the right
	val rightToolbar = new Toolbar
	rightToolbar.orientation = SwingConstants.VERTICAL
	// listen to the list of robots in order to add them to the list
	viewModel.robots.attend(l => {
		rightToolbar.clear
		
		if(! l.isEmpty){
			l.foreach( r => rightToolbar.add(Action(r.name){
				canvas.currentSelection = robotsDrawables(r)
				canvas.repaint()
			}
		))
		}
	})
	container.layout(rightToolbar) = BorderPanel.Position.East
}