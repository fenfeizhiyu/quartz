package org.quarzt.mytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.sql.Connection;

public class ConnectionProxy  implements InvocationHandler {

	private Connection target;

	public ConnectionProxy(Connection con){
		this.target=con;
	}


	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if(method.getName().equals("close")){
			System.out.println("close method is call");
		}
		return method.invoke(args);
	}
	
	
	private void close(){
		
	}

	/**
	 * 关闭连接
	 */
	private void realClose(){

	}
	


}
