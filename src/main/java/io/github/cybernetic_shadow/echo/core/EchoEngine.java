package io.github.cybernetic_shadow.echo.core;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.cybernetic_shadow.echo.graphics.SplashScreen;

public class EchoEngine {
	
	private static final Logger logger = LoggerFactory.getLogger(EchoEngine.class);

	public static String RELITIVE_PATH_MODIFIER = "";
	
	static {
		File runningJar = new File(EchoEngine.class.getProtectionDomain().getCodeSource().getLocation().getPath());
		System.out.println(runningJar.getAbsolutePath());
		ClassLoader cl = ClassLoader.getSystemClassLoader();
        URL[] urls = ((URLClassLoader)cl).getURLs();
        for(URL url: urls){
        	System.out.println(url.getFile());
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
	
	public static final void start(String[] args) {
		
		GLFW.glfwInit();
		
		SplashScreen splash = null;
		
		try {
			splash = new SplashScreen();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
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
		
		GLFWvidmode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		// Center the window
		GLFW.glfwSetWindowPos(window, (vidmode.getWidth() - 500) / 2, (vidmode.getHeight() - 400) / 2);
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwShowWindow(window);
		try {
			splash.requestClose();
		} catch(Exception e) {
			
		}
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
	
	public static final void start(Game game) {
		// TODO extract jar resources if needed
		
		
	}
}
