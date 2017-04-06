package com.googlecode.asyn4j.example;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.core.handler.CacheAsynWorkHandler;
import com.googlecode.asyn4j.core.handler.FileAsynServiceHandler;
import com.googlecode.asyn4j.core.handler.DefaultErrorAsynWorkHandler;

import com.googlecode.asyn4j.service.AsynService;
import com.googlecode.asyn4j.service.AsynServiceImpl;
import com.googlecode.asyn4j.springbean.TestBean;
import com.googlecode.asyn4j.springbean.TestMain;

public class ServiceExcute {
    static ApplicationContext context = null;

    @Before
    public void setUp() {
       // context = new FileSystemXmlApplicationContext("D:/java/asyn4j_1.1/src/main/java/applicationContext.xml");
    }

    @Test
    public void testExecut2() throws InterruptedException {
        AsynService anycService = AsynServiceImpl.getService(300, 3000L, 100, 100,1000);
        anycService.setWorkQueueFullHandler(new CacheAsynWorkHandler(100));
        anycService.setServiceHandler(new FileAsynServiceHandler("c:/asyn.data"));
        anycService.init();
        TestBean aa = new TestBean();
        for (long i = 0; i < 700; i++) {
            anycService.addWork(aa, "myName",new Object[] { "panxiuyan" + i },new MyResult());
            if (i % 99 == 0) {
                System.out.println(anycService.getRunStatInfo());
            }
        }
        
        Thread.sleep(Long.MAX_VALUE);

    }

    @Test
    @Ignore
    public void testExecut3() throws InterruptedException {
        AsynService anycService = AsynServiceImpl.getService();
        anycService.init();
        ArrayList list = new ArrayList();
        anycService.addWork( TestBean.class, "myName", new Object[] { list },new MyResult());
        Thread.sleep(Long.MAX_VALUE);

    }

    @Test
    @Ignore
    public void testExecutSpring() throws InterruptedException {
        TestMain testMain = (TestMain) context.getBean("testMain");
        testMain.maintest();
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void testServiceHandler() throws InterruptedException {
        AsynService anycService = AsynServiceImpl.getService(10000, 3000L, 50, 50,1000);
        anycService.setWorkQueueFullHandler(new CacheAsynWorkHandler(100));
        anycService.setServiceHandler(new FileAsynServiceHandler("c:/asyn.data"));
        anycService.init();
        /*for (long i = 0; i < 100000; i++) {
            anycService.addWork( TestBean.class, "myName",new Object[] { "panxiuyan" + i });

            if (i % 99 == 0) {
                System.out.println(anycService.getRunStatInfo());
            }
            
            if(i==100){
            	Runnable ab = new Runnable(){
            		public void run(){
            			try {
							Thread.sleep(3*1000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
            			System.exit(1);
            		}
            	};
            	
            	Thread t = new Thread(ab);
            	t.start();
            }
        }*/

    }

    @Test
    public void testErrorHandler() throws InterruptedException {
        AsynService anycService = AsynServiceImpl.getService(300, 3000L, 100, 100,1000);
        anycService.setWorkQueueFullHandler(new CacheAsynWorkHandler(100));
        anycService.setServiceHandler(new FileAsynServiceHandler("c:/asyn.data"));
        anycService.setErrorAsynWorkHandler(new DefaultErrorAsynWorkHandler());
        anycService.init();
        for (long i = 0; i < 100000000; i++) {
            anycService.addWork( TestBean.class, "myName",new Object[] { "panxiuyan" + i },new MyResult());

            if (i % 99 == 0) {
                System.out.println(anycService.getRunStatInfo());
            }
        }

    }

    @Test
    @Ignore
    public void testSpringErrorHandler() throws InterruptedException {
        TestMain testMain = (TestMain) context.getBean("testMain");
        testMain.maintest();
        Thread.sleep(Long.MAX_VALUE);
    }

    public static class MyResult extends AsynCallBack {
        public void doNotify() {
            System.out.println("excute ok!");
        }
    }

    public static class MyHasResult extends AsynCallBack {

        public void doNotify() {
            if (this.methodResult != null) {
                System.out.println(methodResult.toString());
            }
        }
    }

}
