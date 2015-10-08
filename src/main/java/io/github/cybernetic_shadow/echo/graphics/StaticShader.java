package io.github.cybernetic_shadow.echo.graphics;

import java.io.File;

import org.joml.Matrix4f;

import io.github.cybernetic_shadow.echo.core.EchoEngine;

public class StaticShader extends ShaderProgram {

	private static final File VERTEX_FILE = new File(EchoEngine.RELITIVE_PATH_MODIFIER + "shaders/v_shader.glsl");
	private static final File FRAG_FILE = new File(EchoEngine.RELITIVE_PATH_MODIFIER + "shaders/f_shader.glsl");
	
	private int location_transformationMatrix;
	private int location_projectionMatrix;
	private int location_viewMatrix;
	
	public StaticShader() {
		super(VERTEX_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttribs() {
		super.bindAttrib(0, "position");
		super.bindAttrib(1, "text_coords");
	}

	@Override
	protected void getAllUniformLocations() {
		location_transformationMatrix = super.getUniformLocation("transformationMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
		location_viewMatrix = super.getUniformLocation("viewMatrix");
	}
	
	public void loadTransformationMatrix(Matrix4f matrix) {
		super.loadMatrix(location_transformationMatrix, matrix);
	}
	
	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrix(location_projectionMatrix, matrix);
	}
	
	private Matrix4f matcam = new Matrix4f();
	
	public void loadViewMatrix(Camera cam) {
		super.loadMatrix(location_viewMatrix, Maths.createViewmatrix(matcam, cam));
	}
}
