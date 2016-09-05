package com.cq.sdk.service.potential.sql;

import com.cq.sdk.service.potential.sql.utils.TransactionMethod;

import javax.sql.DataSource;
import java.util.List;

/**
 * Created by admin on 2016/9/5.
 */
public class TransactionManager {
    private DataSource dataSource;
    private String packName;
    private List<TransactionMethod> transactionMethodList;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public String getPackName() {
        return packName;
    }

    public void setPackName(String packName) {
        this.packName = packName;
    }

    public List<TransactionMethod> getTransactionMethodList() {
        return transactionMethodList;
    }

    public void setTransactionMethodList(List<TransactionMethod> transactionMethodList) {
        this.transactionMethodList = transactionMethodList;
    }
}
