package chibi.generator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import chibi.gumtreediff.gen.jdt.JdtTreeGenerator;
import chibi.gumtreediff.tree.ITree;
import chibi.model.ICBWarning;

public class CBComparer {
	
	public static double threshold = 0.75;

	public static ITree tree(String source) throws IOException {
		source = "class Dummy{ public void dummy(){\n" + source + "\n}}";
		return (new JdtTreeGenerator()).generateFromString(source).getRoot();
	}

	public double check(ITree methodBody, ITree snippet) {
		return new CBSnippetChecker().check(methodBody, snippet);
	}

	public List<ICBWarning> checkWarnings(ITree snippet, ITree methodBody, int snippetStartLineNumber, String className,
			String methodName) {
		List<ICBWarning> messages = new ArrayList<ICBWarning>();

		ICBHeuristicChecker checker = new CBMissingMessageChecker();
		ICBWarning cbWarning = checker.check(methodBody, snippet, snippetStartLineNumber, className, methodName);

		if (cbWarning.hasMessage()) {
			messages.add(cbWarning);
		}

		checker = new CBMissingInitializationParamChecker();
		cbWarning = checker.check(methodBody, snippet, snippetStartLineNumber, className, methodName);
		if (cbWarning.hasMessage()) {
			messages.add(cbWarning);
		}

		checker = new CBMissingControlStatementChecker();
		cbWarning = checker.check(methodBody, snippet, snippetStartLineNumber, className, methodName);

		if (cbWarning.hasMessage()) {
			messages.add(cbWarning);
		}

		return messages;
	}
	
}
