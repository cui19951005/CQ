package com.cq.sdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/11/10.
 */
public final class HtmlAnalysis {

    public static Node analysis(String html){
        return XmlAnalysis.toObject(html);
    }
    public static List<Node> getNode(Node root,String expr){
        return findNode(new ArrayList<>(),root,expr);
    }
    public static List<Node> findNode(List<Node> nodeList,Node node, String clazz){
        String[] classList = clazz.split(" ");
        String nowClass=classList[0];
        if (HtmlAnalysis.classCompare(node,nowClass)) {
            clazz=StringUtils.join(" ", 1, classList.length - 1, classList);
            if(clazz.length()==0){
                nodeList.add(node);
                return nodeList;
            }
        }
        if(node.getNodes()!=null) {
            for (int i = 0; i < node.getNodes().size(); i++) {
                Node n = node.getNodes().get(i);
                findNode(nodeList,n, clazz);
            }
        }
        return nodeList;
    }
    private final static Pattern EQ=Pattern.compile(":eq\\(\\d+\\)");
    private final static Pattern LT=Pattern.compile(":lt\\(\\d+\\)");
    private final static Pattern GT=Pattern.compile(":gt\\(\\d+\\)");
    private final static Pattern NOT=Pattern.compile(":not\\(\\d+\\)");
    private final static Pattern EVEN=Pattern.compile(":even");
    private final static Pattern ODD=Pattern.compile(":odd");
    private final static List<String> INPUT=new ArrayList<String>(){{
        this.add(":text");
        this.add(":password");
        this.add(":radio");
        this.add(":checkbox");
        this.add(":file");
        this.add(":image");
        this.add(":button");
        this.add(":hidden");
        this.add(":reset");
        this.add(":submit");
    }};
    private static boolean classCompare(Node node,String clazz2){
        Matcher matcher=Pattern.compile("[:|#|\\.]([\\w|\\d|=|'|\"|\\(|\\)]*)|\\[\\S*?\\]|\\w+").matcher(clazz2);
        while (matcher.find()){
            String group=matcher.group();
            switch (group.charAt(0)){
                case ':':
                    int result;
                    if((result=HtmlAnalysis.isMatcher(HtmlAnalysis.EQ,group,node))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.LT,group,node))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.GT,group,node))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.NOT,group,node))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.EVEN,group,node))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.ODD,group,node))<2
                            ) {
                        return result==0;
                    }else{
                        for(String item : HtmlAnalysis.INPUT){
                            if(item.equals(group) && HtmlAnalysis.classCompare(node,"[type='"+item.substring(1)+"']")){
                                return true;
                            }
                        }
                    }
                    if(!node.getName().equals(group.substring(1))){
                        return false;
                    }
                    break;
                case '#':
                    String id;
                    if(node.getAttributes()==null || (id=node.getAttributes().get("id"))==null ||!id.equals(group.substring(1))){
                        return false;
                    }
                    break;
                case '.':
                    String clazz;
                    if(node.getAttributes()==null || (clazz=node.getAttributes().get("class"))==null ||!clazz.equals(group.substring(1))){
                        return false;
                    }
                    break;
                case '[':
                    String[] attrStr=group.substring(1,group.length()-1).split("=");
                    if(attrStr.length==1)return false;
                    String attrName=attrStr[0];
                    String value=attrStr[1].replace("'","").replace("\"","");
                    String attr;
                    if(node.getAttributes()==null || (attr=node.getAttributes().get(attrName))==null ||!attr.equals(value)){
                        return false;
                    }
                    break;
                default:
                    if(!node.getName().equals(group)){
                        return false;
                    }
                    break;
            }
        }
        return true;
    }

    /**
     * 是否匹配text
     * @param pattern 正则表达式
     * @param text 被匹配文本
     * @param node 节点
     * @return -1不匹配0匹配2没有匹配正则表达式
     */
    private static final int isMatcher(Pattern pattern,String text,Node node){
        Matcher matcher;
        if((matcher=pattern.matcher(text))!=null&&matcher.find()&&matcher.group().equals(text)){
            String val=StringUtils.subString(text,"(",")");
            int number=Integer.valueOf(val==null?"0":val);
            boolean isTrue=false;
            for(int i=0;i<node.getParent().getNodes().size();i++){
                if(node.getParent().getNodes().get(i).equals(node)){
                    isTrue=true;
                    if(HtmlAnalysis.EQ.equals(pattern)&&number!=i
                            || HtmlAnalysis.LT.equals(pattern)&& number<=i
                            || HtmlAnalysis.GT.equals(pattern)&& number>=i
                            || HtmlAnalysis.NOT.equals(pattern)&&number==i
                            || HtmlAnalysis.EVEN.equals(pattern)&& i%2!=0
                            || HtmlAnalysis.ODD.equals(pattern)&& i%2==0
                            ){
                        return -1;
                    }
                }
            }
            if(isTrue){
                return 0;
            }else{
                return -1;
            }
        } else{
            return 2;
        }
    }
}
