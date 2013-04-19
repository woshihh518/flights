package services.pub;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2007</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
//一些经常用到的简单处理函数
//delspace去空格，包括姓名中间的空格
//Htrim去空格，如果为NULL返回""
//WriteErrLog写错误日志
//AMTADD金额相加不去掉金额末尾的空格
//AMTSUB金额相减不去掉金额末尾的空格
//AMTADD1金额相加去掉金额末尾的空格
//AMTSUB1金额相减去掉金额末尾的空格
//AMTTOINT金额转化为整数不带小数点
//AMTDELZERO去掉金额后面的0
//copyFile复制文件
//20080703 HH新增 财政局系统 LONGAMTADD金额相加并且格式化
//20080703 HH新增 财政局系统 LONGAMTSUB金额相减并且格式化
//20081009 HH新增整数减法
//20081009 HH新增整数加法
//20081009 HH新增浮点除法留N位小数
//20081009 HH新增浮点减法留N位小数
//取当前日期N天内的日期
//将15位身份证转为18位
//判断输入是否为double通过正则表达式
import java.util.*;
import java.io.*;
import java.text.*;
import java.util.regex.Pattern;
import javax.servlet.http.*;

public class PubFunHC {
  public PubFunHC() {
  }
  
  
  //delspace去空格，包括姓名中间的空格
  public String delspace(String name)
  {
  	if(name==null)return name;
  	String  xxname="";
             for(int ixxname=1;ixxname<=name.length();ixxname++)
             {
             	if(!name.substring(ixxname-1,ixxname).equals(" ")&&!name.substring(ixxname-1,ixxname).equals("　"))
             	{
             		xxname=xxname+name.substring(ixxname-1,ixxname);
             	}
             }
    return xxname;
  }
  
//Htrim去空格，如果为NULL返回""
  public String Htrim(String name)
  {
  	if(name==null)return "";
  	
    return name.trim();
  }
  
  //WriteErrLog写错误日志
  public void WriteErrLog(String log,String curmon)
  {
  	try
  	{
  		     String Filename="d:/gzdf/err/" + curmon + ".log.txt";
  		     FileOutputStream out=new FileOutputStream(Filename,true);
   	       PrintStream p =  new PrintStream(out) ;
   	       p.println("------" + new Date() + "--------------------------------------------");
           p.println(log);
   	       p.close();
  	}
  	catch (Exception e) {
                 System.err.println ("Error  WriteErrLog  " + e.getMessage());
                 e.printStackTrace();
        }
  }
  
  //WriteErrLog写错误日志
  public void WriteErrLogWithoutDate(String log,String curmon)
  {
  	try
  	{
  		     String Filename="d:/gzdf/err/" + curmon + ".log.txt";
  		     FileOutputStream out=new FileOutputStream(Filename,true);
   	       PrintStream p =  new PrintStream(out) ;
           p.println(log);
   	       p.close();
  	}
  	catch (Exception e) {
                 System.err.println ("Error  WriteErrLog  " + e.getMessage());
                 e.printStackTrace();
        }
  }
  
  //AMTADD金额相加不去掉金额末尾的空格
  public String AMTADD(String Amt1 ,String Amt2)
  {
  	//先找"."的位置
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	i1=i1+i2;
  	String xitotal=""+i1;
  	         if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
    //xitotal=AMTDELZERO(xitotal);
  	return xitotal;





  }
  //AMTSUB金额相减不去掉金额末尾的空格
  public String AMTSUB(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	String flag="";
  	try
  	{
  	//先找"."的位置
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	i1=i1-i2;
  	xitotal=""+i1;
  	
  	if(xitotal!=null&&xitotal.substring(0,1).equals("-"))
  	{
  		flag="-";
  		xitotal=xitotal.substring(1,xitotal.length());
  	}
  	         if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
             
             //xitotal=AMTDELZERO(xitotal);
  	}
  	catch (Exception e)
  	    {
                    System.err.println ("Error  GZDFSplitErciAllFile for args  :  " + e.getMessage());
                    e.printStackTrace();
        }

  	return flag+xitotal;





  }
  
  
  //AMTADD1金额相加去掉金额末尾的空格
  public String AMTADD1(String Amt1 ,String Amt2)
  {
  	//先找"."的位置
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	i1=i1+i2;
  	String xitotal=""+i1;
  	         if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
    xitotal=AMTDELZERO(xitotal);
  	return xitotal;





  }
  //AMTSUB1金额相减去掉金额末尾的空格
  public String AMTSUB1(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	try
  	{
  	//先找"."的位置
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	i1=i1-i2;
  	xitotal=""+i1;
  	         if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
             
             xitotal=AMTDELZERO(xitotal);
  	}
  	catch (Exception e)
  	    {
                    System.err.println ("Error  GZDFSplitErciAllFile for args  :  " + e.getMessage());
                    e.printStackTrace();
        }

  	return xitotal;





  }

