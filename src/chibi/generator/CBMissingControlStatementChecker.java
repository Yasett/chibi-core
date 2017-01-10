package chibi.generator;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Stream;

import chibi.model.CBStatementType;
import chibi.model.ICBWarning;
import chibi.model.CBWarning;
import chibi.gumtreediff.actions.model.Action;
import chibi.gumtreediff.actions.model.Delete;
import chibi.gumtreediff.actions.model.Insert;
import chibi.gumtreediff.tree.ITree;
import chibi.model.CBEmptyWarning;

public class CBMissingControlStatementChecker extends CBAbstractChecker implements ICBHeuristicChecker{
	
	public ICBWarning check(ITree snippet, ITree methodBody, int snippetStartLineNumber, String className, String methodName) {
		List<Action> actions = getActions(snippet, methodBody);
		HashMap<String, Integer> missingStatements = new HashMap<String, Integer>();
		
		for(Action a: actions){
			if (a instanceof Insert) {
				if (a.getNode().getType() == 61 || a.getNode().getType() == 24 || a.getNode().getType() == 70 || a.getNode().getType() == 54) {//While, For, ForEach
					switch (a.getNode().getType()) {
					case 61:
						Stream<Action> whileActions = actions.stream().filter(n -> n.getNode().getType() == 61 && n instanceof Delete);
						
						if (whileActions.count() == 0) {
							missingStatements.put(CBStatementType.While.toString(), a.getNode().getLineNumber() + snippetStartLineNumber);
						}						
						break;						
					case 24:
						Stream<Action> forActions = actions.stream().filter(n -> n.getNode().getType() == 24 && n instanceof Delete);
						
						if (forActions.count() == 0) {
							missingStatements.put(CBStatementType.For.toString(), a.getNode().getLineNumber() + snippetStartLineNumber);
						}						
						break;
					case 70:
						Stream<Action> forEachActions = actions.stream().filter(n -> n.getNode().getType() == 70 && n instanceof Delete);
						
						if (forEachActions.count() == 0) {
							missingStatements.put(CBStatementType.ForEach.toString(), a.getNode().getLineNumber() + snippetStartLineNumber);
						}						
						break;
					case 54:
						Stream<Action> tryActions = actions.stream().filter(n -> n.getNode().getType() == 54 && n instanceof Delete);
						
						if (tryActions.count() == 0) {
							missingStatements.put(CBStatementType.Try.toString(), a.getNode().getLineNumber() + snippetStartLineNumber);
						}						
						break;
					default:
						break;
					}
				}
			}
		}
		
		if (missingStatements.size() == 0) {
			return new CBEmptyWarning();
		}
		else {
			return new CBWarning("Missing control flow structure(s)", missingStatements, className, methodName);
		}
		
	}
}
