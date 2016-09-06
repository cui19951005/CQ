package com.cq.sdk.service.potential.sql;

import com.cq.sdk.service.potential.sql.utils.TransactionMethod;
import com.cq.sdk.service.utils.PackUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/5.
 */
public class TransactionManager {
    private DataSource dataSource;
    private Pattern packPattern;
    private List<TransactionMethod> transactionMethodList;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void setPackName(String packName) {
       this.packPattern=PackUtils.generatePattern(packName);
    }

    public Pattern getPackPattern() {
        return packPattern;
    }

    public List<TransactionMethod> getTransactionMethodList() {
        return transactionMethodList;
    }

    public void setTransactionMethodList(List<TransactionMethod> transactionMethodList) {
        this.transactionMethodList = transactionMethodList;
    }
}
