package com.cq.sdk.potential.sql.tx.utils;

/**
 * Created by admin on 2016/9/5.
 */
public enum Isolation {
    DEFAULT(),
    READ_UNCOMMITTED(),
    READ_COMMITTED(),
    REPEATABLE_READ(),
    SERIALIZABLE()
}
