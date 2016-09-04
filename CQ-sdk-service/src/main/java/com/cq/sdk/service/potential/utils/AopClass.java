package com.cq.sdk.service.potential.utils;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class AopClass {
    private Object object;
    private String name;
    private String pointcut;
    private String before;
    private String after;
    private String throwing;
    private String round;
    private String returning;

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPointcut() {
        return pointcut;
    }

    public void setPointcut(String pointcut) {
        this.pointcut = pointcut;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getThrowing() {
        return throwing;
    }

    public void setThrowing(String throwing) {
        this.throwing = throwing;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getReturning() {
        return returning;
    }

    public void setReturning(String returning) {
        this.returning = returning;
    }
}
