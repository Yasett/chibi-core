package chibi.model;
import java.util.Collection;

/**
 * A package provider computes a list of packages accessible at runtime.
 */

public interface ICBPackageProvider {
	Collection<String> getPackages();
}
