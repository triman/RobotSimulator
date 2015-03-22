package org.triman.window

import java.net.URL
import scala.swing.Button
import javax.swing.ImageIcon
import javax.swing.BorderFactory

class ImageButton(var imageURL : URL) extends Button{
	icon = new ImageIcon(imageURL)
	border = BorderFactory.createEmptyBorder
}