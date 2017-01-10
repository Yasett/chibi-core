package chibi.model;

import java.util.Collection;
//import java.util.HashSet;
//import java.util.Set;
//
//import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
//import org.osgi.service.packageadmin.ExportedPackage;
import org.osgi.service.packageadmin.PackageAdmin;
//import org.osgi.util.tracker.ServiceTracker;

/**
 * Computes the list of packages exported by all bundles in an Eclipse
 * installation. Don't forget to add "Eclipse-BuddyPolicy: global" to your
 * MANIFEST.MF file if you want to access exported packages from all bundles
 * (and not just the one this plug-in depends on).
 */
@SuppressWarnings("deprecation")
public class CBEclipsePackageProvider implements ICBPackageProvider {

    @Override
    public Collection<String> getPackages() {
//        Set<String> packages = new HashSet<String>();
//        BundleContext context = Activator.getDefault().getBundle()
//                .getBundleContext();
//        Bundle[] bundles = context.getBundles();
//        PackageAdmin pAdmin = getPackageAdmin(context);
//
//        for (Bundle bundle : bundles) {
//            ExportedPackage[] ePackages = pAdmin.getExportedPackages(bundle);
//            if (ePackages != null) {
//                for (ExportedPackage ePackage : ePackages) {
//                    packages.add(ePackage.getName());
//                }
//            }
//        }

        //return packages;
    	return null;
    }

    public PackageAdmin getPackageAdmin(BundleContext context) {
//        ServiceTracker bundleTracker = null;
//        bundleTracker = new ServiceTracker(context,
//                PackageAdmin.class.getName(), null);
//        bundleTracker.open();
//        return (PackageAdmin) bundleTracker.getService();
    	return null;
    }

}
