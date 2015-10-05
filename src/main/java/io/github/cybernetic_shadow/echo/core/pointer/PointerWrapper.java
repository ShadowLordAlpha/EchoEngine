/*
 * Copyright LWJGL. All rights reserved.
 * License terms: http://lwjgl.org/license.php
 */
package io.github.cybernetic_shadow.echo.core.pointer;

import org.lwjgl.LWJGLUtil;
import static org.lwjgl.system.MemoryUtil.*;

/** An object wrapper around a native pointer address. This is a direct copy of LWJGL 3's PointerWrapper */
public abstract class PointerWrapper {

	protected final long pointer;

	protected PointerWrapper(long pointer) {
		if ( LWJGLUtil.CHECKS && pointer == NULL )
			throw new NullPointerException();

		this.pointer = pointer;
	}

	public final long address() {
		return pointer;
	}

	public boolean equals(Object o) {
		if ( this == o ) return true;
		if ( !(o instanceof PointerWrapper) ) return false;

		PointerWrapper that = (PointerWrapper)o;

		return pointer == that.pointer;

	}

	public int hashCode() {
		return (int)(pointer ^ (pointer >>> 32));
	}

	public String toString() {
		return String.format("%s pointer [0x%X]", getClass().getSimpleName(), pointer);
	}
}