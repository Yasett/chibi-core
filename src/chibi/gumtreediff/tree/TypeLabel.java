package chibi.gumtreediff.tree;

import java.util.HashMap;

public class TypeLabel {
	private static HashMap<Integer,String> names=new HashMap<Integer,String>();
	public static String name(int type){
		return names.get(type);
	}
	public static void put(int type,String name){
		names.put(type, name);
	}
}
