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

    /**
     * xml解析
     * 父节点罗列子节点内容，遍历子节点内容，递归每个子节点内容，子节点变父节点
     * 重新找子节点，如果没有子节点设置节点下内容
     * @param xml
     * @return
     */
    public static Node analysis(String xml){
        xml=xml.replaceAll("<!--.*?-->","");
        Node node=new Node();
        String tag=StringUtils.subString(xml,"<",">");
        int endIndex=tag.indexOf(" ");
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
        xml=StringUtils.subStringSymmetric(xml,"<"+tag+">",endTag);
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
                node.setNodes(XmlAnalysis.getTagText(xml).stream().map(item -> XmlAnalysis.analysis( item)).collect(Collectors.toList()));
            }
        }else{
            node.setText(XmlAnalysis.escape(xml));
        }
        return node;
    }
    private static String escape(String text){
        return text.replace("&amp;","&").replace("&lt;","<").replace("&gt;",">").replace("&quot;","\"").replace("&apos","'");
    }
    /**
     * 根据xml内容获取根节点下一级子节点内容
     * @param xml
     * @return
     */
    private static List<String> getTagText(String xml){
        List<String> list=new ArrayList<>();
        while (true) {
            String tag = StringUtils.subString(xml, "<", ">", true);
            if(tag==null){
                return list;
            }
            int endIndex = tag.indexOf(" ");
            String endTag;
            if (endIndex != -1) {
                endTag = "</" + tag.substring(1, endIndex) + ">";
            } else {
                endTag = "</" + tag.substring(1);
            }
            int index=StringUtils.subStringInt(xml,tag,endTag);
            list.add(tag+xml.substring(xml.indexOf(tag)+tag.length(),index)+endTag);
            xml=xml.substring(index+endTag.length());
        }
    }
    private static class Node{
        private String name;
        private Map<String,String> attributes;
        private String text;
        private List<Node> nodes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Map<String, String> getAttributes() {
            return attributes;
        }

        public void setAttributes(Map<String, String> attributes) {
            this.attributes = attributes;
        }

        public List<Node> getNodes() {
            return nodes;
        }

        public void setNodes(List<Node> nodes) {
            this.nodes = nodes;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
