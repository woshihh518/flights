package services.comm;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class SendComm {
	private String hostName="";
	private int port = 0;
	private int timeout = 200000;
	private int HEADER_LENGHT_SERVER = 8; //服务端返回数据信息头长度
	private boolean LENGTH_INCLUDE_HEAD = true; //服务端返回数据信息头长度
	public SendComm(String host,int port)
	{
		this.hostName=host;
		this.port=port;
	}
	public SendComm(String host,int port,int headlen,boolean includeHead)
	{
		this.hostName=host;
		this.port=port;
		this.HEADER_LENGHT_SERVER=headlen;
		this.LENGTH_INCLUDE_HEAD=includeHead;
	}
	public String Send(String message) throws Exception
	  {
	  	  Socket clientSocket=null;
	      DataInputStream inbound = null;
	      DataOutputStream outbound = null;
	      try
	       {
	       	    clientSocket = new Socket(hostName,port);
	            // 得到IO流
	            inbound = new DataInputStream(clientSocket.getInputStream());
	            outbound = new DataOutputStream(clientSocket.getOutputStream());
	            outbound.writeBytes(message);//发送到ICS
	            clientSocket.setSoTimeout(200000);
	            inbound = new DataInputStream(clientSocket.getInputStream());
	            String ICSresponse = inbound.readLine();
	            return ICSresponse;

	       } // end of try
	       catch (Exception e)
	  	   {
	            e.printStackTrace();
	            throw e;
	            //return "9999";
	       }
	       finally
	       {
	    	   // 清除
	          try{
	             if(outbound!=null)outbound.close();
	             if(inbound!=null)inbound.close();
	             if(clientSocket!=null)clientSocket.close();
	             }
	             catch (Exception e)
	  	         {
	            	 e.printStackTrace();
	            	 throw e;
	             }
	       } // end of finally
	  }
}
