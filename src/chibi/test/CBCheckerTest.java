package chibi.test;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import chibi.generator.CBComparer;
import chibi.generator.CBSnippetChecker;
import chibi.gumtreediff.tree.ITree;
public class CBCheckerTest {
	// semantic equivalent expression are not consider as equals
	// the semantic behavior is dynamic, therefore, there is not possible way to
	// statically determine
	// if an expression is semantic equivalent to another.
	// may with symbolic execution, more complex, more tool analysis time.

	@Test
	public void testSnippetCheckerBasic() throws IOException {
		ITree code = CBComparer.tree("x.foo(1);");
		ITree snippet = CBComparer.tree("y.foo(2,3);x.bar();");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double)2/5));
	}

	@Test
	public void testSnippetCheckerBasicRenaming() throws IOException {
		ITree code = CBComparer.tree("x.foo();");
		ITree snippet = CBComparer.tree("a.bar();");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double)2/2));
		
		code = CBComparer.tree("x.foo();");
		snippet = CBComparer.tree("x.foo();");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 0);
		
		code = CBComparer.tree("x.foo();");
		snippet = CBComparer.tree("a.bar().test();");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 1);
	}
	
	@Test
	public void testSnippetCheckerBasicInverse() throws IOException {
		ITree code = CBComparer.tree("x.foo();");
		ITree snippet = CBComparer.tree("x.foo();x.bar();");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 1 / 3));
	}


	@Test
	public void testSnippetCheckerDecisionControlStructure() throws IOException {
		ITree code = CBComparer.tree("y.foo();");
		ITree snippet = CBComparer.tree("if(x>a) x.foo();");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 1 / 3));

		code = CBComparer.tree("x.foo();x.bar();");
		snippet = CBComparer.tree("if(x>a) x.foo(); else x.bar();");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 0);
	
		code = CBComparer.tree("x.foo();");
		snippet = CBComparer.tree("try{x.foo();}catch(Exception e){}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 4 / 7));	
		
		code = CBComparer.tree("x.foo();x.bar();");
		snippet = CBComparer.tree("try{x.foo();}catch(Exception e){x.bar();}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 3 / 9));		
	}
	
	@Test
	public void testSnippetCheckerFlowControlStructure() throws IOException {
		ITree code = CBComparer.tree("x.foo();");
		ITree snippet = CBComparer.tree("for (int i = 0; i < array.length; i++) {x.foo();}");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == (double)6 / 9);
		
		code = CBComparer.tree("for (int x : array) {x.foo();}");
		snippet = CBComparer.tree("for (int i = 0; i < array.length; i++) {x.foo();}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == (double)6 / 9);
		
		code = CBComparer.tree("x.foo();x.bar();");
		snippet = CBComparer.tree("while(x>a){x.foo();x.bar();x++;}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 2 / 8));
		
		code = CBComparer.tree("do { x.foo();x++;	} while (x>a);");
		snippet = CBComparer.tree("while(x>a){x.foo();x++;}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 1 / 6));
		
	}
	
	@Test
	public void testSnippetEnumeration() throws IOException {	
		ITree code = CBComparer.tree("while (en.hasMoreElements()) { type type = (type) en.nextElement();");
		ITree snippet = CBComparer.tree("while (en.hasMoreElements()) { type type = (type) en.nextElement();	}");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 0);
		
		code = CBComparer.tree("x.foo().test();");
		snippet = CBComparer.tree("while (en.hasMoreElements()) { type type = (type) en.nextElement();	}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 10 / 10));
		
		code = CBComparer.tree("while (en.hasMoreElements()) { type type = (type) en.nextElement();	 x.foo().test();}");
		snippet = CBComparer.tree("while (names.hasMoreElements()) {	String name = (String) names.nextElement();}");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 0); 
	}
	
	@Test
	public void testSnippetCopyArray() throws IOException {	
		ITree code = CBComparer.tree("array_type[] array1 = new array_type[array1.length + array.length]; System.arraycopy(array1, 0, array1, 0, array1.length);	System.arraycopy(array, 0, array1, array1.length, array.length); x.foo().test();");
		ITree snippet = CBComparer.tree("int[] person = new int[mujer.length + varon.length]; System.arraycopy(mujer, 0, person, 0, person.length);	System.arraycopy(varon, 0, person, mujer.length, person.length);");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 0 / 21));		
	}
	
	@Test
	public void testSearchForMov() throws IOException {	
		ITree code = CBComparer.tree("x.foo(); ");
		ITree snippet = CBComparer.tree("y.bar();");
		CBSnippetChecker checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == ((double) 2 / 2));
		
		code = CBComparer.tree("x.foo(); y.bar(); z.test();");
		snippet = CBComparer.tree("a.bar(2);x.foo();");
		checker = new CBSnippetChecker();
		assertTrue(checker.check(code, snippet) == 0);
	}
}
