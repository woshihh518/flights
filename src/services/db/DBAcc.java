package services.db;

import java.sql.*;

import javax.naming.*;


import java.util.*;

import services.log.HLog;

//新的数据库连接类，执行各种数据库操作
public class DBAcc {

	Context ctx;
	javax.sql.DataSource ds;
	Connection conn;
	boolean isSucc;
	String msgStr = "交易成功";
	//各种参数保存在jndi.properties里面
	private static ReadCFG cfg = new ReadCFG();
	private static String dbdriver = cfg.getvalue("dbdriver");
	private static String dburl = cfg.getvalue("dburl");
	private static String dbuser = cfg.getvalue("dbuser");
	private static String dbpassword = cfg.getvalue("dbpassword");
	private static String workdir = cfg.getvalue("workdir");
	private static String charsetConvertFlag = "false";
	private static String charsetConvert = "";
	private HLog hlog = new HLog(); // 数据库操作实例
	
	public DBAcc(){
		//JNDI = cfg.getValue("JNDI");
		ctx = null;
        ds = null;
        conn = null;   
        isSucc=true;
	}
	
	
	
	//建立连接
	public Connection getConnection()
    {
        conn = null;
        
            try
            {
            	//com.ibm.db2.jcc.DB2Driver db2Driver =
      	        //  (com.ibm.db2.jcc.DB2Driver)
      	        //    Class.forName("com.ibm.db2.jcc.DB2Driver").newInstance();
            	//System.out.println(dbdriver);
            	Class.forName(dbdriver);
      	        conn = DriverManager.getConnection(dburl,dbuser,dbpassword);
            }
            
            catch(Exception e)
            {
                e.printStackTrace();
                hlog.wExceptionLog(this.getClass().getName(), "getConnection",e,"0000","0000");
                //setMsg("Could not initalize DataSource" + e);
                //setSucc(false);
                return null;
            }
        
        return conn;
    }
	
	//关闭连接
	protected void CloseConnection(java.sql.Connection conn){
		try{
			conn.close();
		}catch(SQLException e){
			System.out.println("couldn't close conn.");
			hlog.wExceptionLog(this.getClass().getName(), "CloseConnection",e,"0000","0000");
		}
	}
	
