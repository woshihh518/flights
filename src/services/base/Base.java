package services.base;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import services.pub.HPub;
import services.log.HLog;

//基础类，作为所有类的基本，由HttpServlet扩展而来,处理结果标志rspFlg,以及登录信息loginsession
public class Base {
	private String msgStr="";		//操作信息
	private boolean rspFlg=true;		//操作结果
	//HttpServletRequest request;
	public HPub pub = new HPub(); // 公共函数
	public HLog hlog = new HLog(); // 
	final public Logger logger = LoggerFactory.getLogger(this.getClass()); 
	final public Logger errlogger = LoggerFactory.getLogger("errlogger"); 
	public Base()
	{
		
		
	}
	
	
	protected void setMsg(String msgStr,boolean rspFlg)
	{
		this.msgStr = msgStr;
		this.rspFlg = rspFlg;
	}

	protected void setMsg(String msgStr)
	{
		this.msgStr = msgStr;
		
	}
	
	protected void setRspFlg(boolean rspFlg)
	{
		this.rspFlg = rspFlg;
		
	}
	
	/* 返回提示信息字符串 */
	public String getMsg()
	{
		return this.msgStr;
	}
	
	public boolean getRspFlg()
	{
		return this.rspFlg;
	}
	
	/*public void setUserInfo(String ID,String value)
    {
		loginSession.setUserInfo(ID, value);
		hs.setAttribute("LoginSession",loginSession);
    }
	
	public void setLoginHash(Hashtable loginHash)
    {
		loginSession.setLoginHash(loginHash);
		hs.removeAttribute("LoginSession");
		hs.setAttribute("LoginSession",loginSession);
    }
	*/
	
	
	/*public Object getUserInfo(String ID)
    {
		this.loginSession=(LoginSession)hs.getAttribute("LoginSession");
		if(loginSession==null)return null;
		else if(loginSession.getUserInfo(ID)==null)return null;
		else return loginSession.getUserInfo(ID);
    }
	
	public String getUserInfoStr(String ID)
    {
		this.loginSession=(LoginSession)hs.getAttribute("LoginSession");
		if(loginSession==null)return "";
		else if(loginSession.getUserInfo(ID)==null)return "";
		else return (String)loginSession.getUserInfo(ID);
    }*/
	
	
	protected void ShowMessage(String msgStr)
	{
		System.out.println(msgStr);
	}
	/*
	protected void setSessionAttribute(String attributeName,Object attributeValue)
	{
		hs.setAttribute(attributeName,attributeValue);
	}
	*/
	
	//组成需要返回的成功标志加上+返回信息
	public String getResult()
	{
		String resultStr="false";
		if(this.rspFlg)resultStr="true";
		return "{\"success\":"+resultStr+",\"message\":\""+this.msgStr+"\"}";
	}
	
	//组成需要返回的成功标志加上+返回信息
	public String getResult(Hashtable tmph)
	{
		String resultStr="false";
		if(this.rspFlg)resultStr="true";
		StringBuffer retBuf = new StringBuffer();
		retBuf.append("{\"success\":"+resultStr+",");
		/*if(tmph!=null)
		{
			for(Iterator it = tmph.keySet().iterator(); it.hasNext();)   
			  {   
		          String fldNam   =   (String)it.next();   
		          String fldVal   =   (String)tmph.get(fldNam); 
		          retBuf.append(","+fldNam+":'"+fldVal+"'");
			  }
		}*/
		retBuf.append(pub.objToJson(tmph));
		retBuf.append("}");
		return retBuf.toString();
	}
	
	

}
