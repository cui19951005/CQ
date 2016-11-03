package com.cq.sdk.net.ftp;

/**
 * Created by admin on 2016/10/31.
 */
public interface FtpUser {
    boolean isAnonymous();
    int login(String user,String password);
    int passiveModePort();
    boolean checkFileVisible(String user,String path);
    boolean checkFileDownload(String user,String path);
    boolean checkFileUpload(String user,String path);
    boolean fileRename(String user,String path);
    boolean createFolder(String user,String path);
    boolean deleteFolder(String user,String path);
}
