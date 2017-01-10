package chibi.model;

import java.io.File;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Computes the list of packages provided by all paths (directories and jar files) on the classpath.
 */
public class CBClasspathPackageProvider implements ICBPackageProvider {

    @Override
    public Collection<String> getPackages() {
        String classpath = System.getProperty("java.class.path");
        return getPackageFromClassPath(classpath);
    }
   
    public static Set<String> getPackageFromClassPath(String classpath) {
        Set<String> packages = new HashSet<String>();
        String[] paths = classpath.split(File.pathSeparator);
        for (String path : paths) {
            if (path.trim().length() == 0) {
                continue;
            } else {
                File file = new File(path);
                if (file.exists()) {
                    String childPath = file.getAbsolutePath();
                    if (childPath.endsWith(".jar")) {
                        packages.addAll(CBClasspathPackageProvider
                                .readZipFile(childPath));
                    } else {
                        packages.addAll(CBClasspathPackageProvider
                                .readDirectory(childPath));
                    }
                }
            }

        }
        return packages;
    }

    public static Set<String> readDirectory(String path) {
        Set<String> packages = new HashSet<String>();
        File file = new File(path);
        int startIndex = path.length() + 1;
        for (File child : file.listFiles()) {
            recursiveRead(child, startIndex, packages);
        }
        return packages;
    }

    public static void recursiveRead(File file, int startIndex, Set<String> packages) {
        if (!file.isFile()) {
            packages.add(file.getAbsolutePath().substring(startIndex)
                    .replace(File.separator, "."));
            for (File child : file.listFiles()) {
                recursiveRead(child, startIndex, packages);
            }
        }
    }

    public static Set<String> readZipFile(String path) {
        Set<String> packages = new HashSet<String>();
        try {
            @SuppressWarnings("resource")
			ZipFile zFile = new ZipFile(path);
            Enumeration<? extends ZipEntry> entries = zFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (!entry.isDirectory()) {
                    String dirName = new File(entry.getName()).getParent();
                    if (dirName != null) {
                        String name = dirName.replace(File.separator, ".");
                        if (name.endsWith(".")) {
                            name = name.substring(0, name.length() - 1);
                        }
                        packages.add(name);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return packages;
    }

}