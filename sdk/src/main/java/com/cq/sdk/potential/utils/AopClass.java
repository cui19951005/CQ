package com.cq.sdk.potential.utils;

import java.util.regex.Pattern;

/**
 * Created by CuiYaLei on 2016/9/4.
 */
public class AopClass {
    private Object object;
    private String name;
    private Pattern pointcut;
    private AopMethod before;
    private AopMethod after;
    private AopMethod throwing;
    private AopMethod round;
    private AopMethod returning;

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

    public Pattern getPointcut() {
        return pointcut;
    }

    public void setPointcut(Pattern pointcut) {
        this.pointcut = pointcut;
    }

    public AopMethod getBefore() {
        return before;
    }

    public void setBefore(AopMethod before) {
        this.before = before;
    }

    public AopMethod getAfter() {
        return after;
    }

    public void setAfter(AopMethod after) {
        this.after = after;
    }

    public AopMethod getThrowing() {
        return throwing;
    }

    public void setThrowing(AopMethod throwing) {
        this.throwing = throwing;
    }

    public AopMethod getRound() {
        return round;
    }

    public void setRound(AopMethod round) {
        this.round = round;
    }

    public AopMethod getReturning() {
        return returning;
    }

    public void setReturning(AopMethod returning) {
        this.returning = returning;
    }
    public AopClass clone() {
        AopClass aopClass=new AopClass();
        aopClass.setName(this.getName());
        aopClass.setObject(this.object);
        aopClass.setRound(this.round);
        aopClass.setPointcut(this.pointcut);
        aopClass.setAfter(this.after);
        aopClass.setThrowing(this.throwing);
        aopClass.setBefore(this.before);
        aopClass.setReturning(this.returning);
        return aopClass;
    }
}
