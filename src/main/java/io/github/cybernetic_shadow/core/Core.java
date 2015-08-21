package io.github.cybernetic_shadow.core;

import java.io.File;
import java.nio.ByteBuffer;

import org.lwjgl.Sys;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cybernetic_shadow.graphic.SplashScreen;
import io.github.cybernetic_shadow.util.Configuration;

/**
 * The Core of Echo.
 * 
 * @author Josh "ShadowLordAlpha"
 *
 */
public class Core {

	private static final Logger LOGGER = LoggerFactory.getLogger(Core.class);

	/**
	 * This value is assumed to be the ID of the main thread due to the way
	 * Threading currently works in Java however this is undocumented behavior
	 * and should not be relied on
	 */
	public static final int MAIN_THREAD_ID = 0x1;

	public void start(Application app) {
		if (Thread.currentThread().getId() != MAIN_THREAD_ID) {
			LOGGER.debug("start method not called on main thread");
			return;
		}
		
		GLFWErrorCallback ecb;
		GLFW.glfwSetErrorCallback(ecb = Callbacks.errorCallbackPrint());

		LOGGER.debug("LWJGL Version {}", Sys.getVersion());

		if (GLFW.glfwInit() != GL11.GL_TRUE) {
			LOGGER.debug("GLFW Initilization Failed");
			return;
		}

		SplashScreen splashScreen = new SplashScreen();
		splashScreen.initilize();
		
		// TODO load window configs
		File file = new File("config/window.properties");
		Configuration windowConfig = new Configuration(file);
		
		long monitor = GLFW.glfwGetPrimaryMonitor();
		ByteBuffer vidmode = GLFW.glfwGetVideoMode(monitor);
		String title = windowConfig.getString("title", "Echo Engine");
		int width = windowConfig.getInt("width", GLFWvidmode.width(vidmode));
		int height = windowConfig.getInt("height", GLFWvidmode.height(vidmode));
		boolean fullscreen = windowConfig.getBoolean("fullscreen", true);
		
		windowConfig.save("AUTO GENERATED FILE DO NOT EDIT UNLESS YOU KNOW WHAT YOU ARE DOING");
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		
		long window = GLFW.glfwCreateWindow(width, height, title, (fullscreen) ? monitor : MemoryUtil.NULL, MemoryUtil.NULL);
		
		GLFW.glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		
		app.init();
		
		try {
			// TODO remove this sleep
			Thread.sleep(5000);
			splashScreen.terminate();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		GLFW.glfwShowWindow(window);
		
		while(GLFW.glfwWindowShouldClose(window) != GL11.GL_TRUE) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			
			// TODO 
			
			GLFW.glfwSwapBuffers(window);
			
			GLFW.glfwPollEvents();
		}
		
		GLFW.glfwDestroyWindow(window);
		GLFW.glfwTerminate();
		return;
	}
}
