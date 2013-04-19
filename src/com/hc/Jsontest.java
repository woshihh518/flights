package com.hc;

import java.util.Iterator;

import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Jsontest {
	final public Logger logger = LoggerFactory.getLogger(this.getClass()); 
	public void test(String sJson)
	{
		try {
			JSONObject rootObject = new JSONObject(sJson.trim());
			testChild("root",rootObject);
		}
		catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }		
	}
	public void testChild(String childName,JSONObject jObj)
	{
		try {
			
			for(Iterator it = jObj.keys(); it.hasNext();)   
			  {
				String fldNam   =   (String)it.next();
				String fldVal = jObj.getString(fldNam);
				try {
					JSONObject object = new JSONObject(fldVal);
					testChild(childName+"."+fldNam,object);
				} catch (JSONException e2) {// 抛错 说明JSON字符根本就不是JSON
					logger.info(childName+"."+fldNam+" : "+fldVal);
				}
			  }
			
			
		}
		catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }	
	}

}
