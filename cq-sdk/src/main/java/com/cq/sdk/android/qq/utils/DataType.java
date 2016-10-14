package com.cq.sdk.android.qq.utils;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public class DataType {
    public enum UserState{
        OffLine(-1,"离线"),Login(0,"登录中"),OnLine(1,"在线"),Success(2,"验证成功"),Verification(3,"需要验证码") ;
        private int state;
        private String msg;
        UserState(int state,String msg){
            this.state=state;
            this.msg=msg;
        }

        public int getState() {
            return state;
        }

        public void setState(int state) {
            this.state = state;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    public enum MsgType{
        Group(0,"群列表"),Friend(1,"好友"),GroupMember(2,"群成员"),LoginEnd(3,"登录结束");
        private int type;
        private String msg;

        MsgType(int type, String msg) {
            this.type = type;
            this.msg = msg;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
}
