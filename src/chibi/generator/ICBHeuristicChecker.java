package chibi.generator;

import chibi.gumtreediff.tree.ITree;
import chibi.model.ICBWarning;

public interface ICBHeuristicChecker {
	public ICBWarning check(ITree methodBody, ITree snippet, int snippetStartLineNumber, String className, String methodName);
}
