package com.cq.sdk.service.android.qq.utils;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public class  ErrorCode {
    public enum User{
        USER_ACCOUNTFORMAT(1001000,"帐号格式不正确"),USER_LOGINFAILED(1001001,"用户登录失败");
        private int code;
        private String msg;
        User(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }
    public enum System{
        NETWORK_CONNECTION_FAILED(4001000,"网络连接失败"),SERVER_CONNECTION_FAILED(4001000,"服务器连接失败");
        private int code;
        private String msg;
        System(int code,String msg){
            this.code=code;
            this.msg=msg;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}
