package Client;

import org.lwjgl.util.vector.Vector3f;

public class Camera {
	
	/* stores camera position */
	public Vector3f position;
	public Vector3f rotation;
	
	public Camera(){
		// Initialize the camera at the origin
		position = new Vector3f(0f, 0f, 0f);
		rotation = new Vector3f(0f, 0f, 0f);
	}
}
