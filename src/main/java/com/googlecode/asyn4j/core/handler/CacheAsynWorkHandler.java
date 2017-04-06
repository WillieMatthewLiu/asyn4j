package com.googlecode.asyn4j.core.handler;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.asyn4j.core.work.AsynWork;

/**
 * @author pan_java
 */
public final class CacheAsynWorkHandler extends WorkQueueFullHandler {
    private final static Log        log       = LogFactory.getLog(CacheAsynWorkHandler.class);

    private BlockingQueue<AsynWork> cacheLink = null;

    public CacheAsynWorkHandler() {
        cacheLink = new ArrayBlockingQueue<AsynWork>(300);
    }

    public CacheAsynWorkHandler(int maxLength) {
        cacheLink = new ArrayBlockingQueue<AsynWork>(maxLength);
    }

    @Override
    public boolean addAsynWork(AsynWork asynWork) {
        boolean result = cacheLink.offer(asynWork);
        if (!result) {
            log.warn("asyn work cache queue is full");
        }
        return result;
    }

    @Override
    public void process() {
        Runnable runnable = new Runnable() {
            public void run() {
                while (true) {
                    Map<String, Long> runstatMap = asynService.getRunStatMap();
                    if (cacheLink.isEmpty() || runstatMap.get("total") - runstatMap.get("execute") > 300) {
                        try {
                            log.debug("work queue is full,wait 6s");
                            Thread.sleep(6000);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        continue;
                    }
                    AsynWork asynWork = null;
                    try {
                        asynWork = cacheLink.take();
                        asynService.addAsynWork(asynWork);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.setDaemon(true);
        thread.start();
    }

}
