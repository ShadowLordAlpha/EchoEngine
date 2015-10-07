package io.github.cybernetic_shadow.echo.graphics;

import org.joml.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f();
	private float pitch; // up down
	private float yaw; // left right
	private float roll; // tilt
	
	public void move() {
		
	}
	
	public Vector3f getPosition() {
		return position;
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public float getYaw() {
		return yaw;
	}
	
	public float getRoll() {
		return roll;
	}
}
