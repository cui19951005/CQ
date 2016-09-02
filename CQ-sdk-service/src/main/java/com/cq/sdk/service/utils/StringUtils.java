package com.cq.sdk.service.utils;

import java.io.File;

/**
 * Created by admin on 2016/9/2.
 */
public class StringUtils {
    public static final String filePathConvertPack(File file){
        String root=StringUtils.class.getResource("/").getFile();
        root=root.substring(1);
        String pack=file.getAbsolutePath().substring(root.length()).replace("\\","/").replace("/",".");
        pack=pack.substring(0,pack.lastIndexOf("."));
        return pack;
    }
}
