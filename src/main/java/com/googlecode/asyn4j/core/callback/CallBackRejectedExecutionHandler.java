package com.googlecode.asyn4j.core.callback;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * TODO Comment of CallBackRejectedExecutionHandler
 * 
 * @author pan_java
 * @version CallBackRejectedExecutionHandler.java 2010-9-13 下午02:52:20
 */
public final class CallBackRejectedExecutionHandler implements RejectedExecutionHandler {

	private static final Log log = LogFactory.getLog(CallBackRejectedExecutionHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		AsynCallBack asynCallBack = (AsynCallBack) r;
		log.warn(r + " not execute");
	}

}
