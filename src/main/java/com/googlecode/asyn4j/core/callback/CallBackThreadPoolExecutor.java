package com.googlecode.asyn4j.core.callback;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * TODO Comment of CallBackThreadPoolExecutor
 *
 * @author pan_java
 * @version CallBackThreadPoolExecutor.java 2010-9-13 下午02:51:09
 *
 */
public class CallBackThreadPoolExecutor extends ThreadPoolExecutor {

	private AtomicLong callBackNum;

	/**
	 * @param corePoolSize
	 * @param maximumPoolSize
	 * @param keepAliveTime
	 * @param unit
	 * @param workQueue
	 * @param threadFactory
	 * @param handler
	 */
	public CallBackThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler,
			AtomicLong callBackNum) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		this.callBackNum = callBackNum;
	}

	@Override
	protected void afterExecute(Runnable r, Throwable t) {
		super.afterExecute(r, t);
	}

	@Override
	protected void beforeExecute(Thread t, Runnable r) {
		super.beforeExecute(t, r);
		callBackNum.incrementAndGet();
	}

	@Override
	protected void terminated() {
		super.terminated();
	}

}
