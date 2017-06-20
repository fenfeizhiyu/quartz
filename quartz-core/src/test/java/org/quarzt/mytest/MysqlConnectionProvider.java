package org.quarzt.mytest;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import java.lang.reflect.Proxy;
import org.quartz.mytest.util.FileUtil;
import org.quartz.utils.ConnectionProvider;
import org.quartz.utils.PropertiesParser;

public class MysqlConnectionProvider implements ConnectionProvider{

	private static String DBNAME="mysql";
	
	private static String PROP_FILE_PATH="org/mytest/mysql.properties";
	
	private static PropertiesParser dbProp=null;
	
	private static Connection con=null;
	
	private static final Integer QUEUE_SIZE=2;
	
	private static BlockingQueue<Connection> queue=null;
	
	private static Set<Connection> useSet=null;
	
	private static Object lock=new Object();
	
	public MysqlConnectionProvider(){
		dbProp=new PropertiesParser(FileUtil.getProperties(PROP_FILE_PATH));
	
	}
	
	
	public void init(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			queue=new ArrayBlockingQueue<Connection>(QUEUE_SIZE);
			useSet=new HashSet<Connection>();
			for(int i=0;i<QUEUE_SIZE;i++){
				//数据库连接
				Connection con=DriverManager.getConnection(dbProp.getStringProperty("jdbcUrl"),
						dbProp.getStringProperty("jdbcUsername"), dbProp.getStringProperty("jdbcPassword"));
				//代理连接
				ConnectionProxy cp=new ConnectionProxy(con);
				queue.add((Connection)Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{Connection.class},cp));
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	/**
	 * 从阻塞队列取出一个连接，没有则进入等待
	 */
	@Override
	public Connection getConnection() throws SQLException {
		try {
			
			Connection con= queue.take();
			synchronized (lock){
				if(useSet.contains(con)){
					throw new RuntimeException("connect use error");
				}else{
					useSet.add(con);
				}
			}
			return con;
		} catch (InterruptedException e) {
			e.printStackTrace();
			return null;
		}
	}

	public void releaseConnection(Connection con){
		try{
			queue.put(con);
			useSet.remove(con);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

	}

	@Override
	public void shutdown() throws SQLException {
		
		
	}

	@Override
	public void initialize() throws SQLException {
		
		
	}
	


}