	//查询
	public Vector doQuery(String sqlStr)
    {
		isSucc=true;
        Vector result;
        result = new Vector();
        conn = getConnection();
        if(!isSucc)
            return null;
        try {
			result = doQuery(conn, sqlStr);
			conn.close();
			return result;
		} catch(Exception e){
			hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e2) {
				hlog.wExceptionLog(this.getClass().getName(), "doQuery",e2,"0000","0000");
			}
			return null;
		}
        
    }
    
	
	//查询
    public Vector doQuery(Connection conn, String sqlStr)
    {
        if(conn == null)
            return null;
        Vector result = new Vector();
        try
        {
        	//hlog.wTransLog(0,this.getClass().getName(), "doQuery",
			//		sqlStr, "000000", "000000");
            Statement stmt = conn.createStatement();
            if(charsetConvertFlag.equals("true"))
            sqlStr = new String(sqlStr.getBytes(charsetConvert.split(":")[0]),charsetConvert.split(":")[1]);
            System.out.println("xx3");
            ResultSet rs = stmt.executeQuery(sqlStr);
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();
        	long time = System.currentTimeMillis();

            Hashtable row;
            String columnType[]=new String [num+1];
            for(int i = 1; i <= num; i++)
            {
            	 columnType[i] = rsmd.getColumnTypeName(i);
            }
            for(; rs.next(); result.add(row))
            {
            	//System.out.println("xx2:"+(System.currentTimeMillis()-time));
            	//time=System.currentTimeMillis();
                
            	row = new Hashtable();
                for(int i = 1; i <= num; i++)
                {
                	//System.out.println("xx3:"+(System.currentTimeMillis()-time));
                	//time=System.currentTimeMillis();
                    //String columnType = rsmd.getColumnTypeName(i);
                    //System.out.println("xx31:"+(System.currentTimeMillis()-time));
                	//time=System.currentTimeMillis();
                    String key = rsmd.getColumnName(i);
                    //System.out.println("xx4:"+(System.currentTimeMillis()-time));
                	//time=System.currentTimeMillis();
                    String value = null;
                    if(columnType[i].equalsIgnoreCase("CLOB"))
                    {
                        Clob tmpClob = rs.getClob(i);
                        if(tmpClob != null)
                            value = tmpClob.getSubString(1L, (int)tmpClob.length());
                        else
                            value = null;
                    } else
                    {
                        value = rs.getString(i);
                    }
                    if(value == null)
                        value = "";
                    //System.out.println("xx5:"+(System.currentTimeMillis()-time));
                	//time=System.currentTimeMillis();
                    //if(key.equals("REMARK"))System.out.println("xx5:"+value);
                    if(charsetConvertFlag.equals("true"))
                    row.put(key, new String(value.getBytes(charsetConvert.split(":")[1]),charsetConvert.split(":")[0]).trim());
                    else
                    	row.put(key, value.trim());
                	
                    
                }
                

            }

            rs.close();
            stmt.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			setMsg(e.toString() + "SQL:" + sqlStr);
			setSucc(false);
            return null;
        }
        return result;
    }
    
    //分页查询
    public Vector doQuery(String sqlStr, int rowStart, int pagesize)
    {
        conn = getConnection();
        Vector result = new Vector();
        try
        {
        	//hlog.wTransLog(0,this.getClass().getName(), "doQuery",
			//		sqlStr, "000000", "000000");
            Statement stmt = conn.createStatement(1004, 1007);
            if(charsetConvertFlag.equals("true"))
                sqlStr = new String(sqlStr.getBytes(charsetConvert.split(":")[0]),charsetConvert.split(":")[1]);
    			
            ResultSet rs = stmt.executeQuery(sqlStr);
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();
            if(rowStart == 0)
            {
                rs.first();
                rs.beforeFirst();
            } else
            {
                rs.absolute(rowStart);
            }
            String columnType[]=new String [num+1];
            
            for(int i = 1; i <= num; i++)
            {
            	 columnType[i] = rsmd.getColumnTypeName(i);
            }
            for(int count = 1; rs.next(); count++)
            {
                if(count > pagesize)
                    break;
                Hashtable row = new Hashtable();
                for(int i = 1; i <= num; i++)
                {
                    //String columnType = rsmd.getColumnTypeName(i);
                    String key = rsmd.getColumnName(i);
                    String value = null;
                    if(columnType[i].equalsIgnoreCase("CLOB"))
                    {
                        Clob tmpClob = rs.getClob(i);
                        if(tmpClob != null)
                            value = tmpClob.getSubString(1L, (int)tmpClob.length());
                        else
                            value = null;
                    } else
                    {
                        value = rs.getString(i);
                    }
                    if(value == null)
                        value = "";
                    if(charsetConvertFlag.equals("true"))
                        row.put(key, new String(value.getBytes(charsetConvert.split(":")[1]),charsetConvert.split(":")[0]).trim());
                        else
                        	row.put(key, value.trim());
                }

                result.add(row);
            }

            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {
        	hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			setMsg(e.toString() + "SQL:" + sqlStr);
			setSucc(false);
            try
            {
                if(conn != null)
                    conn.close();
            }
            catch(Exception e2)
            {
                System.out.println(e2);
            }
            return null;
        }
        return result;
    }
    
    //update
    public boolean executeUpdate(String sqlStr) {
    	isSucc=true;
		conn = getConnection();
		if (!isSucc)
			return false;
		try {
			isSucc = executeUpdate(conn, sqlStr);
			conn.close();
			return isSucc;
		}catch(Exception e){
		    hlog.wExceptionLog("ConnectionFactory",e,"0000","0000");
			try {
				if (conn != null)
					conn.close();
			} catch (Exception e1) {
				hlog.wExceptionLog("ConnectionFactory",e1,"0000","0000");
			}
			return false;
		}
	}

	//update
	public boolean executeUpdate(Connection conn, String sqlStr) {
		if (conn == null) {
			//setMsg("数据库连接为空！");
			setSucc(false);
			return false;
		}
		try {
			Statement stmt = conn.createStatement();
			//专为fims处理，后台informix支持ISO8859_1编码
			//System.out.println("updatesql charset---"+StringUtil.getCharset(sqlStr));
			//if(!(StringUtil.getCharset(sqlStr).equals("GBK"))){
				//sqlStr = new String(sqlStr.getBytes("GB2312"),"ISO8859_1");
			//}
			//hlog.wTransLog(0,this.getClass().getName(), "doQuery",
			//		sqlStr, "000000", "000000");
			if(charsetConvertFlag.equals("true"))
                sqlStr = new String(sqlStr.getBytes(charsetConvert.split(":")[0]),charsetConvert.split(":")[1]);
    		
			stmt.executeUpdate(sqlStr);
			stmt.close();
			setSucc(true);
			return true;
		} catch (SQLException e) {
			hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			setMsg(e.toString() + "SQL:" + sqlStr);
			setSucc(false);
			return false;
		}catch(Exception e1){
			hlog.wExceptionLog(this.getClass().getName(), "doQuery",e1,"0000","0000");
			setMsg(e1.toString() + "SQL:" + sqlStr);
			setSucc(false);
			return false;
		}
	}
	
	/**
	 * Method：public boolean execSysSql(Vector sqlVec)
	 * Function:
	 * 		执行import sql
	 * 输入参数：
	 * 			sql：含有需要执行的sql
	 * 输出参数:  执行结果 boolean
	 * 
	 */
	public boolean execDB2import(String sql)
	{
		int rows_read;
	    int rows_skipped;
	    int rows_loaded;
	    int rows_rejected;
	    int rows_deleted;
	    int rows_committed;

	    String msg_retrieval = null;
	    String msg_removal = null;
	    String sqlcode = null;
	    String msg = null;
	    
	    CallableStatement callStmt1 = null;
	    ResultSet rs1 = null;
	    PreparedStatement stmt1 = null;
	    ResultSet rs2 = null;
	    CallableStatement callStmt2 = null;
	    
	    isSucc=true;
		conn = getConnection();
		if (!isSucc)
			return false;
		try {
			String sql1 = "CALL SYSPROC.ADMIN_CMD(?)";
	        callStmt1 = conn.prepareCall(sql1);

	        callStmt1.setString(1, sql);
	        hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
					sql, "000000", "000000");
			System.out.println("CALL ADMIN_CMD('" + sql + "')");
		       
	        // execute export by calling ADMIN_CMD
	        callStmt1.execute();
	        rs1 = callStmt1.getResultSet();
	        // retrieve the resultset  
	     // retrieving the resultset  
	        if( rs1.next())
	        { 
	          // retrieve the no of rows read
	          rows_read = rs1.getInt(1);
	          // retrieve the no of rows skipped
	          rows_skipped = rs1.getInt(2);
	          // retrieve the no of rows loaded
	          rows_loaded = rs1.getInt(3);
	          // retrieve the no of rows rejected
	          rows_rejected = rs1.getInt(4);
	          // retrieve the no of rows deleted
	          rows_deleted = rs1.getInt(5);
	          // retrieve the no of rows committed
	          rows_committed = rs1.getInt(6);

	          // retrieve the select stmt for message retrival 
	          // containing SYSPROC.ADMIN_GET_MSGS
	          msg_retrieval = rs1.getString(7);
	  
	          // retrive the stmt for message cleanup
	          // containing CALL of SYSPROC.ADMIN_REMOVE_MSGS
	          msg_removal = rs1.getString(8);
	      
	          // Displaying the resultset
	          System.out.print("\nTotal number of rows read      : ");
	          System.out.println(rows_read);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows read  : " + rows_read, "000000", "000000");
	          System.out.print("Total number of rows skipped   : ");
	          System.out.println( rows_skipped);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows skipped  : " + rows_skipped, "000000", "000000");
	          System.out.print("Total number of rows loaded    : ");
	          System.out.println(rows_loaded);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows rows_loaded  : " + rows_loaded, "000000", "000000");
	          System.out.print("Total number of rows rejected  : "); 
	          System.out.println(rows_rejected);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows rows_rejected  : " + rows_rejected, "000000", "000000");
	          System.out.print("Total number of rows deleted   : "); 
	          System.out.println(rows_deleted);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows deleted  : " + rows_deleted, "000000", "000000");
	          System.out.print("Total number of rows committed : "); 
	          System.out.println(rows_committed);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Total number of rows committed  : " + rows_committed, "000000", "000000");
	          System.out.print("SQL for retrieving the messages: "); 
	          System.out.println(msg_retrieval); 
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "SQL for retrieving the messages: " + msg_retrieval, "000000", "000000");
	          System.out.print("SQL for removing the messages  : "); 
	          System.out.println(msg_removal);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "SQL for removing the messages  : " + msg_removal, "000000", "000000");
	        } 
	      
	        stmt1 = conn.prepareStatement(msg_retrieval);
	        System.out.println("\n" + "Executing " + msg_retrieval);  

	        // message retrivel 
	        rs2 = stmt1.executeQuery();
		
	        // retrieving the resultset
	        while(rs2.next())
	        {
	          // retrieving the sqlcode
		    sqlcode = rs2.getString(1);
	      
	          //retrieving the error message
	          msg = rs2.getString(2);

	          System.out.println("Sqlcode : " +sqlcode);
	          System.out.println("Msg     : " +msg);
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Sqlcode  : " + sqlcode, "000000", "000000");
	          hlog.wTransLog(0,this.getClass().getName(), "execSysSql",
	        		  "Msg  : " + msg, "000000", "000000");
	        }

	        System.out.println("\n Executing " + msg_removal);
	        callStmt2 = conn.prepareCall(msg_removal);

	        // executing the message retrivel
	        callStmt2.execute();      
	           
	        
			return isSucc;
		}
		catch(Exception e){
		    hlog.wExceptionLog("ConnectionFactory",e,"0000","0000");
			return false;
		}
		finally
	      {
	        try
	        {
	          // close the statements 
	          if(callStmt1!=null)callStmt1.close();
	          if(callStmt2!=null)callStmt2.close();
	          if(stmt1!=null)stmt1.close();

	          // close the resultsets
	          if(rs1!=null)rs1.close();
	          if(rs2!=null)rs2.close();
	     
	          // roll back any changes to the database made by this sample
	          //conn.rollback();

	          // close the connection                                   
	          conn.close();
	        }
	        catch (Exception x)
	        { 
	        	hlog.wExceptionLog("ConnectionFactory",x,"0000","0000");
	        }
	      }
	}
	
