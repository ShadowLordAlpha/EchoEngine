package io.github.cybernetic_shadow.graphic;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;

public class SplashScreen implements Runnable {
	
	private boolean closeRequested = false;
	private Thread thread = null;

	/**
	 * 
	 */
	public SplashScreen() {
		
	}
	
	@Override
	public void run() {
		
		int width = 650;
		int height = 400;
		
		// Reset GLFW to Default hints
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, GL11.GL_TRUE);
		
		long splashScreenWindow = GLFW.glfwCreateWindow(width, height, "ECHO ENGINE", MemoryUtil.NULL, MemoryUtil.NULL);
		
		// Get the resolution of the primary monitor
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center our window
		GLFW.glfwSetWindowPos(splashScreenWindow, (GLFWvidmode.width(vidmode) - width) / 2, (GLFWvidmode.height(vidmode) - height) / 2);
		
		GLFW.glfwShowWindow(splashScreenWindow);
		
		// Make Window current and get ready for OpenGL commands
		GLFW.glfwMakeContextCurrent(splashScreenWindow);
		GLContext.createFromCurrent();
		
		GL11.glClearColor(0.2f, 0.3f, 0.1f, 1.0f);
		
		while(!closeRequested) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// TODO render image
			
			GLFW.glfwSwapBuffers(splashScreenWindow);
		}
		
		GLFW.glfwDestroyWindow(splashScreenWindow);
	}
	
	/**
	 * Creates a new thread and starts the splash screen on that thread
	 * @return true if the Splash screen was started on a new thread, false if the Splash screen could not be started or is already running
	 */
	public boolean initilize() {
		if(thread != null) {
			return false;
		}
		
		thread = new Thread(this, "SplashScreenThread");
		thread.start();
		return true;
	}
	
	/**
	 * Tells the running Splash Screen to stop and blocks until the splash screen has stopped running
	 * @throws InterruptedException
	 */
	public void terminate() throws InterruptedException {
		closeRequested = true;
		thread.join();
	}
}
