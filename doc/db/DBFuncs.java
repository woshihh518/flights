package services.db;

import java.util.*;

import services.pub.*;
import services.log.*;
import services.base.*;
public class DBFuncs extends Base{
    
	private String tblnam="";//表名
	private String user="000000";
	private String inst="000000";
	private DBAcc dba;
	/*public   DBFuncs(HttpServletRequest request)
	{
		  super(request,1); 
		  //user = this.getUserInfoStr(request,"USERID");
		  //inst = this.getUserInfoStr(request,"CRPCOD");
		//if(this.getRspFlg()==false);
		  dba = new DBAcc(); // 数据库操作实例
	}*/
	public   DBFuncs()
	{
		  
		  dba = new DBAcc(); // 数据库操作实例
	}
	/*public   DBFuncs(String jndiNam)
    {
          
          dba = new DBAcc(jndiNam); // 数据库操作实例
    }*/
	/**
	 * Method：public   DBFuncs(HttpServletRequest request,String tblNam)
	 * Function:
	 * 		选择非默认的JNDI
	 * 输入参数：
	 * 			jndiNam：
	 */	
	/*public   DBFuncs(HttpServletRequest request,String jndiNam)
	{
		  super(request,1); 
		  dba = new DBAcc(jndiNam); // 数据库操作实例
		//if(this.getRspFlg()==false);
	}*/
	/*public   DBFuncs(HttpServletRequest request,String tblNam)
	{
		  super(request,1); 
		  this.tblnam=tblNam;
		//if(this.getRspFlg()==false);
	}*/
	private HLog hlog = new HLog(); // 
	//private DBAcc dba = new DBAcc(); // 数据库操作实例
	
	/**
	 * Method：public Hashtable readSingleRecord(String sql)
	 * Function:
	 * 		读取单条sql
	 * 输入参数：
	 * 			sql：
	 * 输出参数:  含有所有结果字段的Hashtable
	 * 
	 */	
	public Hashtable readSingleRecord(String sql)
	{
		 hlog.wTransLog(1,this.getClass().getName(), "readSingleRecord",
		  		  sql, user, inst);
		 try
		  {
			  Vector tmpv = dba.doQuery(sql);//查询用户信息
			  if(!dba.isSucc())//查询失败
			  {
				  setMsg(dba.getMsg());
				  setRspFlg(false);
				  return null;
			  }
			  else if(tmpv.isEmpty())//查询不到信息
			  {
				  return null;
			  }
			  else
			  {
				  Hashtable tmph = (Hashtable) tmpv.elementAt(0);
				  return tmph;
			  }
			  
		  }
		  catch(Exception e)
		  {
			    hlog.wTransLog(1,this.getClass().getName(), "readSingleRecord:"+sql+":error",
						e.getMessage(), user, inst);
			    hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
				e.printStackTrace();
				setMsg("执行"+sql+"时出错：" + e.getMessage(),false);
				
		  }
		  return null;	     
		
	}
	
	/**
	 * Method：public Vector queryTableAll(String sql)
	 * Function:
	 * 		多条查询
	 * 输入参数：
	 * 			sql：
	 * 输出参数:  含有所有记录的Vector
	 * 
	 */	
	public Vector queryTableAll(String sql)
	{
		 hlog.wTransLog(1,this.getClass().getName(), "queryTableAll",
		  		  sql, user, inst);
		 try
		  {
			  //long time = System.currentTimeMillis();
			  Vector tmpv = dba.doQuery(sql);//查询用户信息
			  //System.out.println("xx1:"+(System.currentTimeMillis()-time));
			  if(!dba.isSucc())//查询失败
			  {
				  setMsg(dba.getMsg());
				  setRspFlg(false);
				  return null;
			  }
			  else if(tmpv.isEmpty())//查询不到信息
			  {
				  return null;
			  }
			  else
			  {
				  return tmpv;
			  }
			  
		  }
		  catch(Exception e)
		  {
			    hlog.wTransLog(1,this.getClass().getName(), "queryTableAll:"+sql+":error",
						e.getMessage(), user, inst);
			    hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
				e.printStackTrace();
				setMsg("执行"+sql+"时出错：" + e.getMessage(),false);
				
		  }
		  return null;	     
		
	}
	
	/**
	 * Method：public Vector queryTableAll(String sql, int rowStart, int pagesize )
	 * Function:
	 * 		多条查询(分页)
	 * 输入参数：
	 * 			sql：
	 * 输出参数:  含有所有记录的Vector
	 * 
	 */	
	public Vector queryTableAll(String sql, int rowStart, int pagesize )
	{
		 hlog.wTransLog(1,this.getClass().getName(), "queryTableAll",
		  		  sql, user, inst);
		 try
		  {
			  Vector tmpv = dba.doQuery(sql,rowStart,pagesize);//查询用户信息
			  if(!dba.isSucc())//查询失败
			  {
				  setMsg(dba.getMsg());
				  setRspFlg(false);
				  return null;
			  }
			  else if(tmpv.isEmpty())//查询不到信息
			  {
				  return null;
			  }
			  else
			  {
				  return tmpv;
			  }
			  
		  }
		  catch(Exception e)
		  {
			    hlog.wTransLog(1,this.getClass().getName(), "queryTableAll:"+sql+":error",
						e.getMessage(), user, inst);
			    hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
				e.printStackTrace();
				setMsg("执行"+sql+"时出错：" + e.getMessage(),false);
				
		  }
		  return null;	     
		
	}
	
