<%@ page contentType="text/html; charset=gb2312" 
import="services.pub.*,services.textmsg.*"%>

<% request.setCharacterEncoding("gb2312");%>
<%

    SendTextmsg s1 = new SendTextmsg();
		String usr = request.getParameter("usr");
		String pwd = request.getParameter("pwd");
		String tel = request.getParameter("tel");
		String msg = request.getParameter("msg");
		msg=msg.replaceAll( "\r\n", " " );
		msg=msg.replaceAll( "\n", " " );
		String addinfo="username:"+usr+".";
		System.out.println("usr:"+usr);
		System.out.println("pwd:"+pwd);
		System.out.println("tel:"+tel);
		System.out.println("msg:"+msg+".xxxmsgend");
		s1.SendSingleSms(tel,msg,addinfo);
		System.out.println("tel:"+tel);
%>
