package io.github.cybernetic_shadow.echo.audio.openal;

import org.lwjgl.openal.AL10;

public class ALBuffer {
	
	private int buffer = 0;

	public ALBuffer() {
		buffer = AL10.alGenBuffers();
	}
	
	public int getBuffer() {
		return buffer;
	}
}
