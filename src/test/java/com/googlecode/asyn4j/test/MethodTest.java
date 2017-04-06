package com.googlecode.asyn4j.test;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import com.googlecode.asyn4j.service.AsynServiceImpl;

public class MethodTest extends TestCase {
	
	public void testMethodName(){
		Class asiClass = AsynServiceImpl.class;
		
		System.out.println(asiClass);
		
		Method[] methods = asiClass.getDeclaredMethods();
		
		for(Method method:methods){
			Class[] paramTypeList = method.getParameterTypes();
			 for (Class paramType : paramTypeList) {
	                System.out.println("  " + paramType.toString());// 参数类型
	                
	            }
		}
		
		
		
	}

}
