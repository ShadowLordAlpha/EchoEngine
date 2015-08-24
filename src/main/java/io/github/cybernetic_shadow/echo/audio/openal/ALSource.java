package io.github.cybernetic_shadow.echo.audio.openal;

import java.nio.ByteBuffer;

import org.joml.Vector3f;
import org.lwjgl.openal.AL10;
import org.lwjgl.system.jemalloc.JEmalloc;

public class ALSource {
	
	private int source;
	private ByteBuffer position;
	private ByteBuffer velocity;
	private float pitch = 1.0f;
	private float gain = 1.0f;
	private int buffer;
	private boolean looping = false;
	
	public ALSource() {
		source = AL10.alGenSources();
		position = JEmalloc.je_calloc(3, 4);
		velocity = JEmalloc.je_calloc(3, 4);
	}
	
	public int getSource() {
		return source;
	}
	
	public Vector3f getPosition(Vector3f dest) {
		return dest.set(0, position);
	}
	
	public void setPosition(Vector3f v) {
		v.get(0, position);
		AL10.alSourcefv(source, AL10.AL_POSITION, position);
	}
	
	public Vector3f getVelocity(Vector3f dest) {
		return dest.set(0, velocity);
	}
	
	public void setVelocity(Vector3f v) {
		v.get(0, velocity);
		AL10.alSourcefv(source, AL10.AL_VELOCITY, velocity);
	}
	
	public float getPitch() {
		return pitch;
	}
	
	public void setPitch(float pitch) {
		this.pitch = pitch;
		AL10.alSourcef(source, AL10.AL_PITCH, pitch);
	}
	
	public float getGain() {
		return gain;
	}
	
	public void setGain(float gain) {
		this.gain = gain;
		AL10.alSourcef(source, AL10.AL_GAIN, gain);
	}
	
	public int getBuffer() {
		return buffer;
	}
	
	public void setBuffer(ALBuffer buffer) {
		this.buffer = buffer.getBuffer();
		AL10.alSourcei(source, AL10.AL_BUFFER, buffer.getBuffer());
	}
	
	public boolean isLooping() {
		return looping;
	}
	
	public void setLooping(boolean looping) {
		this.looping = looping;
		AL10.alSourcei(source, AL10.AL_LOOPING, (looping) ? AL10.AL_TRUE : AL10.AL_FALSE);
	}

	public void delete() {
		JEmalloc.je_free(position);
		JEmalloc.je_free(velocity);
		AL10.alDeleteSources(source);
		source = 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + buffer;
		result = prime * result + Float.floatToIntBits(gain);
		result = prime * result + (looping ? 1231 : 1237);
		result = prime * result + Float.floatToIntBits(pitch);
		result = prime * result + ((position == null) ? 0 : position.hashCode());
		result = prime * result + source;
		result = prime * result + ((velocity == null) ? 0 : velocity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ALSource other = (ALSource) obj;
		if (buffer != other.buffer)
			return false;
		if (Float.floatToIntBits(gain) != Float.floatToIntBits(other.gain))
			return false;
		if (looping != other.looping)
			return false;
		if (Float.floatToIntBits(pitch) != Float.floatToIntBits(other.pitch))
			return false;
		if (position == null) {
			if (other.position != null)
				return false;
		} else if (!position.equals(other.position))
			return false;
		if (source != other.source)
			return false;
		if (velocity == null) {
			if (other.velocity != null)
				return false;
		} else if (!velocity.equals(other.velocity))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ALSource [source=" + source + ", position=" + position + ", velocity=" + velocity + ", pitch=" + pitch
				+ ", gain=" + gain + ", buffer=" + buffer + ", looping=" + looping + "]";
	}

	@Override
	protected void finalize() throws Throwable {
		if(source != 0) {
			JEmalloc.je_free(position);
			JEmalloc.je_free(velocity);
			AL10.alDeleteSources(source);
		}
	}
}
