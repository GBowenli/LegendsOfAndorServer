package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterDatabase {
    private static MasterDatabase singletonMasterDatabase = null;

    private PlayerDatabase masterPlayerDatabase = new PlayerDatabase();
    private HashMap<String, BroadcastContentManager<MessageDatabase>> masterBroadcastContentManager = new HashMap<>();

    private MasterDatabase() {}

    public static MasterDatabase getInstance() {
        if (singletonMasterDatabase == null) {
            singletonMasterDatabase = new MasterDatabase();
        }
        return singletonMasterDatabase;
    }

    public void addMessageDatabaseBCM(String gameName) { // need to call this method when Player creates a new Game
        MessageDatabase m = new MessageDatabase(gameName);
        masterBroadcastContentManager.put(gameName, new BroadcastContentManager<>(m));
    }

    public PlayerDatabase getMasterPlayerDatabase() {
        return masterPlayerDatabase;
    }

    public HashMap<String, BroadcastContentManager<MessageDatabase>> getMasterBroadcastContentManager() {
        return masterBroadcastContentManager;
    }
}
