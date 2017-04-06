package com.googlecode.asyn4j.core.handler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.googlecode.asyn4j.core.WorkProcessor;
import com.googlecode.asyn4j.core.work.AsynWork;


/**
 * TODO Comment of DefauleCloseHandler
 * 
 * @author pan_java
 * @version DefauleCloseHandler.java 2010-8-27 下午07:41:03
 */
public class FileAsynServiceHandler extends AsynServiceHandler {

    private final static Log log = LogFactory.getLog(FileAsynServiceHandler.class);
    
    private String dataFilePath = null;
    
    public FileAsynServiceHandler(String dataFilePath){
    	this.dataFilePath = dataFilePath;
    }

	@Override
	public void init() {
		try {
			InputStream input =  new FileInputStream(new File(dataFilePath));
			ObjectInputStream oi = new ObjectInputStream(input);
			List<AsynWork> asynWorkList = (List<AsynWork>) oi.readObject();
			
			for(AsynWork asynWork:asynWorkList){
				this.asynService.addAsynWork(asynWork);
			}
		}catch(FileNotFoundException e){
			log.info("not data file");
		}catch (Exception e) {
			log.error(e);
		}
	}

	@Override
	public void destroy(){
		try {
			OutputStream out =  new FileOutputStream(new File(dataFilePath));
			ObjectOutputStream oo = new ObjectOutputStream(out);
			List<AsynWork> list = new ArrayList<AsynWork>();
			Iterator<Runnable> asynIterator = asynWorkQueue.iterator();
			
			while(asynIterator.hasNext()){
				list.add(((WorkProcessor)asynIterator.next()).getAsynWork());
			}
			oo.writeObject(list);
		} catch (Exception e) {
			log.error(e);
		}
		log.debug("asyn work have " + asynWorkQueue.size() + " no run!");
		log.debug("call back have " + callBackQueue.size() + " no run!");
    }
    
    

}
