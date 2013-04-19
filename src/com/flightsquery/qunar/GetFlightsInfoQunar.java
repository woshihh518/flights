package com.flightsquery.qunar;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;

import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.json.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.db.DBFuncs;
import services.db.ReadCFG;
import services.javatohttp.*;
import services.pub.*;

import com.hc.*;

public class GetFlightsInfoQunar extends Thread{
	final public Logger logger = LoggerFactory.getLogger(this.getClass()); 
	final public Logger errlogger = LoggerFactory.getLogger("errlogger"); 
	private static ReadCFG cfg = new ReadCFG();
	private static String charset = cfg.getvalue("charset");
	private DBFuncs d1 = new DBFuncs();
	private PubFunHC pf = new PubFunHC();
	HttpCli hc = new HttpCli();
	private GetFlightsInfoQunarDB gdb = new GetFlightsInfoQunarDB();
	
	/*
	 * 取2个城市之间某一天的信息
	 */
	public void getSingleFlightInfo(String depCity,String ArrCity,String queryDate)
	{
		//准备flight_totalinfo表参数，并向表插入一条记录
		Hashtable<String,String> ht = new Hashtable<String,String>();
		String opdate = pf.getdate("yyyyMMdd", 0);
		String term = " opdate='"+opdate+"'";
		String logno = opdate+d1.getSeqNo("flight_totalinfo", "LOGNO", 6, term);
		String optime = pf.getdate("HHmmss", 0);
		int token = (int)Math.floor(Math.random() * 100000);
		ht.put("opdate", opdate);
		ht.put("logno", logno);
		ht.put("optime", optime);
		ht.put("da", depCity);
		ht.put("aa", ArrCity);
		ht.put("fdate", queryDate);
		ht.put("token", ""+token);
		String insSql = d1.getInsertSql("flight_totalinfo", null, ht);
		System.out.println(insSql);
		d1.execSql(insSql);
		logger.info("logno:{} ,info: {}", logno, ht );   
		 
		System.out.println("token="+token);
		//getDynamicFlightInfo(depCity,ArrCity,queryDate,token,logno); 
		getLongwell(depCity,ArrCity,queryDate,token,logno); 
	}
	
