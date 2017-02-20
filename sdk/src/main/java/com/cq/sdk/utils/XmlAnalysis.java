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
            Matcher matcher=Pattern.compile("\\w[\\w|\\S]*?=[\"|'].*?[\"|']").matcher(attributeText);
            while (matcher.find()){
                String kV=matcher.group();
                int split=kV.indexOf("=");
                map.put(kV.substring(0,split),kV.substring(split+2,kV.length()-1));
            }
            node.setAttributes(map);
        }
        if(tag.charAt(tag.length()-1)=='/'){
            XmlAnalysis.setParentText(xml,rightIndex,node.getParent());
            return node;
        }
        String temp= Str.subStringSymmetric(xml,"<"+node.getName(),endTag);
        if(temp==null){
            XmlAnalysis.setParentText(xml,rightIndex,node.getParent());
            return node;
        }else{
            temp = temp.substring(temp.indexOf(">") + 1);
            xml=temp;
        }
        rightIndex=xml.indexOf("<");
        if(rightIndex!=-1){
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
                StringBuffer sb=new StringBuffer();
                List<String> xmlLIst=XmlAnalysis.getTagText(xml,sb);
                node.setText(sb.toString());
                node.setNodes(xmlLIst.stream().map(item -> {
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
    private static List<String> getTagText(String xml,StringBuffer sb){
        List<String> list=new ArrayList<>();
        while (true) {
            //String tag = Str.subString(xml, "<", ">", true);
            Matcher matcher=Pattern.compile("<[\\w\\d]").matcher(xml);
            String tag=null;
            if(matcher.find())tag=Str.subString(xml,matcher.group(),">",true);
            if(tag==null){
                sb.append(xml);
                return list;
            }else if(tag.charAt(tag.length()-2)=='/'){
                list.add(tag);
                xml=xml.substring(xml.indexOf("/>")+2);
                continue;
            }
            int tagIndex=xml.indexOf(tag);
            sb.append(xml.substring(0,tagIndex));
            int endIndex = tag.indexOf(Str.SPACE);
            String endTag;
            if (endIndex != -1) {
                endTag = "</" + tag.substring(1, endIndex) + ">";
            } else {
                endTag = "</" + tag.substring(1);
            }
            int index= Str.subStringInt(xml,tag.substring(0,endIndex==-1?tag.length()-1:endIndex),endTag);
            if(index!=-1) {
                list.add(tag + xml.substring( tagIndex+ tag.length(), index) + endTag);
                xml = xml.substring(index + endTag.length());
            }else{
                xml=xml.substring(xml.indexOf(tag)+tag.length());
                list.add(tag);
            }
        }
    }
   public static class Node {
        private String name;
        private Map<String,String> attributes;
        private String text;
        private Node parent;
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

        public Node getParent() {
            return parent;
        }

        public void setParent(Node parent) {
            this.parent = parent;
        }
    }
}

