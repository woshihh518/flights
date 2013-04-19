package services.log;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.TimeZone;
import java.io.*;
import java.util.*;
import services.db.ReadCFG;

//专用写日志的类
public class HLog {
    private String logPath;
    private StringWriter sWriter = new StringWriter();
	private PrintWriter pWriter = new PrintWriter(sWriter);
	private static ReadCFG cfg = new ReadCFG();
	private static String workdir = cfg.getvalue("workdir");
	private static String logdir = workdir+"\\log";
	//写正常的日志
	public void wTransLog(int logType, String ClassMethodName, String Describe, String Content, String User, String Inst)
    {
		try
        {
         if(logPath == null)
         	logPath = logdir;//日志路径保存在jndi.properties里面
         String curdat=getDate("yyyyMMdd");
         String logDir = logPath + File.separator + curdat;
         File myFile = new File(logDir);
         if(logType==1)
         {
        	 String tempClassMethodName=getUpClassMethodName(ClassMethodName);
        	 if(tempClassMethodName!=null)ClassMethodName=tempClassMethodName;
         }
         if(User==null||User.equals(""))User="000000";
         String fileName = logDir + File.separator + Inst+".txt";
         if(!myFile.exists())
             myFile.mkdirs();
         	FileOutputStream fileStream = null;
         	try
	         {
	             fileStream = new FileOutputStream(fileName, true);
	             String currentTime = getDate("HH:mm:ss");;
	             String contents = "[Time:" + currentTime + "]["+ClassMethodName+"][User:" + User + "][Dept:" + Inst + "]" + Describe + " | " + Content + " | " + "\n";
	             fileStream.write(contents.getBytes());
	         }
	         catch(Exception e)
	         {
	             System.out.println("[wTransLog出现错误：]" + e.toString());
	         }finally{
	         	try{
	         		if(fileStream!=null){
	         			fileStream.close();
	         		}         		
	         	}catch(Exception e1){
	         		System.out.println("关闭Translog.txt fileStream失败");
	         	}
	         }
        }
	         catch(Exception e)
	         {
	             System.out.println("[wTransLog出现错误：]" + e.toString());
	         }
               	
    }	
	
	//写出错日志
	public void wExceptionLog(String ClassMethodName, String Describe, Exception e, String User, String Inst){
   	    if(logPath == null)
   		logPath = cfg.getvalue("LogDir");//日志路径保存在jndi.properties里面
   	 String curdat=getDate("yyyyMMdd");
     String logDir = logPath + File.separator + curdat;
     String fileName = logDir + File.separator + "Exceptionlog.txt";
     File myFile = new File(logDir);
     if(!myFile.exists())
         myFile.mkdirs();
        FileOutputStream fileStream = null;
        try {
			fileStream = new FileOutputStream(fileName, true);
			String currentTime = getDate("HH:mm:ss");;
            String contents = "[Time:" + currentTime + "]["+ClassMethodName+"][User:" + User + "][Dept:" + Inst + "]" + Describe + " | \n";
			e.printStackTrace(pWriter);
			fileStream.write(contents.toString().getBytes());
			fileStream.write(sWriter.toString().getBytes());
		} catch (Exception e1) {
			System.out.println("[wExceptionLog：]" + e.toString());
		}finally{
        	try{
        		if(fileStream!=null){
        			fileStream.close();
        		}         		
        	}catch(Exception e1){
        		System.out.println("关闭Exceptionlog fileStream失败");
        	}
        }        	  	
   }
	
	//写出错日志
	public void wExceptionLog(String ClassMethodName, Exception e, String User, String Inst){
   	    if(logPath == null)
   		logPath = cfg.getvalue("LogDir");//日志路径保存在jndi.properties里面
   	 String curdat=getDate("yyyyMMdd");
     String logDir = logPath + File.separator + curdat;
     String fileName = logDir + File.separator + "Exceptionlog.txt";
     File myFile = new File(logDir);
     if(!myFile.exists())
         myFile.mkdirs();
        FileOutputStream fileStream = null;
        try {
			fileStream = new FileOutputStream(fileName, true);
			String currentTime = getDate("HH:mm:ss");;
            String contents = "[Time:" + currentTime + "]["+ClassMethodName+"][User:" + User + "][Dept:" + Inst + "]\n";
			e.printStackTrace(pWriter);
			fileStream.write(contents.toString().getBytes());
			fileStream.write(sWriter.toString().getBytes());
		} catch (Exception e1) {
			System.out.println("[wExceptionLog：]" + e.toString());
		}finally{
        	try{
        		if(fileStream!=null){
        			fileStream.close();
        		}         		
        	}catch(Exception e1){
        		System.out.println("关闭Exceptionlog fileStream失败");
        	}
        }        	  	
   }
	
	//按照指定格式取当前日期
	public String getDate(String formatstr){ 

	       TimeZone timeZone = TimeZone.getTimeZone("Asia/Shanghai");   	
		   SimpleDateFormat fm = new SimpleDateFormat(formatstr);
		   fm.setTimeZone(timeZone);
	    	Date myDate = new Date();
	      	
	       String datenow = fm.format(myDate);


	        return datenow;
	    }
	
	//logType为1时需要通过此函数取到原函数的调用者来看究竟是那个函数写的日志
	public String getUpClassMethodName(String ClassMethodName)
	{
		try
		{
			String fullClassName = ClassMethodName;
	    	StackTraceElement stack[] = (new Throwable()).getStackTrace();
	    	int ix = 0;
	        while (ix < stack.length) 
	        {
	            StackTraceElement frame = stack[ix];
	            String cname = frame.getClassName();
	            //System.out.println("在"+cname+"中调用次方法的方法名："+frame.getMethodName());
	            if (cname.equals(fullClassName)) 
	            {
	                break;
	            }
	            ix++;
	        }
	        
	        while (ix < stack.length) 
	        {
	            StackTraceElement frame = stack[ix];
	            String cname = frame.getClassName();
	            //System.out.println("在"+cname+"中调用次方法的方法名："+frame.getMethodName());
	            if (!cname.equals(fullClassName)) 
	            {
	            	return cname+":"+frame.getMethodName();
	            }
	            ix++;
	        }
			return null;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}
