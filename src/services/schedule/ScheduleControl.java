package services.schedule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import services.schedule.schedules.*;
public class ScheduleControl extends HttpServlet{

	public void init() throws ServletException
	  {
	  	//∂Ã–≈∂”¡–
		System.out.println("info :server init started");
		SendTextMessageSchedule g = new SendTextMessageSchedule();
	    g.start();
	    SysSchedule g1 = new SysSchedule();
	    g1.start();
	   
	  }
}
