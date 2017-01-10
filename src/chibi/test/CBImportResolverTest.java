package chibi.test;

import org.junit.Before;
import org.junit.Test;

import chibi.model.CBClasspathPackageProvider;
import chibi.model.CBJavaLibraryPackageProvider;
import chibi.model.CBRuntimePackageProvider;
import chibi.model.ICBPackageProvider;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CBImportResolverTest {
	private List<ICBPackageProvider> providers;
	private Set<String> packages;

	@Before
	public void setUp() {
		providers = new ArrayList<ICBPackageProvider>();
		providers.add(new CBRuntimePackageProvider());
		providers.add(new CBClasspathPackageProvider());
		providers.add(new CBJavaLibraryPackageProvider());
        
        
        
        // Only add this line if the code is executed inside an Eclipse plug-in
        //providers.add(new CBEclipsePackageProvider());
	}

	@Test
	public void getClassNameTest() {
	     
	     assert(getFQNs("List").size()> 0);
	     assert(getFQNs("File").size()> 0);
	     assert(getFQNs("IResource").size()> 0);
	     System.out.println("test");
	}

	private Set<String> getPackages() {
		Set<String> packages = new HashSet<String>();

		for (ICBPackageProvider provider : providers) {
			packages.addAll(provider.getPackages());
		}
		return packages;
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
}
