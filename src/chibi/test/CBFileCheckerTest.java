package chibi.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.Test;

import chibi.generator.CBComparer;
import chibi.generator.CBMethodParser;
import chibi.gumtreediff.tree.ITree;
import chibi.model.CBMethod;
import chibi.model.ICBWarning;

public class CBFileCheckerTest {

	@Test
	public void MultipleMethodsTest() throws UnsupportedOperationException, IOException {
		
		String testCodeClass = "/TestCode.sp";
		String snippetCodeClass = "/Snippet.sp";
		
		List<CBMethod> snippets = CBMethodParser.parse(snippetCodeClass);
		List<CBMethod> methods = CBMethodParser.parse(testCodeClass);

		for (CBMethod cbMethod : methods) {
			for (CBMethod cbSnippet : snippets) {
				ITree snippetTree = CBComparer.tree(cbSnippet.getSource());
				ITree methodCodeTree = CBComparer.tree(cbMethod.getSource());
				CBComparer comparator = new CBComparer();
				double difference = comparator.check(methodCodeTree, snippetTree);
				
				System.out.println("Difference found is: " + String.format("%.2f", difference * 100) + "%");

				if (difference < CBComparer.threshold) {
					List<ICBWarning> warnings = comparator.checkWarnings(snippetTree, methodCodeTree, cbSnippet.getStartLineNumber(), testCodeClass, cbMethod.getMethodName());
					
					assertTrue(warnings.stream().filter(w -> w != null).count() > 0);
					
					for (ICBWarning cbWarning : warnings) {
						System.out.println(cbWarning);
					}
				}
			}
		}
	}
	
}