//	事务处理
    public boolean executeBatchUpdate(Vector sqlVec) {
    	String sqlNow="";
    	isSucc=true;
		conn = getConnection();
		if (!isSucc)
			return false;
		try {
			if (conn == null) {
				setMsg("数据库连接为空！");
				setSucc(false);
				return false;
			}
			conn.setAutoCommit(false);
			Statement stmt = conn.createStatement();
			//System.out.println("debug1");
			for(int i =0;i<sqlVec.size();i++)
			{
				//System.out.println("debug1");
				sqlNow = (String)sqlVec.elementAt(i);  
				//hlog.wTransLog(0,this.getClass().getName(),"executeBatchUpdate",
				//		sqlNow, "000000", "000000");
				if(charsetConvertFlag.equals("true"))
					sqlNow = new String(sqlNow.getBytes(charsetConvert.split(":")[0]),charsetConvert.split(":")[1]);
	    		
				if(sqlNow!=null)stmt.addBatch(sqlNow);
				System.out.println("sqlNow:"+sqlNow);
			}
			//System.out.println("debug3");
			stmt.executeBatch();
			//System.out.println("debug4");
			conn.commit();
			//System.out.println("debug5");
			stmt.close();
			setSucc(true);
			return true;
		}catch(SQLException e){
			e.printStackTrace();
		    hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			try {
				
					conn.rollback();
			} catch (SQLException e1) {
				hlog.wExceptionLog(this.getClass().getName()," rollback false:",e1,"0000","0000");
			}
			setMsg("批量执行sql发生错误!");
			return false;
		}
		catch(Exception e){
			e.printStackTrace();
		    hlog.wExceptionLog(this.getClass().getName(), "doQuery",e,"0000","0000");
			try {
				
					conn.rollback();
			} catch (SQLException e1) {
				hlog.wExceptionLog(this.getClass().getName()," rollback false:",e1,"0000","0000");
			}
			setMsg("批量执行sql发生错误!");
			return false;
		}
		finally
		{
			try {
				  conn.setAutoCommit(true);
				  conn.close();
		    } catch (SQLException e1) {
			hlog.wExceptionLog(this.getClass().getName()," rollback false:",e1,"0000","0000");
		    }
		}
	}

	
	
    //	返回表结构
    public Vector getFields(String sql)
    {
    	isSucc=true;
        conn = getConnection();
        if (!isSucc)
			return null;
        Vector result = new Vector();
        try
        {
        	if(sql.toUpperCase().indexOf("WHERE")==-1)
        	{
        		sql=sql+" where 1=2";
        	}
        	else
        	{
        		sql=sql+" and 1=2";
        	}
            Statement stmt = conn.createStatement(1004, 1007);
            ResultSet rs = stmt.executeQuery(sql);
            ResultSetMetaData rsmd = rs.getMetaData();
            int num = rsmd.getColumnCount();
            
           for(int i = 1; i <= num; i++)
                {
                    String key = rsmd.getColumnName(i);
                    String columnType = rsmd.getColumnTypeName(i);
                    //System.out.println(key+":"+columnType);
                    result.add(key+"~"+columnType);
                }

                

            rs.close();
            stmt.close();
            conn.close();
        }
        catch(Exception e)
        {
        	hlog.wExceptionLog(this.getClass().getName()," rollback false:",e,"0000","0000");
			setMsg(e.toString() + "SQL:" + "sql");
			setSucc(false);
            try
            {
                if(conn != null)
                    conn.close();
            }
            catch(Exception e2)
            {
                System.out.println(e2);
            }
            return null;
        }
        return result;
    }
	
    
  
    
	public boolean isSucc() {
		return isSucc;
	}
	
    private void setSucc(boolean b)
    {
        isSucc = b;
    }
    
    private void setMsg(String string) {
		System.out.println(string);
		msgStr = string;
	}
    
    public String getMsg() {
		return msgStr;
	}
    
    
}
