package chibi.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Test;

import chibi.generator.CBComparer;
import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.TreeUtils;

public class CBCompletionTest {
	
	public List<String> completeParameter(String methodSource, int methodStartLine, int offsetLine, String parameterType){
		List<String> parameterNames = new ArrayList<String>();
		String methodSection = "";
		try {
			methodSection = source(methodSource, methodStartLine, offsetLine);
			
			ITree methodSectionTree = CBComparer.tree(methodSection);
			List<ITree> lstTree =  TreeUtils.breadthFirst(methodSectionTree);
			
			int index = 0;
			for (ITree iTree : lstTree) {
				if (iTree.getType() == 8) {
					break;
				}
				index++;
			}
			
			lstTree.subList(0, index).clear();
			
			List<ITree> lstDeclarations = lstTree.stream().filter(n -> n.getType() == 60).collect(Collectors.toList());
			
			for (ITree iTreeDeclaration : lstDeclarations) {
				
				List<ITree> lstChildren = iTreeDeclaration.getChildren();
				for (int i = 0; i < lstChildren.size(); i++) {
					ITree children = lstChildren.get(i);
					if (children.getType() == 43 || children.getType() == 39) {
						if (children.getLabel().equals(parameterType)) {
							if (iTreeDeclaration.getChild(i+1).getType() == 59) {
								String nombreVariable = iTreeDeclaration.getChild(i+1).getChild(0).getLabel();
								parameterNames.add(nombreVariable);
							}
						}
						break;
					}					
				}							
			}
			
			System.out.println(lstTree);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return parameterNames;
	}
	
	private String source(String methodSource, int start, int end) throws IOException {
		StringBuilder sb = new StringBuilder();
		try {
			String[] lines = methodSource.split("\n");
			for (int i = 0; i <= end - start; i++) {
				sb.append(lines[i]);
				sb.append("\n");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return sb.toString();
	}
	
	private String getInvokeLine(String methodSource, int lineNumber) throws IOException {
		
		try {
			String[] lines = methodSource.split("\n");
			if (lines.length > 0) {
				return lines[lineNumber];	
			}			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
		return "";
	}
	
	public String getVariableTypeByName(ITree itreeBlock, String name){
		String type = "";
		
		List<ITree> lstDeclarations = itreeBlock.getChildren().stream().filter(n -> n.getType() == 60).collect(Collectors.toList());
		
		for (ITree iTreeDeclaration : lstDeclarations) {
			if (!type.equals("")) {
				break;
			}
			List<ITree> lstChildren = iTreeDeclaration.getChildren();
			for (int i = 0; i < lstChildren.size(); i++) {
				ITree children = lstChildren.get(i);
				ITree nameChildren = lstChildren.get(i+1);
				if (children.getType() == 43 || children.getType() == 39) {
					if (nameChildren.getLabel().equals(name)) {
						type = children.getLabel();
					}
					break;
				}					
			}							
		}
		
		return type;
	}
	
	public List<String> getSnippetParameterTypes(){
		String methodSource = "		JFrame f = new JFrame(n);";
		String snippetSource = "";
		//int lineStart = 1; //from CBMethod
		int lineEnd = 1;//offset 269
		int offsetCursor = 269;//from cursor
		int offsetLine = 214;//from api method
		
		List<String> lstParameterTypes = new ArrayList<String>();
		
		try {
			//Verify if the invocation is between two parentheses
			String invokeLine = getInvokeLine(methodSource, lineEnd);
			char leftChar = invokeLine.charAt(offsetCursor-offsetLine-1);
			char rightChar = invokeLine.charAt(offsetCursor-offsetLine+1);
			
			if (leftChar == '(' && rightChar== ')') {
				//get the type of method declaration, example Frame() or callMethod()
				
				int lastIndexDot = invokeLine.lastIndexOf(".", offsetCursor - offsetLine);
				int lastIndexSpace = invokeLine.lastIndexOf(" ", offsetCursor - offsetLine);
				int lastIndexNew = invokeLine.lastIndexOf("new", offsetCursor - offsetLine);
				ITree snippetTree = CBComparer.tree(snippetSource);
				
				
				//if it is an instance creation
				if (lastIndexSpace > -1 && lastIndexNew > -1) {
					String variableName = invokeLine.substring(lastIndexSpace,offsetCursor-offsetLine-1);
					
					List<ITree> lstDeclarations = snippetTree.getChildren().stream().filter(n -> n.getType() == 59).collect(Collectors.toList());
					
					for (ITree iTreeDeclaration : lstDeclarations) {
						if (iTreeDeclaration.getChild(0).getType() == 61 &&iTreeDeclaration.getChild(0).getLabel().equals(variableName) ) {
							ITree iTreeClassInstanceCreation = (iTreeDeclaration.getChild(1)).getChild(1);
							
							if (iTreeClassInstanceCreation.getType() == 14 && iTreeClassInstanceCreation.getChild(0).getType() == 43 && iTreeClassInstanceCreation.getChildren().size() > 1) {
								for (int i = 1; i < iTreeClassInstanceCreation.getChildren().size(); i++) {
									ITree iTreeParameter= iTreeClassInstanceCreation.getChild(i);
									if (iTreeParameter.getType() == 14) {
										lstParameterTypes.add(iTreeParameter.getChild(0).getLabel());
									}
									else if (iTreeParameter.getType() == 43) {
										lstParameterTypes.add(iTreeParameter.getLabel());
									}
								}
							}
						}
					}
				}
				else if (lastIndexDot > -1 || lastIndexSpace > -1) {
					//if it is a method invocation
					String methodName = invokeLine.substring(lastIndexDot,offsetCursor-offsetLine-1);
					List<ITree> lstMethodInvocations = snippetTree.getChildren().stream().filter(n -> n.getType() == 42).collect(Collectors.toList());
									
					//Verify is there is a receiver of the method call, example "x" in x.foo
					if (lastIndexDot > -1) {
						for (ITree iTreeMethod : lstMethodInvocations) {
							if (iTreeMethod.getChild(1).getLabel().equals(methodName)) {
								for (int i = 2; i < iTreeMethod.getChildren().size(); i++) {
									ITree iTreeParameter= iTreeMethod.getChild(i);
									if (iTreeParameter.getType() == 14) {
										lstParameterTypes.add(iTreeParameter.getChild(0).getLabel());
									}
									else if (iTreeParameter.getType() == 43) {
										lstParameterTypes.add(iTreeParameter.getLabel());
									}
								}
							}
						}
					}
					else{
						for (ITree iTreeMethod : lstMethodInvocations) {
							if (iTreeMethod.getChild(0).getLabel().equals(methodName)) {
								for (int i = 1; i < iTreeMethod.getChildren().size(); i++) {
									ITree iTreeParameter= iTreeMethod.getChild(i);
									if (iTreeParameter.getType() == 14) {
										lstParameterTypes.add(iTreeParameter.getChild(0).getLabel());
									}
									else if (iTreeParameter.getType() == 43) {
										lstParameterTypes.add(iTreeParameter.getLabel());
									}
								}
							}
						}
					}
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstParameterTypes;
	}
	
	@Test
	public void testParameter(){
		String methodSource = "		JFrame f = foo(n);";
		int lineStart = 13; //from CBMethod
		int lineEnd = 13;//offset 269
		completeParameter(methodSource, lineStart, lineEnd, "JFrame");
		List<String> lstParameterTypes = getSnippetParameterTypes();
		for (String parameterType : lstParameterTypes) {
			completeParameter(methodSource, lineStart, lineEnd, parameterType);
			
		}		
	}

}
