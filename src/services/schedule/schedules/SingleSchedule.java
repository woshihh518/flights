package services.schedule.schedules;

import java.lang.reflect.Method;
import java.util.Vector;
import java.util.Hashtable;

import services.db.DBFuncs;
import services.log.HLog;
import services.pub.HPub;

public class SingleSchedule extends Thread{
	private String scheduleId="";
	private String scheduleClass="";
	private String scheduleMethod="";
	private Hashtable scheduleHash=new Hashtable();
	private String finishflag="3";
	private String remark="";
	private HLog hlog = new HLog(); // 数据库操作实例
	public void setArgs(String id,String className ,String method ,Hashtable sH)
	{
		this.scheduleId=id;
		this.scheduleClass=className;
		this.scheduleMethod=method;
		this.scheduleHash=sH;
	}
	public void run()
	{
		try
		{
			System.out.println("run single schedule start:"+this.scheduleId);
			Class t1=Class.forName(this.scheduleClass);
			Object o =t1.newInstance();
			Method m=t1.getMethod("setArgs", new Class[]{Hashtable.class});
			Object[] realArgs = new Object[1]; 
			realArgs[0]=scheduleHash;
			m.invoke(o,realArgs);
			Method m1=t1.getMethod(this.scheduleMethod, new Class[0]);
			String s1 = (String)m1.invoke(o,new Object[0]);
			hlog.wTransLog(0,this.getClass().getName(), "run schedule",
					"id:"+this.scheduleId+",result:"+s1, "000000", "000000");
			if(s1.equals("success"))finishflag="2";
			if(s1.equals("noinsert"))finishflag="x";
			remark=s1;
		}
		catch(Exception e)
		{
			System.out.println("run schedule error,id:"+this.scheduleId);
			e.printStackTrace();
		}
		finally
		{
			try
			{
				DBFuncs d1 = new DBFuncs();
				String updStr="update pub_scheduleinfo set finish_flag='"+finishflag+"' where schedule_id='"+this.scheduleId+"'";
				Vector sqlV = new Vector();
				sqlV.add(updStr);
				if(!finishflag.equals("x"))
				{
					HPub hpub = new HPub();
					String time = hpub.getDate("yyyyMMddHHmmss", 0);
					String insStr="insert into pub_schedulejnl values " +
					"('"+this.scheduleId+"','"+time+"','"+time.substring(0, 4)+"','"+time.substring(4, 6)+"','"+time.substring(6,8)+"','"+time.substring(8,10)+"'," +
					"'"+time.substring(10, 12)+"','"+finishflag+"','"+remark+"','0')";
					sqlV.add(insStr);
				}				
				d1.execSqlBatch(sqlV);
			}
			catch(Exception e1)
			{
				e1.printStackTrace();
			}

		}
	}

}
