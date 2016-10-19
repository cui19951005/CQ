package com.cq.sdk.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by CuiYaLei on 2016/8/13.
 */
public final class Validate {
    public static final boolean number(String val){
        Pattern pattern=Pattern.compile("\\d+");
        Matcher matcher=pattern.matcher(val);
        return matcher.matches();
    }
}
