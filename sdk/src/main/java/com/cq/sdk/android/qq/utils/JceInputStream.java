package com.cq.sdk.android.qq.utils;

import com.cq.sdk.android.qq.struct.HeadData;
import com.cq.sdk.android.qq.struct.JceMap;
import com.cq.sdk.utils.ByteSet;
import com.cq.sdk.utils.Number;

/**
 * Created by CuiYaLei on 2016/8/21.
 */
public class JceInputStream {
    private UnPack unPack=new UnPack();
    public void wrap(ByteSet bytes){
        this.unPack.setData(bytes);
    }
    public boolean skipToTag(int tag){
        HeadData headData=new HeadData();
        int step=this.packHead(headData);
        if(headData.type==11) {
            return false;
        }else if(headData.tag<=tag){
            if(headData.tag!=tag){
                return false;
            }
            return true;
        }else{
            return false;
        }
    }
    public void skipField(int type){
        HeadData headData=new HeadData();
        switch (type){
            case 0:
                this.skip(1);
                break;
            case 1:
                this.skip(2);
                break;
            case 2:
                this.skip(4);
                break;
            case 3:
                this.skip(8);
                break;
            case 13:
                int len=0;
                this.unPack.getByte();
                this.readHead(headData,null);
                if(headData.tag==12){

                }else {
                    if (headData.tag == 1) {
                        len = this.unPack.getShort();
                    }else{
                        len=this.unPack.getByte();
                    }
                    this.skip(len);
                }
                break;
            case 8:
                this.readHead(headData,null);
                if(headData.type==12){

                }else{
                    this.readHead(headData,null);
                    int count=headData.type;
                    for(int i=0;i<count;i++){
                        this.readHead(headData,null);
                        this.skipField(headData.type);
                        this.readHead(headData,null);
                        this.skipField(headData.type);
                    }
                }
                break;
            case 12:
                break;
            case 6:
                len=this.unPack.getByte();
                this.skip(len);
                break;
            case 9:
                this.readHead(headData,null);
                if(headData.type==12){

                }else{
                    int count=0;
                    if(headData.type==1){
                        count=this.unPack.getShort();
                    }else{
                        count=this.unPack.getByte();
                    }
                    for(int i=0;i<count;i++){
                        this.readHead(headData,null);
                        this.skipField(headData.type);
                    }
                }
                break;
            case 7:
                len=this.unPack.getInt();
                this.skip(len);
                break;
            case 10:
                this.readHead(headData,null);
                while (headData.type!=11){
                    this.skipField(headData.type);
                    this.readHead(headData,null);
                }
                break;
        }
    }
    public void skip(int step){
        this.unPack.getBin(step);
    }
    public int packHead(HeadData headData){
        return this.readHead(headData,this.unPack.getAll());
    }
    public int readHead(HeadData headData, ByteSet bytes){
        if(bytes==null){
            byte b=this.unPack.getByte();
            headData.type= (byte) (b & 15);
            headData.tag=(b&240)<<4;
            if(headData.tag==15){
                headData.tag=this.unPack.getByte();
                return 2;
            }
            return 1;
        }else{
            UnPack tmpUnPack=new UnPack();
            Pack pack=new Pack();
            pack.empty();
            pack.setBin(bytes);
            tmpUnPack.setData(pack.getAll());
            byte b=tmpUnPack.getByte();
            headData.type= (byte) (b & 15);
            headData.tag=(b&240)<<4;
            if(headData.tag==15){
                headData.tag=this.unPack.getByte();
                return 2;
            }
            return 1;
        }
    }
    public String readObj(int type){
        String val=null;
        switch (type){
            case Constants.TYPE_BYTE:
                val=new String(new byte[]{this.unPack.getByte()});
                break;
            case Constants.TYPE_SHORT:
                val=new String(Number.unsignedShortToByte2(this.unPack.getShort()).getByteSet());
                break;
            case Constants.TYPE_INT:
                val=new String(Number.intToByte4(this.unPack.getInt()).getByteSet());
                break;
            case Constants.TYPE_LONG:
                val=new String(Number.longToByte8(this.unPack.getLong()).getByteSet());
                break;
            case Constants.TYPE_SIMPLE_LIST:
                this.unPack.getByte();
                int len;
                if(this.unPack.getByte()==Constants.TYPE_SHORT){
                    len=this.unPack.getShort();
                }else{
                    len=this.unPack.getByte();
                }
                val= this.unPack.getBin(len).toStringHex();
                break;
            case Constants.TYPE_MAP:
                HeadData headData=new HeadData();
                this.readHead(headData,null);
                StringBuilder sb=new StringBuilder("{");
                if(type==Constants.TYPE_ZERO_TAG){

                }else{
                    this.readHead(headData,null);
                    for(int i=0;i<type;i++){
                        this.readHead(headData,null);
                        sb.append("k=");
                        sb.append(this.readObj(headData.type));
                        this.readHead(headData,null);
                        sb.append(", v=");
                        this.readHead(headData,null);
                        sb.append(" ");
                    }
                }
                sb.append("}");
                val=sb.toString();
                break;
            case Constants.TYPE_ZERO_TAG:
                val="0";
                break;
            case Constants.TYPE_STRING1:
                len=this.unPack.getByte();
                val=new String(this.unPack.getBin(len).getByteSet());
                break;
            case Constants.TYPE_LIST:
                headData=new HeadData();
                this.readHead(headData,null);
                sb=new StringBuilder("{");
                if(type==Constants.TYPE_ZERO_TAG){

                }else{
                    int count;
                    if(type==Constants.TYPE_SHORT){
                        count=this.unPack.getShort();
                    }else{
                        count=this.unPack.getByte();
                    }
                    for(int i=0;i<count;i++){
                        this.readHead(headData,null);
                        sb.append(this.readObj(headData.type));
                        if(i!=count-1){
                            sb.append(",");
                        }
                    }
                    sb.append("}");
                }
                break;
            case Constants.TYPE_STRING4:
                len=this.unPack.getInt();
                val=new String(this.unPack.getBin(len).getByteSet());
                break;
            case Constants.TYPE_STRUCT_BEGIN:
                headData=new HeadData();
                this.readHead(headData,null);
                sb=new StringBuilder();
                while (headData.type!=Constants.TYPE_STRUCT_END){
                    sb.append(this.readObj(headData.type));
                    this.readHead(headData,null);
                }
                break;
        }
        return val;

    }
    public byte readByte(int tag){
        return (byte) this.readShort(tag);
    }
    public short readShort(int tag){
        return (short) this.readLong(tag);
    }
    public int readInt(int tag){
        return (int) this.readLong(tag);
    }
    public long readLong(int tag){
        long paramByte = 0;
        if(!this.skipToTag(tag)){
            return paramByte;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        if(headData.type==Constants.TYPE_ZERO_TAG){
            paramByte=0;
        }else if(headData.type==Constants.TYPE_BYTE){
            paramByte=this.unPack.getByte();
        }else if(headData.type==Constants.TYPE_SHORT){
            paramByte= this.unPack.getShort();
        }else if(headData.type==Constants.TYPE_INT){
            paramByte=this.unPack.getInt();
        }else if(headData.type==Constants.TYPE_LONG){
            paramByte=this.unPack.getLong();
        }
        return paramByte;
    }
    public String readString(int tag){
        String val=null;
        if(!this.skipToTag(tag)){
            return val;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        if(headData.type==Constants.TYPE_ZERO_TAG){
        }else if(headData.type==Constants.TYPE_STRING1){
            int len=this.unPack.getByte();
            val=new String(this.unPack.getBin(len).getByteSet());
        }else if(headData.type==Constants.TYPE_STRING4){
            int len=this.unPack.getInt();
            val=new String(this.unPack.getBin(len).getByteSet());
        }
        return val;
    }
    public ByteSet readSimpleList(int tag){
        ByteSet val=new ByteSet();
        if(!this.skipToTag(tag)){
            return val;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        if(headData.type==Constants.TYPE_SIMPLE_LIST){
            this.unPack.getByte();
            int type=this.unPack.getByte();
            int len=0;
            if(type==Constants.TYPE_ZERO_TAG){
                return val;
            }else if(type==Constants.TYPE_SHORT){
                len=this.unPack.getShort();
            }else {
                len=this.unPack.getByte();
            }
            val=this.unPack.getBin(len);
        }
        return val;
    }
    public String[] readList(int tag){
        if(!this.skipToTag(tag)){
            return null;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        if(headData.type==Constants.TYPE_ZERO_TAG){

        }else if(headData.type==Constants.TYPE_LIST){
            int count=this.readShort(0);
            String[] array=new String[count];
            for(int i=0;i<count;i++){
                this.readHead(headData,null);
                array[i]=this.readObj(headData.type);
            }
            return array;
        }
        return null;
    }
    public byte readType(){
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        return headData.type;
    }
    public byte readToTag(int tag){
        if(!this.skipToTag(tag)){
            return -1;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        return headData.type;
    }
    public void skipToEnd(){
        for(int i=0;i<100;i++){
            HeadData headData=new HeadData();
            String te=this.unPack.getAll().toStringHex();
            int step=this.readHead(headData,null);
            te=this.unPack.getAll().toStringHex();
            if(headData.type==Constants.TYPE_ZERO_TAG){
                continue;
            }else if(headData.type==Constants.TYPE_STRUCT_END){
                break;
            }
            this.skipField(headData.type);
        }
    }
    public JceMap[] readMap(int tag){
        if(!this.skipToTag(tag)){
            return null;
        }
        HeadData headData=new HeadData();
        this.readHead(headData,null);
        if(headData.type!=Constants.TYPE_MAP){
            return null;
        }
        this.readHead(headData,null);
        if(headData.type==Constants.TYPE_ZERO_TAG){

        }else{
            int count;
            if(headData.type==Constants.TYPE_SHORT){
                count=this.unPack.getShort();
            }else{
                count=this.unPack.getByte();
            }
            JceMap[] jceMaps=new JceMap[count];
            for(int i=0;i<count;i++){
                this.readHead(headData,null);
                String value=this.readObj(headData.type);
                JceMap jceMap=new JceMap();
                jceMap.keyType=headData.type;
                jceMap.key= ByteSet.parse(value.getBytes());
                this.readHead(headData,null);
                value=this.readObj(headData.type);
                jceMap.valType=headData.type;
                jceMap.val= ByteSet.parse(value.getBytes());
                jceMaps[i]=jceMap;
            }
            return jceMaps;
        }
        return null;
    }
    public ByteSet getAll(){
        return this.unPack.getAll();
    }
}
