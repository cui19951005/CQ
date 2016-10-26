package com.cq.sdk.test;

import com.sun.glass.ui.Size;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Created by admin on 2016/10/24.
 */
public class Game {
    private Block block;
    private JFrame frame;
    private Size blockSize;
    private Point base;
    private Size size;
    private java.util.List<Block> blockList;
    public Game(JFrame frame,Size blockSize,Point base) {
        this.frame = frame;
        this.blockSize=blockSize;
        this.base=base;
        this.size=new Size(this.getPane().width/blockSize.width,this.getPane().height/blockSize.height);
    }

    public void start(){
        this.create();
        com.cq.sdk.utils.Timer.open(()-> {
            this.block.add(new Point(0,1));
            if(Block.isBorder(this.blockSize,this.getPane(),this.block.getPosition(),this.removeListEle(this.blockList,this.block))!=-1){
                this.block.add(new Point(0,-1));
                this.eliminate();
                this.create();
            }
            this.frame.repaint();
        },500);
    }
    public void eliminate(){
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
        for(Map.Entry<Integer,Integer> entry : map.entrySet()){
            if(entry.getValue()==this.size.width){
                for(Block block : this.blockList){
                    block.removeY(entry.getKey());
                    block.less(entry.getKey(),1);
                }
            }
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
        for(Block block : this.blockList){
            block.draw(g);
        }
    }
    private Rectangle getPane(){
        return new Rectangle(new Point(),this.frame.getContentPane().getSize());
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
        this.frame.repaint();
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
}
