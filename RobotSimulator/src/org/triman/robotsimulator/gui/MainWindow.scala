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

	val zoomLevelLabel = new Label("zoom: " + (canvas.zoom() * 100).round.toString() + "%")
	zoomLevelLabel.preferredSize = new Dimension(70, 15)
	zoomLevelLabel.horizontalAlignment = Alignment.Left
	canvas.zoom.attend(z => { zoomLevelLabel.text = "zoom: " + (z * 100).round.toString() + "%" })
	statusBar.add(zoomLevelLabel)
		
	// menu
	menuBar = new MenuBar
    {
       contents += new Menu("File")
       {
      contents += new MenuItem(new Action("New")
         {
          def apply
          {
          	val viewModel = new SimulationViewModel()
          	val window = new SimulationDefinitionWindow(viewModel)
          	window.showDialog
          }
         })
			contents += new MenuItem(new Action("Load room...")
         {
          def apply
          {
          	// ToDo: Read saved simulation (from file)
          	/*val chooser = new FileChooser
          	val r = chooser.showOpenDialog(container)
          	r match{
          		case FileChooser.Result.Approve => {
          				RoombaSimulatorApplication.loadRoom(chooser.selectedFile.getAbsolutePath())
          			}
          		case _ => {}
          	}
          	*/
          }
         })
       }
    }
  // toolbar
	val toolbar = new Toolbar
	// add buttons to the toolbar
	val startButton = new ImageButton(getClass().getResource("play.png"))
	val pauseButton = new ImageButton(getClass().getResource("pause.png"))
	val resetButton = new ImageButton(getClass().getResource("reset.png"))
	toolbar.add(startButton)
	toolbar.add(pauseButton)
	toolbar.peer.addSeparator
	toolbar.add(resetButton)
	
	ToolbarCommands.init
	
	container.layout(toolbar) = BorderPanel.Position.North
	
}