package com.cq.sdk.potential.sql.frame.hibernate;

/**
 * Created by admin on 2016/9/13.
 */
public enum  HibernateClassName {
    Configuration("org.hibernate.cfg.Configuration"),
    SessionFactoryImplementor("org.hibernate.engine.spi.SessionFactoryImplementor"),
    SessionFactory("org.hibernate.SessionFactory"),
    MappingReference$Type("org.hibernate.boot.cfgxml.spi.MappingReference$Type"),
    MappingReference("org.hibernate.boot.cfgxml.spi.MappingReference"),
    MySQL5Dialect("org.hibernate.dialect.MySQL5Dialect"),
    CurrentSessionContext("org.hibernate.context.spi.CurrentSessionContext") ;
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
