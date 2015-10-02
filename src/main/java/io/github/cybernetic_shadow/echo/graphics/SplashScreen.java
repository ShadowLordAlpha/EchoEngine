package io.github.cybernetic_shadow.echo.graphics;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryUtil;
import org.lwjgl.system.jemalloc.JEmalloc;

import io.github.cybernetic_shadow.echo.core.EchoEngine;

public class SplashScreen implements Runnable {

	private static final String DEFAULT_FILE = EchoEngine.RELITIVE_PATH_MODIFIER + "textures/splash-screen.png";

	private ByteBuffer image;
	private int width;
	private int height;
	private int components;
	private boolean close = false;

	public SplashScreen() throws IOException, URISyntaxException {
		this(new File(DEFAULT_FILE));
	}

	public SplashScreen(File file) throws IOException {
		this(FileChannel.open(file.toPath()));
	}

	public SplashScreen(FileChannel fChannel) throws IOException {
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
	}

	public void requestClose() {
		close = true;
	}

	@Override
	public void run() {
		// Setup GLFW Window
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_DECORATED, GL11.GL_FALSE);
		GLFW.glfwWindowHint(GLFW.GLFW_FLOATING, GL11.GL_TRUE);
		long window = GLFW.glfwCreateWindow(width, height, "Echo Engine", MemoryUtil.NULL, MemoryUtil.NULL);
		GLFWvidmode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());
		GLFW.glfwSetWindowPos(window, (vidmode.getWidth() - width) / 2, (vidmode.getHeight() - height) / 2);
		GLFW.glfwMakeContextCurrent(window);
		GL.createCapabilities();

		GLFW.glfwShowWindow(window);
		if (window != MemoryUtil.NULL) {
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
			while (!close) {
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

				GLFW.glfwSwapBuffers(window);
			}

			GLFW.glfwDestroyWindow(window);
		}
		
		STBImage.stbi_image_free(image);
	}
}
