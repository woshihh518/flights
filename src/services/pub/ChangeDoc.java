package services.pub;

import java.io.File;
import java.io.IOException;
import com.jacob.com.*;
import com.jacob.activeX.*;

public class ChangeDoc {

	public String  change(String dir,String Filename)
	{
		try
		{
		 //启动word
		String type = Filename.substring(Filename.length()-3,Filename.length());
		String dir1 = dir + Filename.substring(0,8)+"\\";
		  //要转换的word文件
		String inFile = dir+Filename; 
		  //目标文件
		String tpFilename = Filename.substring(0,Filename.length()-4)+".htm";
	  String tpFile = dir+tpFilename;
	  System.out.println("inFile:"+inFile);
	  System.out.println("tpFile:"+tpFile);
	  if(type.equals("doc")||type.equals("DOC"))wordToHtml(inFile,tpFile);  
	  else excelToHtml(inFile,tpFile);

	  	return tpFilename;
		}
		catch( Exception e )
		{
		 e.printStackTrace();
		}
		return null;
	}
	//word转html
	void wordToHtml(String inFile, String tpFile)  throws  Exception
	{ 
		
		ActiveXComponent app = new ActiveXComponent("Word.Application");

	try
	{
	 //设置word不可见
	 app.setProperty( "Visible", new Variant(false) );
	  Object docs = app.getProperty( "Documents" ).toDispatch();
	 //打开word文件
	  Object doc = Dispatch.invoke(
	  (Dispatch) docs ,
	  "Open" ,
	  Dispatch.Method ,
	  new Object[]
	  {
	   inFile ,
	   new Variant(false) ,
	   new Variant(true)
	  } ,
	  new int[1]
	        ).toDispatch();
	 
	 //作为html格式保存到临时文件
	 Dispatch.invoke(
	  (Dispatch) doc ,
	  "SaveAs" ,
	  Dispatch.Method ,
	  new Object[]
	  {
	   tpFile,new Variant( 8 )
	  } ,
	  new int[1]
	       );
	 
	 Variant f = new Variant( false );
	 Dispatch.call( (Dispatch) doc , "Close" , f );
	}
	catch( Exception e )
	{
	 e.printStackTrace();
	}
	finally
	{
	 app.invoke( "Quit" , new Variant[]{} );

	}
	}   


	// EXCEL转HTML  

	 void excelToHtml(String xlsfile, String htmlfile)   
	{   
	    ActiveXComponent app = new ActiveXComponent("Excel.Application"); // 启动word   
	    try  
	    {   
	        app.setProperty("Visible", new Variant(false));   
	        Dispatch excels = app.getProperty("Workbooks").toDispatch();   
	        Dispatch excel = Dispatch.invoke(   
	                excels,   
	                "Open",   
	                Dispatch.Method,   
	                new Object[] { xlsfile, new Variant(false),   
	                        new Variant(true) }, new int[1]).toDispatch();   
	        Dispatch.invoke(excel, "SaveAs", Dispatch.Method, new Object[] {   
	                htmlfile, new Variant(44) }, new int[1]);   
	        Variant f = new Variant(false);   
	        Dispatch.call(excel, "Close", f);   
	    }   
	    catch (Exception e)   
	    {   
	        e.printStackTrace();   
	    }   
	    finally  
	    {   
	        app.invoke("Quit", new Variant[] {});   
	    }   
	}
}
