package com.cq.sdk.net.ftp;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by admin on 2016/10/31.
 */
public enum ServerCommand {
    OpenDataStartTransmission(125),
    OpenConnection(150),
    Success(200),
    CommandNotExecuted(202),
    SystemStateReply(211),
    DirectoriesStateReply(212),
    FileStateReply(213),
    HelpStateReply(214),
    SystemTypeReply(215),
    ServerReady(220),
    ExitNetwork(221),
    OpenDataConnection(225),
    EndDataConnection(226),
    PassiveMode(227),
    LoginSuccess(230),
    FileSuccess(250),
    NowPathName(257),
    RequestPassword(331),
    RequestAccount(332),
    FileBehaviorPause(350),
    ServiceClose(421),
    NotOpenDateConnection(425),
    EndConnection(426),
    FileCannotBeUsed(450),
    InvaildCommand(500),
    ErrorParam(501),
    FileNotAvailable(550),
    FileNotAllow(553);
    private int code;

    ServerCommand(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
