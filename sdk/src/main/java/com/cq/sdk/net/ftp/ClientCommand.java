package com.cq.sdk.net.ftp;

/**
 * Created by admin on 2016/10/31.
 */
public interface ClientCommand {
    String USER="USER";
    String PASS="PASS";
    String OPTS="OPTS";
    String SYST="SYST";
    String SITE="SITE";
    String PWD="PWD";
    String NOOP="NOOP";
    String TYPE="TYPE";
    String CWD="CWD";
    String PASV="PASV";
    String PORT="PORT";
    String LIST="LIST";
    String SIZE="SIZE";
    String RETR="RETR";
    String STOR="STOR";
    String DELE="DELE";
    String RNFR="RNFR";
    String RNTO="RNTO";
    String MKD="MKD";
    String RMD="RMD";
    String QUIT="QUIT";
    String CDUP="CDUP";
    String NLST="NLST";
    String XPWD="XPWD";
    String FEAT="FEAT";
    String MDTM="MDTM";
    String EPRT="EPRT";
    String EPSV="EPSV";
}
