package com.cq.sdk.potential.sql.tx.utils;

/**
 * Created by admin on 2016/9/5.
 */
public class TransactionMethod {
    private String name;
    private Propagation propagation;
    private Isolation isolation;
    private boolean readOnly;
    private int timeout;
    private String rollbackFor;
    private String noRollbackFor;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Propagation getPropagation() {
        return propagation;
    }

    public void setPropagation(Propagation propagation) {
        this.propagation = propagation;
    }

    public Isolation getIsolation() {
        return isolation;
    }

    public void setIsolation(Isolation isolation) {
        this.isolation = isolation;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getRollbackFor() {
        return rollbackFor;
    }

    public void setRollbackFor(String rollbackFor) {
        this.rollbackFor = rollbackFor;
    }

    public String getNoRollbackFor() {
        return noRollbackFor;
    }

    public void setNoRollbackFor(String noRollbackFor) {
        this.noRollbackFor = noRollbackFor;
    }
}