  //AMTTOINT金额转化为整数不带小数点
  public String AMTTOINT(String Amt1)
  {

  	try
  	{
    Amt1=Amt1.trim();
    if(Amt1.substring(0,1).equals("."))return Amt1.substring(1,Amt1.length());
  	StringTokenizer lineString = new StringTokenizer(Amt1,".");
  	if(!lineString.hasMoreTokens())
  	{
        //return Amt1;   
  	}
    
  	String arg1=lineString.nextToken();
  	String arg2="00";
  	if(lineString.hasMoreTokens())
  	{
  		arg2=lineString.nextToken();
  	}
  	if(arg2.length()==2)
  	{
  		Amt1=arg1+arg2;
  	}
  	if(arg2.length()==1)
  	{
  		Amt1=arg1+arg2+"0";
  	}

  	}
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

  	return Amt1;
  }
  
  //AMTDELZERO去掉金额后面的0
  public String AMTDELZERO(String Amt1)
  {

  	try
  	{
      Amt1=Amt1.trim();
  	  StringTokenizer lineString = new StringTokenizer(Amt1,".");
  	  if(!lineString.hasMoreTokens())
  	  {
          //return Amt1;   
  	  }
      
  	  String arg1=lineString.nextToken();
  	  String arg2="00";
  	  if(lineString.hasMoreTokens())
  	  {
  	  	arg2=lineString.nextToken();
  	  	if(arg2.length()==2)
  	    {
  	    	if(arg2.equals("00"))
  	    	{
  	    		Amt1=arg1;
  	    	}
  	    	else if(arg2.substring(1,2).equals("0"))
  	    	{
  	    		Amt1=arg1+"."+arg2.substring(0,1);
  	    	}
  	    }
  	    if(arg2.length()==1)
  	    {
  	    	if(arg2.equals("0"))
  	    	{
  	    		Amt1=arg1;
  	    	}
  	    }
  	  	
  	  }
  	  
  	  	return Amt1;
  	  

  	}
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

  	return Amt1;
  }
  
