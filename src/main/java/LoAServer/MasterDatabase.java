package LoAServer;

import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.BroadcastContentManager;

import java.util.ArrayList;
import java.util.HashMap;

public class MasterDatabase {
    private static MasterDatabase singletonMasterDatabase = null;

    private PlayerDatabase masterPlayerDatabase = new PlayerDatabase();
    private GameDatabase masterGameDatabase = new GameDatabase();

    private ArrayList<MessageDatabase> masterMessageDatabase = new ArrayList<>();
    private HashMap<String, BroadcastContentManager<MessageDatabase>> masterMessageDatabaseBCM = new HashMap<>(); // one message database BCM per Player
    private HashMap<String, BroadcastContentManager<Game>> masterGameBCM = new HashMap<>();

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
                masterMessageDatabaseBCM.put(username, new BroadcastContentManager<>(md));
                break;
            }
        }
    }

    public void removeMessageDatabaseBCM(String username) {
        masterMessageDatabaseBCM.remove(username);
    }

    public void addGameBCM(String username, String gameName) {
        masterGameBCM.put(username, new BroadcastContentManager<>(masterGameDatabase.getGame(gameName)));
    }

    public void removeGameBCM(String username) {
        masterGameBCM.remove(username);
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
        return masterMessageDatabaseBCM;
    }

    public ArrayList<MessageDatabase> getMasterMessageDatabase() {
        return masterMessageDatabase;
    }

    public GameDatabase getMasterGameDatabase() {
        return masterGameDatabase;
    }

    public HashMap<String, BroadcastContentManager<Game>> getMasterGameBCM() {
        return masterGameBCM;
    }
}
