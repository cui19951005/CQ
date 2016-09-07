package com.cq.sdk.service.potential.sql.tx;

import com.cq.sdk.service.potential.sql.utils.TransactionMethod;
import com.cq.sdk.service.utils.PackUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/5.
 */
public class TransactionManager {
    private Transaction transaction;
    private Pattern packPattern;
    private List<TransactionMethod> transactionMethodList;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setPackName(String name){
        this.packPattern=PackUtils.generatePattern(name);
    }
    public Pattern getPackPattern() {
        return packPattern;
    }

    public void setPackPattern(Pattern packPattern) {
        this.packPattern = packPattern;
    }

    public List<TransactionMethod> getTransactionMethodList() {
        return transactionMethodList;
    }

    public void setTransactionMethodList(List<TransactionMethod> transactionMethodList) {
        this.transactionMethodList = transactionMethodList;
    }
}
