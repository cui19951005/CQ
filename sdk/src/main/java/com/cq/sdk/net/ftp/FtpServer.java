package com.cq.sdk.net.ftp;

import com.cq.sdk.net.socket.SocketReceiveData;
import com.cq.sdk.net.socket.SocketServer;
import com.cq.sdk.net.socket.SocketSession;
import com.cq.sdk.utils.*;
import com.cq.sdk.utils.File;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by admin on 2016/10/31.
 */
public class FtpServer implements SocketReceiveData {
    private int port;
    private SocketServer socketServer;
    private ServerSocket fileServer;
    private String language = "中文";
    private String encoding = "gbk";
    private String basePath;
    private String newline = "\r\n";
    private FtpUser ftpUser;

    public FtpServer(int port) {
        this.port = port;
    }

    private void sendCommand(Socket socket, int command, String text) {
        Logger.info("command:{0},text:{1}", command, text);
        this.socketServer.sendText(socket, String.valueOf(command) + " " + text + this.newline);
    }

    private void sendCommand(Socket socket, int command) {
        this.sendCommand(socket, command, Constant.message.get(this.language).get(command));
    }

    public void startup() {
        try {
            this.socketServer = new SocketServer(port);
            this.fileServer = new SocketServer(this.ftpUser.passiveModePort());
            this.socketServer.setEncoding(this.encoding);
            this.socketServer.startup(this);
        } catch (IOException e) {
            Logger.info("ftp server startup error", e);
        }
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }

    public void setBasePath(String basePath) {
        basePath = basePath.replace("\\", "/");
        if (basePath.charAt(basePath.length() - 1) != '/') {
            basePath += "/";
        }
        this.basePath = basePath;
    }

    public void setFtpUser(FtpUser ftpUser) {
        this.ftpUser = ftpUser;
    }

    @Override
    public void connection(SocketSession socketSession) {
        this.sendCommand(socketSession.getSocket(), ServerCommand.ServerReady.getCode());
    }

