/**
 * Project: asyn4j
 * 
 * File Created at 2010-9-13
 * $Id$
 * 
 * Copyright 2008 Alibaba.com Croporation Limited.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * Alibaba Company. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Alibaba.com.
 */
package com.googlecode.asyn4j.core.work;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import com.googlecode.asyn4j.core.WorkProcessor;
import com.googlecode.asyn4j.core.handler.WorkQueueFullHandler;

/**
 * TODO Comment of AsynWorkRejectedExecutionHandler
 * 
 * @author yuncheng
 * @version AsynWorkRejectedExecutionHandler.java 2010-9-13 下午01:38:35
 */
public final class AsynWorkRejectedExecutionHandler implements RejectedExecutionHandler {

    private WorkQueueFullHandler workQueueFullHandler;

    public AsynWorkRejectedExecutionHandler(WorkQueueFullHandler workQueueFullHandler) {
        this.workQueueFullHandler = workQueueFullHandler;
    }

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        AsynWork asynWork = ((WorkProcessor) r).getAsynWork();
        workQueueFullHandler.addAsynWork(asynWork);
    }
    
    
    

}
