package com.hc;

import java.io.IOException;
import services.db.*;
import services.pub.PubFunHC;

import java.util.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import java.net.URLEncoder;
import com.flightsquery.qunar.*;

public class Httptest {
	private static PubFunHC pf = new PubFunHC();
	private static ReadCFG cfg = new ReadCFG();
	private static String charset = cfg.getvalue("charset");
	
	public  static void main(String[] args) {
		//post1();
		//get1();
		//dbtest();
		//String optime = pf.getdate("yyyyMMdd HHmmss", 0);
		//System.out.println("xxxxxxxxxxxxxxxxx"+optime);
		//GetFlightsInfoQunar gq =new GetFlightsInfoQunar();
		//gq.getSingleFlightInfo("武汉", "北京", "2013-05-12");
		//getSingleFlight();
		queryIdtest();
	}
	public static void  getSingleFlight() {
		  
		  try {
			  System.out.println(URLEncoder.encode("中文","GBK"));
		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
		 }
	public static  void queryIdtest()
	{
		try {
			String N = "192.168.25.46:61c4.;4:4gccg2f42;e952ee12";
			int K = N.indexOf(":");
	        String M = N.substring(0, K + 1);
	        String J = N.substring(K + 1);
	        String I="";
	        for (int b = 0, d = J.length(); b < d; b++) {
	        	int i=(int)(J.charAt(b)) - 1;
	        	//System.out.println((char)i);
	        	I=(char)i+I;
            }
	        System.out.println(URLEncoder.encode(M+I,charset));
	        


		   
		  } catch (Exception e) {
		   e.printStackTrace();
		  } 
		  	finally {
		  }
	}
	public static  void dbtest()
	{
		DBFuncs d1 = new DBFuncs();
		/*String sql = "select * from city";
		 Vector resultV=d1.queryTableAll(sql);
         if(resultV==null)System.out.println("xx");
         Hashtable returnH = (Hashtable) resultV.elementAt(0);
         System.out.println("xx:"+resultV.size());
         */
		String term = " opdate='20130301'";
		String logno=d1.getSeqNo("flight_totalinfo", "LOGNO", 6, term);
		System.out.println("xx:"+logno);
	}
	public static void get1() {
		  //构造HttpClient的实例
		  HttpClient httpClient = new HttpClient();
		  //创建GET方法的实例
		  //GetMethod getMethod = new GetMethod("http://www.ceair.com/mu/front/reservation/flight-search!doFlightSearch.shtml?cond.tripType=OW&cond.depCode=WUH&cond.depDate=2013-03-13&cond.arrCode=PEK&cond.arrDate=2013-03-13&cond.depCodeInt=&cond.arrCodeInt=&cond.depDateInt=2013-03-13&cond.depCodeIntRt=&cond.arrCodeIntRt=&cond.depDateIntRt=2013-03-13&cond.cabinRank=ECONOMY&cond.adultNumber=1&cond.childNumber=0&cond.sortType=1&cond.isInternationalFlight=331&cond.region=CN");
		  //GetMethod getMethod = new GetMethod("http://flight.qunar.com/twell/longwell?&http%3A%2F%2Fwww.travelco.com%2FsearchArrivalAirport=%E6%AD%A6%E6%B1%89&http%3A%2F%2Fwww.travelco.com%2FsearchDepartureAirport=%E5%8C%97%E4%BA%AC&http%3A%2F%2Fwww.travelco.com%2FsearchDepartureTime=2013-03-31&http%3A%2F%2Fwww.travelco.com%2FsearchReturnTime=2013-03-31&locale=zh&nextNDays=0&searchLangs=zh&searchType=OneWayFlight&tags=1&from=fi_ont_search&_token=42747");
		  GetMethod getMethod = new GetMethod("http://flight.qunar.com/twell/flight/DynamicFlightInfo.jsp?&departureCity=%E5%8C%97%E4%BA%AC&arrivalCity=%E6%AD%A6%E6%B1%89&departureDate=2013-03-31&fromCity=%E5%8C%97%E4%BA%AC&toCity=%E6%AD%A6%E6%B1%89&from=fi_ont_search&_token=57603");
		  
		  //使用系统提供的默认的恢复策略
		  getMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,
		    new DefaultHttpMethodRetryHandler());
		  try {
		   //执行getMethod
		   int statusCode = httpClient.executeMethod(getMethod);
		   if (statusCode != HttpStatus.SC_OK) {
		    System.err.println("Method failed: "
		      + getMethod.getStatusLine());
		   }
		   //读取内容 
		   byte[] responseBody = getMethod.getResponseBody();
		   //处理内容
		   System.out.println(new String(responseBody,"UTF-8"));
		  } catch (HttpException e) {
		   //发生致命的异常，可能是协议不对或者返回的内容有问题
		   System.out.println("Please check your provided http address!");
		   e.printStackTrace();
		  } catch (IOException e) {
		   //发生网络异常
		   e.printStackTrace();
		  } finally {
		   //释放连接
		   getMethod.releaseConnection();
		  }
		 }
	
	public  static void post1() {
		  //构造HttpClient的实例
		  HttpClient httpClient = new HttpClient();
		  String url = "http://flight.mangocity.com/flights-search.shtml";
		  PostMethod postMethod = new PostMethod(url);
		  //GetMethod getMethod = new GetMethod("http://flight.mangocity.com/oneway-Cheapest.shtml?
		  //id_dpt=WUH&id_arr=PEK&startDate=2013-03-31&id_lineType1=ow");
		  
		  try {
		  // 填入各个表单域的值
		  NameValuePair[] data = { new NameValuePair("id_dpt", "WUH"),new NameValuePair("id_arr", "PEK"),				
		  new NameValuePair("startDate", "2013-03-31"),new NameValuePair("id_lineType1", "ow") };
		  // 将表单的值放入postMethod中
		  postMethod.setRequestBody(data);
		  // 执行postMethod
		  int statusCode = httpClient.executeMethod(postMethod);
		  // HttpClient对于要求接受后继服务的请求，象POST和PUT等不能自动处理转发
		  // 301或者302
		  System.out.println("statusCode:" + statusCode);
		  if (statusCode == HttpStatus.SC_MOVED_PERMANENTLY || 
		  statusCode == HttpStatus.SC_MOVED_TEMPORARILY) {
		      // 从头中取出转向的地址
		      Header locationHeader = postMethod.getResponseHeader("location");
		      String location = null;
		      if (locationHeader != null) {
		       location = locationHeader.getValue();
		       System.out.println("The page was redirected to:" + location);
		      } else {
		       System.err.println("Location field value is null.");
		      }
		      return;
		  }
		//读取内容 
		   byte[] responseBody = postMethod.getResponseBody();
		   //处理内容
		   System.out.println(new String(responseBody,"UTF-8"));
		  } catch (HttpException e) {
			   //发生致命的异常，可能是协议不对或者返回的内容有问题
			   System.out.println("Please check your provided http address!");
			   e.printStackTrace();
			  } catch (IOException e) {
			   //发生网络异常
			   e.printStackTrace();
			  } finally {
			   //释放连接
				  postMethod.releaseConnection();
			  }
		 }

}
