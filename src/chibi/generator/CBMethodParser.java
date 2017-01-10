package chibi.generator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;

import chibi.model.CBClass;
import chibi.model.CBMethod;

public class CBMethodParser extends ASTVisitor {
	private List<CBMethod> methods;
	private String fileName;
	private static CompilationUnit cu;
	private static List<String> lines;
	private static CBClass cbClass;

	public CBMethodParser(String fn) {
		fileName = fn;
		methods = new ArrayList<CBMethod>();
	}

	public boolean visit(MethodDeclaration node) {

		StringBuilder builder = new StringBuilder();
//		for (Object n : node.getBody().statements()) {
//			builder.append(n.toString());
//		
			
		String signature = lines.get(cu.getLineNumber(node.getStartPosition())-1).trim().replace("{", "").replace("}", "");		
		for (int i = cu.getLineNumber(node.getStartPosition()); i < cu.getLineNumber(node.getStartPosition()+node.getLength()-1)-1; i++) {
			builder.append(lines.get(i));
			builder.append("\n");
		}
//
//		int inicioBusqueda = cu.getLineNumber(node.getStartPosition());
//		int lineasVacias = 0;
//
//		while (lines.get(inicioBusqueda).replace("\t", "").equals("")) {
//			lineasVacias++;
//			inicioBusqueda++;
//		}

		CBMethod method = new CBMethod(node.getName().getIdentifier(), fileName, node.parameters().size(),
				builder.toString(), cu.getLineNumber(node.getStartPosition()), signature, cbClass);
		
		//Optional, only for snippets - obtain description
		String description = "";
		if (cu.getLineNumber(node.getStartPosition())-2 >= 0) {
			description = lines.get(cu.getLineNumber(node.getStartPosition())-2);
			description = description.replace("\t", "").replace("\n", "");
			
			if (description.startsWith("/*") && description.endsWith("*/")) {
				description = description.replace("/*", "").replace("*/", "");
				method.setDescription(description);
			}
		}	
				
		methods.add(method);
		return super.visit(node);
	}

	public List<CBMethod> methods() {
		return methods;
	}

	public static List<CBMethod> parse(String fileName) throws IOException {
		return parse(source(fileName), fileName);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static List<CBMethod> parse(String source, String fileName) {
		
//		if (lines == null) {
			fillLines(source);
//		}
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		Map pOptions = JavaCore.getOptions();
		pOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_8);
		pOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_8);
		pOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);
		pOptions.put(JavaCore.COMPILER_DOC_COMMENT_SUPPORT, JavaCore.ENABLED);
		parser.setCompilerOptions(pOptions);
		parser.setSource(source.toCharArray());
		cu = (CompilationUnit) parser.createAST(null);
		
		cbClass = new CBClass(getImports(source));
		
		CBMethodParser methodParser = new CBMethodParser(fileName);
		cu.accept(methodParser);
		return methodParser.methods();
	}

	private static void fillLines(String source) {
		lines = Arrays.asList(source.split("\n"));		
	}

	private static String source(String fileName) throws IOException {
		try {
			InputStream in = CBMethodParser.class.getResourceAsStream(fileName); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder code = new StringBuilder();
			String line = "";
			while ((line = reader.readLine()) != null) {
				code.append(line);
				code.append("\n");
			}
			
			return code.toString();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
	
	private static List<String> getImports(String source){
		String[] lines = source.split("\n");
		List<String> lstImportLines = new ArrayList<String>();
		for (int i = 0; i < lines.length; i++) {
			if (lines[i].contains("class")){
				break;
			}
			else{
				if (lines[i].contains("import ")){
					lstImportLines.add(lines[i]);					
				}
			}
		}
		return lstImportLines;
	}
}
