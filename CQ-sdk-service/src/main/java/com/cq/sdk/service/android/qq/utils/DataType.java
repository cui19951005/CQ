package com.cq.sdk.service.android.qq.utils;

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
}
