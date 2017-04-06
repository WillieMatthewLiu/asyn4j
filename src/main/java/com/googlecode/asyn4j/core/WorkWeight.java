
package com.googlecode.asyn4j.core;

/**
 * TODO Comment of WorkWeight
 * 
 * @author pan_java
 * @version WorkWeight.java 2010-9-3 下午06:19:21
 */
public enum WorkWeight {

    LOW(1),
    MIDDLE(5),
    HIGH(9);

    private int value;

    WorkWeight(int value) {
        this.value = value;
    }
    
    public int getValue(){
        return value;
    }

}
