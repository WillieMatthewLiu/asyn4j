package com.googlecode.asyn4j.core.callback;

import java.util.concurrent.ThreadFactory;

/**
 * TODO Comment of CallBackThreadFactroy
 *
 * @author pan_java
 * @version CallBackThreadFactroy.java 2010-9-13 下午02:54:48
 *
 */
public class CallBackThreadFactory implements ThreadFactory {

	@Override
	public Thread newThread(Runnable r) {
		Thread thread = new Thread(r);
		return thread;
	}

}
