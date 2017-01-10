package chibi.generator;

import chibi.gumtreediff.actions.model.Action;
import chibi.gumtreediff.actions.model.Insert;
import chibi.gumtreediff.tree.ITree;
import chibi.model.CBEmptyWarning;
import chibi.model.ICBWarning;
import chibi.model.CBWarning;

import java.util.HashMap;
import java.util.List;

public class CBMissingInitializationParamChecker extends CBAbstractChecker implements ICBHeuristicChecker{

	public ICBWarning check(ITree methodBody, ITree snippet, int snippetStartLineNumber, String className, String methodName) {
		HashMap<String, Integer> missingInitializationParams = new HashMap<String, Integer>();
		List<Action> actions = getActions(methodBody, snippet);

		for (Action a : actions) {
			if (a instanceof Insert) {
				// Ask if node is SimpleName (42) and its parent is a ClassInstanceCreation (14)
				if (a.getNode().getParent().getType() == 14) {
					missingInitializationParams.put(a.getNode().getLabel(), a.getNode().getLineNumber() + snippetStartLineNumber);
				}
			}
		}

		if (missingInitializationParams.size() == 0) {
			return new CBEmptyWarning();
		} else {
			return new CBWarning("Missing parameter(s) during initialization", missingInitializationParams, className, methodName);
		}

	}
}
