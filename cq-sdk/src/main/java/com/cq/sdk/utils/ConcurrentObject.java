package com.cq.sdk.utils;

import java.io.*;
import java.lang.reflect.Array;
import java.util.concurrent.Semaphore;

/**
 * Created by admin on 2016/10/19.
 */
public final class ConcurrentObject<T> {
    private T[] objects;
    private static final int OBJECT_COUNT=10;
    private Semaphore semaphore;
    public ConcurrentObject(T obj) {
        this.objects= (T[]) Array.newInstance(obj.getClass(),OBJECT_COUNT);
        for(int i=0;i<OBJECT_COUNT;i++){
            this.objects[i]= (T) ObjectUtils.clone(obj);
        }
        this.semaphore=new Semaphore(OBJECT_COUNT);
    }
    public <T> T acquire() throws InterruptedException {
        this.semaphore.acquire();
        return (T) this.objects[this.semaphore.availablePermits()];
    }
    public void release(){
        this.semaphore.release();
    }
}
