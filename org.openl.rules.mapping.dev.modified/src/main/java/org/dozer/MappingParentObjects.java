package org.dozer;

import java.util.List;
import java.util.Stack;

public class MappingParentObjects {
	private Stack<Object> srcObjects = new Stack<Object>();
	private Stack<Object> dstObjects = new Stack<Object>();

	protected MappingParentObjects() {
	}

	protected void push(Object src, Object dest) {
		srcObjects.push(src);
		dstObjects.push(dest);
	}

	protected void pop() {
		srcObjects.pop();
		dstObjects.pop();
	}
	
	public List<Object> getSourceParents() {
		return srcObjects.subList(0, srcObjects.size());
	}

	public List<Object> getDestParents() {
		return dstObjects.subList(0, dstObjects.size());
	}

}