package chibi.generator;

import chibi.model.CBWarning;
import chibi.gumtreediff.actions.model.Action;
import chibi.gumtreediff.actions.model.Insert;
import chibi.gumtreediff.actions.model.Update;
import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.TreeMethodInvocation;
import chibi.gumtreediff.tree.TreeUtils;
import chibi.model.CBEmptyWarning;
import chibi.model.ICBWarning;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class CBMissingMessageChecker extends CBAbstractChecker implements ICBHeuristicChecker{
	
	public ICBWarning check(ITree methodBody, ITree snippet, int snippetStartLineNumber, String className, String methodName) {
		HashMap<String, Integer> missingMethods = new HashMap<String, Integer>();
		List<Action> actions = getActions(methodBody, snippet);
		for (Action a : actions) {
			if (a instanceof Insert) {
				if (a.getNode().getType() == 32) {// Method invocation
					//isMissingMessage = true;
					missingMethods.put(((TreeMethodInvocation) a.getNode()).getMethodName(), a.getNode().getLineNumber() + snippetStartLineNumber);
				}
			} else if (a instanceof Update) {
				if (a.getNode().getType() == 42) {// SimpleName
					// Check if it's not already added to Actions list
					String methodSearched = ((Update) a).getValue();
					long exists = actions.stream().filter(n -> n instanceof Insert && n.getNode().getChildren().stream().filter(p -> p.getLabel().equals(methodName)).count() > 0).count();

					if (exists == 0) {
						// Search if it's not an simple name UPD, but a new method INS
						List<ITree> methodFound = TreeUtils.preOrder(snippet).stream().filter(n -> n.getType() == 32 && ((TreeMethodInvocation) n).getMethodName().equals(methodSearched))
								.collect(Collectors.toList());
						if (methodFound.size() > 0) {
							//isMissingMessage = true;
							TreeMethodInvocation treeMethod = (TreeMethodInvocation)methodFound.get(0);
							missingMethods.put(treeMethod.getMethodName(), ((Update) a).getOrigin().getLineNumber() + snippetStartLineNumber);
						}
					}
				}
			}

//			if (isMissingMessage) {
//				missingMethods.put(((TreeMethodInvocation) a.getNode()).getMethodName(), a.getNode().getLineNumber() + snippetStartLineNumber);
//			}
		}

		if (missingMethods.size() == 0) {
			return new CBEmptyWarning();
		} else {
			return new CBWarning("Missing message(s)", missingMethods, className, methodName);
		}

	}
}
