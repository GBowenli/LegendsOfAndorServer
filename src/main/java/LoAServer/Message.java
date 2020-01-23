package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentHasher;

public class Message {
    private String username;
    private String msg;

    public Message() {}

    public Message (String username, String msg) {
        this.username = username;
        this.msg = msg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
