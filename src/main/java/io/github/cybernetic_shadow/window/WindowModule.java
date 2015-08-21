package io.github.cybernetic_shadow.window;

import org.lwjgl.glfw.GLFW;

/**
 * 
 * @author Josh "ShadowLordAlpha"
 */
public class WindowModule {

	public void init() {
		//GLFW.GLFWErrorCallback();
		GLFW.glfwInit();
	}
	
	public void terminate() {
		GLFW.glfwTerminate();
	}
}
