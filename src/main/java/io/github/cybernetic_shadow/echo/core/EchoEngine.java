package io.github.cybernetic_shadow.echo.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cybernetic_shadow.echo.graphics.SplashScreen;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWapi;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWwindow;

public class EchoEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(EchoEngine.class);
	
	public static String RELITIVE_PATH_MODIFIER = "";
	private static GLFWwindow window;
	
	static {
		File runningJar = new File(EchoEngine.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		//System.out.println(runningJar.getAbsolutePath());
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls){
        	//System.out.println(url.getFile());
        }
		if(runningJar.getName().endsWith(".jar")) {
			logger.debug("Extracting Jar Resources");
			
			// TODO
			
			logger.debug("Done Extracting Jar Resources");
		} else {
			logger.info("Jar not found!");
			if(runningJar.getAbsolutePath().endsWith("build\\classes\\main")) {
				logger.info("Assuming Gradle Dev Enviornment");
				RELITIVE_PATH_MODIFIER = "build/resources/main/";
			} else {
				logger.info("Assuming Basic Dev Enviornment");
				RELITIVE_PATH_MODIFIER = "bin/";
			}
		}
	}
	
	public static final void start(Game game) {
		// TODO extract jar resources if needed
		
		logger.debug("Pre-Initialization");
		game.preInit();
		
		logger.debug("Initializing GLFW");
		if(!GLFWapi.glfwInit()) {
			logger.error("Failed to initialize GLFW!");
			System.exit(-1);
		}
		
		logger.debug("Setting up Window");
		game.setupWindow();
		
		// create window
		window = GLFWapi.glfwCreateWindow(1024, 768, "Echo Engine", null, null);
		
		if(window == null) {
			logger.error("Failed to Create GLFW Window!");
			System.exit(-1);
		}
		
		GLFWapi.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		
		SplashScreen splash = null;
		try {
			splash = new SplashScreen();
			new Thread(splash, "Splash-Screen").start();
			GLFWapi.glfwShowWindow(splash.getWindow());
		} catch (Exception e) {
			logger.warn("Failed to Properly Create Splash Screen: {}", e);
		}
		
		GL11.glViewport(0, 0, 1024, 768);
		GL11.glClearColor(0.4f, 1.0f, 0.5f, 1.0f);
		game.init();
		
		if(splash != null) {
			splash.requestClose();
		}
		
		GLFWapi.glfwShowWindow(window);
		// TODO show window
		game.postInit();
		//GLFWapi.glfwSwapInterval(1);
		// Core game loop variables
		double deltaTime = 1000/60; // given in ms
		
		double frameStartTime = (double) System.nanoTime() / 1000000.0;
		double accumulator = 0;
		
		// Core game loop
		while(!GLFWapi.glfwWindowShouldClose(window)) {
			// Process frame time and start next frame time cycle
			double frameEndTime = (double) System.nanoTime() / 1000000.0;
			double frameTime = (frameEndTime - frameStartTime);
			frameStartTime = frameEndTime;
			accumulator += frameTime;
			
			while(accumulator >= deltaTime) {
				// Poll user input
				GLFW.glfwPollEvents(); 
				// update game
				game.update(deltaTime);
				accumulator -= deltaTime;
			}
			
			// Estimate where the actual physics parts are for this frame as the
			// physics and render speed are no lock together
			game.polate(accumulator/deltaTime);
			
			// TODO drop frames
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
				game.render();
				GLFWapi.glfwSwapBuffers(window);
		}
		
		GLFWapi.glfwHideWindow(window);
		game.dispose();
		GLFWapi.glfwDestroyWindow(window);
		GLFWapi.glfwTerminate();
	}
	
	public static GLFWwindow getWindow() {
		return window;
	}
}
