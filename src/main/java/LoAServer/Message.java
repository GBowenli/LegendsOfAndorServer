package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;

public class Message {
    private Player player;
    private String msg;
    private boolean belongsToCurrentUser;

    public Message() {}

    public Message (Player player, String msg, boolean belongsToCurrentUser) {
        this.player = player;
        this.msg = msg;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
}
