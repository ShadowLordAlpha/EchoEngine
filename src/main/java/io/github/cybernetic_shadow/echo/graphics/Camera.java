package io.github.cybernetic_shadow.echo.graphics;

import io.github.cybernetic_shadow.echo.core.EchoEngine;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWapi;

import org.joml.Vector3f;

public class Camera {
	
	private Vector3f position = new Vector3f();
	private float pitch; // up down
	private float yaw; // left right
	private float roll; // tilt
	
	public void move() {
		int state = GLFWapi.glfwGetKey(EchoEngine.getWindow(), GLFWapi.GLFW_KEY_W);
		//System.out.println("State: " + state);
		if(state == GLFWapi.GLFW_PRESS || state == GLFWapi.GLFW_REPEAT) {
			position.z += 0.02f;
		}
		
		state = GLFWapi.glfwGetKey(EchoEngine.getWindow(), GLFWapi.GLFW_KEY_S);
		if(state == GLFWapi.GLFW_PRESS || state == GLFWapi.GLFW_REPEAT) {
			position.z -= 0.02f;
		}
		
		state = GLFWapi.glfwGetKey(EchoEngine.getWindow(), GLFWapi.GLFW_KEY_A);
		if(state == GLFWapi.GLFW_PRESS || state == GLFWapi.GLFW_REPEAT) {
			position.x -= 0.02f;
		}
		
		state = GLFWapi.glfwGetKey(EchoEngine.getWindow(), GLFWapi.GLFW_KEY_D);
		if(state == GLFWapi.GLFW_PRESS || state == GLFWapi.GLFW_REPEAT) {
			position.x += 0.02f;
		}
		
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
