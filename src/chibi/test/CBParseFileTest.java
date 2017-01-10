package chibi.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

import org.junit.*;

import chibi.generator.CBMethodParser;
import chibi.model.CBMethod;

public class CBParseFileTest {


	@Test
	public void testMethodParse() throws IOException {
		List<CBMethod> methods = CBMethodParser.parse("/Test.sp");
		assertEquals(2, methods.size());
		assertEquals("x.bar();\n", methods.get(0).getSource());
		assertEquals(0, methods.get(0).getArgumentSize());
		assertEquals("foo", methods.get(0).getMethodName());
		assertEquals("return 1+2;\n", methods.get(1).getSource());
	}

}
