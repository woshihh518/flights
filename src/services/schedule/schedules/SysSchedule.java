package services.schedule.schedules;

import java.lang.reflect.Method;
import java.util.Hashtable;
import java.util.Vector;
import java.util.Calendar;
import services.db.DBFuncs;
import services.pub.HPub;

public class SysSchedule extends Thread{
	public void  run()
	  {
	     try{
	    	 while(true)
	          {
	    		 sleep(60*1000);
	    		 loadSchedule();
	          }
	        }//end of try
	       catch (Exception ex)
	       	{
	            ex.printStackTrace();
	        }
	  }//end of run
	
	/*读取计划任务*/
	public void loadSchedule() throws Exception
	{
		
		/*取所有的计划任务*/
		DBFuncs d1 = new DBFuncs();
		String sql = "select * from pub_Scheduleinfo where flag='1' and finish_flag!='1'";
		System.out.println("sql:"+sql);
		Vector resultV=d1.queryTableAll(sql);
		if(resultV==null)return;
		
		for(int i=0;i<resultV.size();i++)
		{
			Hashtable tmph = new Hashtable();
			tmph=(Hashtable)(resultV.elementAt(i));
			if(checkRunCondition(tmph))
			{
				String scheduleId = (String)(tmph.get("SCHEDULE_ID"));
				String updSql = "update pub_scheduleinfo set finish_flag='1' where schedule_id='"+scheduleId+"'";
				d1.execSql(updSql);
				String className = (String)(tmph.get("SCHEDULE_CLASS"));
				String methodName = (String)(tmph.get("SCHEDULE_METHOD"));
				Class t1=Class.forName("services.schedule.schedules.SingleSchedule");
				Object o =t1.newInstance();
				Method m=t1.getMethod("setArgs", new Class[]{String.class,String.class,String.class,Hashtable.class});
				Object[] realArgs = new Object[4]; 
				realArgs[0]=scheduleId;
				realArgs[1]=className;
				realArgs[2]=methodName;
				realArgs[3]=tmph;
				m.invoke(o,realArgs);
				Method m1=t1.getMethod("start", new Class[0]);
				m1.invoke(o,new Object[0]);
			}
				
		}
		
	}
	
	/*检查计划在当前时间是否要运行*/
	public boolean checkRunCondition(Hashtable tmph)
	{
		try
		{
			/*取当前的时间*/
			Calendar c = Calendar.getInstance();
			int curYear=c.get(Calendar.YEAR);
			int curMonth= c.get(Calendar.MONTH) + 1;
			int curDay= c.get(Calendar.DAY_OF_MONTH);
			int curHour=c.get(Calendar.HOUR_OF_DAY);//中国属于东八区，时间加8小时
			int curMinute=c.get(Calendar.MINUTE);
			int curWeekday=c.get(Calendar.DAY_OF_WEEK)-1;
			if(curWeekday<=0)curWeekday=7;
			System.out.println("curYear:"+curYear);
			System.out.println("curMonth:"+curMonth+":"+chechSingleCondition(tmph,"MONTH",curMonth));
			System.out.println("curDay:"+curDay+":"+chechSingleCondition(tmph,"DAY",curDay));
			System.out.println("curHour:"+curHour+":"+chechSingleCondition(tmph,"HOUR",curHour));
			System.out.println("curMinute:"+curMinute+":"+chechSingleCondition(tmph,"MINUTE",curMinute));
			System.out.println("curWeekday:"+curWeekday+":"+chechSingleCondition(tmph,"WEEKDAY",curWeekday));
			
			String FREQUENCY = (String)tmph.get("FREQUENCY");
			String scheduleId = (String)(tmph.get("SCHEDULE_ID"));
			if(tmph==null)return false;
			if(chechSingleCondition(tmph,"MONTH",curMonth)
					&&chechSingleCondition(tmph,"DAY",curDay)
					&&chechSingleCondition(tmph,"WEEKDAY",curWeekday)
					&&chechSingleCondition(tmph,"HOUR",curHour)
					&&chechSingleCondition(tmph,"MINUTE",curMinute))
			{}
			else return false;
			if(FREQUENCY.equalsIgnoreCase("day"))
			{
					HPub hpub = new HPub();
					String time = hpub.getDate("yyyyMMdd", 0);
					DBFuncs d1 = new DBFuncs();
					String sql = "select * from pub_Schedulejnl where finish_flag='2' and schedule_Id='"+scheduleId+"'" +
					" and year||month||day='"+time+"' ";
					System.out.println("sql:"+sql);
					Vector resultV=d1.queryTableAll(sql);
					if(resultV!=null)return false;
			}
			return true;
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
		
	}
	
	/*检查单个条件*/
	public boolean chechSingleCondition(Hashtable tmph,String condition,int curValue) throws Exception
	{
		
		if(tmph.get(condition)==null)return false;
		String conValue=((String)tmph.get(condition)).trim();
		if(conValue.equals("*"))return true;
		/*1*/
		else if(conValue.indexOf(",")!=-1)
		{
			String[] conValues = conValue.split(",");
			for(int i=0;i<conValues.length;i++)
			{
				if(Integer.parseInt(conValues[i])==curValue)return true;
			}
			return false;
		}
		else if(conValue.indexOf("-")!=-1)
		{
			String[] conValues = conValue.split("-");
			if(conValues.length!=2)return false;
			int conMin=0;
			if(conValues[0]!=null&&!conValues[0].trim().equals(""))conMin=Integer.parseInt(conValues[0]);
			int conMax=999;
			if(conValues[1]!=null&&!conValues[1].trim().equals(""))conMax=Integer.parseInt(conValues[1]);
			if(conMin<=curValue&&conMax>=curValue)return true;
			else return false;
			
		}
		else
		{
			int conInt = Integer.parseInt(conValue);
			if(conInt==curValue)return true;
			else return false;
		}
		
	}
}
