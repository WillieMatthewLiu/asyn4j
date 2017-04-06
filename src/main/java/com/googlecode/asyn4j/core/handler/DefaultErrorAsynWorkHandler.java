package com.googlecode.asyn4j.core.handler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.asyn4j.core.work.AsynWork;

/**
 * TODO Comment of DefaultErrorAsynWorkHandler
 * 
 * @author pan_java
 * @version DefaultErrorAsynWorkHandler.java 2010-8-27 下午07:56:56
 */
public class DefaultErrorAsynWorkHandler extends ErrorAsynWorkHandler {
    private final static Log log = LogFactory.getLog(DefaultErrorAsynWorkHandler.class);

    @Override
    public void addErrorWork(AsynWork asynWork, Throwable throwable) {
        log.warn(asynWork.getThreadName() + " run is error, error info: " + throwable);
    }

}
