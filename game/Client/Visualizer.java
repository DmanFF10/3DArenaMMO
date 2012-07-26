package Client;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/*
 * displays the graphics
 */

public class Visualizer extends Thread {
	
	private Callbacks.visualizerCBs cbs;
	
	public Visualizer(Callbacks.visualizerCBs	cbs){
		// store the callbacks
		this.cbs = cbs;
	}

	public void run() {
		
		try {
			// create the rendering window
			Display.setDisplayMode(new DisplayMode(800, 600));
			Display.create();		
		
			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
		    
			// sets a simple prospective
		    GL11.glOrtho(0, 800, 0, 600, 1, -1);
		    
		    GL11.glMatrixMode(GL11.GL_MODELVIEW);
		    GL11.glLoadIdentity();
		    
			while(!Display.isCloseRequested()){
				// clear the screen
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				
				// set the draw color
				GL11.glColor3f(0.5f, 0.5f, 1.0f);
				
				//NOTE: sample data
				GL11.glBegin(GL11.GL_QUADS);
				GL11.glVertex2f(100, 100);
				GL11.glVertex2f(300, 100);
				GL11.glVertex2f(300, 300);
				GL11.glVertex2f(100, 300);
				GL11.glEnd();
				
				// gets inputs and draws data to the screen
				Display.update();
			}
			
			cbs.endLive();
			Display.destroy();
		    
		} catch (LWJGLException e) {
			System.out.println("Error: graphical issue");
			cbs.endLive();	
		}
	}
}
