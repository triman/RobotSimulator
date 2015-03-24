package org.triman.window

import scala.swing.TextField
import scala.swing.Button
import org.triman.utils.Notifier
import scala.swing.BorderPanel
import scala.swing.Action
import scala.swing.FileChooser
import scala.swing.event.EditDone
import java.io.File

class FileInput extends BorderPanel{
	
	val filePath = new Notifier[String, Symbol](""){val id = 'FileInput}
	private val input = new TextField()
	private val button = new Button("...")
	button.action = new Action("...")
         {
          def apply
          {
            val chooser = new FileChooser(new File(filePath()))
            val r = chooser.showOpenDialog(FileInput.this)
          	r match{
          		case FileChooser.Result.Approve => {
          				filePath.update(chooser.selectedFile.getAbsolutePath())
          			}
          		case _ => {}
          	}
          }
         }
	
	filePath.attend(s => input.text = s);
	listenTo(input)
	reactions += {
	  case _ : EditDone => {
	    filePath.update(input.text)
	  }
	}
	
	// set the layout and add the components
	layout(button) = BorderPanel.Position.East
	layout(input) = BorderPanel.Position.Center
	
	
}