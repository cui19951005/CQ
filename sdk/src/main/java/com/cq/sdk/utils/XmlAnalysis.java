package com.cq.sdk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by admin on 2016/11/8.
 */
public final class XmlAnalysis {

    public static Node toObject(String xml){
        return XmlAnalysis.analysis(new Node(),xml);
    }
    /**
     * xml解析
     * 父节点罗列子节点内容，遍历子节点内容，递归每个子节点内容，子节点变父节点
     * 重新找子节点，如果没有子节点设置节点下内容
     * @param xml
     * @return
     */
    private static Node analysis(Node node,String xml){
        xml=xml.replaceAll("<!--[\\S|\\s]*?-->", Str.EMPTY).replaceAll("<![\\S|\\s]*?>", Str.EMPTY).replaceAll("<\\?.*?\\?>", Str.EMPTY);
        int leftIndex=xml.indexOf("<");
        int rightIndex=xml.indexOf(">");
        String tag=xml.substring(leftIndex+1,rightIndex);
        int endIndex=tag.indexOf(Str.SPACE);
        String endTag;
        String attributeText=null;
        if(endIndex!=-1){
            node.setName(tag.substring(0,endIndex));
            endTag="</"+node.getName()+">";
            attributeText=tag.substring(endIndex+1,tag.length());
        }else{
            node.setName(tag);
            endTag="</"+tag+">";
        }
        if(attributeText!=null){
            Map<String,String> map=new HashMap<>();
            Matcher matcher=Pattern.compile("\\w[\\w|\\S]*?=\".*?\"").matcher(attributeText);
            while (matcher.find()){
                String kV=matcher.group();
                String[] array=kV.split("=");
                map.put(array[0],array[1].substring(1,array[1].length()-1));
            }
            node.setAttributes(map);
        }
        if(tag.charAt(tag.length()-1)=='/'){
            XmlAnalysis.setParentText(xml,rightIndex,node.getParent());
            return node;
        }
        String temp= Str.subStringSymmetric(xml,"<"+tag+">",endTag);
        if(temp==null){
            XmlAnalysis.setParentText(xml,rightIndex,node.getParent());
            return node;
        }else{
            xml=temp;
        }
        if(xml.indexOf("<")!=-1){
            if(xml.indexOf("<![CDATA[")!=-1){
                StringBuilder sb=new StringBuilder();
                String[] array=xml.split("<!\\[CDATA\\[");
                for(int i=0;i<array.length;i++){
                    if(array[i].length()>0){
                        String[] ar=array[i].split("\\]\\]>");
                        if(ar.length==1) {
                            sb.append(XmlAnalysis.escape(ar[0]));
                        }else if(ar.length==2){
                            sb.append(ar[0]);
                            if(ar[1].length()>0) {
                                sb.append(XmlAnalysis.escape(ar[1]));
                            }
                        }
                    }
                }
                node.setText(sb.toString());
            }else {
                node.setNodes(XmlAnalysis.getTagText(xml).stream().map(item -> {
                    Node n=new Node();
                    n.setParent(node);
                    return XmlAnalysis.analysis(n,item);
                }).collect(Collectors.toList()));
            }
        }else{
            node.setText(XmlAnalysis.escape(xml));
        }
        return node;
    }
    private static void setParentText(String xml,int rightIndex,Node parent){
        int leftIndex=xml.indexOf("<",rightIndex);
        if(leftIndex==-1){
            leftIndex=xml.length();
        }
        parent.setText((parent.getText()==null? Str.EMPTY:parent.getText())+XmlAnalysis.escape(xml.substring(rightIndex+1,leftIndex)));
    }
    private static String escape(String text){
        return text.replace("&amp;","&").replace("&lt;","<").replace("&gt;",">").replace("&quot;","\"").replace("&apos","'").replaceAll("\\s*", Str.EMPTY);
    }
    /**
     * 根据xml内容获取根节点下一级子节点内容
     * @param xml
     * @return
     */
    private static List<String> getTagText(String xml){
        List<String> list=new ArrayList<>();
        while (true) {
            String tag = Str.subString(xml, "<", ">", true);
            if(tag==null){
                return list;
            }else if(tag.charAt(tag.length()-2)=='/'){
                list.add(tag);
                xml=xml.substring(xml.indexOf("/>")+2);
                continue;
            }
            int endIndex = tag.indexOf(Str.SPACE);
            String endTag;
            if (endIndex != -1) {
                endTag = "</" + tag.substring(1, endIndex) + ">";
            } else {
                endTag = "</" + tag.substring(1);
            }
            int index= Str.subStringInt(xml,tag,endTag);
            if(index!=-1) {
                list.add(tag + xml.substring(xml.indexOf(tag) + tag.length(), index) + endTag);
                xml = xml.substring(index + endTag.length());
            }else{
                xml=xml.substring(xml.indexOf(tag)+tag.length());
                int leftIndex=xml.indexOf("<");
                if(leftIndex==-1){
                    leftIndex=xml.length();
                }
                list.add(tag+xml.substring(0,leftIndex).replaceAll("\\s*", Str.EMPTY));
                xml=xml.substring(leftIndex);
            }
        }
    }
}
