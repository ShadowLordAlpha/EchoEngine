package io.github.cybernetic_shadow.echo.graphics;

import org.joml.Matrix4f;
import org.joml.Vector3f;

public class Maths {

	public static Matrix4f createTransformationMatrix(Vector3f translation, float rx, float ry, float rz, float scale){
		Matrix4f matrix = new Matrix4f();
		matrix.identity();
		matrix.translate(translation);
		matrix.rotateXYZ(rx, ry, rz);
		matrix.scale(scale);
		return matrix;
	}
	
	public static Matrix4f createProjectionMatrix(Matrix4f dest, float width, float height, float fov, float zfar, float znear) {
		float aspectRatio = width / height;
		float y_scale = (float) ((1f / Math.tan(Math.toRadians(fov / 2f))) * aspectRatio);
		float x_scale = y_scale / aspectRatio;
		float frustum_length = zfar - znear;
		
		dest.zero();
		dest.m00 = x_scale;
		dest.m11 = y_scale;
		dest.m22 = -((zfar + znear) / frustum_length);
		dest.m23 = -1;
		dest.m32 = -((2 * znear * zfar) / frustum_length);
		dest.m33 = 0;
		
		return dest;
	}
	
	public static Matrix4f createViewmatrix(Matrix4f dest, Camera camera) {
		dest.identity();
		dest.rotateXYZ(camera.getPitch(), camera.getYaw(), camera.getRoll());
		dest.translate(camera.getPosition().negate(new Vector3f()));
		return dest;
	}
}
