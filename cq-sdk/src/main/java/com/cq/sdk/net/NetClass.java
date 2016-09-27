package com.cq.sdk.net;

import java.io.Serializable;

/**
 * Created by admin on 2016/9/23.
 */
public class NetClass implements Serializable {
    private String name;
    private String[] interfaceName;
    private String[] annotationList;

    public String[] getAnnotationList() {
        return annotationList;
    }

    public void setAnnotationList(String[] annotationList) {
        this.annotationList = annotationList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String[] interfaceName) {
        this.interfaceName = interfaceName;
    }
}
