package com.cq.sdk.utils;

import java.util.Map;

/**
 * Created by admin on 2016/12/6.
 */
public class Entry<K,V> implements Map.Entry<K,V> {
    private K key;
    private V value;
    public Entry(K key,V value) {
        this.key=key;
        this.value=value;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public V setValue(V value) {
        return this.value;
    }
}
