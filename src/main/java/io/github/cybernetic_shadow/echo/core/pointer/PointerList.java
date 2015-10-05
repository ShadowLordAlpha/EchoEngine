package io.github.cybernetic_shadow.echo.core.pointer;

import java.util.concurrent.ConcurrentHashMap;

/**
 * This class contains a list of the pointer and their object. This is to be able to return the same object over and
 * over instead of creating a new object each time a pointer needs to be wrapped in one.
 * 
 * @author Josh "ShadowLordAlpha"
 */
public final class PointerList {

	private PointerList() {}
	
	private static ConcurrentHashMap<Long, PointerWrapper> pointerList;
	
	public static void init() {
		pointerList = new ConcurrentHashMap<Long, PointerWrapper>();
	}
	
	public static void terminate() {
		pointerList.clear();
		pointerList = null;
	}
	
	public static void addPointer(PointerWrapper pointer) {
		if(pointerList != null && pointer != null) {
			pointerList.put(pointer.address(), pointer);
		}
	}
	
	public static PointerWrapper getPointer(long pointer) {
		if(pointerList != null) {
			return pointerList.get(pointer);
		}
		return null;
	}
	
	public static void removePointer(PointerWrapper pointer) {
		if(pointerList != null && pointer != null) {
			pointerList.remove(pointer.address());
		}
	}
}
