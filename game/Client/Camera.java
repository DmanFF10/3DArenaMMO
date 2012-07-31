package Client;

public class Camera {
	
	/* stores camera position */
	
	// x y and z hold positional identifiers of the camera
	public float X, Y, Z;
	// x y and z rots hold the rotations of the camera
	public float Xrot, Yrot, Zrot;
	
	public Camera(){
		// Initialize the camera at the origin
		X = 0f; Y = 0f; Z = 0f;
		Xrot = 0f; Yrot = 0f;
		Zrot = 0f;
	}
}
