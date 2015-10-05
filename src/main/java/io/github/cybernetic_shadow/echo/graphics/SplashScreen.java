package io.github.cybernetic_shadow.echo.graphics;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.jemalloc.JEmalloc;

import io.github.cybernetic_shadow.echo.core.EchoEngine;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWapi;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWwindow;

public class SplashScreen implements Runnable {

	private static final String DEFAULT_FILE = EchoEngine.RELITIVE_PATH_MODIFIER + "textures/splash-screen.png";

	private ByteBuffer image;
	private int width;
	private int height;
	private int components;
	private GLFWwindow window;

	public SplashScreen() throws IOException, URISyntaxException {
		this(new File(DEFAULT_FILE));
	}

	public SplashScreen(File file) throws IOException {
		this(FileChannel.open(file.toPath()));
	}

	public SplashScreen(FileChannel fChannel) throws IOException {
		
		// Load image to be used as the splash screen
		ByteBuffer imageData = JEmalloc.je_malloc(fChannel.size() + 1);
		int read;
		do {
			read = fChannel.read(imageData);
		} while(read != -1);
		imageData.flip();
		
		ByteBuffer imagePart = JEmalloc.je_malloc(12);

		// Using native method in order to use only one ByteBuffer instead of 3
		long ipMemAddress = memAddress(imagePart);
		long __result = STBImage.nstbi_load_from_memory(memAddress(imageData), imageData.remaining(), ipMemAddress, ipMemAddress + 4, ipMemAddress + 8, 0);
		JEmalloc.je_free(imageData);
		
		width = imagePart.getInt(0);
		height = imagePart.getInt(4);
		components = imagePart.getInt(8);
		JEmalloc.je_free(imagePart);
		
		image = memByteBuffer(__result, width * height * components);
		
		// Create hidden window to display splash screen
		// reset all hints to set needed hints
		GLFWapi.glfwDefaultWindowHints();
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_VISIBLE, false);
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_DECORATED, false);
		
		window = GLFWapi.glfwCreateWindow(width, height, "Echo Engine", null, null);
		
		// TODO center window
		GLFWapi.glfwSetWindowPos(window, 0, 0);
		
	}

	public void requestClose() {
		GLFWapi.glfwSetWindowShouldClose(window, true);
		GLFWapi.glfwHideWindow(window);
	}

	@Override
	public void run() {
		
		GLFWapi.glfwMakeContextCurrent(window);
		GL.createCapabilities();
		
		if (window != null) {
			int texID = GL11.glGenTextures();

			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID);
			if (components == 3) {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
			} else {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}

			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

			GL11.glEnable(GL11.GL_TEXTURE_2D);

			GL11.glMatrixMode(GL11.GL_PROJECTION);
			GL11.glLoadIdentity();
			GL11.glOrtho(0.0, width, height, 0.0, -1.0, 1.0);
			GL11.glMatrixMode(GL11.GL_MODELVIEW);
			GL11.glViewport(0, 0, width, height);

			// Main Render Loop
			while (!GLFWapi.glfwWindowShouldClose(window)) {
				GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

				GL11.glPushMatrix();

				GL11.glBegin(GL11.GL_QUADS);
				{
					GL11.glTexCoord2f(0.0f, 0.0f);
					GL11.glVertex2f(0.0f, 0.0f);

					GL11.glTexCoord2f(1.0f, 0.0f);
					GL11.glVertex2f(width, 0.0f);

					GL11.glTexCoord2f(1.0f, 1.0f);
					GL11.glVertex2f(width, height);

					GL11.glTexCoord2f(0.0f, 1.0f);
					GL11.glVertex2f(0.0f, height);
				}
				GL11.glEnd();

				GL11.glPopMatrix();

				GLFWapi.glfwSwapBuffers(window);
			}			
		}
		
		GLFWapi.glfwDestroyWindow(window);
		STBImage.stbi_image_free(image);
		image = null;
		window = null;
	}

	public GLFWwindow getWindow() {
		return window;
	}
}
