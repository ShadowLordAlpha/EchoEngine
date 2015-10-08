package io.github.cybernetic_shadow.echo.core;

import java.io.File;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import io.github.cybernetic_shadow.echo.graphics.Camera;
import io.github.cybernetic_shadow.echo.graphics.Entity;
import io.github.cybernetic_shadow.echo.graphics.Loader;
import io.github.cybernetic_shadow.echo.graphics.ModelTexture;
import io.github.cybernetic_shadow.echo.graphics.RawModel;
import io.github.cybernetic_shadow.echo.graphics.Renderer;
import io.github.cybernetic_shadow.echo.graphics.StaticShader;
import io.github.cybernetic_shadow.echo.graphics.TexturedModel;
import io.github.cybernetic_shadow.echo.graphics.glfw.GLFWapi;

public class Game {

	Loader loader = new Loader();
	Renderer renderer = null;
	Entity entity = null;
	TexturedModel model = null;
	StaticShader shader;
	Camera camera = null;

	/**
	 * This method is used to setup the primary GLFWwindow used for the game.
	 * The values it sets are the recommended values.
	 */
	public void setupWindow() {
		// Set all window hints to Default
		GLFWapi.glfwDefaultWindowHints();
		// Set window hints
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_VISIBLE, false);
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_CONTEXT_VERSION_MAJOR, 3);
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_CONTEXT_VERSION_MINOR, 3);
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_OPENGL_FORWARD_COMPAT, true);
		GLFWapi.glfwWindowHint(GLFWapi.GLFW_OPENGL_PROFILE,
				GLFWapi.GLFW_OPENGL_CORE_PROFILE);
		// TODO some way to set size

	}

	public void preInit() {

	}

	public void init() {
		shader = new StaticShader();
		camera = new Camera();
		renderer = new Renderer(shader);
		float[] vertices = { -0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
				-0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f,
				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f,
				-0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, -0.5f, 0.5f,
				-0.5f, 0.5f, 0.5f, -0.5f, 0.5f, 0.5f, -0.5f, 0.5f, -0.5f, 0.5f,
				0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f,
				-0.5f, -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, -0.5f, 0.5f

		};

		float[] textureCoords = {
		0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0,
				0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0, 1, 1,
				1, 1, 0

		};

		int[] indices = { 0, 1, 3, 3, 1, 2, 4, 5, 7, 7, 5, 6, 8, 9, 11, 11, 9,
				10, 12, 13, 15, 15, 13, 14, 16, 17, 19, 19, 17, 18, 20, 21, 23,
				23, 21, 22

		};
		RawModel model = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture text = new ModelTexture(loader.loadTexture(new File(
				EchoEngine.RELITIVE_PATH_MODIFIER
						+ "textures/AncientHopscotch.png")));
		this.model = new TexturedModel(model, text);
		entity = new Entity(this.model, new Vector3f(0, 0, -1), 0, 0, 0, 1);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}

	public void postInit() {

	}

	public void render() {
		shader.start();
		shader.loadViewMatrix(camera);
		renderer.render(entity, shader);
		shader.stop();
	}

	public void update(double deltaTime) {
		// entity.increasePos(0, 0, -0.001f);
		entity.increaseRot(0.02f, 0.01f, 0.03f);
		camera.move();
	}

	public void polate(double accumulator) {

	}

	public void dispose() {
		loader.cleanUp();
		shader.cleanUp();
	}
}
