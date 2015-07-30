package io.github.cybernetic_shadow.graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class ArrayObject {
	
	private int vaoID = 0;
	private BufferObject[] attributeList = new BufferObject[16];
	
	public ArrayObject() {
		vaoID = GL30.glGenVertexArrays();
		bind();
	}
	
	public void bind() {
		if(GL11.glGetInteger(GL30.GL_VERTEX_ARRAY_BINDING) != vaoID) {
			GL30.glBindVertexArray(vaoID);
		}
	}
}
