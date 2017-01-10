package chibi.gumtreediff.tree;

import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.Tree;
import chibi.gumtreediff.tree.TreeMethodInvocation;

public class TreeMethodInvocation extends Tree{
	
	private String methodName;

	public TreeMethodInvocation(int type, String label, String methodName) {
		super(type, label);
		this.methodName = methodName;
	}

	public TreeMethodInvocation(TreeMethodInvocation other) {
		super(other);
		methodName = other.methodName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	@Override
    public TreeMethodInvocation deepCopy() {
		TreeMethodInvocation copy = new TreeMethodInvocation(this);
        for (ITree child : getChildren())
            copy.addChild(child.deepCopy());
        return copy;
    }

}
