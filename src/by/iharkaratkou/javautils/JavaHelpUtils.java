package by.iharkaratkou.javautils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class JavaHelpUtils {
	/**
	 * This method makes a "deep clone" of any Java object it is given.
	 */
	public Object deepClone(Object object) {
	   try {
	     ByteArrayOutputStream baos = new ByteArrayOutputStream();
	     ObjectOutputStream oos = new ObjectOutputStream(baos);
	     oos.writeObject(object);
	     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
	     ObjectInputStream ois = new ObjectInputStream(bais);
	     return ois.readObject();
	   }
	   catch (Exception e) {
	     e.printStackTrace();
	     return null;
	   }
	 }
	
	public static List<TreeMap<Integer, List<String>>> cloneListHm(List<TreeMap<Integer, List<String>>> list) {
		List<TreeMap<Integer, List<String>>> clone = new ArrayList<TreeMap<Integer, List<String>>>(list.size());
	    for(TreeMap<Integer, List<String>> item: list) clone.add((TreeMap<Integer, List<String>>) item.clone());
		return clone;
	}
}
