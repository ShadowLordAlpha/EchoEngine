package io.github.cybernetic_shadow.echo.graphics;

import java.io.File;

import io.github.cybernetic_shadow.echo.core.EchoEngine;

public class StaticShader extends ShaderProgram {

	private static final File VERTEX_FILE = new File(EchoEngine.RELITIVE_PATH_MODIFIER + "shaders/v_shader.glsl");
	private static final File FRAG_FILE = new File(EchoEngine.RELITIVE_PATH_MODIFIER + "shaders/f_shader.glsl");
	
	public StaticShader() {
		super(VERTEX_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttribs() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "text_coords");
	}
}
