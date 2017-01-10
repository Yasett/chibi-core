package chibi.generator;

import java.util.List;

import chibi.gumtreediff.actions.ActionGenerator;
import chibi.gumtreediff.actions.model.Action;
import chibi.gumtreediff.matchers.Matcher;
import chibi.gumtreediff.matchers.Matchers;
import chibi.gumtreediff.tree.ITree;

public abstract class CBAbstractChecker {
	private double threshold;	
	
	public List<Action> getActions(ITree methodTree, ITree snippetTree) {
		snippetTree = snippetTree.deepCopy();
		methodTree = methodTree.deepCopy();
		// retrieve the default matcher
		Matcher matcher = Matchers.getInstance().getMatcher(methodTree, snippetTree);
		matcher.match();
		ActionGenerator actionGenerator = new ActionGenerator(methodTree, snippetTree, matcher.getMappings());
		actionGenerator.generate();
		List<Action> actions = actionGenerator.getActions();
		for (Action a : actions) {
			System.out.println(a.getNode().toTreeString());
		}
		return actions;
	}
	
	public CBAbstractChecker[] checkers() {
		return new CBAbstractChecker[] { new CBSnippetChecker(), new CBMissingMessageChecker(), new CBMissingControlStatementChecker() };
	}

	public double getThreshold() {
		return threshold;
	}

	public void setThreshold(double threshold) {
		this.threshold = threshold;
	}
}
