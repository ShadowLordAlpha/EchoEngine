package io.github.cybernetic_shadow.echo.core;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.jemalloc.JEmalloc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SplashScreen implements Runnable {

	private static final Logger log = LoggerFactory.getLogger(SplashScreen.class);
	
	private File file;
	private boolean close = false;
	
	public SplashScreen(File file) {
		this.file = file;
	}
	
	public void requestClose() {
		close = true;
	}
	
	@Override
	public void run() {
		System.out.println(file.length());
		if(file.isFile()) {
			try(FileChannel fc = new FileInputStream(file).getChannel()) {
				ByteBuffer imageData = JEmalloc.je_calloc(1, file.length() + 1);
				
				int bytes;
				do {
					bytes = fc.read(imageData);
				} while (bytes != -1);
				imageData.flip();
				ByteBuffer width = JEmalloc.je_malloc(4);
				ByteBuffer height = JEmalloc.je_malloc(4);
				ByteBuffer components = JEmalloc.je_malloc(4);
				ByteBuffer image;
				image = STBImage.stbi_load_from_memory(imageData, imageData.remaining(), width, height, components, 0);
				if(image != null) {
					// Setup GLFW Window
					GLFW.glfwDefaultWindowHints();
					GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
					GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GL11.GL_FALSE);
					long window = GLFW.glfwCreateWindow(width.getInt(0), height.getInt(0), "Echo Engine", MemoryUtil.NULL, MemoryUtil.NULL);
					GLFWvidmode vidmode = new GLFWvidmode(GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor()));
					GLFW.glfwSetWindowPos(window, (vidmode.getWidth() - width.getInt(0)) / 2, (vidmode.getHeight() - height.getInt(0)) / 2);
					GLFW.glfwMakeContextCurrent(window);
					GL.createCapabilities();
					GLFW.glfwSwapInterval(1);
					GLFW.glfwShowWindow(window);
					if(window != MemoryUtil.NULL) {
						int texID = GL11.glGenTextures();

						GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
						if (components.getInt(0) == 3) {
							GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width.getInt(0), height.getInt(0), 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
						} else {
							GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width.getInt(0), height.getInt(0), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

							GL11.glEnable(GL11.GL_BLEND);
							GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
						}

						GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
						GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

						GL11.glEnable(GL11.GL_TEXTURE_2D);
						
						GL11.glMatrixMode(GL11.GL_PROJECTION);
						GL11.glLoadIdentity();
						GL11.glOrtho(0.0, width.getInt(0), height.getInt(0), 0.0, -1.0, 1.0);
						GL11.glMatrixMode(GL11.GL_MODELVIEW);
						GL11.glViewport(0, 0, width.getInt(0), height.getInt(0));
						
						// Main Render Loop
						while(!close) {
							GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

							GL11.glPushMatrix();

							GL11.glBegin(GL11.GL_QUADS);
							{
								GL11.glTexCoord2f(0.0f, 0.0f);
								GL11.glVertex2f(0.0f, 0.0f);

								GL11.glTexCoord2f(1.0f, 0.0f);
								GL11.glVertex2f(width.getInt(0), 0.0f);

								GL11.glTexCoord2f(1.0f, 1.0f);
								GL11.glVertex2f(width.getInt(0), height.getInt(0));

								GL11.glTexCoord2f(0.0f, 1.0f);
								GL11.glVertex2f(0.0f, height.getInt(0));
							}
							GL11.glEnd();

							GL11.glPopMatrix();

							GLFW.glfwSwapBuffers(window);
						}
						
						GL.destroy();
						GLFW.glfwDestroyWindow(window);
					}
					STBImage.stbi_image_free(image);
				}
				
				// Delete and free all memory and objects
				JEmalloc.je_free(imageData);
				JEmalloc.je_free(width);
				JEmalloc.je_free(height);
				JEmalloc.je_free(components);
			} catch(Exception e) {
				log.warn("Splash Screen Exception!", e);
				return;
			}
			
		}
	}
}
