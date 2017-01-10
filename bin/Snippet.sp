
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.*;
import javax.swing.*;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileVisitOption;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Arrays;
import java.util.Calendar;
import java.util.EnumSet;
import java.util.List;

import javax.imageio.ImageIO;

public class Snippet {

//	public static void FileWrite() {
//
//		try {
//
//			java.io.FileOutputStream fileOutputStream = new java.io.FileOutputStream("File.ser");
//			java.io.ObjectOutputStream objectOutputStream = new java.io.ObjectOutputStream(fileOutputStream);
//
//			try {
//				objectOutputStream.writeObject("data");
//			} finally {
//				objectOutputStream.close();
//			}
//
//		} catch (FileNotFoundException e) {
//
//		} catch (IOException e) {
//
//		}
//	}
	/*Inicializar un objeto de la clase JFrame*/
	public void testFrame(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice gs = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gs.getDefaultConfiguration();
		JFrame frame = new JFrame("Title", gc);
		
	}
	
	/*Crear un archivo*/
	public void createFile() {

		File fileFromPath = Paths.get("test.txt").toFile();
	}

	/*Obtener la ruta relativa de un archivo*/
	public void resolveRelativePath() {
		Path directory = Paths.get("C:/");
		Path pathInDirectory = directory.resolve("test.txt");
	}

	/*Eliminar un archivo*/
	public void deleteIfExists() {
		Path path = Paths.get("test");
		try {
			Files.deleteIfExists(path);
		} catch (IOException e) {
		}
	}

	/*Escribir en un archivo*/
	public void writeFile() {
		List<String> lines = Arrays.asList(String.valueOf(Calendar.getInstance().getTimeInMillis()), "line test 1",
				"line test 2");

		try {
			Path path = Paths.get("test.txt");
			OutputStream outputStream = Files.newOutputStream(path, StandardOpenOption.CREATE_NEW);
			for (String line : lines) {
				outputStream.write((line + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
			}

		} catch (Exception e) {

		}
	}

	/*Recorrer los archivos de un directorio*/
	public void iterateFolderFiles() {

		try {
			Path directory = Paths.get("C:/");
			Files.walkFileTree(directory, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult preVisitDirectory(Path selectedPath, BasicFileAttributes attrs)
						throws IOException {
					System.out.println("directory " + selectedPath.toAbsolutePath());
					return FileVisitResult.CONTINUE;
				}

				public FileVisitResult visitFile(Path selectedPath, BasicFileAttributes attrs) throws IOException {
					System.out.println("file " + selectedPath.toAbsolutePath());
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
		}
	}


	/*Copiar contenido de un archivo*/
	public void CopyFile() {
		File sourceFile = new File("test.txt");
		File destFile = new File("test.txt");
		if (!sourceFile.exists() || !destFile.exists()) {
			// Source or destination file doesn't exist
			return;
		}
		
		try {
			FileInputStream fis = new FileInputStream(sourceFile);
			FileOutputStream fos = new FileOutputStream(destFile);
			FileChannel srcChannel = fis.getChannel();
			FileChannel sinkChanel = fos.getChannel();

			try {
				srcChannel.transferTo(0, srcChannel.size(), sinkChanel);
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (srcChannel != null) {
					srcChannel.close();
				}
				if (sinkChanel != null) {
					sinkChanel.close();
				}

				if (fis != null) {
					fis.close();
				}

				if (fos != null) {
					fos.close();
				}
			}

		} catch (Exception e) {

		}
	}
	

	// public static void main(String[] args) {
	// FileWrite();
	// }
	//
	// public void loopHashTable(){
	// Hashtable<String,Integer> h = new Hashtable<String,Integer>(20);
	// Enumeration<String> en = h.keys();
	// while (en.hasMoreElements()) {
	// Object object = (Object) en.nextElement();
	// System.out.println(object);
	// }
	// }
	//
	// public void loopHashTableAlt(){
	// Hashtable<String,Integer> h = new Hashtable<String,Integer>(20);
	// Set<Entry<String, Integer>> en = h.entrySet();
	//
	// for (Entry<String, Integer> entry : en) {
	// Integer value = entry.getValue();
	// String key = entry.getKey();
	// System.out.println(value + key);
	// }
	// }

}
