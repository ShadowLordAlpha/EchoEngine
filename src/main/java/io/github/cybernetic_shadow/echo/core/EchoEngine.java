package io.github.cybernetic_shadow.echo.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoEngine {
	
	private static final Logger log = LoggerFactory.getLogger(EchoEngine.class);

	public static final void start() {
		log.debug("Loading Native Libraries");
		SharedLibraryLoader.load();
		
		log.debug("Initilizing GLFW");
		if(GLFW.glfwInit() != GL11.GL_TRUE) {
			log.error("Failed to Initilize GLFW");
			return;
		}
		
		
	}
}
