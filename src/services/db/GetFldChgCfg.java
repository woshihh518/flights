package services.db;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Iterator;
import java.util.Set;   
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import services.base.Base;
import services.db.Config;
import services.db.DBAcc;
import services.pub.HashSort;
public class GetFldChgCfg extends Base{
	
	private static String cfgPath;
	private static Hashtable tblcfgHash=new Hashtable();
	private DBAcc dba = new DBAcc(); // 数据库操作实例
	protected void getFldChgCfg(HttpServletRequest request,HttpServletResponse response) 
	throws Exception
	{
		try
	     {
			response.setContentType("text/xml;charset=GB2312");
			Map map= request.getParameterMap();
			Iterator it=map.keySet().iterator();
			while(it.hasNext())
			{
				System.out.println("Map:"+map.get(it.next()));
				
			}
			
			String fldNam = request.getParameter("fldNam");
			System.out.println("fldNam:"+fldNam);
			getFilcfg();
			StringBuffer rntBuf = new StringBuffer();
			rntBuf.append("[");
			String[] fldNams = fldNam.split(":");
			for(int i=0;i<fldNams.length;i++)
			{
				System.out.println("111:"+fldNams[i]);
				String tmps = getConvertcfg(fldNams[i]);
				if(tmps!=null&&!tmps.equals(""))
				{
					rntBuf.append(tmps);
					if(i!=fldNams.length-1)rntBuf.append(",");
				}
				
				
			}
			rntBuf.append("]");
			//String rntstr = getConvertcfg(fldNam);
			//this.setStringAndReturn(rntBuf.toString(), response);
	     }
		 catch(Exception e)
		 {
			 e.printStackTrace();
			 //this.setMsgAndReturn(e.getMessage(), false, request,response);
		 }
		

	}
	
	public void getFilcfg() throws Exception
	  {
		  try
		    {
			  if(cfgPath == null)
				  cfgPath = Config.getValue("CFGDir");//日志路径保存在jndi.properties里面
			  String s="";
			  
		      FileInputStream   is;   
	          InputStreamReader   ir;   
	          BufferedReader   in;   
	          is=new   FileInputStream(cfgPath + File.separator + "fldchgcfg.txt");   
	          ir=new   InputStreamReader(is);   
	          in=new   BufferedReader(ir);   
	          while((s=in.readLine())!=null)   
	          {       
	        	  if(s.equals(""))continue;
	        	  if(s.split("~").length<4)continue;
	        	  Hashtable tmpv1 = new Hashtable();
		          
	        	  tmpv1.put("tblnam",s.split("~")[1].toUpperCase());
	        	  tmpv1.put("tblfldnam",s.split("~")[2].toUpperCase());
	        	  tmpv1.put("smrlist",s.split("~")[3].toUpperCase());
	        	  if(s.split("~").length==5)tmpv1.put("term",s.split("~")[4].toUpperCase());
	        	  tblcfgHash.put(s.split("~")[0].toUpperCase(), tmpv1);
	        	  //System.out.println("tblnam:"+s.split("~")[1].toUpperCase()); 
	          }   
	              
	          in.close();
	          ir.close();
	          is.close();
		      
		    }
			catch (Exception e)
		    {
			  e.printStackTrace();
			  throw new Exception(e.getMessage());
	        }
			
	  }
	
