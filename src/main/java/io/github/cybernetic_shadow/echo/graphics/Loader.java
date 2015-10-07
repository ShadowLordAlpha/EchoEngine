package io.github.cybernetic_shadow.echo.graphics;

import static org.lwjgl.system.MemoryUtil.memAddress;
import static org.lwjgl.system.MemoryUtil.memByteBuffer;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.jemalloc.JEmalloc;

public class Loader {

	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(float[] positions, float[] textCoords, int[] indices) {
		int vaoID = createVAO();
		bindIndicesBuffer(indices);
		storeDataInAttribList(0, 3, positions);
		storeDataInAttribList(1, 2, textCoords);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	public int loadTexture(File file) {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		FileChannel fChannel;
		try {
			fChannel = FileChannel.open(file.toPath());
			
			// Load image to be used as the splash screen
			ByteBuffer imageData = JEmalloc.je_malloc(fChannel.size() + 1);
			int read;
			do {
				read = fChannel.read(imageData);
			} while (read != -1);
			imageData.flip();

			ByteBuffer imagePart = JEmalloc.je_malloc(12);
			// Using native method in order to use only one ByteBuffer instead of 3
			long ipMemAddress = memAddress(imagePart);
			long __result = STBImage.nstbi_load_from_memory(memAddress(imageData), imageData.remaining(), ipMemAddress,
					ipMemAddress + 4, ipMemAddress + 8, 0);
			JEmalloc.je_free(imageData);

			int width = imagePart.getInt(0);
			int height = imagePart.getInt(4);
			int components = imagePart.getInt(8);
			JEmalloc.je_free(imagePart);

			ByteBuffer image = memByteBuffer(__result, width * height * components);
			
			int textureID = GL11.glGenTextures();
			
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
			if (components == 3) {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, image);
			} else {
				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, image);

				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			}
			
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			
			//JEmalloc.je_free(image); // ????
			
			textures.add(textureID);
			return textureID;
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return 0;
	}

	private int createVAO() {
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	private void storeDataInAttribList(int attribNumber, int coordSize, float[] data) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		FloatBuffer buffer = storeDataInFloatBuffer(data);
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		GL20.glVertexAttribPointer(attribNumber, coordSize, GL11.GL_FLOAT, false, 0, 0);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	private void unbindVAO() {
		GL30.glBindVertexArray(0);
	}

	private void bindIndicesBuffer(int[] indices) {
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		IntBuffer buffer = storeDataInIntBuffer(indices);
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	private IntBuffer storeDataInIntBuffer(int[] data) {
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	private FloatBuffer storeDataInFloatBuffer(float[] data) {
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();
		return buffer;
	}

	public void cleanUp() {
		for (int vao : vaos) {
			GL30.glDeleteVertexArrays(vao);
		}

		for (int vbo : vbos) {
			GL15.glDeleteBuffers(vbo);
		}
		
		for (int text : textures) {
			GL11.glDeleteTextures(text);
		}
	}
}
