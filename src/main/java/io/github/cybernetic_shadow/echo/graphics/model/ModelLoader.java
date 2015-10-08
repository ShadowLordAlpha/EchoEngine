package io.github.cybernetic_shadow.echo.graphics.model;

import io.github.cybernetic_shadow.echo.graphics.RawModel;

import java.io.File;

public interface ModelLoader {

	public RawModel loadModel(File flie);
}
