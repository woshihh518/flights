package services.db;

import java.io.*;
import java.util.*;
import services.log.*;

/**
 * <p>
 * 描述:获取JNDI文件的信息.
 * </p>
 */
public class Config{
	static Hashtable ht = new Hashtable();
	static{
		//DebugWriter writerLog=new DebugWriter();
		try{
			ResourceBundle resources = ResourceBundle.getBundle("jndi", Locale.getDefault());
			Enumeration enum1 = resources.getKeys();
			while (enum1.hasMoreElements()) {
				String key = (String) enum1.nextElement();
				String value = resources.getString(key);
				ht.put(key, value);
			}
		}catch (MissingResourceException e) {
			e.printStackTrace();//writerLog.WriteDebug("MissingResourceExceptino="+e);
		}catch (NullPointerException e) {
			e.printStackTrace();//writerLog.WriteDebug("exception="+e);
		}
	}

	public static String getValue(String key) {
		return (String)ht.get(key);
	}

	public static Object getObject(String key) {
		return ht.get(key);
	}

	public static Hashtable getConfig() {
		return ht;
	}
}
