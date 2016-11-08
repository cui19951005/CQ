package com.cq.sdk.net.ftp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/11/1.
 */
public interface Constant {
    String FILE_SOCKET="ftp.file.socket";
    String PATH="ftp.path";
    String USER="ftp.user";
    String FILE_NAME="ftp.file.name";
    Integer USER_SUSPEND=1000;
    Integer USER_HAVE_NO_ACCESS_DOWNLOAD=1001;
    Integer USER_HAVE_NO_ACCESS_UPLOAD=1002;
    Integer USER_HAVE_NO_ACCESS_RENAME=1003;
    Integer USER_HAVE_NO_ACCESS_CREATE_FOLDER=1004;
    Integer USER_HAVE_NO_ACCESS_DELETE_FOLDER=1005;
    Integer STREAM_BUFFER_SIZE=8192;
    String FTP_DATE_FORMAT="MMM dd HH:mm";
    Map<String,Map<Integer,String>> message=new HashMap<String,Map<Integer,String>>(){
        {
            this.put("中文",new HashMap<Integer,String>(){{
                this.put(ServerCommand.OpenDataStartTransmission.getCode(),"打开数据连接");
                this.put(150,"打开连接");
                this.put(200,"成功");
                this.put(202,"命令没有执行");
                this.put(211,"系统状态回复");
                this.put(212,"目录状态回复");
                this.put(213,"文件状态回复");
                this.put(214,"帮助信息回复");
                this.put(215,"系统类型回复");
                this.put(220,"服务就绪");
                this.put(221,"退出网络");
                this.put(225,"打开数据连接");
                this.put(226,"结束数据连接");
                this.put(227,"被动模式");
                this.put(230,"登录成功");
                this.put(250,"文件行为完成");
                this.put(257,"当前路径名称");
                this.put(331,"要求密码");
                this.put(332,"要求帐号");
                this.put(350,"文件行为暂停");
                this.put(421,"服务关闭");
                this.put(425,"无法打开数据连接");
                this.put(426,"结束连接");
                this.put(450,"文件操作未执行");
                this.put(500,"无效命令");
                this.put(501,"错误参数");
                this.put(550,"文件不可用");
                this.put(553,"文件不允许");
                this.put(Constant.USER_SUSPEND,"用户暂停使用");
                this.put(Constant.USER_HAVE_NO_ACCESS_DOWNLOAD,"用户无权限下载");
                this.put(Constant.USER_HAVE_NO_ACCESS_UPLOAD,"用户无权限上传");
                this.put(Constant.USER_HAVE_NO_ACCESS_RENAME,"用户无权重命名");
                this.put(Constant.USER_HAVE_NO_ACCESS_CREATE_FOLDER,"用户无权创建目录");
                this.put(Constant.USER_HAVE_NO_ACCESS_DELETE_FOLDER,"用户无权删除目录");
            }});
        }
    };
}