  //copyFile复制文件
  public void copyFile(String src,String dest) throws IOException{
        FileInputStream in=new FileInputStream(src);
        File file=new File(dest);
        if(file.exists())file.delete();
            file.createNewFile();
        FileOutputStream out=new FileOutputStream(file);
        int c;
        byte buffer[]=new byte[1024];
        while((c=in.read(buffer))!=-1){
            for(int i=0;i<c;i++)
                out.write(buffer[i]);        
        }
        in.close();
        out.close();
    }
  
  
    //20080703 HH新增 财政局系统 LONGAMTADD金额相加并且格式化
  public String LONGAMTADD(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	try
  	{
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	long i1 = Long.parseLong(Amt1);
  	long i2 = Long.parseLong(Amt2);
  	
  	i1=i1+i2;
  	Amt1=""+i1;
  	xitotal=Amt1;
  	String sign="";
  	if(xitotal.length()>1&&xitotal.substring(0,1).equals("-"))
  	{
  		sign="-";
  		xitotal=xitotal.substring(1,xitotal.length());
  	}
  	if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
    //xitotal=AMTDELZERO(xitotal);
  	return sign+xitotal;

    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
   //20080703 HH新增 财政局系统 LONGAMTSUB金额相减并且格式化
  public String LONGAMTSUB(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	try
  	{
  	Amt1=AMTTOINT(Amt1);
  	Amt2=AMTTOINT(Amt2);
  	long i1 = Long.parseLong(Amt1);
  	long i2 = Long.parseLong(Amt2);
  	
  	i1=i1-i2;
  	Amt1=""+i1;
  	xitotal=Amt1;
  	String sign="";
  	if(xitotal.length()>1&&xitotal.substring(0,1).equals("-"))
  	{
  		sign="-";
  		xitotal=xitotal.substring(1,xitotal.length());
  	}
  	if(xitotal.length()>2)
             {
               xitotal=xitotal.substring(0,(xitotal.length()-2))+"."+xitotal.substring((xitotal.length()-2),xitotal.length());
             }
             if(xitotal.length()==2)
             {
               xitotal="0."+xitotal;
             }
             if(xitotal.length()==1)
             {
               xitotal="0.0"+xitotal;
             }
    //xitotal=AMTDELZERO(xitotal);
  	return sign+xitotal;

    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
  //20081009 HH新增整数减法
  public String INTSUB(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	try
  	{
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	
  	i1=i1-i2;
  	Amt1=""+i1;
  	xitotal=Amt1;
  	
  	return xitotal;

    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
  //20081009 HH新增整数加法
  public String INTADD(String Amt1 ,String Amt2)
  {
  	String xitotal="";
  	try
  	{
  	int i1 = Integer.parseInt(Amt1);
  	int i2 = Integer.parseInt(Amt2);
  	
  	i1=i1+i2;
  	Amt1=""+i1;
  	xitotal=Amt1;
  	
  	return xitotal;

    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  AMTTOINT  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
  //20081009 HH新增浮点除法留N位小数
  public String FLOATDIV(String Amt1 ,String Amt2,int n)
  {
  	String xitotal="";
  	try
  	{
  	float ix1 = Float.parseFloat(Amt1);
  	float ix2 = Float.parseFloat(Amt2);
  	if(ix2==0)return "0.0";
  	ix1=ix1/ix2;
  	Amt1=""+ix1;
  	xitotal=Amt1;
  	StringTokenizer lineString = new StringTokenizer(Amt1,".");
  	if(!lineString.hasMoreTokens())
  	  {
          return xitotal;   
  	  }
  	String arg1=lineString.nextToken();
  	xitotal=""+arg1+".";
  	if(lineString.hasMoreTokens())
  	  {
  	     String arg2=lineString.nextToken();
  	     while(arg2.length()<n)arg2=arg2+"0";
  	     xitotal=xitotal+arg2.substring(0,n);
  	  }  
  	   
  	return xitotal;

    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  FLOATDIV  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
  //20081009 HH新增浮点减法留N位小数
  public String FLOATSUB(String Amt1 ,String Amt2,int n)
  {
  	String xitotal="";
  	try
  	{
  		/*
  	float ix1 = Float.parseFloat(Amt1);
  	float ix2 = Float.parseFloat(Amt2);
  	
  	ix1=ix1-ix2;
  	Amt1=""+ix1;
  	xitotal=Amt1;
  	StringTokenizer lineString = new StringTokenizer(Amt1,".");
  	if(!lineString.hasMoreTokens())
  	  {
          return xitotal;   
  	  }
  	String arg1=lineString.nextToken();
  	xitotal=""+arg1+".";
  	if(lineString.hasMoreTokens())
  	  {
  	     String arg2=lineString.nextToken();
  	     while(arg2.length()<n)arg2=arg2+"0";
  	     xitotal=xitotal+arg2.substring(0,n);
  	  }  
  	   
  	return xitotal;
  	*/
      Amt1=Amt1+"0";
      Amt2=Amt2+"0";
      Amt1=AMTSUB(Amt1 ,Amt2);
      Amt1=Amt1.substring(0,Amt1.length()-1);
      return Amt1;
      
    }
  	catch (Exception e)
  	    {
                    System.err.println ("Error  FLOATDIV  :  " + e.getMessage());
                    e.printStackTrace();
        }

    return Amt1;

  }
  
  //取当前日期N天内的日期("yyyyMMdd HHmmss")
  public String getdate(String format,int n)
  {
	  Calendar   cal   =   Calendar.getInstance();
      cal.setTime(new   java.util.Date());     //注意
      cal.add(java.util.Calendar.DAY_OF_YEAR,   n);
      java.util.Date   date1   =   cal.getTime();

      SimpleDateFormat df1 = new SimpleDateFormat(format);
      String CurDate=(String)df1.format(date1);
      return CurDate;
  }
  
  //取当前日期N天内的日期
  public String GETDATE(String format,int n)
  {
	  Calendar   cal   =   Calendar.getInstance();
      cal.setTime(new   java.util.Date());     //注意
      cal.add(java.util.Calendar.DAY_OF_YEAR,   n);
      java.util.Date   date1   =   cal.getTime();

      SimpleDateFormat df1 = new SimpleDateFormat(format);
      String CurDate=(String)df1.format(date1);
      return CurDate;
  }
  
  public static String getDistanceDay(String date, int day) {
  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
      Date d;
      Calendar c =Calendar.getInstance();
      try {
          d = dateFormat.parse(date);
          c.setTime(d);
          c.add(Calendar.DATE, day);
      } catch (ParseException e) {
          e.printStackTrace();
      }
      return dateFormat.format(c.getTime());
   }
  
  //将15位身份证转为18位
  public static String fixPersonIDCode(String personIDCode)
  {
    String retIDCode = "";
    if ( personIDCode == null || personIDCode.trim().length() != 15 ) {
      return personIDCode;
    }
    String id17 = personIDCode.substring(0,6) + "19" + personIDCode.substring(6,15);  //15为身份证补'19'

    char[] code = {'1','0','X','9','8','7','6','5','4','3','2'};  //11个
    int[] factor={0, 2,4,8, 5,10,9,7, 3,6,1,2, 4,8,5,10, 9,7}; //18个;
    int[] idcd = new int[18];
    int     i;
    int     j;
    int     sum;
    int     remainder;

    for (i=1; i<18; i++)
    {
      j = 17 - i ;
      idcd[i] = Integer.parseInt(id17.substring(j, j+1));
    }

    sum = 0;
    for (i=1; i<18; i++)
    {
      sum = sum  + idcd[i] * factor[i];
    }
    remainder = sum%11;
    String lastCheckBit = String.valueOf(code[ remainder ]);
    return id17 + lastCheckBit;
    }

  public static boolean isDouble(String str)
  {
	  Pattern p=Pattern.compile("[-\\+]?\\d+(\\.)?\\d{0,2}");
	  return p.matcher(str).matches();
  }
  
  public static String delCtrlChar(String str)
  {
	  
	  return str.replaceAll("　","").replaceAll(" ","").replaceAll("\n","").replaceAll(",","").replaceAll("\"￥\"","").replaceAll("\\$","");
  }
  
  public static String delReturn(String str)
  {
	  
	  return str.replaceAll("\r\n","").replaceAll("\n","");
  }

  public  int getDaysBetween (String beginDate, String endDate) throws ParseException

  {
       SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
       Date bDate = format.parse(beginDate);
       Date eDate = format.parse(endDate);
       Calendar d1 = new GregorianCalendar();
       d1.setTime(bDate);
       Calendar d2 = new GregorianCalendar();
      d2.setTime(eDate);
      int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
      int y2 = d2.get(Calendar.YEAR);
      if (d1.get(Calendar.YEAR) != y2)
      {
             d1 = (Calendar) d1.clone();
            do   {
                        days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);//得到当年的实际天数
                       d1.add(Calendar.YEAR, 1);
          }    while (d1.get(Calendar.YEAR) != y2);
     }

      return days;

 }
  
  /**
  20
   * 将cookie封装到Map里面
  21
   * @param request
  22
   * @return
  23
   */
  private  Map ReadCookieMap(HttpServletRequest request){ 
	      Map cookieMap = new HashMap();
	      Cookie[] cookies = request.getCookies();
	      //System.out.println("cookies:"+cookies.length);
	      if(null!=cookies){
	          for(int i=0;i<cookies.length;i++){
	        	  Cookie cookie=cookies[i];
	              cookieMap.put(cookie.getName(), cookie);
	          }
	      }
	      return cookieMap;
  }
  
  /**
  02
   * 根据名字获取cookie
  03
   * @param request
  04
   * @param name cookie名字
  05
   * @return
  06
   */
  public  Cookie getCookieByName(HttpServletRequest request,String name){
      Map cookieMap = ReadCookieMap(request);
      if(cookieMap.containsKey(name)){
          Cookie cookie = (Cookie)cookieMap.get(name);
          return cookie;
      }else{
          return null;
      }  
  }
  public static void main(String[] args) {
    PubFunHC pubFunHC1 = new PubFunHC();
  }



}
