package io.github.cybernetic_shadow.echo.core;

import java.io.File;

import io.github.cybernetic_shadow.echo.graphics.Loader;
import io.github.cybernetic_shadow.echo.graphics.ModelTexture;
import io.github.cybernetic_shadow.echo.graphics.RawModel;
import io.github.cybernetic_shadow.echo.graphics.Renderer;
import io.github.cybernetic_shadow.echo.graphics.StaticShader;
import io.github.cybernetic_shadow.echo.graphics.TexturedModel;

public class Game {

	Loader loader = new Loader();
	Renderer renderer = new Renderer();
	TexturedModel model = null;
	StaticShader shader;

	public void preInit() {

	}

	public void init() {
		shader = new StaticShader();
		float[] vertices = {-0.5f, 0.5f, 0.0f, -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.5f, 0.5f, 0.0f};
		float[] textCoords = {0, 0, 0, 1, 1, 1, 1, 0};
		int[] indices = {0, 1, 3, 3, 1, 2};
		RawModel model = loader.loadToVAO(vertices, textCoords, indices);
		ModelTexture text = new ModelTexture(loader.loadTexture(new File(EchoEngine.RELITIVE_PATH_MODIFIER + "textures/fern.png")));
		this.model = new TexturedModel(model, text);
	}

	public void postInit() {

	}

	public void render() {
		shader.start();
		renderer.render(model);
		shader.stop();
	}

	public void update(double deltaTime) {

	}

	public void polate(double accumulator) {

	}

	public void dispose() {
		loader.cleanUp();
		shader.cleanUp();
	}
}
