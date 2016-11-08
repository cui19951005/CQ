package com.cq.sdk.potential.utils;

/**
 * Created by admin on 2016/9/12.
 */
public enum  SynchronizationType {
    HibernateSession("HibernateSession");
    private String type;

    SynchronizationType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
