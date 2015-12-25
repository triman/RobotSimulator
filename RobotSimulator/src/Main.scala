
import org.triman.robotsimulator.gui.MainWindow
import javax.swing.UIManager

object Main {
	def main(args : Array[String]){
		// enable macosx look and feel
		System.setProperty("apple.laf.useScreenMenuBar", "true")
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Robot simulator")
		UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())
		MainWindow.visible = true
	}
}