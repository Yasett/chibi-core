package chibi.model;

import java.util.Collection;

/**
 * Computes the list of packages provided by the bootstrap libraries (e.g., rt.jar)
 */
public class CBJavaLibraryPackageProvider implements ICBPackageProvider {

    @Override
    public Collection<String> getPackages() {
        String classpath = System.getProperty("sun.boot.class.path");
        return CBClasspathPackageProvider.getPackageFromClassPath(classpath);
    }

}
