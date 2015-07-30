package io.github.cybernetic_shadow.core;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.ContextCapabilities;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;

public class Game {
	
	public void preInit() {
		
	}

	public void init() {
		GLFW.glfwInit();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		long window = GLFW.glfwCreateWindow(5, 5, "", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		ContextCapabilities cc = GLContext.createFromCurrent().getCapabilities();
		System.out.println("OpenGL version: " + GL11.glGetString(GL11.GL_VERSION));
		GLFW.glfwDestroyWindow(window);
		
		GLFW.glfwWindowHint(GLFW.GLFW_CLIENT_API, GLFW.GLFW_OPENGL_API);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
		GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
		
		if(cc.OpenGL45) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 5);
		} else if(cc.OpenGL44) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 4);
		} else if(cc.OpenGL43) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		} else if(cc.OpenGL42) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
		} else if(cc.OpenGL41) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 1);
		} else if(cc.OpenGL40) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 4);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 0);
		} else if(cc.OpenGL33) {
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
			GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 3);
		} else {
			
		}
		
		window = GLFW.glfwCreateWindow(640, 480, "Echo Engine", 0, 0);
		GLFW.glfwMakeContextCurrent(window);
		GLContext.createFromCurrent();
		GLFW.glfwShowWindow(window);
		// Set the clear color
        GL11.glClearColor(0.3f, 0.6f, 0.0f, 0.3f);
 
        // Run the rendering loop until the user has attempted to close
        // the window or has pressed the ESCAPE key.
        while (GLFW.glfwWindowShouldClose(window) == GL11.GL_FALSE ) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // clear the framebuffer
 
            GLFW.glfwSwapBuffers(window); // swap the color buffers
 
            // Poll for window events. The key callback above will only be
            // invoked during this call.
            GLFW.glfwPollEvents();
        }
        
        GLFW.glfwTerminate();
	}
}
