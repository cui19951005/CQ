package com.cq.sdk.test;

import com.cq.sdk.potential.validate.*;

/**
 * Created by admin on 2016/11/17.
 */
public class User {
    @Required(message = "用户名不能为空")
    @Pattern(value = "\\w{6,15}",message = "用户名输入不正确")
    private String user;
    @Required(message = "密码输入不能为空")
    @Pattern(value = "\\w{6,15}",message = "密码输入不正确")
    private String password;
    @Equals(value = "password",message = "重复密码与密码不正确")
    private String rePwd;
    @Email(message = "邮箱地址输入不正确")
    private String email;
    @DateTime(value = "yyyy-MM-dd HH:mm:ss",message = "时间输入不正确")
    private String date;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePwd() {
        return rePwd;
    }

    public void setRePwd(String rePwd) {
        this.rePwd = rePwd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
