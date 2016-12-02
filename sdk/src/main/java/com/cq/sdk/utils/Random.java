package com.cq.sdk.utils;

/**
 * Created by CuiYaLei on 2016/8/12.
 */
public final class Random {

    private int min;
    private int max;
    private Integer[] list;
    private int count;
    public Random(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public int repetition(){
        if(this.list==null ||this.list.length!=max){
            this.list=new Integer[max];
        }else if(count==max){
            return -1;
        }
        int val;
        while (this.list[(val=Random.value(this.min,this.max))]!=null){}
        this.list[val]=val;
        this.count++;
        return val;
    }
    private final static Random random=new Random(0,100);
    private static int randomCount;
    public static int pseudo(int probability) {
        int val = Random.random.repetition();
        if(val<probability&&++Random.randomCount==probability){
            Random.randomCount=0;
            Random.random.list=null;
        }
        return val;
    }
    public static final int value(int min, int max){
        return (int)(Math.random()*(max-min)+min);
    }
}
