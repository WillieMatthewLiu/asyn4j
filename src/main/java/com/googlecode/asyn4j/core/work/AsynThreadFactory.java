package com.googlecode.asyn4j.core.work;

import java.util.concurrent.ThreadFactory;

/**
 * TODO Comment of AsynThreadFactory
 * 
 * @author pan_java
 * @version AsynThreadFactory.java 2010-9-13 下午01:43:11
 */
public class AsynThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setDaemon(false);
        return thread;
    }

}
