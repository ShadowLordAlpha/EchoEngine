package io.github.cybernetic_shadow.echo.core;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;

public class EchoEngine {

	public static final void start() {
		SharedLibraryLoader.load();
		
		GLFW.glfwInit();
		
		SplashScreen splash = new SplashScreen(new File("src/main/java/splash-screen.png"));
		new Thread(splash, "Splash Screen").start();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Splash Screen load and display
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		long window = GLFW.glfwCreateWindow(500, 400, "Test 2", MemoryUtil.NULL, MemoryUtil.NULL);
		
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center the window
		GLFW.glfwSetWindowPos(window, (GLFWvidmode.width(vidmode) - 500) / 2, (GLFWvidmode.height(vidmode) - 400) / 2);
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
		splash.requestClose();
		GL.createCapabilities(true);
		
		GL11.glClearColor(0.0f, 0.3f, 0.0f, 0.0f);
		
		// Main Loop
		while(GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GLFW.glfwPollEvents();
			
			// render
			
			GLFW.glfwSwapBuffers(window);
		}
		
		GLFW.glfwTerminate();
	}
}
