package com.googlecode.asyn4j.service;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.asyn4j.core.ApplicationContext;
import com.googlecode.asyn4j.core.WorkProcessor;
import com.googlecode.asyn4j.core.WorkWeight;
import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.callback.CallBackRejectedExecutionHandler;
import com.googlecode.asyn4j.core.callback.CallBackThreadFactory;
import com.googlecode.asyn4j.core.callback.CallBackThreadPoolExecutor;
import com.googlecode.asyn4j.core.handler.AsynServiceHandler;
import com.googlecode.asyn4j.core.handler.ErrorAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.WorkQueueFullHandler;
import com.googlecode.asyn4j.core.work.AsynThreadFactory;
import com.googlecode.asyn4j.core.work.AsynThreadPoolExecutor;
import com.googlecode.asyn4j.core.work.AsynWork;
import com.googlecode.asyn4j.core.work.AsynWorkEntity;
import com.googlecode.asyn4j.core.work.AsynWorkRejectedExecutionHandler;
import com.googlecode.asyn4j.exception.Asyn4jException;
import com.googlecode.asyn4j.spring.AsynSpringUtil;

/***
 * 
 * @author pan_java
 *
 */

@SuppressWarnings("unchecked")
public class AsynServiceImpl extends ApplicationContext implements AsynService {
	private static final Log log = LogFactory.getLog(AsynServiceImpl.class);
	// service run flag
	private static boolean run = false;
	// status info stringbuffer
	private static StringBuilder infoSb = new StringBuilder();

	public final static ConcurrentHashMap<String, Object> targetCacheMap = new ConcurrentHashMap<String, Object>();

	private static Lock lock = new ReentrantLock();

	private AsynServiceImpl() {
		this(maxCacheWork, addWorkWaitTime, work_thread_num, callback_thread_num, closeServiceWaitTime);
	}

	private AsynServiceImpl(int maxCacheWork, long addWorkWaitTime, int workThreadNum, int callBackThreadNum,
			long closeServiceWaitTime) {
		super(maxCacheWork, addWorkWaitTime, workThreadNum, callBackThreadNum, closeServiceWaitTime);
	}

	public static AsynServiceImpl getService() {
		lock.lock();
		try {
			if (instance == null) {
				instance = new AsynServiceImpl();
			}
		} finally {
			lock.unlock();
		}
		return instance;
	}

	public static AsynServiceImpl getService(int maxCacheWork, long addWorkWaitTime, int workThreadNum,
			int callBackThreadNum, long closeServiceWaitTime) {
		lock.lock();
		try {
			if (instance == null) {
				instance = new AsynServiceImpl(maxCacheWork, addWorkWaitTime, workThreadNum, callBackThreadNum,
						closeServiceWaitTime);
			}
		} finally {
			lock.unlock();
		}
		return instance;

	}

