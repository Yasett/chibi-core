import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;

public class TestCode {

//	public void FileWrite()	{
//		try {
//			String reportUrl = "http://www.mercadopublico.cl/Procurement/Modules/RFB/DetailsAcquisition.aspx";
//
//			// write object to file
//			FileOutputStream fos = new FileOutputStream("Report.ser");
//			ObjectOutputStream oos = new ObjectOutputStream(fos);
//			oos.writeObject(reportUrl);
//			oos.close();
//
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//	
//	public void FileTest(){
//		FileWrite();
//	}
	
	public void testFrame(){
		JFrame frame = new JFrame();		
	}

	// public void loopHashTable() {
	// Hashtable<String, Integer> days = new Hashtable<String, Integer>(20);
	//
	// Enumeration<String> keys = days.keys();
	//
	// while (keys != null) {
	// String day = (String) keys.nextElement();
	// System.out.println(day);
	// }
	// }
	//
	// public void loopHashTableAlt() {
	// Hashtable<String, Integer> days = new Hashtable<String, Integer>(20);
	//
	// days.put("Mon", 1);
	// days.put("Tue", 2);
	// days.put("Wed", 3);
	//
	// for (String key : days.keySet()) {
	// Integer value = days.get(key);
	// System.out.println(value + key);
	// }
	// }

}
