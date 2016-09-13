package com.cq.sdk.potential.sql.frame.hibernate;

/**
 * Created by admin on 2016/9/13.
 */
public enum  HibernateClassName {
    SessionFactoryImplementor("org.hibernate.engine.spi.SessionFactoryImplementor");
    private String className;
    HibernateClassName(String className){
        this.className=className;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