	public String getConvertcfg(String fldNam) throws Exception
	  {
		  try
		    {
			  if(tblcfgHash==null)getFilcfg();
			  Hashtable   tmpv1   =   (Hashtable)tblcfgHash.get(fldNam.toUpperCase());   
		      String tblNam=(String)tmpv1.get("tblnam");
		      String tblfldnam=(String)tmpv1.get("tblfldnam");
		      String smrList=(String)tmpv1.get("smrlist");
		      String fldList="";
		      StringBuffer sbuf =new StringBuffer(); 
		      if(tblNam==null||tblNam.trim().equals(""))
        	  {
        	      if(smrList.trim().equals(""))return "";
        	      String[] fldLists=smrList.split(";");
        	      //System.out.println();
        	      for(int i=0;i<fldLists.length;i++)
        	      {
        	    	  //tmpv1.put(fldLists[i].split("\\,")[0],fldLists[i].split("\\,")[1]);
        	    	  sbuf.append("{\"value\":\""+fldLists[i].split("\\,")[0]+"\",\"text\":\""+fldLists[i].split("\\,")[1]+"\"}");
        	    	  if(i!=fldLists.length-1)sbuf.append(",");
        	      }
        	      tmpv1.remove("tblnam");
				  tmpv1.remove("smrlist");
				  tmpv1.remove("tblfldnam");
				  //tblcfgHash.put(fldNam, tmpv1);
				  //System.out.println("1111:"+sbuf.toString());
        	      return sbuf.toString();
        	  }    
		      
		      for(int i=0;i<smrList.split(":").length;i++)
	          {
	        	  fldList = fldList +smrList.split(":")[i]+",";
	          }
	          
	          if(fldList.trim().equals(""))return "";
	          else
	          {
	        	  //if(fldList.toUpperCase().indexOf(tblfldnam.toUpperCase())!=-1)
	        	  if(fldList.toUpperCase().substring(0,tblfldnam.length()+1).equals(tblfldnam.toUpperCase()+","))
	        	  {
	        		  fldList=fldList.substring(0,fldList.length()-1);
	        	  }
	        	  else
	        	  {
	        		  fldList=fldList+tblfldnam;
	        	  }
	        	  String sql = "select distinct "+fldList+" from "+tblNam+" ";
	        	  if(tmpv1.get("term")!=null&&!((String)tmpv1.get("term")).equals(""))
	        	  {
	        		  sql = sql + " where "+ (String)tmpv1.get("term");
	        	  }
	        	  sql = sql + " order by " +tblfldnam;
	        	  Vector tmpv = dba.doQuery(sql);//查询用户信息
				  //System.out.println(sql);
				  if(!dba.isSucc())//查询失败
				  {
					  
				  }
				  else if(tmpv.isEmpty())//查询不到信息
				  {
					  
				  }
				  else
				  {
					  for(int j=0;j<tmpv.size();j++)
					  {
						  Hashtable tmph = (Hashtable) tmpv.elementAt(j);
						  String fldNam1=((String) tmph.get(tblfldnam.toUpperCase())).trim();
						  String fldVals="";
						  for(int i=0;i<smrList.split(":").length;i++)
				          {
							  fldVals = fldVals +((String) tmph.get(smrList.split(":")[i].toUpperCase())).trim()+":";
				          }
						  //tmpv1.put(fldNam1,fldVals.substring(0,fldVals.length()-1));
						  sbuf.append("{\"value\":\""+fldNam1+"\",\"text\":\""+fldVals.substring(0,fldVals.length()-1)+"\"}");
						  if(j!=tmpv.size()-1)sbuf.append(",");
					  }
					  //System.out.println("2222:"+sbuf.toString());
					  return sbuf.toString();
				  }
				  return "";
	          }   
		       
		      
		      
		    }
			catch (Exception e)
		    {
			  e.printStackTrace();
			  throw new Exception(e.getMessage());
	        }
			
	  }
	
