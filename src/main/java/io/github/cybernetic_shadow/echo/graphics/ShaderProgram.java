package io.github.cybernetic_shadow.echo.graphics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

public abstract class ShaderProgram {

	private static FloatBuffer worker = BufferUtils.createFloatBuffer(16);
	
	private int programID;
	private int vertexShaderID;
	private int fragmentShaderID;
	
	public ShaderProgram(File vShader, File fShader) {
		vertexShaderID = loadShader(vShader, GL20.GL_VERTEX_SHADER);
		fragmentShaderID = loadShader(fShader, GL20.GL_FRAGMENT_SHADER);
		programID = GL20.glCreateProgram();
		GL20.glAttachShader(programID, vertexShaderID);
		GL20.glAttachShader(programID, fragmentShaderID);
		bindAttribs();
		GL20.glLinkProgram(programID);
		GL20.glValidateProgram(programID);
		getAllUniformLocations();
	}
	
	protected abstract void getAllUniformLocations();
	
	protected int getUniformLocation(String uniformName) {
		return GL20.glGetUniformLocation(programID, uniformName);
	}
	
	protected void loadFloat(int location, float value) {
		GL20.glUniform1f(location, value);
	}
	
	protected void loadVector(int location, Vector3f value) {
		GL20.glUniform3f(location, value.x, value.y, value.z);
	}
	
	protected void loadBoolean(int location, boolean value) {
		GL20.glUniform1f(location, (value) ? 1 : 0);
	}
	
	protected void loadMatrix(int location, Matrix4f value) {
		value.get(worker);
		GL20.glUniformMatrix4fv(location, false, worker);
	}
	
	public void start() {
		GL20.glUseProgram(programID);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(programID, vertexShaderID);
		GL20.glDetachShader(programID, fragmentShaderID);
		GL20.glDeleteShader(vertexShaderID);
		GL20.glDeleteShader(fragmentShaderID);
		GL20.glDeleteProgram(programID);
	}
	
	protected abstract void bindAttribs();
	
	protected void bindAttrib(int attribute, String name) {
		GL20.glBindAttribLocation(programID, attribute, name);
	}
	
	private static int loadShader(File file, int type) {
		StringBuffer buffer = new StringBuffer();
		
		try(BufferedReader fr = new BufferedReader(new FileReader(file))) {
			String line;
			while((line = fr.readLine()) != null) {
				buffer.append(line).append("\n");
			}
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, buffer);
		GL20.glCompileShader(shaderID);
		if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to Compile Shader!");
			System.err.println(GL20.glGetShaderInfoLog(shaderID, 500));
			System.exit(-1);
		}
		return shaderID;
	}
}
