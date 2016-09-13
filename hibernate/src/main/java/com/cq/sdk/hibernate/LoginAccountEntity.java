package com.cq.sdk.hibernate;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by admin on 2016/9/7.
 */
@Entity
@Table(name = "LoginAccount", schema = "fundstore")
public class LoginAccountEntity {
    private long pid;
    private String accountName;
    private String password;
    private String loginStatus;
    private String accStatus;
    private String loginSource;
    private String userToken;
    private String loginChannel;
    private String remark;
    private String lockStatus;
    private Timestamp unLockTime;
    private String createBy;
    private Timestamp createAt;
    private String modifyBy;
    private Timestamp modifyAt;

    @Id
    @Column(name = "pid")
    public long getPid() {
        return pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    @Basic
    @Column(name = "accountName")
    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    @Basic
    @Column(name = "password")
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Basic
    @Column(name = "loginStatus")
    public String getLoginStatus() {
        return loginStatus;
    }

    public void setLoginStatus(String loginStatus) {
        this.loginStatus = loginStatus;
    }

    @Basic
    @Column(name = "accStatus")
    public String getAccStatus() {
        return accStatus;
    }

    public void setAccStatus(String accStatus) {
        this.accStatus = accStatus;
    }

    @Basic
    @Column(name = "loginSource")
    public String getLoginSource() {
        return loginSource;
    }

    public void setLoginSource(String loginSource) {
        this.loginSource = loginSource;
    }

    @Basic
    @Column(name = "userToken")
    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    @Basic
    @Column(name = "loginChannel")
    public String getLoginChannel() {
        return loginChannel;
    }

    public void setLoginChannel(String loginChannel) {
        this.loginChannel = loginChannel;
    }

    @Basic
    @Column(name = "remark")
    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Basic
    @Column(name = "lockStatus")
    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    @Basic
    @Column(name = "unLockTime")
    public Timestamp getUnLockTime() {
        return unLockTime;
    }

    public void setUnLockTime(Timestamp unLockTime) {
        this.unLockTime = unLockTime;
    }

    @Basic
    @Column(name = "createBy")
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    @Basic
    @Column(name = "createAt")
    public Timestamp getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Timestamp createAt) {
        this.createAt = createAt;
    }

    @Basic
    @Column(name = "modifyBy")
    public String getModifyBy() {
        return modifyBy;
    }

    public void setModifyBy(String modifyBy) {
        this.modifyBy = modifyBy;
    }

    @Basic
    @Column(name = "modifyAt")
    public Timestamp getModifyAt() {
        return modifyAt;
    }

    public void setModifyAt(Timestamp modifyAt) {
        this.modifyAt = modifyAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LoginAccountEntity that = (LoginAccountEntity) o;

        if (pid != that.pid) return false;
        if (accountName != null ? !accountName.equals(that.accountName) : that.accountName != null) return false;
        if (password != null ? !password.equals(that.password) : that.password != null) return false;
        if (loginStatus != null ? !loginStatus.equals(that.loginStatus) : that.loginStatus != null) return false;
        if (accStatus != null ? !accStatus.equals(that.accStatus) : that.accStatus != null) return false;
        if (loginSource != null ? !loginSource.equals(that.loginSource) : that.loginSource != null) return false;
        if (userToken != null ? !userToken.equals(that.userToken) : that.userToken != null) return false;
        if (loginChannel != null ? !loginChannel.equals(that.loginChannel) : that.loginChannel != null) return false;
        if (remark != null ? !remark.equals(that.remark) : that.remark != null) return false;
        if (lockStatus != null ? !lockStatus.equals(that.lockStatus) : that.lockStatus != null) return false;
        if (unLockTime != null ? !unLockTime.equals(that.unLockTime) : that.unLockTime != null) return false;
        if (createBy != null ? !createBy.equals(that.createBy) : that.createBy != null) return false;
        if (createAt != null ? !createAt.equals(that.createAt) : that.createAt != null) return false;
        if (modifyBy != null ? !modifyBy.equals(that.modifyBy) : that.modifyBy != null) return false;
        if (modifyAt != null ? !modifyAt.equals(that.modifyAt) : that.modifyAt != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (pid ^ (pid >>> 32));
        result = 31 * result + (accountName != null ? accountName.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (loginStatus != null ? loginStatus.hashCode() : 0);
        result = 31 * result + (accStatus != null ? accStatus.hashCode() : 0);
        result = 31 * result + (loginSource != null ? loginSource.hashCode() : 0);
        result = 31 * result + (userToken != null ? userToken.hashCode() : 0);
        result = 31 * result + (loginChannel != null ? loginChannel.hashCode() : 0);
        result = 31 * result + (remark != null ? remark.hashCode() : 0);
        result = 31 * result + (lockStatus != null ? lockStatus.hashCode() : 0);
        result = 31 * result + (unLockTime != null ? unLockTime.hashCode() : 0);
        result = 31 * result + (createBy != null ? createBy.hashCode() : 0);
        result = 31 * result + (createAt != null ? createAt.hashCode() : 0);
        result = 31 * result + (modifyBy != null ? modifyBy.hashCode() : 0);
        result = 31 * result + (modifyAt != null ? modifyAt.hashCode() : 0);
        return result;
    }
}
