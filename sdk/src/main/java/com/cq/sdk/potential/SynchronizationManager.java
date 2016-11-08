package com.cq.sdk.potential;

import com.cq.sdk.potential.utils.SynchronizationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/9/12.
 */
public class SynchronizationManager {
    private static ThreadLocal resource=new ThreadLocal();
    public static void bind(SynchronizationType key, Object value){
        Map map= (Map) resource.get();
        if(map==null){
            map=new HashMap();
        }
        map.put(key.getType(),value);
        resource.set(map);
    }
    public static Object get(SynchronizationType key){
        Map map= (Map) resource.get();
        if(map==null){
            return null;
        }
        return map.get(key.getType());
    }
}