    @Override
    public void receive(SocketSession session, ByteSet byteSet, String host, int port) {
        try {
            String text = new String(byteSet.getByteSet(), Sys.getSystemEncoding()).replace("\r\n", "").replace("\n", "");
            String[] array=text.split(" ");
            StringBuilder stringBuilder=new StringBuilder();
            stringBuilder.append(array[0]);
            for(int i=1;i<array.length;i++){
                stringBuilder.append(array[i]);
                if(i!=array.length-1) {
                    stringBuilder.append(" ");
                }
            }
            text=stringBuilder.toString();
            Logger.info(text);
            if (this.isCommand(text,ClientCommand.USER)) {
                String user = text.substring(ClientCommand.USER.length());
                session.setAttribute(Constant.USER, user);
                session.setAttribute(Constant.PATH, this.basePath);
                if (this.ftpUser.isAnonymous()) {
                    this.sendCommand(session.getSocket(), ServerCommand.LoginSuccess.getCode());
                } else {
                    this.sendCommand(session.getSocket(), ServerCommand.RequestPassword.getCode());
                }
            } else if (this.isCommand(text,ClientCommand.PASS)) {
                String password = text.substring(ClientCommand.PASS.length());
                int result = this.ftpUser.login(session.getAttribute(Constant.USER).toString(), password);
                if (result == 0) {
                    this.sendCommand(session.getSocket(), ServerCommand.LoginSuccess.getCode());
                } else if (result == 1) {
                    this.sendCommand(session.getSocket(), ServerCommand.RequestPassword.getCode());
                } else if (result == -1) {
                    this.sendCommand(session.getSocket(), ServerCommand.InvaildCommand.getCode(),Constant.message.get(this.language).get(Constant.USER_SUSPEND));
                }
            } else if (this.isCommand(text,ClientCommand.OPTS)) {
                String state = text.substring(ClientCommand.OPTS.length());
                if (state.equals("off")) {
                    this.encoding = "gbk";
                    this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
                } else if (state.equals("no")) {
                    this.encoding = "utf-8";
                    this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
                } else {
                    this.sendCommand(session.getSocket(), ServerCommand.ErrorParam.getCode());
                }
            } else if (this.isCommand(text,ClientCommand.SYST)) {
                this.sendCommand(session.getSocket(), ServerCommand.SystemTypeReply.getCode(), Sys.getOSName().toString());
            } else if (this.isCommand(text,ClientCommand.SITE)) {
                this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
            } else if (this.isCommand(text,ClientCommand.FEAT)) {
                this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
            } else if (this.isCommand(text,ClientCommand.PWD)
                    ||
                    this.isCommand(text,ClientCommand.XPWD)
                    ) {
                String pwd = session.getAttribute(Constant.PATH).toString();
                pwd = "/" + pwd.substring(this.basePath.length());
                this.sendCommand(session.getSocket(), ServerCommand.NowPathName.getCode(), "\"" + pwd + "\"");
            } else if (this.isCommand(text,ClientCommand.NOOP)) {
                this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
            } else if (this.isCommand(text,ClientCommand.TYPE)) {
                this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
            } else if (this.isCommand(text,ClientCommand.CWD)) {
                String path = text.substring(ClientCommand.CWD.length()).replace("\\", "/");
                if (path.length() == 0) {
                    return;
                }
                String absPath = this.path(session.getAttribute(Constant.PATH).toString(),path);
                if(absPath==null){
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode());
                }else {
                    if (absPath.charAt(absPath.length() - 1) != '/') {
                        absPath += "/";
                    }
                    File file = new File(absPath);
                    if (file.exists()) {
                        session.setAttribute(Constant.PATH, absPath);
                        this.sendCommand(session.getSocket(), ServerCommand.FileSuccess.getCode());
                    } else {
                        this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode());
                    }
                }
            } else if (this.isCommand(text,ClientCommand.PASV)) {
                try {
                    String[] ipArray = session.getSocket().getLocalAddress().getHostAddress().split("\\.");
                    int passivePort = this.ftpUser.passiveModePort();
                    StringBuilder sb = new StringBuilder(Constant.message.get(this.language).get(ServerCommand.PassiveMode.getCode()));
                    sb.append("(");
                    sb.append(Str.join(",", ipArray));
                    sb.append(",");
                    sb.append(passivePort / 256);
                    sb.append(",");
                    sb.append(passivePort % 256);
                    sb.append(")");
                    this.sendCommand(session.getSocket(), ServerCommand.PassiveMode.getCode(), sb.toString());
                    session.setAttribute(Constant.FILE_SOCKET, this.fileServer.accept());
                } catch (IOException e) {
                    Logger.error("get account error", e);
                }
            } else if (this.isCommand(text,ClientCommand.PORT)) {
                String[] ipArray = text.substring(ClientCommand.PORT.length()).split(",");
                try {
                    session.setAttribute(Constant.FILE_SOCKET, new Socket(Str.join(".", Arrays.copyOf(ipArray, 4)), Integer.valueOf(ipArray[4]) * 256 + Integer.valueOf(ipArray[5])));
                    this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if(this.isCommand(text,ClientCommand.EPRT)){
                String[] ipArray=text.substring(ClientCommand.EPRT.length()).split("\\|");
                try {
                    if (ipArray[1].equals("1")) {//ip4
                        session.setAttribute(Constant.FILE_SOCKET, new Socket(ipArray[2], Integer.valueOf(ipArray[3])));
                    }else if(ipArray[1].equals("2")){//ip6
                        session.setAttribute(Constant.FILE_SOCKET, new Socket(ipArray[2], Integer.valueOf(ipArray[3])));
                    }
                    this.sendCommand(session.getSocket(),ServerCommand.Success.getCode());
                }catch (IOException e){
                    e.printStackTrace();
                }
            }else if(this.isCommand(text,ClientCommand.EPSV)){
                this.sendCommand(session.getSocket(),ServerCommand.InvaildCommand.getCode());
            }else if (this.isCommand(text,ClientCommand.LIST)
                    ||
                    this.isCommand(text,ClientCommand.NLST)
                    ){
                Socket socket = null;
                try {
                    socket = (Socket) session.getAttribute(Constant.FILE_SOCKET);
                    socket.getOutputStream().write(this.fileList(session.getAttribute(Constant.USER).toString(), session.getAttribute(Constant.PATH).toString()).getBytes(this.encoding));
                    this.sendCommand(session.getSocket(), ServerCommand.OpenConnection.getCode());
                    this.sendCommand(session.getSocket(), ServerCommand.EndDataConnection.getCode());
                } catch (IOException e) {
                    Logger.error("get file data stream fail", e);
                } finally {
                    if (socket != null) {
                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else if (this.isCommand(text,ClientCommand.SIZE)) {
                File file = new File(this.path(session.getAttribute(Constant.PATH).toString() , text.substring(ClientCommand.SITE.length())));
                if (file.exists()) {
                    this.sendCommand(session.getSocket(), ServerCommand.FileStateReply.getCode(), String.valueOf(file.length()));
                } else {
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode());
                }
            } else if (this.isCommand(text,ClientCommand.MDTM)) {
                File file = new File(this.path(session.getAttribute(Constant.PATH).toString(),text.substring(ClientCommand.MDTM.length())));
                if (file.exists()) {
                    this.sendCommand(session.getSocket(), ServerCommand.FileStateReply.getCode(), new Time(file.lastModified()).toString(Constant.FTP_DATE_FORMAT, Locale.ENGLISH));
                } else {
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode());
                }
            } else if (this.isCommand(text,ClientCommand.RETR)) {
                Socket socket = (Socket) session.getAttribute(Constant.FILE_SOCKET);
                File file = new File(this.path(session.getAttribute(Constant.PATH).toString() , text.substring(ClientCommand.RETR.length())));
                if (file.exists() && this.ftpUser.checkFileDownload(session.getAttribute(Constant.USER).toString(), file.getAbsolutePath())) {
                    InputStream inputStream = null;
                    try {
                        inputStream = new FileInputStream(file);
                        OutputStream outputStream = socket.getOutputStream();
                        this.stream(inputStream,outputStream);
                        this.sendCommand(session.getSocket(), ServerCommand.OpenConnection.getCode());
                        this.sendCommand(session.getSocket(), ServerCommand.EndDataConnection.getCode());
                    } catch (FileNotFoundException e) {
                        Logger.error("file not find", e);
                    } catch (IOException e) {
                        Logger.error("file download error", e);
                    } finally {
                        try {
                            if (inputStream != null) inputStream.close();
                            socket.close();
                        } catch (IOException e) {
                            Logger.error("socket close error", e);
                        }
                    }
                } else if (file.exists()) {
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode(), Constant.message.get(this.language).get(Constant.USER_HAVE_NO_ACCESS_DOWNLOAD));
                } else {
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode());
                }
            } else if (this.isCommand(text, ClientCommand.STOR)) {
                Socket socket = (Socket) session.getAttribute(Constant.FILE_SOCKET);
                File file = new File(this.path(session.getAttribute(Constant.PATH).toString() ,text.substring(ClientCommand.STOR.length())));
                if (this.ftpUser.checkFileUpload(session.getAttribute(Constant.USER).toString(), file.getAbsolutePath())) {
                    this.sendCommand(session.getSocket(), ServerCommand.OpenConnection.getCode());
                    FileOutputStream outputStream = null;
                    try {
                        outputStream = new FileOutputStream(file);
                        InputStream inputStream = socket.getInputStream();
                        this.stream(inputStream,outputStream);
                        this.sendCommand(session.getSocket(), ServerCommand.EndDataConnection.getCode());
                    } catch (FileNotFoundException e) {
                        this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode());
                    } catch (IOException e) {
                        this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode());
                        Logger.error("file download error", e);
                    } finally {
                        try {
                            if (outputStream != null) {
                                outputStream.close();
                            }
                            socket.close();
                        } catch (IOException e) {
                            Logger.error("socket close error", e);
                        }
                    }
                }else{
                    this.sendCommand(session.getSocket(), ServerCommand.FileNotAvailable.getCode(), Constant.message.get(this.language).get(Constant.USER_HAVE_NO_ACCESS_UPLOAD));
                }
            } else if (this.isCommand(text, ClientCommand.CDUP)) {
                String path = session.getAttribute(Constant.PATH).toString();
                path = path.substring(0, path.lastIndexOf("/", path.length() - 2) + 1);
                session.setAttribute(Constant.PATH, path);
                this.sendCommand(session.getSocket(), ServerCommand.Success.getCode());
            }else if(this.isCommand(text,ClientCommand.QUIT)){
                this.sendCommand(session.getSocket(),ServerCommand.ExitNetwork.getCode());
                try {
                    session.getSocket().close();
                } catch (IOException e) {
                    Logger.error("close socket error",e);
                }
            }else if(this.isCommand(text,ClientCommand.RNFR)){
                String path=this.path(session.getAttribute(Constant.PATH).toString(),text.substring(ClientCommand.RNFR.length()));
                session.setAttribute(Constant.FILE_NAME,path);
                if(this.ftpUser.fileRename(session.getAttribute(Constant.USER).toString(),path)) {
                    this.sendCommand(session.getSocket(), ServerCommand.FileBehaviorPause.getCode());
                }else{
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode(),Constant.message.get(this.language).get(Constant.USER_HAVE_NO_ACCESS_RENAME));
                }
            }else if(this.isCommand(text,ClientCommand.RNTO)){
                String path= (String) session.getAttribute(Constant.FILE_NAME);
                if(path==null){
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode());
                }else {
                    String newPath=this.path(session.getAttribute(Constant.PATH).toString(),text.substring(ClientCommand.RNTO.length()));
                    File newFile=new File(newPath);
                    if(newFile.exists()){
                        this.sendCommand(session.getSocket(),ServerCommand.FileNotAllow.getCode());
                    }else{
                        File file=new File(path);
                        file.renameTo(newFile);
                        this.sendCommand(session.getSocket(),ServerCommand.FileSuccess.getCode());
                    }
                }
            }else if(this.isCommand(text,ClientCommand.MKD)){
                File file=new File(this.path(session.getAttribute(Constant.PATH).toString(),text.substring(ClientCommand.MKD.length())));
                if(!file.exists()&&this.ftpUser.createFolder(session.getAttribute(Constant.USER).toString(),file.getAbsolutePath())){
                    file.mkdirs();
                    this.sendCommand(session.getSocket(),ServerCommand.FileSuccess.getCode());
                }else if(file.exists()){
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode());
                }else{
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode(),Constant.message.get(this.language).get(Constant.USER_HAVE_NO_ACCESS_CREATE_FOLDER));
                }
            }else if(this.isCommand(text,ClientCommand.RMD) || this.isCommand(text,ClientCommand.DELE)){
                String command=null;
                if(this.isCommand(text,ClientCommand.RMD)) {
                    command = ClientCommand.RMD;
                }else if(this.isCommand(text,ClientCommand.DELE)){
                    command = ClientCommand.DELE;
                }
                File file=new File(this.path(session.getAttribute(Constant.PATH).toString(), text.substring(command.length())));
                if(file.exists() && this.ftpUser.deleteFolder(session.getAttribute(Constant.USER).toString(),file.getAbsolutePath())){
                    this.deleteFile(file);
                    this.sendCommand(session.getSocket(),ServerCommand.FileSuccess.getCode());
                }else if(file.exists()){
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode(),Constant.message.get(this.language).get(Constant.USER_HAVE_NO_ACCESS_DELETE_FOLDER));
                }else{
                    this.sendCommand(session.getSocket(),ServerCommand.FileNotAvailable.getCode());
                }
            }
        } catch (UnsupportedEncodingException e) {
            Logger.error("encoding:{0} not exists", e, this.encoding);
        }
    }
    private void stream(InputStream inputStream,OutputStream outputStream) throws IOException {
        while (true) {
            byte[] bytes = new byte[Constant.STREAM_BUFFER_SIZE];
            int length = inputStream.read(bytes);
            if (length <= 0) {
                break;
            }
            outputStream.write(bytes, 0, length);
        }
    }
    private boolean isCommand(String text, String command) {
        return text.length() >= command.length() && text.substring(0, command.length()).equals(command);
    }

    private String fileList(String user, String path) {
        StringBuilder sb = new StringBuilder();
        File[] fileList = new File(path).listFiles();
        for (File file : fileList) {
            if (this.ftpUser.checkFileVisible(user, file.getAbsolutePath())) {
                if (file.isFile()) {
                    sb.append("-rwxr-xr-x 777 user root\t");
                } else if (file.isDirectory()) {
                    sb.append("drwxr-xr-x 777 user root\t");
                }
                sb.append(file.length());
                sb.append(" ");
                sb.append(new Time(file.lastModified()).toString(Constant.FTP_DATE_FORMAT, Locale.ENGLISH));
                sb.append(" ");
                sb.append(file.getName());
                sb.append(this.newline);
            }
        }
        return sb.toString();
    }
    private void deleteFile(File file){
        if(file.isFile()){
            file.delete();
        }else if(file.isDirectory()){
            for(File f : file.listFiles()){
                deleteFile(f);
            }
            file.delete();
        }
    }
    private String path(String basePath,String path) {
        String absPath=basePath+path;
        while (true){
            String temp=absPath.replace("//","/");
            if(temp.equals(absPath)){
                break;
            }else{
                absPath=temp;
            }
        }
        if(path.charAt(0)=='/'){
            if((path.length()>1&&path.charAt(1)!='.') || path.length()==1){
                return this.basePath+path.substring(1);
            }
        }
        if (absPath.indexOf("/../") != -1) {
            while (true) {
                String temp = absPath.replaceAll("/?[\\w|\\d|\\s|:]*/\\.\\./", "/");
                if (temp.equals(absPath)) {
                    break;
                } else {
                    absPath = temp;
                }
            }
        }
        if (absPath.length()>=3&&(absPath.substring(absPath.length() - 3).equals("/..") || absPath.substring(absPath.length() - 3).equals("../"))) {
            absPath = absPath.substring(0, absPath.length() - 3);
            int index = absPath.lastIndexOf("/");
            if (index != -1) {
                absPath = absPath.substring(0, index+1);
            } else {
                return null;
            }
        }
        if(absPath.length()<this.basePath.length()){
            return null;
        }
        int index=absPath.lastIndexOf("/")+1;
        return absPath.substring(0,index).replaceAll("/\\.*/", "/").replace("//", "/")+absPath.substring(index);
    }
}
