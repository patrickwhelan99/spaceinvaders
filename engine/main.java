package engine;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import graphics.drawer;

public class main 
{
	/*
	 * Class: 			main 
	 * Author: 			Patrick
	 */
	

	public static void main(String[] args) throws IOException 
	{
		/*
		 * Method: 			main
		 * Author: 			Patrick
		 * Description: 	Entry point for the program, thread is kept alive to relaunch the game on return to menu
		 */
		
		JFrame window = null;
		drawer d = null;
		
		window = new JFrame("Space Invaders");
		window.setSize(1920, 1080);
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
		d = new drawer();
		d.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());
		d.addPaneltoFrame(window.getContentPane());
		
		window.setBounds(0, 0, Toolkit.getDefaultToolkit().getScreenSize().width, Toolkit.getDefaultToolkit().getScreenSize().height);
		window.setVisible(true);
		
		
		while(true)
		{	
			if(d.engine == null)
				d.engine = new gameEngine(d, false, d.maxLevelStore);
			
			//System.out.println(d.engine); // Necessary to do this, probably due to java's garbage collection not realising that engine is dereferencened and therefore not destroying it
		}
	}
}
