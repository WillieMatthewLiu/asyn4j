package com.googlecode.asyn4j.core.work;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.googlecode.asyn4j.core.WorkWeight;
import com.googlecode.asyn4j.core.callback.AsynCallBack;
import com.googlecode.asyn4j.util.MethodUtil;

/**
 * @author pan_java
 */
public class AsynWorkEntity implements AsynWork,Serializable {

    private Object                           target;

    private String                           method;

    private Object[]                         params;

    private AsynCallBack                     anycResult;

    private WorkWeight                       workWeight     = WorkWeight.MIDDLE;

    // method Cache Map
    private final static Map<String, Method> methodCacheMap = new ConcurrentHashMap<String, Method>();
    
    
    public AsynWorkEntity(Object target, String method) {
        this(target,method,null);
    }
    
    
    public AsynWorkEntity(Object target, String method,Object[]  params) {
        this(target,method,params,null,null);
    }

    public AsynWorkEntity(Object target, String method, Object[] params, AsynCallBack anycResult, WorkWeight workWeight) {
        if(target==null||method==null){
            throw new IllegalArgumentException("target or method  is null");
        }
        this.target = target;
        this.method = method;
        this.params = params;
        this.anycResult = anycResult;
        if(workWeight!=null){
            this.workWeight = workWeight;
        }
    }

    @Override
    public AsynCallBack call() throws Exception {

        if (target == null)
            throw new RuntimeException("target object is null");

        Class clazz = target.getClass();

        String methodKey = MethodUtil.getClassMethodKey(clazz, params, method);

        Method targetMethod = methodCacheMap.get(methodKey);

        if (targetMethod == null) {
            targetMethod = MethodUtil.getTargetMethod(clazz, params, method);
            if (targetMethod != null) {
                methodCacheMap.put(methodKey, targetMethod);
            }
        }

        if (targetMethod == null) {
            throw new IllegalArgumentException("target method is null");
        }

        Object result = targetMethod.invoke(target, params);
        if (anycResult != null) {//if call back is not null
            anycResult.setInokeResult(result);
        }
        return anycResult;

    }

    @Override
    public AsynCallBack getAnycResult() {
        return anycResult;
    }

    @Override
    public String getThreadName() {
        String className = this.target.getClass().getSimpleName();
        StringBuilder sb = new StringBuilder();
        sb.append(className).append("-").append(this.method);
        return sb.toString();
    }


    @Override
    public int getWeight() {
        return workWeight.getValue();
    }
    
    

}