	/**
	 * init Asyn Service
	 */
	@Override
	public void init() {

		if (!run) {
			run = true;
			// init work execute queue
			workQueue = new PriorityBlockingQueue<Runnable>(maxCacheWork);

			if (workQueueFullHandler != null) {
				workExecutor = new AsynThreadPoolExecutor(work_thread_num, work_thread_num, 0L, TimeUnit.MILLISECONDS,
						workQueue, new AsynThreadFactory(), new AsynWorkRejectedExecutionHandler(workQueueFullHandler),
						executeWorkNum);
			} else {
				workExecutor = new AsynThreadPoolExecutor(work_thread_num, work_thread_num, 0L, TimeUnit.MILLISECONDS,
						workQueue, new AsynThreadFactory(), executeWorkNum);
			}

			// init callback queue
			callBackQueue = new LinkedBlockingQueue<Runnable>();

			callBackExecutor = new CallBackThreadPoolExecutor(callback_thread_num, callback_thread_num, 0L,
					TimeUnit.MILLISECONDS, callBackQueue, new CallBackThreadFactory(),
					new CallBackRejectedExecutionHandler(), callBackNum);

			if (serviceHandler != null) {
				serviceHandler.setServiceStat(AsynService.SERVICE_INIT);
				serviceHandler.setAsynService(this);
				serviceHandler.process();
			}

			if (workQueueFullHandler != null) {
				workQueueFullHandler.process();
			}

			// jvm close run
			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					close(closeServiceWaitTime);
				}
			});
		}

	}

	@Override
	public void close() {
		// defaule wait time
		this.close(closeServiceWaitTime);
	}

	@Override
	public void close(long waitTime) {
		if (run) {
			run = false;
			try {
				workExecutor.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
				// workExecutor is wait sometime,so callBackExecutor wait time
				// is 0
				callBackExecutor.awaitTermination(0, TimeUnit.MILLISECONDS);
			} catch (InterruptedException e) {
				log.error(e);
			}
			workExecutor.shutdown();
			callBackExecutor.shutdown();
			if (serviceHandler != null) {
				serviceHandler.setAsynWorkQueue(workQueue);
				serviceHandler.setCallBackQueue(callBackQueue);
				serviceHandler.setServiceStat(AsynService.SERVICE_CLOSE);
				serviceHandler.process();
			}
		}
	}

	@Override
	public void addWork(Object tagerObject, String method) {
		addWork(tagerObject, method, null);
	}

	@Override
	public void addWork(Object tagerObject, String method, Object[] params) {
		addWork(tagerObject, method, params, null);

	}

	@Override
	public void addWork(Object tagerObject, String method, Object[] params, AsynCallBack asynCallBack) {
		addWork(tagerObject, method, params, asynCallBack, DEFAULT_WORK_WEIGHT);
	}

	@Override
	public void addWork(Object tagerObject, String method, Object[] params, AsynCallBack asynCallBack,
			WorkWeight weight) {
		addWork(tagerObject, method, params, asynCallBack, DEFAULT_WORK_WEIGHT, false);
	}

	@Override
	public void addWork(Object tagerObject, String method, Object[] params, AsynCallBack asynCallBack,
			WorkWeight weight, boolean cache) {
		if (tagerObject == null || method == null) {
			throw new IllegalArgumentException("target name is null or  target method name is null");
		}

		Object target = null;
		if (tagerObject.getClass().isAssignableFrom(String.class)) {// tagerObject
																	// form
																	// string to
																	// spirng
																	// name

			addWorkWithSpring(tagerObject.toString(), method, params, asynCallBack, weight);
			return;

		} else if (tagerObject instanceof Class) {// tagerObject form class to
													// targetclass
			String classKey = ((Class) tagerObject).getSimpleName();
			if (cache) {
				target = targetCacheMap.get(classKey);
				if (target == null) {
					target = newObject((Class) tagerObject);
					targetCacheMap.put(classKey, target);
				}
			} else {
				target = newObject((Class) tagerObject);
			}
		} else {// tagerObject is a entity object
			target = tagerObject;
		}

		if (target == null) {
			throw new IllegalArgumentException("target object is null");
		}
		AsynWork anycWork = new AsynWorkEntity(target, method, params, asynCallBack, weight);

		addAsynWork(anycWork);

	}

	/**
	 * class instantiation object
	 * 
	 * @param clzss
	 * @return
	 */
	private Object newObject(Class clzss) {
		try {
			Constructor constructor = clzss.getConstructor();
			if (constructor == null) {
				throw new IllegalArgumentException("target not have default constructor function");
			}
			// Instance target object
			return clzss.newInstance();
		} catch (Exception e) {
			log.error(e);
			return null;
		}
	}

	@Override
	public void addWorkWithSpring(String target, String method, Object[] params, AsynCallBack asynCallBack,
			WorkWeight weight) {

		if (target == null || method == null) {
			throw new IllegalArgumentException("target name is null or  target method name is null or weight less 0");
		}
		// get spring bean
		Object bean = AsynSpringUtil.getBean(target);

		if (bean == null)
			throw new IllegalArgumentException("spring bean is null");

		AsynWork anycWork = new AsynWorkEntity(bean, method, params, asynCallBack, weight);

		addAsynWork(anycWork);

	}

	/**
	 * add asyn work
	 * 
	 * @param asynWork
	 * @throws Asyn4jException
	 */
	public void addAsynWork(AsynWork asynWork) {
		if (!run) {// if asyn service is stop or no start!
			throw new Asyn4jException("asyn service is stop or no start!");
		}
		if (asynWork == null) {
			throw new IllegalArgumentException("asynWork is null");
		}
		try {
			// get acquire wait addWorkWaitTime
			if (semaphore.tryAcquire(addWorkWaitTime, TimeUnit.MILLISECONDS)) {//
				WorkProcessor workProcessor = new WorkProcessor(asynWork, this);
				// asyn work execute
				workExecutor.execute(workProcessor);
				totalWork.incrementAndGet();
			} else {
				log.warn("work queue is full,add work to cache queue");
				workQueueFullHandler.addAsynWork(asynWork);
			}
		} catch (InterruptedException e) {
			log.error(e);
		}

	}

	@Override
	public Map<String, Long> getRunStatMap() {
		if (run) {
			statMap.clear();
			statMap.put("total", totalWork.get());
			statMap.put("execute", executeWorkNum.get());
			statMap.put("callback", callBackNum.get());
		}
		return statMap;
	}

	@Override
	public String getRunStatInfo() {
		if (run) {
			infoSb.delete(0, infoSb.length());
			infoSb.append("total asyn work:").append(totalWork.get()).append("\t");
			infoSb.append(",excute asyn work:").append(executeWorkNum.get()).append("\t");
			infoSb.append(",callback asyn result:").append(callBackNum.get()).append("\t");
		}
		return infoSb.toString();
	}

	@Override
	public void setWorkQueueFullHandler(WorkQueueFullHandler workQueueFullHandler) {
		if (run)
			throw new IllegalArgumentException("asyn running");
		if (workQueueFullHandler == null)
			throw new IllegalArgumentException("workQueueFullHandler is null");
		this.workQueueFullHandler = workQueueFullHandler;
		this.workQueueFullHandler.setAsynService(this);
	}

	@Override
	public void setServiceHandler(AsynServiceHandler serviceHandler) {
		if (run)
			throw new IllegalArgumentException("asyn running");
		if (serviceHandler == null)
			throw new IllegalArgumentException("closeHander is null");
		this.serviceHandler = serviceHandler;
	}

	@Override
	public void setErrorAsynWorkHandler(ErrorAsynWorkHandler errorAsynWorkHandler) {
		if (run)
			throw new IllegalArgumentException("asyn running");
		if (errorAsynWorkHandler == null)
			throw new IllegalArgumentException("errorAsynWorkHandler is null");
		this.errorAsynWorkHandler = errorAsynWorkHandler;
	}

}
