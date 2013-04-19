package services.schedule.schedules;

import java.util.Hashtable;
import java.util.Vector;

import services.db.DBFuncs;
import services.pub.HPub;
import services.comm.*;

public class SendTextMessageSchedule extends BaseSchedule {
	private int msgsPerSec = 1;
	private String smsServer = "182.47.30.227";
	//private String smsServer = "182.47.232.15";
	private int smsPort = 6781;
	public void setSmsServer()
	{
		Hashtable scheduleHash=this.getScheduleHash();
		String addInfo=(String)scheduleHash.get("ADDINFO");
		smsServer=addInfo;
		System.out.println("smsServer:"+smsServer);
	}
	public String SendSms()
	  {
	     try{
	    	 	setSmsServer();
	    	 	int msgsPerTime = msgsPerSec*50;
	    	 	
	    	 	HPub hpub = new HPub();
				String curDate = hpub.getDate("yyyyMMdd", 0);
				
	    	 	/*取短信*/
	    	 	DBFuncs d1 = new DBFuncs();
				String sql = "select TEXTMSG_ID,MOBILE,TEXTCONTENT,EXPIREDATE from pub_Textmsgque where flag='0' " +
					" fetch first "+msgsPerTime+" rows only";
				System.out.println("sql:"+sql);
				Vector resultV=d1.queryTableAll(sql);
				if(resultV==null)return "noinsert";
				for(int i=0;i<resultV.size();i++)
				{
					Hashtable tmph = new Hashtable();
					tmph=(Hashtable)(resultV.elementAt(i));
					SendSingleSms(tmph,curDate);
					sleep(1000/msgsPerSec);
				}
				return "success";

	        }//end of try
	       catch (Exception ex)
	       	{
	            ex.printStackTrace();
	            System.out.println("SendTextMessageSchedule ex.getMessage:"+ex.getMessage());
	            return ex.getMessage();
	        }
	  }//end of run
	
	/*逐条发送*/
	public void SendSingleSms(Hashtable tmph,String curDate) throws Exception
	  {
		 String flag="0";
		 String TEXTMSG_ID = (String)tmph.get("TEXTMSG_ID");
	     try{
	    	 	String EXPIREDATE = ((String)tmph.get("EXPIREDATE")).trim();
	    	 	if(EXPIREDATE.length()==8&&Integer.parseInt(curDate.trim())>=Integer.parseInt(EXPIREDATE.trim()))
	    	 	{
	    	 		flag="3";
	    	 		return;
	    	 	}
	    	 	String MOBILE = (String)tmph.get("MOBILE");
	    	 	if(MOBILE.trim().length()!=11)
	    	 	{
	    	 		flag="4";
	    	 		return;
	    	 	}
	    	 	String TEXTCONTENT = (String)tmph.get("TEXTCONTENT");
	    	 	while (MOBILE.length()<30)MOBILE=MOBILE+" ";
	    	 	String sendString =MOBILE+TEXTCONTENT;
	    	 	System.out.println("send:"+sendString);
	    	 	sendString=new String(sendString.trim().getBytes("GBK"),"ISO8859_1");
	    	 	String sendLen = sendString.length()+"";
	    	 	while (sendLen.length()<8)sendLen="0"+sendLen;
	    	 	SendComm s = new SendComm(smsServer,smsPort);
	    	 	String rntString = s.Send(sendLen+sendString+"                             ");
	    	 	rntString=new String(rntString.trim().getBytes("ISO8859_1"),"GBK");
	    	 	System.out.println("rntString:"+rntString);
	    	 	if(rntString.substring(0,4).equals("0000"))flag="2";

	        }//end of try
	       catch (Exception ex)
	       	{

	            ex.printStackTrace();
	            throw ex;   
	        }
	       finally
			{
				try
				{
					DBFuncs d1 = new DBFuncs();
					String updStr="update pub_textmsgque set flag='"+flag+"' where TEXTMSG_ID='"+TEXTMSG_ID+"'";
					d1.execSql(updStr);
				}
				catch(Exception e1)
				{
					e1.printStackTrace();
				}

			}
	  }//end of run
}
