package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;

import java.util.ArrayList;

public class MessageDatabase implements BroadcastContent {
    private ArrayList<Message> messages;
    private String gameName;

    public MessageDatabase (String gameName) {
        this.gameName = gameName;
        messages = new ArrayList<>();
    }

    public String getGameName() {
        return gameName;
    }

    public void add(Message m) {
        messages.add(m);
    }

    @Override
    public boolean isEmpty() {
        return messages.isEmpty();
    }

    @Override
    public String serialize() {
        return "";
    }

    @Override
    public boolean equalsByMD5(BroadcastContent broadcastContent) {
        return false;
        //return BroadcastContentHasher.hash(this).equals(BroadcastContentHasher.hash(broadcastContent)); cannot do this because sometimes does not detect change
    }
}
