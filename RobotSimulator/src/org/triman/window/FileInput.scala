package org.triman.window

import scala.swing.TextField
import scala.swing.Button
import org.triman.utils.Notifier
import scala.swing.BorderPanel

class FileInput extends BorderPanel{
	
	val filePath = new Notifier[String, Symbol](""){val id = 'FileInput}
	private val input = new TextField()
	private val button = new Button("...")
	
	// set the layout and add the components
	layout(button) = BorderPanel.Position.East
	layout(input) = BorderPanel.Position.Center
}