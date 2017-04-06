package com.googlecode.asyn4j.core.handler;

import com.googlecode.asyn4j.core.work.AsynWork;

/**
 * TODO Comment of ErrorAsynWorkHandler
 * 
 * @author pan_java
 * @version ErrorAsynWorkHandler.java 2010-8-27 下午07:53:49
 */
public abstract class ErrorAsynWorkHandler implements AsynHandler {

    public void process() {
        //no to do
    }
    
    public abstract void addErrorWork(AsynWork asynWork,Throwable throwable);

}
