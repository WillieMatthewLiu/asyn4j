package com.googlecode.asyn4j.util;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.junit.Test;

import com.googlecode.asyn4j.springbean.TestBean;

public class MethodUtilTest {
	
	@Test
	public void testGetTargetMethod(){
		Method m1 = MethodUtil.getTargetMethod(TestBean.class,null, "test");
		
		TestCase.assertNotNull(m1);
		
        Method m2 = MethodUtil.getTargetMethod(TestBean.class,null, "myName");
		
		TestCase.assertNull(m2);
		
        Method m3 = MethodUtil.getTargetMethod(TestBean.class,new Object[]{"cccc"}, "myName");
		
		TestCase.assertNotNull(m3);
		
		
		
	}
	
	@Test
	public void testGetClassMethodKey(){
		
		String key1 = MethodUtil.getClassMethodKey(TestBean.class,null, "test");
		
		TestCase.assertEquals(key1, "class com.googlecode.asyn4j.springbean.TestBean.test");
		
		String key2 = MethodUtil.getClassMethodKey(TestBean.class,new Object[]{"cccc"}, "test");
		
		TestCase.assertEquals(key2, "class com.googlecode.asyn4j.springbean.TestBean.test-class java.lang.String");
		
		
		
	}

}
