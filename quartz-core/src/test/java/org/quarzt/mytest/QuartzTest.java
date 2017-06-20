package org.quarzt.mytest;

import java.sql.Connection;
import java.sql.SQLException;

import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.DBConnectionManager;

public class QuartzTest {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String db_name="blog";
		String SCHEDULER_NAME="scheduler_test";
	}
	
	public static void create_dateBase(String dbName)throws Exception{
		DBConnectionManager.getInstance().addConnectionProvider("", null);
	}

	
}