	/*
	 * 取航班的动态信息，包括准点率和航班基本信息
	 * */
	public void getDynamicFlightInfo(String depCity,String ArrCity,String queryDate,int token,String logno) {
		  
		  try {
			  
			  //组装 DynamicFlightInfo URL
			  String depCityEncode=URLEncoder.encode(depCity,charset);
			  String ArrCityEncode=URLEncoder.encode(ArrCity,charset);
				
			  String urlDynamicFlightInfo="http://flight.qunar.com/twell/flight/DynamicFlightInfo.jsp?";
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&departureCity="+depCityEncode;
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&arrivalCity="+ArrCityEncode;
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&departureDate="+queryDate;
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&fromCity="+depCityEncode;
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&toCity="+ArrCityEncode;
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&from="+"fi_ont_search";
			  urlDynamicFlightInfo=urlDynamicFlightInfo+"&_token="+token;
			  logger.info("logno:{} ,url: {}", logno, urlDynamicFlightInfo );
			  String respDynamicFlightInfo=hc.get1(urlDynamicFlightInfo);
			  logger.info("logno:{} ,resp: {}", logno, pf.delReturn(respDynamicFlightInfo) );
			  respDynamicFlightInfo=preDealResp(respDynamicFlightInfo.trim());
			  //System.out.println(respDynamicFlightInfo);
			  JSONObject rootObject = new JSONObject(respDynamicFlightInfo.trim());
			  //System.out.println(rootObject.get("corrInfo"));
			  JSONObject corrInfo = (JSONObject)rootObject.get("corrInfo");
			  JSONObject extInfo = (JSONObject)rootObject.get("extInfo");
			  //System.out.println("corrInfo:"+corrInfo);
			  //corrInfo.
			  getCorrInfo(depCity,ArrCity,queryDate,corrInfo,logno);
			  getExtInfo(depCity,ArrCity,queryDate,extInfo,logno);
			  
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
	}
	
	/*
	 * 处理准点率信息
	 */
	public void getCorrInfo(String depCity,String ArrCity,String queryDate,JSONObject corrInfo,String logno) {
		  
		  try {
			  Vector vDynamicFlightInfo = new Vector();
			  for(Iterator it = corrInfo.keys(); it.hasNext();)   
			  {   
				  Hashtable<String,String> tmph = new Hashtable<String,String>();
				  tmph.put("logno", logno);
				  tmph.put("da", depCity);
				  tmph.put("aa", ArrCity);
				  tmph.put("fdate", queryDate);
		          String fldNam   =   (String)it.next();   
		          tmph.put("fid", fldNam);
		          //String fldVal   =   corrInfo.getString(fldNam); 
		          JSONObject sCorrInfo = (JSONObject)corrInfo.get(fldNam);
		          for(Iterator sit = sCorrInfo.keys(); sit.hasNext();)   
				  {
		        	  String sfldNam   =   (String)sit.next();  
		        	  String sfldVal   =   sCorrInfo.getString(sfldNam); 
		        	  tmph.put(sfldNam, sfldVal);
		        	  System.out.println(sfldNam+":"+sfldVal);
		        	  
				  }
		          vDynamicFlightInfo.add(tmph);
			  }
			  gdb.insertCorrInfo(vDynamicFlightInfo);
			  
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
	}
	
	/*
	 * 处理航班基本信息
	 * */
	public void getExtInfo(String depCity,String ArrCity,String queryDate,JSONObject extInfo,String logno) {
		  
		  try {
			  Vector vDynamicFlightInfo = new Vector();
			  for(Iterator it = extInfo.keys(); it.hasNext();)   
			  {   
				  Hashtable<String,String> tmph = new Hashtable<String,String>();
				  tmph.put("logno", logno);
				  tmph.put("da", depCity);
				  tmph.put("aa", ArrCity);
				  tmph.put("fdate", queryDate);
		          String fldNam   =   (String)it.next();   
		          tmph.put("fid", fldNam);
		          //String fldVal   =   corrInfo.getString(fldNam); 
		          JSONObject sCorrInfo = (JSONObject)extInfo.get(fldNam);
		          for(Iterator sit = sCorrInfo.keys(); sit.hasNext();)   
				  {
		        	  String sfldNam   =   (String)sit.next();  
		        	  if(sfldNam.equals("sc")||sfldNam.equals("zj"))
		        	  {
		        		  JSONObject ssCorrInfo = (JSONObject)sCorrInfo.get(sfldNam);
		        		  for(Iterator ssit = ssCorrInfo.keys(); ssit.hasNext();)   
						  {
		        			  String ssfldNam   =   (String)ssit.next();  
		        			  String ssfldVal   =   ssCorrInfo.getString(ssfldNam);
		        			  tmph.put(sfldNam+"_"+ssfldNam, ssfldVal);
						  }
		        	  }
		        	  else
		        	  {
		        		  String sfldVal   =   sCorrInfo.getString(sfldNam); 
		        		  tmph.put(sfldNam, sfldVal);
		        	  }  
		        	  //System.out.println(sfldNam+":"+sfldVal);
		        	  
				  }
		          vDynamicFlightInfo.add(tmph);
			  }
			  gdb.insertExtInfo(vDynamicFlightInfo);
			  
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
	}
	
	/*
	 * 取longwell数据，即所有航班信息及价格数据
	 */
	public void getLongwell(String depCity,String arrCity,String queryDate,int token,String logno) {
		  
		  try {
			  
			  //组装 Longwell url
			  token = (int)Math.floor(Math.random() * 100000);
			  String args="";
			  args=args+"&"+URLEncoder.encode("http://www.travelco.com/searchArrivalAirport",charset)+"="+URLEncoder.encode(arrCity,charset);
			  args=args+"&"+URLEncoder.encode("http://www.travelco.com/searchDepartureAirport",charset)+"="+URLEncoder.encode(depCity,charset);
			  args=args+"&"+URLEncoder.encode("http://www.travelco.com/searchDepartureTime",charset)+"="+queryDate;
			  args=args+"&"+URLEncoder.encode("http://www.travelco.com/searchReturnTime",charset)+"="+queryDate;
			  args=args+"&locale=zh&nextNDays=0&searchLangs=zh&searchType=OneWayFlight&tags=1&from=fi_ont_search";
			  args=args+"&_token="+token;
			  //args=URLEncoder.encode(args,charset);
			  System.out.println(args);
			  String urlLongwell="http://flight.qunar.com/twell/longwell?"+args;
			  logger.info("logno:{} ,urlLongwell: {}", logno, urlLongwell );
			  String respLongwell=hc.get1(urlLongwell);
			  //logger.info("logno:{} ,respLongwell: {}", logno, pf.delReturn(respLongwell) );
			  respLongwell=preDealResp(respLongwell.trim());
			  System.out.println(respLongwell);
			  JSONObject rootObject = new JSONObject(respLongwell.trim());
			  //Jsontest jt =new Jsontest();
			  //jt.test(respLongwell.trim());

			  
			  JSONObject oneway_data = (JSONObject)rootObject.get("oneway_data");
			  JSONObject flightInfo = (JSONObject)oneway_data.get("flightInfo");
			  JSONObject priceInfo = (JSONObject)oneway_data.get("priceInfo");
			  if(priceInfo==null)
			  {
				  System.out.println("priceInfo is null");
			  }
			  logger.info("logno:{} ,priceInfo: {}", logno, priceInfo );
			  //组装groupdata url
			  int token1 = (int)Math.floor(Math.random() * 100000);
			  int token2 = (int)Math.floor(Math.random() * 100000);
			  int token3 = (int)Math.floor(Math.random() * 100000);  
			  String queryID = rootObject.getString("queryID");
			  logger.info("logno:{} ,queryID: {}", logno, queryID );
			  queryID=getQueryId(queryID);
			  String serverIP = rootObject.getString("serverIP");
			  String status = oneway_data.getString("status");
			  System.out.println("queryID:"+queryID);
			  String groupdataArgs = "";
			  groupdataArgs=groupdataArgs+"&departureCity="+URLEncoder.encode(depCity,charset);
			  groupdataArgs=groupdataArgs+"&arrivalCity="+URLEncoder.encode(arrCity,charset);
			  groupdataArgs=groupdataArgs+"&departureDate="+queryDate;
			  groupdataArgs=groupdataArgs+"&returnDate="+queryDate;
			  groupdataArgs=groupdataArgs+"&nextNDays=0&searchType=OneWayFlight&searchLangs=zh&locale=zh&from=fi_ont_search";
			  //groupdataArgs=groupdataArgs+"&queryID="+URLEncoder.encode(queryID,charset);
			  groupdataArgs=groupdataArgs+"&queryID="+URLEncoder.encode(queryID,charset);
			  groupdataArgs=groupdataArgs+"&serverIP="+URLEncoder.encode(serverIP,charset);
			  groupdataArgs=groupdataArgs+"&status="+URLEncoder.encode(status,charset);
			  //groupdataArgs=groupdataArgs+"&_token="+token;
			  System.out.println(groupdataArgs);
			  String urlOneWayFlight_Info="http://flight.qunar.com/twell/flight/OneWayFlight_Info.jsp?"+groupdataArgs+"&_token="+token1;
			  String urlGroupdata="http://flight.qunar.com/twell/flight/tags/onewayflight_groupdata.jsp?"+groupdataArgs+"&_token="+token2;
			  String urlDeduceGroupdata="http://flight.qunar.com/twell/flight/tags/deduceonewayflight_groupdata.jsp?"+groupdataArgs+"&_token="+token3;
			  logger.info("logno:{} ,urlOneWayFlight_Info: {}", logno, urlOneWayFlight_Info );
			  sleep(5000);
			  String respOneWayFlight_Info=hc.get1(urlOneWayFlight_Info);
			  logger.info("logno:{} ,respOneWayFlight_Info: {}", logno, pf.delReturn(respOneWayFlight_Info) );
			  sleep(1000);
			  /*logger.info("logno:{} ,urlDeduceGroupdata: {}", logno, urlDeduceGroupdata );
			  String respDeduceGroupdata=hc.get1(urlDeduceGroupdata);
			  logger.info("logno:{} ,resp: {}", logno, pf.delReturn(respDeduceGroupdata) );
			  */
			  String dataCompleted = "false";
			  int querytimes=0;
			  while(!dataCompleted.equals("true"))
			  {
				  sleep(1000);
				  querytimes++;
				  if(querytimes>10)break;
				  logger.info("logno:{} ,urlGroupdata: {}", logno, urlGroupdata );
				  String respGroupdata=hc.get1(urlGroupdata);
				  logger.info("logno:{} ,resp: {}", logno, pf.delReturn(respGroupdata) );
				  JSONObject groupdataObject = new JSONObject(respGroupdata.trim());
				  JSONObject groupdataOneway_data = (JSONObject)groupdataObject.get("oneway_data");
				  dataCompleted = groupdataOneway_data.getString("dataCompleted").trim();
				  System.out.println("dataCompleted:"+dataCompleted);
				  break;
				  
			  }
			  
			  
			  
			  //corrInfo.
			  //getCorrInfo(depCity,ArrCity,queryDate,corrInfo,logno);
			  //getExtInfo(depCity,ArrCity,queryDate,extInfo,logno);
			  
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
	}
	
	/*
	 * 返回信息预处理，去掉头尾的()使返回是标准的json
	 */
	public String preDealResp(String resp)
	{
		//String resp1=resp.trim().substring(1,resp.length()-1);
		//System.out.println(resp1.substring(resp1.length()-1));
		return resp.substring(1,resp.length()-1);
	}
	/*
	 * 计算queryId
	 */
	public static  String getQueryId(String A)
	{
		try {
			String N = A;
			int K = N.indexOf(":");
	        String M = N.substring(0, K + 1);
	        String J = N.substring(K + 1);
	        String I="";
	        for (int b = 0, d = J.length(); b < d; b++) {
	        	int i=(int)(J.charAt(b)) - 1;
	        	//System.out.println((char)i);
	        	I=(char)i+I;
            }
	        return M+I;
	        


		   
		  } catch (Exception e) {
		   e.printStackTrace();
		   return null;
		  } 
		  	finally {
		  }
	}
	

}