	/**
	 * Method：public String getSeqNo(String tblNam,String seqFld,int seqLen,String term)
	 * Function:
	 * 		按条件取顺序号
	 * 输入参数：
	 * 			tblNam：表名;
	 *          seqFld: 字段名;
	 *          seqLen: 顺序号的长度;
	 *          term: 限制条件
	 * 输出参数:  seqLen长度的seqNo
	 * 
	 */	
	public String getSeqNo(String tblNam,String seqFld,int seqLen,String term)
	{
		try
		  {
			  String sql="select max("+seqFld+") "+seqFld+" from "+tblNam+" ";
			  if(!term.equals(""))
				 {
					 sql = sql +" where " + term;
				 }
			  hlog.wTransLog(1,this.getClass().getName(), "getSeqNo",
				  		  sql, user, inst);
			  Vector tmpv = dba.doQuery(sql);//查询用户信息
			  if(!dba.isSucc())//查询失败
			  {
				  setMsg(dba.getMsg());
				  setRspFlg(false);
				  return null;
			  }
			  else if(tmpv.isEmpty())//查询不到信息
			  {
				  setMsg("查询不到信息");
				  setRspFlg(false);
				  return null;
			  }
			  else
			  {
				  Hashtable tmph = (Hashtable) tmpv.elementAt(0);
				  String seqno = ((String) tmph.get(seqFld.toUpperCase())).trim();
				  if(seqno==null||seqno.equals("null")||seqno.equals(""))
				  {
					 return null;
				  }
				  int iseqno = Integer.parseInt(seqno.substring(seqno.length()-seqLen));
				  iseqno++;
				  
				  String sseqno=""+iseqno;
				  while(sseqno.length()<seqLen)sseqno="0"+sseqno;
				  return sseqno;
			  }
	 }
	catch(Exception e)
	  {
		    hlog.wTransLog(1,this.getClass().getName(), "getSeqNo error",
					e.getMessage(), user, inst);
		    hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
		    e.printStackTrace();
			setMsg("取"+seqFld+"时发生异常：" + e.getMessage(),false);
			
	  }
		return null;
	}
	
	/**
	 * Method：public boolean execSql(String sql)
	 * Function:
	 * 		执行sql
	 * 输入参数：
	 * 			sql：
	 * 输出参数:  执行结果 boolean
	 * 
	 */	
	public boolean execSql(String sql)
	{
			  try
			  {
				  
				  String updStr=sql;
				  hlog.wTransLog(1,this.getClass().getName(), "execSql",
				  		  sql, user, inst);
			    	dba.executeUpdate(updStr);
				      if(!dba.isSucc())//update失败
				      {
					      setMsg(dba.getMsg());
					      setRspFlg(false);
					      return false;
				      }
				  
				      return true;
		 }
		catch(Exception e)
		  {
			    hlog.wTransLog(1,this.getClass().getName(), "execSql:"+sql+":error",
					e.getMessage(), user, inst);
		        hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
			    e.printStackTrace();
			    setMsg("执行"+sql+"时出错：" + e.getMessage(),false);
				
		  }
		  return false;
	}
	
	/**
	 * Method：public boolean execSql(String sql)
	 * Function:
	 * 		执行sql
	 * 输入参数：
	 * 			sql：
	 * 输出参数:  执行结果 boolean
	 * 
	 */	
	public boolean execDB2import(String sql)
	{
			  try
			  {
				  
				  String updStr=sql;
				  hlog.wTransLog(1,this.getClass().getName(), "execSysSql",
				  		  sql, user, inst);
			    	dba.execDB2import(updStr);
				      if(!dba.isSucc())//update失败
				      {
					      setMsg(dba.getMsg());
					      setRspFlg(false);
					      return false;
				      }
				  
				      return true;
		 }
		catch(Exception e)
		  {
			    hlog.wTransLog(1,this.getClass().getName(), "execSql:"+sql+":error",
					e.getMessage(), user, inst);
		        hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
			    e.printStackTrace();
			    setMsg("执行"+sql+"时出错：" + e.getMessage(),false);
				
		  }
		  return false;
	}
	
	/**
	 * Method：public boolean execSqlBatch(Vector sqlVec)
	 * Function:
	 * 		批量执行sql
	 * 输入参数：
	 * 			sqlVec：含有需要执行的sql的Vector
	 * 输出参数:  执行结果 boolean
	 * 
	 */
	public boolean execSqlBatch(Vector sqlVec)
	{
	    try
		    {
	    	      if(sqlVec!=null||sqlVec.size()>0)
	    	      {
	    	    	  for(int i =0;i<sqlVec.size();i++)
	    				{
	    	    		    String sqlNow = (String)sqlVec.elementAt(i);  
	    	    		    hlog.wTransLog(1,this.getClass().getName(),"executeBatchUpdate",
	    							sqlNow, user, inst);
	    				}
	    	      }
	    	      else
	    	      { 
	    	    	  setMsg("无需要执行的sql");
				      setRspFlg(false);
				      return false;
	    	      }
	    	      //dba.executeBatchUpdate(sqlVec);
	    	      if(!dba.executeBatchUpdate(sqlVec)||!dba.isSucc())//update失败
			      {
				      setMsg(dba.getMsg());
				      setRspFlg(false);
				      return false;
			      }
	    	      return true;
		     }
		catch(Exception e)
		     {
			    hlog.wTransLog(1,this.getClass().getName(), "execSqlBatch error",
					e.getMessage(), user, inst);
		        hlog.wExceptionLog(this.getClass().getName(),e, user, inst);
			    e.printStackTrace();
			    setMsg("批量执行语句时出错：" + e.getMessage(),false);
		      }
		  return false;
	}

	
	
	
}
