package chibi.completion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import chibi.generator.CBComparer;
import chibi.gumtreediff.tree.ITree;
import chibi.gumtreediff.tree.TreeUtils;
import chibi.model.CBClasspathPackageProvider;
import chibi.model.CBJavaLibraryPackageProvider;
import chibi.model.CBRuntimePackageProvider;
import chibi.model.ICBPackageProvider;

public class CBImportsResolver {
	private List<ICBPackageProvider> providers;
	private Set<String> packages;
	
	public CBImportsResolver(){
		providers = new ArrayList<ICBPackageProvider>();
		providers.add(new CBRuntimePackageProvider());
		providers.add(new CBClasspathPackageProvider());
		providers.add(new CBJavaLibraryPackageProvider());
	}
	
	public List<String> getMethodTypes(String methodSource){
		List<String> lstTypes = new ArrayList<String>();
		try {
			ITree methodTree = CBComparer.tree(methodSource);
			List<ITree> lstMethodTree =  TreeUtils.breadthFirst(methodTree);
			
			for (ITree trMethod : lstMethodTree) {
				if (trMethod.getType() == 43 && !lstTypes.contains(trMethod.getLabel())) {
					lstTypes.add(trMethod.getLabel());
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lstTypes;
	}
	
	public List<String> getImports(List<String> lstImportLines, String snippetSource, List<String> lstSnippetImports){
		List<String> lstImports = new ArrayList<String>();
		
		List<String> lstTypes = getMethodTypes(snippetSource);
		
		for (String type : lstTypes) {
			if (lstImportLines.stream().filter(n -> n.contains(type)).count() == 0) {
				List<String> lstFQNs = getFQNs(type);
				
				for (int i = 0; i < lstSnippetImports.size(); i++) {
					lstSnippetImports.set(i, lstSnippetImports.get(i).replace("import ", "").replace(";", "").replace(".*", "").trim());
				}
				
				for (String fqn : lstFQNs) {
					List<String> snippetImportsMatch = lstSnippetImports.stream().filter(n->fqn.startsWith(n)).collect(Collectors.toList());
					if (snippetImportsMatch.size() > 0) {
						lstImports.add(fqn);
						break;
					}
				}				
			}
		}
		
		return lstImports;
	}
	
	public List<String> getFQNs(String simpleName) {
		if (this.packages == null) {
			this.packages = getPackages();
		}

		List<String> fqns = new ArrayList<String>();
		for (String aPackage : packages) {
			try {
				String fqn = aPackage + "." + simpleName;
				Class.forName(fqn);
				fqns.add(fqn);
			} catch (Exception e) {
				// Ignore
			}
		}

		return fqns;
	}
	
	private Set<String> getPackages() {
		Set<String> packages = new HashSet<String>();

		if (providers != null) {
			for (ICBPackageProvider provider : providers) {
				packages.addAll(provider.getPackages());
			}
			
		}

		return packages;
	}
}
