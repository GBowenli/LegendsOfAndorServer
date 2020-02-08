package LoAServer;

import java.util.ArrayList;
import java.util.List;

enum HostGameResponses {
    HOST_GAME_SUCCESS, ERROR_GAME_ALREADY_EXISTS
}

enum JoinGameResponses {
    JOIN_GAME_SUCCESS, ERROR_GAME_FULL, ERROR_GAME_DNE
}

enum IsReadyResponses {
    IS_READY_SUCCESS, ERROR_NO_SELECTED_HERO
}

enum SelectHeroResponses {
    SELECT_HERO_SUCCESS, ERROR_HERO_ALREADY_SELECTED, ERROR_DUPLICATE_HERO
}


public class GameDatabase {
    private List<Game> games;

    public GameDatabase(){
        games = new ArrayList<Game>();
    }

    public Game getGame(String gameName){
        for(Game g : games){
            if (g.getGameName().equals(gameName)){
                return g;
            }
        }
        return null;
    }

    public HostGameResponses hostGame(Game g) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(g.getGameName()) == null) {
            games.add(g);
            masterDatabase.addMessageDatabase(g.getGameName());
            masterDatabase.addGameBCM(g.getPlayers()[0].getUsername(), g.getGameName());
            masterDatabase.addMessageDatabaseBCM(g.getPlayers()[0].getUsername(), g.getGameName()); // when running/testing this remove hardcode in MessageController
            return HostGameResponses.HOST_GAME_SUCCESS;
        } else {
            return HostGameResponses.ERROR_GAME_ALREADY_EXISTS;
        }
    }


    public JoinGameResponses joinGame(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName) == null) {
            return JoinGameResponses.ERROR_GAME_DNE;
        } else {
            int currentNumPlayers = getGame(gameName).getCurrentNumPlayers();
            int maxNumPlayers = getGame(gameName).getMaxNumPlayers();

            if (currentNumPlayers < maxNumPlayers) {
                for (int i = 0; i < currentNumPlayers; i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).getCurrentBroadcastContent().addPlayer(masterDatabase.getMasterPlayerDatabase().getPlayer(username));
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                getGame(gameName).addPlayer(masterDatabase.getMasterPlayerDatabase().getPlayer(username));
                masterDatabase.addGameBCM(username, gameName);
                masterDatabase.addMessageDatabaseBCM(username, gameName);
                return JoinGameResponses.JOIN_GAME_SUCCESS;
            } else {
                return JoinGameResponses.ERROR_GAME_FULL;
            }
        }
    }

    public ArrayList<String> getAllGames() {
        ArrayList<String> gamesStr = new ArrayList<>();
        for (Game g: games) {
            gamesStr.add(g.getGameName());
        }
        return gamesStr;
    }

    public void leavePregame(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        masterDatabase.removeGameBCM(username);
        masterDatabase.removeMessageDatabaseBCM(username);

        if (getGame(gameName).getCurrentNumPlayers() == 1) {
            games.remove(getGame(gameName));
        } else {
            getGame(gameName).removePlayer(username);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).getCurrentBroadcastContent().removePlayer(username);
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }
        }
    }

    public IsReadyResponses playerIsReady(String gameName, String username) { // add logic in android that if it is the player himself do not update changes (update after clicked)
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName).getSinglePlayer(username).getHero() == null) {
            return IsReadyResponses.ERROR_NO_SELECTED_HERO;
        } else {
            getGame(gameName).getSinglePlayer(username).setReady(!getGame(gameName).getSinglePlayer(username).isReady());

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).getCurrentBroadcastContent().getSinglePlayer(username).setReady(getGame(gameName).getSinglePlayer(username).isReady());
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }
            return IsReadyResponses.IS_READY_SUCCESS;
        }
    }

    public SelectHeroResponses playerSelectHero(String gameName, String username, Hero hero) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName).getSinglePlayer(username).getHero() == null) {
            for (Hero h : getGame(gameName).getAllHeroes()) {
                if (h.equals(hero)) {
                    return SelectHeroResponses.ERROR_DUPLICATE_HERO;
                }
            }

            getGame(gameName).getSinglePlayer(username).setHero(hero);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).getCurrentBroadcastContent().getSinglePlayer(username).setHero(hero);
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return SelectHeroResponses.SELECT_HERO_SUCCESS;
        } else {
            return SelectHeroResponses.ERROR_HERO_ALREADY_SELECTED;
        }

    }


}
