package services.schedule.schedules;

import java.util.Hashtable;

public class BaseSchedule extends Thread{
	private Hashtable scheduleHash=new Hashtable();
	public void setArgs(Hashtable sH)
	{
		this.scheduleHash=sH;
	}
	public Hashtable getScheduleHash()
	{
		return this.scheduleHash;
	}
}
