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

    public ArrayList<Message> getMessages() {
        return messages;
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
}
