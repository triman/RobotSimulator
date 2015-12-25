package org.triman.window

import java.net.URL
import scala.swing.ToggleButton
import javax.swing.ImageIcon
import javax.swing.BorderFactory
import java.awt.image.BufferedImage
import java.awt.Color
import java.awt.SystemColor

class ImageToggleButton(var imageURL : URL) extends ToggleButton{
	var imageIcon = new ImageIcon(imageURL)
	icon = imageIcon
	
	// create dark icon
	val bufferedImage =  new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);  
  val g2 = bufferedImage.createGraphics(); 
  g2.setColor(this.background.darker())
  g2.fillRect(0,0,bufferedImage.getWidth(), bufferedImage.getHeight())
  g2.drawImage(imageIcon.getImage(), 0, 0, null);  
  g2.dispose();
  selectedIcon = new ImageIcon(bufferedImage)
  
	border = BorderFactory.createEmptyBorder
	
}