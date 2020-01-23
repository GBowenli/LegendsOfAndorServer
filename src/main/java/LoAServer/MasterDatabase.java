package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterDatabase {
    private static MasterDatabase singletonMasterDatabase = null;

    private PlayerDatabase masterPlayerDatabase = new PlayerDatabase();

    private ArrayList<MessageDatabase> masterMessageDatabase = new ArrayList<>();
    private HashMap<String, BroadcastContentManager<MessageDatabase>> masterBroadcastContentManager = new HashMap<>();

    private MasterDatabase() {}

    public static MasterDatabase getInstance() {
        if (singletonMasterDatabase == null) {
            singletonMasterDatabase = new MasterDatabase();
        }
        return singletonMasterDatabase;
    }

    public void addMessageDatabase(String gameName) { // need to call this method when Player creates a new Game
        MessageDatabase m = new MessageDatabase(gameName);
        masterMessageDatabase.add(m);
        masterBroadcastContentManager.put(gameName, new BroadcastContentManager<>(m));
    }

    public PlayerDatabase getMasterPlayerDatabase() {
        return masterPlayerDatabase;
    }

    public ArrayList<MessageDatabase> getMasterMessageDatabase() {
        return masterMessageDatabase;
    }

    public MessageDatabase getSingleMessageDatabase(String gameName) {
        for (MessageDatabase m : masterMessageDatabase) {
            if (m.getGameName().equals(gameName)) {
                return m;
            }
        }
        return null; // this should never be executed
    }

    public HashMap<String, BroadcastContentManager<MessageDatabase>> getMasterBroadcastContentManager() {
        return masterBroadcastContentManager;
    }
}
