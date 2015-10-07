package io.github.cybernetic_shadow.echo.graphics;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {
	
	private static float FOV = 90;
	private static float FAR_PLANE = 1000;
	private static float NEAR_PLANE = 0.01f;
	
	private Matrix4f projectionMatrix;
	
	public Renderer(StaticShader shader) {
		projectionMatrix = Maths.createProjectionMatrix(new Matrix4f(), 1024, 768, FOV, FAR_PLANE, NEAR_PLANE);
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}
	
	public void render(Entity entity, StaticShader shader) {
		TexturedModel tModel = entity.getModel();
		RawModel model = tModel.getRawModel();
		GL30.glBindVertexArray(model.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
		shader.loadTransformationMatrix(transformationMatrix);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tModel.getTexture().getTextureID());
		GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL30.glBindVertexArray(0);
	}
}
