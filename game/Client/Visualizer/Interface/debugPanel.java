package Client.Visualizer.Interface;

import de.matthiasmann.twl.DialogLayout;
import de.matthiasmann.twl.Label;

public class debugPanel extends DialogLayout{
		final Label fps = new Label("fps: 0");
		final Label x = new Label("X: 0");
		final Label y = new Label("Y: 0");
		final Label z = new Label("Z: 0");
		final Label rotX = new Label ("X Rotation: 0");
		final Label rotY = new Label ("Y Rotation: 0");
		final Label rotZ = new Label ("Z Rotation: 0");
	
		public debugPanel(){
		DialogLayout.Group hLabels = this.createParallelGroup(fps, x, y, z, rotX, rotY, rotZ);
			this.setHorizontalGroup(hLabels);
		
			this.setVerticalGroup(this.createSequentialGroup()
				.addWidget(fps)
				.addWidget(x).addWidget(y).addWidget(z)
				.addWidget(rotX).addWidget(rotY).addWidget(rotZ));
		
		this.setSize(50, 100);
	}
	
	public void update(int fps, int x, int y, int z, int rotX, int rotY, int rotZ){
		this.fps.setText("fps: " + fps);
		this.x.setText("X: " + x);
		this.y.setText("Y: " + y);
		this.z.setText("Z: " + z);
		this.rotX.setText("X Rotation: " + rotX);
		this.rotY.setText("Y Rotation: " + rotY);
		this.rotZ.setText("Z Rotation: " + rotZ);
		
	}
}
