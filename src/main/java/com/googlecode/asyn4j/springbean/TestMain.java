package com.googlecode.asyn4j.springbean;

import com.googlecode.asyn4j.service.AsynService;

public class TestMain {
	
	public AsynService asynService;

	public void setAsynService(AsynService asynService) {
		this.asynService = asynService;
	}
	
	public void maintest(){
		for(int i=0;i<100000000;i++){
			asynService.addWork("testBean", "myName",new Object[] { "panxiuyan" + i });
			if(i%99==0){
			    System.out.println(asynService.getRunStatInfo());
			}
		}
		
		System.out.println("ok");
	}

}
