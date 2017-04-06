package com.googlecode.asyn4j.spring;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * spring tool 
 * @author pan_java
 *
 */
public class AsynSpringUtil implements ApplicationContextAware {

	private static ApplicationContext context = null;

	public void setApplicationContext(ApplicationContext arg0)
			throws BeansException {
		this.context = arg0;

	}

	/**
	 * get spring bean with beanName
	 * @param beanName
	 * @return
	 */
	public static Object getBean(String beanName) {
		return context.getBean(beanName);
	}

	/**
	 * get spring bean with beanName and Class Type
	 * @param <T>
	 * @param object
	 * @param beanName
	 * @return
	 */
	public static <T> T getBean(Class<T> object, String beanName) {
		return (T) context.getBean(beanName);
	}

}
