package chibi.model;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * Computes the list of packages accessible by the current class loader. May be useful to detect dynamically generated classes/packages.
 */
public class CBRuntimePackageProvider implements ICBPackageProvider {

    @Override
    public Collection<String> getPackages() {
        Set<String> packages = new HashSet<String>();
        for (Package aPackage : Package.getPackages()) {
            packages.add(aPackage.getName());
        }
        return packages;
    }

}
