package com.cq.sdk.test;
import com.cq.sdk.potential.annotation.*;
import com.cq.sdk.potential.utils.InjectionType;
import com.cq.sdk.utils.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by admin on 2016/9/2.
 */
@Entrance(value = "com.cq.sdk.android.qq.*",injectionType = InjectionType.Annotation)
@LoadProperties({"jdbc.properties"})
//@NetAddress(port=2020,value = "localhost:2021")
public class Main {
    public static void main(String[] args) throws Exception{
        String html= "<li class=\"toggle more\"><a href=\"javascript:void(0);\">展开更多<i class=\"arrow-icon\"></i></a></li>\n";
        XmlAnalysis.Node node= HtmlAnalysis.analysis(html);
        List<XmlAnalysis.Node> nodeList=HtmlAnalysis.getNode(node,".menu-junior li");
        System.out.println(node);
    }
}