	protected void doGet(HttpServletRequest request,HttpServletResponse response){
		try {
			getFldChgCfg(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
	}
	protected void doPost(HttpServletRequest request,HttpServletResponse response){
		try {
			getFldChgCfg(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	/*
	private static DBAcc dba = new DBAcc(); // 数据库操作实例
	private static String cfgPath;
	private static Hashtable tblcfgHash=new Hashtable();
	private static HashSort sort=new HashSort();
	public GetFldChgCfg()
	{
		//getFilcfg();
		//getConvertcfg();
	}
	
	//原来用的,现在针对EXT重新写
	public static void getFilcfg()
	  {
		  try
		    {
			  if(cfgPath == null)
				  cfgPath = Config.getValue("CFGDir");//日志路径保存在jndi.properties里面
			  String s="";
			  
		      FileInputStream   is;   
	          InputStreamReader   ir;   
	          BufferedReader   in;   
	          is=new   FileInputStream(cfgPath + File.separator + "fldchgcfg.txt");   
	          ir=new   InputStreamReader(is);   
	          in=new   BufferedReader(ir);   
	          while((s=in.readLine())!=null)   
	          {       
	        	  if(s.equals(""))continue;
	        	  if(s.split("~").length<4)continue;
	        	  Hashtable tmpv1 = new Hashtable();
		          
	        	  tmpv1.put("tblnam",s.split("~")[1].toUpperCase());
	        	  tmpv1.put("tblfldnam",s.split("~")[2].toUpperCase());
	        	  tmpv1.put("smrlist",s.split("~")[3].toUpperCase());
	        	  if(s.split("~").length==5)tmpv1.put("term",s.split("~")[4].toUpperCase());
	        	  tblcfgHash.put(s.split("~")[0].toUpperCase(), tmpv1);
	  	           
	          }   
	          //System.out.println("The   total   rows   are:"+rntstrbuf.toString());    
	          in.close();
	          ir.close();
	          is.close();
		      
		    }
			catch (Exception e)
		    {
			  e.printStackTrace();
	        }
			
	  }
	
	  public static void getConvertcfg()
	  {
		  try
		    {
			  if(tblcfgHash==null)getFilcfg();
			  for   (Iterator   it   =   tblcfgHash.keySet().iterator();   it.hasNext();   )   
			  {   
		          String   fldNam   =   (String)   it.next();   
		          Hashtable   tmpv1   =   (Hashtable)tblcfgHash.get(fldNam);   
		          String tblNam=(String)tmpv1.get("tblnam");
		          String tblfldnam=(String)tmpv1.get("tblfldnam");
		          String smrList=(String)tmpv1.get("smrlist");
		          //System.out.println(fldNam+":"+tblNam+":"+smrList);
		          String fldList="";
		          if(tblNam==null||tblNam.trim().equals(""))
		        	  {
		        	      if(smrList.trim().equals(""))continue;
		        	      String[] fldLists=smrList.split(";");
		        	      //System.out.println();
		        	      for(int i=0;i<fldLists.length;i++)
		        	      {
		        	    	  tmpv1.put(fldLists[i].split("\\,")[0],fldLists[i].split("\\,")[1]);
		        	      }
		        	      tmpv1.remove("tblnam");
						  tmpv1.remove("smrlist");
						  tmpv1.remove("tblfldnam");
						  tblcfgHash.put(fldNam, tmpv1);
		        	      continue;
		        	  }
		          
		          
		          for(int i=0;i<smrList.split(":").length;i++)
		          {
		        	  fldList = fldList +smrList.split(":")[i]+",";
		          }
		          
		          if(fldList.trim().equals(""))continue;
		          else
		          {
		        	  if(fldList.toUpperCase().indexOf(fldNam.toUpperCase())!=-1)
		        	  {
		        		  fldList=fldList.substring(0,fldList.length()-1);
		        	  }
		        	  else
		        	  {
		        		  fldList=fldList+tblfldnam;
		        	  }
		        	  String sql = "select distinct "+fldList+" from "+tblNam+" ";
		        	  if(tmpv1.get("term")!=null&&!((String)tmpv1.get("term")).equals(""))
		        	  {
		        		  sql = sql + " where "+ (String)tmpv1.get("term");
		        	  }
		        	  sql = sql + " order by " +tblfldnam;
		        	  Vector tmpv = dba.doQuery(sql);//查询用户信息
					  System.out.println(sql);
					  if(!dba.isSucc())//查询失败
					  {
						  
					  }
					  else if(tmpv.isEmpty())//查询不到信息
					  {
						  
					  }
					  else
					  {
						  for(int j=0;j<tmpv.size();j++)
						  {
							  Hashtable tmph = (Hashtable) tmpv.elementAt(j);
							  String fldNam1=((String) tmph.get(tblfldnam.toUpperCase())).trim();
							  String fldVals="";
							  for(int i=0;i<smrList.split(":").length;i++)
					          {
								  fldVals = fldVals +((String) tmph.get(smrList.split(":")[i].toUpperCase())).trim()+":";
					          }
							  tmpv1.put(fldNam1,fldVals.substring(0,fldVals.length()-1));
						  }
					  }
					  tmpv1.remove("tblnam");
					  tmpv1.remove("smrlist");
					  tmpv1.remove("tblfldnam");
					  tmpv1.remove("term");
					  //System.out.println(tmpv1);
					  
					  tblcfgHash.put(fldNam, tmpv1);
		          }
		      }
		      
		    }
			catch (Exception e)
		    {
			  e.printStackTrace();
	        }
			
	  }
	  
	  public static Hashtable getFldChg(String fldNam)
	  {
		  return (Hashtable)tblcfgHash.get(fldNam.toUpperCase());
	  }
	  public static Set getFldChgKeySet(String fldNam)
	  {
		  return sort.sort((Hashtable)tblcfgHash.get(fldNam.toUpperCase()));
	  }
	  public static String getFldChg(String fldNam,String fldVal)
	  {
		  if(tblcfgHash.get(fldNam.toUpperCase())==null)
			  return fldVal;
		  return (String)((Hashtable)tblcfgHash.get(fldNam.toUpperCase())).get(fldVal);
	  }
      */
}
