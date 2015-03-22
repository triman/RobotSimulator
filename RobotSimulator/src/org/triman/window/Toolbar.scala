package org.triman.window

import scala.swing.Component
import scala.swing.Container
import scala.swing.Action

class Toolbar extends Component with Container.Wrapper {
	override lazy val peer : javax.swing.JToolBar = new javax.swing.JToolBar with SuperMixin
	
	def add( action: Action ) { peer.add( action.peer )}
  
  def add( component: Component ) { peer.add( component.peer )}
	
}