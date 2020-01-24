package LoAServer;

import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterDatabase {
    private static MasterDatabase singletonMasterDatabase = null;

    private PlayerDatabase masterPlayerDatabase = new PlayerDatabase();
    private ArrayList<MessageDatabase> masterMessageDatabase = new ArrayList<>();
    private HashMap<String, BroadcastContentManager<MessageDatabase>> masterMessageDatabaseDBCM = new HashMap<>(); // one message database BCM per Player

    private MasterDatabase() {}

    public static MasterDatabase getInstance() {
        if (singletonMasterDatabase == null) {
            singletonMasterDatabase = new MasterDatabase();
        }
        return singletonMasterDatabase;
    }

    public void addMessageDatabase(String gameName) {
        MessageDatabase m = new MessageDatabase(gameName);
        masterMessageDatabase.add(m);
    }

    public void addMessageDatabaseBCM(String username, String gameName) { // need to call this method when Player creates a new Game
        for (MessageDatabase md : masterMessageDatabase) {
            if (md.getGameName().equals(gameName)) {
                MessageDatabase msgDatabaseDeepCopy = new Gson().fromJson(new Gson().toJson(md), MessageDatabase.class);
                masterMessageDatabaseDBCM.put(username, new BroadcastContentManager<>(msgDatabaseDeepCopy));
                break;
            }
        }
    }

    public MessageDatabase getSingleMessageDatabase(String gameName) {
        for (MessageDatabase md : masterMessageDatabase) {
            if (md.getGameName().equals(gameName)) {
                return md;
            }
        }
        return null;
    }

    public PlayerDatabase getMasterPlayerDatabase() {
        return masterPlayerDatabase;
    }

    public HashMap<String, BroadcastContentManager<MessageDatabase>> getMasterMessageDatabaseDBCM() {
        return masterMessageDatabaseDBCM;
    }

    public ArrayList<MessageDatabase> getMasterMessageDatabase() {
        return masterMessageDatabase;
    }
}
