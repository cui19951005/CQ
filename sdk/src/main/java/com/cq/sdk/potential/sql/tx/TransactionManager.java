package com.cq.sdk.potential.sql.tx;

import com.cq.sdk.potential.annotation.Autowired;
import com.cq.sdk.potential.sql.tx.utils.TransactionMethod;
import com.cq.sdk.potential.utils.PackUtils;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by admin on 2016/9/5.
 */
public class TransactionManager {
    @Autowired
    private Transaction transaction;
    private Pattern packPattern;
    private String packName;
    private List<TransactionMethod> transactionMethodList;

    public Transaction getTransaction() {
        return transaction;
    }

    public void setPackName(String name){
        this.packName=name;
        this.packPattern=PackUtils.generateNamePattern(name);
    }

    public String getPackName() {
        return packName;
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
