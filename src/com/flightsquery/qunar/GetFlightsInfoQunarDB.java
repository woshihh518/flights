package com.flightsquery.qunar;

import java.net.URLEncoder;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.json.JSONObject;

import services.db.DBFuncs;


public class GetFlightsInfoQunarDB {
	private DBFuncs d1 = new DBFuncs();
	public void insertCorrInfo(Vector vDynamicFlightInfo) {
		  
		  try {
			  if(vDynamicFlightInfo!=null)
			  {
				  for(int i=0;i<vDynamicFlightInfo.size();i++)
				  {
					  Hashtable hDynamicFlightInfo=(Hashtable)vDynamicFlightInfo.elementAt(i);
					  String insSql = d1.getInsertSql("flight_corrinfo", null, hDynamicFlightInfo);
					  System.out.println(insSql);
					  d1.execSql(insSql);
					  
				  }
			  }
			  //return "success";
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		   //return e.getMessage();
		  } 
		  	finally {
		  }
	}
	public void insertExtInfo(Vector vDynamicFlightInfo) {
		  
		  try {
			  if(vDynamicFlightInfo!=null)
			  {
				  for(int i=0;i<vDynamicFlightInfo.size();i++)
				  {
					  Hashtable hDynamicFlightInfo=(Hashtable)vDynamicFlightInfo.elementAt(i);
					  String insSql = d1.getInsertSql("flight_extinfo", null, hDynamicFlightInfo);
					  System.out.println(insSql);
					  d1.execSql(insSql);
					  
				  }
			  }
			  //return "success";
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		   //return e.getMessage();
		  } 
		  	finally {
		  }
	}

}
