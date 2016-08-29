/* 
 * Git library
 * Copyright (c) Project Nayuki
 * 
 * https://www.nayuki.io/
 */

package io.nayuki.git;

import java.lang.ref.WeakReference;


public final class TreeEntry {
	
	public final Type type;
	public final String name;
	public final ObjectId id;
	
	
	
	public TreeEntry(Type type, String name, byte[] hash, WeakReference<Repository> srcRepo) {
		if (name == null || hash == null)
			throw new NullPointerException();
		if (name.indexOf('\0') != -1)
			throw new IllegalArgumentException("Name cannot contai null character");
		
		this.type = type;
		this.name = name;
		if (type == Type.NORMAL_FILE || type == Type.EXECUTABLE_FILE)
			id = new BlobId(hash, srcRepo);
		else if (type == Type.DIRECTORY)
			id = new TreeId(hash, srcRepo);
		else
			throw new IllegalArgumentException();
	}
	
	
	
	public String toString() {
		return String.format("TreeEntry(mode=%s, name=\"%s\", id=%s)", Integer.toString(type.mode, 8), name, id.hexString);
	}
	
	
	
	public enum Type {
		
		// Numbers are octal
		DIRECTORY      (0040000),
		NORMAL_FILE    (0100644),
		EXECUTABLE_FILE(0100755),
		SYMBOLIC_LINK  (0120000);
		
		
		public final int mode;
		
		
		private Type(int mode) {
			this.mode = mode;
		}
		
		
		public static Type fromMode(int mode) {
			if (mode == DIRECTORY.mode      ) return DIRECTORY;
			if (mode == NORMAL_FILE.mode    ) return NORMAL_FILE;
			if (mode == EXECUTABLE_FILE.mode) return EXECUTABLE_FILE;
			if (mode == SYMBOLIC_LINK.mode  ) return SYMBOLIC_LINK;
			throw new IllegalArgumentException("Unknown mode: " + Integer.toString(mode, 8));
		}
		
	}
	
}