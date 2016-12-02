package com.cq.sdk.test;
import com.cq.sdk.utils.ObjectUtils;
import com.cq.sdk.utils.Random;
import com.sun.glass.ui.Size;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admin on 2016/10/21.
 */
public class Block {
    private Size size;
    private List<Point> position;
    private int type;
    private int style;
    private Color color;
    private Point basePoint;
    private Block(Size size,List<Point> position, int type, int style) {
        this.size=size;
        this.position = position;
        this.type = type;
        this.style = style;
        this.color=Block.getColor(type);
        this.basePoint=new Point();
    }
    public Block add(Point point){
        for(Point p : this.position){
            p.setLocation(p.getX()+point.getX(),p.getY()+point.getY());
        }
        return this;
    }
    public Block addPoint(Point point){
        if(basePoint==null){
            basePoint=point;
        }else{
            basePoint.setLocation(basePoint.getX()+point.getX(),basePoint.getY()+point.getY());
        }
        return this;
    }
    public Block draw(Graphics g){
        g.setColor(this.color);
        for(Point p : position){
            g.drawRect(p.x*this.size.width+this.basePoint.x,p.y*this.size.height+this.basePoint.y,this.size.width,this.size.height);
        }
        return this;
    }

    public List<Point> getPosition() {
        List<Point> list=new ArrayList<>();
        for(int i=0;i<this.position.size();i++){//获取四个方向最靠边的
            list.add(new Point(this.position.get(i).x*this.size.width,this.position.get(i).y*this.size.height));
        }
        return list;
    }
    public static int getStyleNumber(int type){
        return Block.BLOCK_MAP.get(type).size();
    }
    public static Color getColor(int type){
        switch (type){
            /*case 0:
                return new Color(22, 4, 213);
            case 1:
                return new Color(171, 0, 255);
            case 2:
                return new Color(22, 255, 9);
            case 3:
                return new Color(0, 255, 211);*/
            default:
                return new Color(0, 0, 0);
        }
    }
    public static Block createRandom(Size size){
        int type=Random.value(0,Block.BLOCK_MAP.size());
        return Block.create(size,type,Random.value(0,Block.getStyleNumber(type)));
    }
    public final static Map<Integer,Map<Integer,ArrayList<Point>>> BLOCK_MAP=new HashMap<Integer,Map<Integer,ArrayList<Point>>>(){
        {
            this.put(0,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(1,0));
                    this.add(new Point(2,0));
                    this.add(new Point(3,0));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(0,1));
                    this.add(new Point(0,2));
                    this.add(new Point(0,3));
                }});
            }});
            this.put(1,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(0,1));
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(0,1));
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                }});
            }});
            this.put(2,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(1,0));
                    this.add(new Point(2,0));
                    this.add(new Point(1,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(0,1));
                    this.add(new Point(0,2));
                    this.add(new Point(1,1));
                }});
                this.put(2,new ArrayList<Point>(){{
                    this.add(new Point(1,0));
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                    this.add(new Point(2,1));
                }});
                this.put(3,new ArrayList<Point>(){{
                    this.add(new Point(0,1));
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                    this.add(new Point(1,2));
                }});
            }});
            this.put(3,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                    this.add(new Point(1,0));
                    this.add(new Point(2,0));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                    this.add(new Point(1,2));
                }});
            }});
            this.put(4,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                    this.add(new Point(2,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                    this.add(new Point(0,1));
                    this.add(new Point(0,2));
                }});
            }});
            this.put(5,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//━┓
                    this.add(new Point(1,0));
                    this.add(new Point(2,0));
                    this.add(new Point(2,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(1,0));//  ┃
                    this.add(new Point(1,1));//  ┛
                    this.add(new Point(1,2));
                    this.add(new Point(0,2));
                }});
                this.put(2,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┗━
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                    this.add(new Point(2,1));
                }});
                this.put(3,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┏
                    this.add(new Point(1,0));//┃
                    this.add(new Point(0,1));
                    this.add(new Point(0,2));
                }});
            }});
            this.put(6,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┏━
                    this.add(new Point(1,0));
                    this.add(new Point(2,0));
                    this.add(new Point(0,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┃
                    this.add(new Point(0,1));//┗
                    this.add(new Point(0,2));
                    this.add(new Point(1,2));
                }});
                this.put(2,new ArrayList<Point>(){{
                    this.add(new Point(0,1));//━┛
                    this.add(new Point(1,1));
                    this.add(new Point(2,1));
                    this.add(new Point(2,0));
                }});
                this.put(3,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┓
                    this.add(new Point(1,0));//┃
                    this.add(new Point(1,1));
                    this.add(new Point(1,2));
                }});
            }});
            this.put(7,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(1,0));//  ┃
                    this.add(new Point(0,1));//━━━
                    this.add(new Point(1,1));//  ┃
                    this.add(new Point(2,1));
                    this.add(new Point(1,2));
                }});

            }});
            this.put(8,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(1,0));//┛
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┗
                    this.add(new Point(0,1));
                    this.add(new Point(1,1));
                }});
                this.put(2,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┏
                    this.add(new Point(1,0));
                    this.add(new Point(0,1));
                }});
                this.put(3,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┓
                    this.add(new Point(1,0));
                    this.add(new Point(1,1));
                }});
            }});
            this.put(9,new HashMap<Integer,ArrayList<Point>>(){{
                this.put(0,new ArrayList<Point>(){{
                    this.add(new Point(0,0));//┃  ┃
                    this.add(new Point(0,1));// ━━
                    this.add(new Point(1,1));//
                    this.add(new Point(2,1));
                    this.add(new Point(2,0));
                }});
                this.put(1,new ArrayList<Point>(){{
                    this.add(new Point(0,0));// ━━
                    this.add(new Point(1,0));//┃
                    this.add(new Point(0,1));// ━━
                    this.add(new Point(0,2));
                    this.add(new Point(1,2));
                }});
                this.put(2,new ArrayList<Point>(){{
                    this.add(new Point(0,0));// ━━
                    this.add(new Point(1,0));//┃  ┃
                    this.add(new Point(2,0));//┃  ┃
                    this.add(new Point(0,1));
                    this.add(new Point(2,1));
                }});
                this.put(3,new ArrayList<Point>(){{
                    this.add(new Point(0,0));// ━━
                    this.add(new Point(1,0));//    ┃
                    this.add(new Point(1,1));// ━━
                    this.add(new Point(1,2));
                    this.add(new Point(0,2));
                }});
            }});

        }
    };

    public static Block create(Size size,int type, int style){
        return new Block(size, (List<Point>) ObjectUtils.clone(Block.BLOCK_MAP.get(type).get(style)),type,style);
    }
    public static int isBorder(Size size,Rectangle rect,List<Point> points,java.util.List<Block> blockList){
        for(Point point : points){
            if(point.x<rect.x){//左
                return 0;
            }else if(point.y<rect.y){//上
                return 1;
            }else if(point.x+size.width>rect.width){//右
                return 2;
            }else if(point.y+size.height>rect.height){//下
                return 3;
            }
            for(Block block : blockList){
                for(Point item : block.getPosition()){
                    if(item.x==point.x && item.y==point.y){
                        return 0;
                    }
                }
            }
        }
        return -1;
    }
    public void left(Rectangle rectangle,java.util.List<Block> blockList){
        this.add(new Point(-1,0));
        if(this.isBorder(this.size,rectangle,this.getPosition(),blockList)!=-1){
            this.right(rectangle,blockList);
        }
    }
    public void right(Rectangle rectangle,java.util.List<Block> blockList){
        this.add(new Point(1,0));
        if(this.isBorder(this.size,rectangle,this.getPosition(),blockList)!=-1){
            this.left(rectangle,blockList);
        }
    }
    public void down(Rectangle rectangle,java.util.List<Block> blockList){
        this.add(new Point(0,1));
        if(this.isBorder(this.size,rectangle,this.getPosition(),blockList)!=-1){
            this.add(new Point(0,-1));
        }
    }
    public void shape(Rectangle rectangle,java.util.List<Block> blockList){
        this.shape(1);
        if(this.isBorder(this.size,rectangle,this.getPosition(),blockList)!=-1){
            this.shape(-1);
        }
    }
    private void shape(int value){
        int style=this.style;
        int count=Block.getStyleNumber(this.type);
        if(style+value>=count){
            style=0;
        }else if(style+value<0){
            style=count-1;
        }else{
            style+=value;
        }
        Block block=this.create(this.size,this.type,style);
        block.addPoint(this.basePoint);
        Point[] points=this.position.toArray(new Point[this.position.size()]);
        ObjectUtils.copy(block,this);
        Point min=new Point(Integer.MAX_VALUE,Integer.MAX_VALUE);
        for(Point item : points){
            if(item.y<min.y){
                min.y=item.y;
            }
            if(item.x<min.x){
                min.x=item.x;
            }
        }
        this.add(min);
    }
    public void removeY(int y){
        for(int i=0;i<this.getPosition().size();i++){
            if(this.getPosition().get(i).y==y){
                this.position.remove(i);
                --i;
            }
        }
    }
    public void less(int y,int add){
        List<Point> list=this.getPosition();
        for(int i=0;i<list.size();i++){
            if(list.get(i).y<y){
                this.position.get(i).y+=add;
            }
        }
    }
}
