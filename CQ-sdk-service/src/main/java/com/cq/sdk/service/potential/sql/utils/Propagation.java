package com.cq.sdk.service.potential.sql.utils;

/**
 * Created by admin on 2016/9/5.
 */
public enum Propagation {
    REQUIRED(),
    SUPPORTS(),
    MANDATORY(),
    REQUIRES_NEW(),
    NOT_SUPPORTED,
    NEVER,
    NESTED
}
