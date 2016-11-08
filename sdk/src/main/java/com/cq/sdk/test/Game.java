package com.cq.sdk.test;

import com.sun.glass.ui.Size;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.cq.sdk.utils.Timer;
/**
 * Created by admin on 2016/10/24.
 */
public class Game {
    private Block block;
    private Size blockSize;
    private Point base;
    private Size size;
    private List<Block> blockList;
    private Rectangle pane;
    private int integral;
    private int timerId;
    public Game( Size blockSize, Point base,Rectangle rectangle) {
        this.blockSize=blockSize;
        this.base=base;
        this.pane=rectangle;
        this.size=new Size(this.getPane().width/blockSize.width,this.getPane().height/blockSize.height);
    }

    public void start(Timer.TimerTask timerTask,int interval){
        this.create();
        this.timerId=Timer.open((int id)-> {
            this.block.add(new Point(0,1));
            if(Block.isBorder(this.blockSize,this.getPane(),this.block.getPosition(),this.removeListEle(this.blockList,this.block))!=-1){
                this.block.add(new Point(0,-1));
                this.eliminate();
                this.accelerate();
                this.create();
            }
            timerTask.execute(id);
        },interval);
    }
    private void accelerate(){
        Timer.set(this.timerId,1000-this.getIntegral()/10);
    }
    private void eliminate(){
        Map<Integer,Integer> map=new HashMap<>();
        for(Block block : this.blockList){
            for(Point point : block.getPosition()){
                Integer value=map.get(point.y);
                if(value==null){
                    map.put(point.y,1);
                }else{
                    map.put(point.y,++value);
                }
            }
        }
        Supplier<Stream<Map.Entry<Integer,Integer>>> supplier=()->map.entrySet().stream().filter(entry -> entry.getValue() == this.size.width);
        supplier.get().forEach(entry -> {
            for (Block block : this.blockList) {
                block.removeY(entry.getKey());
                block.less(entry.getKey(), 1);
            }
        });
        long count=supplier.get().count();
        if(count>0) {
            this.integral += count * 100 + (count - 1) * 50;
        }
    }
    public void create(){
        this.block=Block.createRandom(this.blockSize);
        this.block.addPoint(base);
        if(this.blockList==null){
            this.blockList=new ArrayList<>();
        }
        this.blockList.add(this.block);
    }

    public void draw(Graphics g){
        if(this.blockList==null){
            return ;
        }
        for(int i=0;i<this.blockList.size();i++){
            this.blockList.get(i).draw(g);
        }
    }
    private Rectangle getPane(){
        return this.pane;
    }
    public void operation(int type){
        List<Block> list=this.removeListEle(this.blockList,this.block);
        switch (type){
            case 0:
                this.block.left(this.getPane(),list);
                break;
            case 1:
                this.block.right(this.getPane(),list);
                break;
            case 2:
                this.block.shape(this.getPane(),list);
                break;
            case 3:
                this.block.down(this.getPane(),list);
                break;
        }
    }
    private <T> List<T> removeListEle(List<T> list,Object object){
        Object[] objects=list.toArray();
        List objList=new ArrayList();
        for(Object obj : objects){
            if(obj!=object) {
                objList.add(obj);
            }
        }
        return objList;
    }
    public int getIntegral() {
        return integral;
    }
}
