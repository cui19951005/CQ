package com.cq.sdk.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/11/10.
 */
public final class HtmlAnalysis {

    public static XmlAnalysis.Node analysis(String html){
        return XmlAnalysis.toObject(html);
    }
    public static List<XmlAnalysis.Node> getNode(XmlAnalysis.Node root,String expr){
        List<XmlAnalysis.Node> nodeList=new ArrayList<>();
        findNode(nodeList,root,expr,0);
        return nodeList;
    }
    private static int findNode(List<XmlAnalysis.Node> nodeList,XmlAnalysis.Node node, String clazz,int last){
        String[] classList = clazz.split(" ");
        String nowClass=classList[0];
        Integer result=HtmlAnalysis.classCompare(node,nowClass,last);
        if (result!=null&&result==0) {
            clazz= Str.join(" ", 1, classList.length - 1, classList);
            if(clazz.length()==0){
                nodeList.add(node);
                return result+last;
            }
        }else if(result==null){
            result=0;
        }
        result+=last;
        if(node.getNodes()!=null) {
            int lastCount=0;
            for (int i = 0; i < node.getNodes().size(); i++) {
                XmlAnalysis.Node n = node.getNodes().get(i);
                lastCount=findNode(nodeList,n, clazz,lastCount);
            }
        }
        return result;
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
    private static Integer classCompare(XmlAnalysis.Node node,String clazz2,int last){
        Matcher matcher=Pattern.compile("[:|#|\\.]([\\w|\\d|=|'|\"|\\(|\\)|-]*)|\\[\\S*?\\]|\\w+").matcher(clazz2);
        while (matcher.find()){
            String group=matcher.group();
            switch (group.charAt(0)){
                case ':':
                    int result;
                    if((result=HtmlAnalysis.isMatcher(HtmlAnalysis.EQ,group,node,last))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.LT,group,node,last))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.GT,group,node,last))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.NOT,group,node,last))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.EVEN,group,node,last))<2
                            || (result=HtmlAnalysis.isMatcher(HtmlAnalysis.ODD,group,node,last))<2
                            ) {
                        if(result!=0){
                            return 1;
                        }else {
                            return 0;
                        }
                    }else{
                        for(String item : HtmlAnalysis.INPUT){
                            if(item.equals(group) && HtmlAnalysis.classCompare(node,"[type='"+item.substring(1)+"']",0)==0){
                                return 0;
                            }
                        }
                    }
                    if(!node.getName().equals(group.substring(1))){
                        return null;
                    }
                    break;
                case '#':
                    String id;
                    if(node.getAttributes()==null || (id=node.getAttributes().get("id"))==null ||!id.equals(group.substring(1))){
                        return null;
                    }
                    break;
                case '.':
                    String clazzStr;
                    if(node.getAttributes()==null || (clazzStr=node.getAttributes().get("class"))==null){
                        return null;
                    }
                    boolean isTrue=false;
                    String[] clazzList=clazzStr.split(Str.SPACE);
                    for(String clazz : clazzList){
                        if(clazz.equals(group.substring(1))){
                            isTrue=true;
                            break;
                        }
                    }
                    if(isTrue)continue;
                    else return null;
                case '[':
                    String[] attrStr=group.substring(1,group.length()-1).split("=");
                    if(attrStr.length==1)return null;
                    String attrName=attrStr[0];
                    String value=attrStr[1].replace("'","").replace("\"","");
                    String attr;
                    if(node.getAttributes()==null || (attr=node.getAttributes().get(attrName))==null ||!attr.equals(value)){
                        return null;
                    }
                    break;
                default:
                    if(!node.getName().equals(group)){
                        return null;
                    }
                    break;
            }
        }
        return 0;
    }

    /**
     * 是否匹配text
     * @param pattern 正则表达式
     * @param text 被匹配文本
     * @param node 节点
     * @return -1不匹配0匹配2没有匹配正则表达式
     */
    private static final int isMatcher(Pattern pattern,String text,XmlAnalysis.Node node,int count){
        Matcher matcher;
        if((matcher=pattern.matcher(text))!=null&&matcher.find()&&matcher.group().equals(text)){
            String val= Str.subString(text,"(",")");
            int number=Integer.valueOf(val==null?"0":val);
            if(HtmlAnalysis.EQ.equals(pattern)&&number!=count
                    || HtmlAnalysis.LT.equals(pattern)&& number<=count
                    || HtmlAnalysis.GT.equals(pattern)&& number>=count
                    || HtmlAnalysis.NOT.equals(pattern)&&number==count
                    || HtmlAnalysis.EVEN.equals(pattern)&& count%2!=0
                    || HtmlAnalysis.ODD.equals(pattern)&& count%2==0
                    ){
                return -1;
            }else {
                return 0;
            }

        } else{
            return 2;
        }
    }
}
