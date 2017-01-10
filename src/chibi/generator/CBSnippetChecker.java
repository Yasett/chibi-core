package chibi.generator;

import chibi.gumtreediff.actions.model.*;
import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.TreeMethodInvocation;
import chibi.gumtreediff.tree.TreeUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CBSnippetChecker extends CBAbstractChecker {
	
	public double check(ITree methodBody, ITree snippet) {
		List<Action> actions = getActions(methodBody, snippet);
		List<ITree> snippetTree = TreeUtils.preOrder(snippet);
		List<ITree> sourceCodeTree = TreeUtils.preOrder(methodBody);
		List<ITree> methodsSourceCode = sourceCodeTree.stream().filter(n -> n.getType() == 32).collect(Collectors.toList());
		List<ITree> methodsSnippet = snippetTree.stream().filter(n -> n.getType() == 32).collect(Collectors.toList());
		int exceptionalMethodInvocations = 0;

		Set<ITree> missingNodes = new HashSet<ITree>();
		for (Action a : actions) {
			if (a instanceof Delete) {
				// User code might have more statements than the snippet, which
				// will not be counted
			} else if (a instanceof Update) {
				if (a.getNode().getType() == 42) {// SimpleName
					//Special case 1: Pseudo UPD of a method, that is an INS instead
					// if it's not already added to Actions list
					String methodName = ((Update) a).getValue();
					long exists = actions.stream().filter(n -> n instanceof Insert && n.getNode().getChildren().stream().filter(p -> p.getLabel().equals(methodName)).count() > 0).count();
					if (exists == 0) {
						// Search if it's not an simple name UPD, but a new method INS
						List<ITree> methodFound = methodsSnippet.stream().filter(n -> n.getType() == 32 && ((TreeMethodInvocation) n).getMethodName().equals(methodName)).collect(Collectors.toList());
						if (methodFound.size() > 0) {
							missingNodes.addAll(methodFound);
							// Because of the extra weight given to methods
							exceptionalMethodInvocations++;
						}
					}									
				}
				else if (a.getNode().getType() == 43){
					//Special case 2: Pseudo UPD of a type, that is an INS instead
					//Get the type name from the UPD action and look for it in the snippet
					String updatedType = ((Update)a).toString().substring(((Update)a).toString().indexOf(" to ") + 4);
					if (snippetTree.stream().filter(n -> n.getType() == 43 && n.getLabel().equals(updatedType)).count() > 0) {
						missingNodes.add(a.getNode());
					}
				}
			} else if (a instanceof Move) {
				// missingNodes.add(a.getNode()); the same instruction, even
				// moved, is a good indicator of snippet match
			} else if (a instanceof Insert) {
				int nodeType = a.getNode().getType();
				if (nodeType == 8) {// Block
					// Search if it has children already present in the tree
					// if so, block is not necessary
					Stream<Action> children = actions.stream().filter(n -> n.getNode().getParent().getId() == a.getNode().getId());

					if (children.count() == 0) {
						missingNodes.add(a.getNode());
					}
				} else if (nodeType == 61) {// While
					// Search if same while will be in DEL action
					Stream<Action> whileActions = actions.stream().filter(n -> n.getNode().getType() == 61 && n instanceof Delete);

					if (whileActions.count() == 0) {
						missingNodes.add(a.getNode());
					}

				} else if(nodeType == 21){//ExpressionStatement
					//Special case 2: Pseudo MethodInvocation, that is just a receiver renaming. 
					//For example: x.foo() to y.foo(), is not an insert
					Optional<ITree> methodInvocation = a.getNode().getChildren().stream().filter(n -> n.getType() == 32)
							.findFirst();
					if (methodInvocation.isPresent()) {
						List<Action> childrenDelete = actions
								.stream().filter(
										n -> n instanceof Delete && n.getNode().getType() == 21
												&& n.getNode().getChildren().stream()
														.filter(p -> ((TreeMethodInvocation) p).getMethodName()
																.equals(((TreeMethodInvocation)methodInvocation.get()).getMethodName()))
														.count() > 0)
								.collect(Collectors.toList());
						
						if (childrenDelete.size() == 0) {
							missingNodes.add(a.getNode());
						}						
					}
					else {
						//add node normally
						missingNodes.add(a.getNode());
					}
					
				}else if (nodeType != 42 && nodeType != 39 && nodeType != 26) {			
					missingNodes.add(a.getNode());
				}
			}
		}
		snippetTree.removeIf(a -> a.getType() == 42 || a.getType() == 39);
		double numberNodesSnippet = snippetTree.size() - 6;
		// Method invocations will have a weight of 2, but the rest of sentences have a weight of 1
		//long snippetMethodInvocations = snippetTree.stream().filter(n -> n.getType() == 32).count();
		//long actionMethodInvocations = actions.stream().filter(n -> n.getNode().getType() == 32 && n instanceof Insert).count();
		
		double difference = 0;
		
		
			//Count snippet methods that are present in the developer code
			int methodsMatch = 0;
			for (ITree methodSnippet : methodsSnippet) {
				if (methodsSourceCode.stream().filter(p -> ((TreeMethodInvocation) p).getMethodName()
																	.equals(((TreeMethodInvocation)methodSnippet).getMethodName())).count() > 0) {
					methodsMatch++;
				}
			}
			
			if (missingNodes.size() - methodsMatch < 0) {
				difference =  0;
			}else if(missingNodes.size() + exceptionalMethodInvocations - methodsMatch > numberNodesSnippet){
				difference = 1;
			}else{
				difference =  (double)(missingNodes.size() + exceptionalMethodInvocations - methodsMatch) / numberNodesSnippet;
			}
				
		
		//return (((double) missingNodes.size() + actionMethodInvocations + exceptionalMethodInvocations) / (numberNodesSnippet + snippetMethodInvocations));
		return difference;
	}

}
