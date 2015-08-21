package io.github.cybernetic_shadow.graphic;

import java.io.File;

import org.lwjgl.opengl.GL20;

public class Shader {
	
	private int shaderName;

	public Shader(int shaderType, File file) {
		
		// Load Shader Data into Memory
		
		shaderName = GL20.glCreateShader(GL20.GL_VERTEX_SHADER);
		GL20.glShaderSource(shaderName, );
	}
}
