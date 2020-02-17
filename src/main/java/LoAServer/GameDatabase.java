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

enum StartGameResponses {
    START_GAME_SUCCESS, ERROR_PLAYER_NOT_READY, ERROR_NOT_HOST, ERROR_NOT_ENOUGH_PLAYERS
}


public class GameDatabase {
    private ArrayList<Game> games;

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
            masterDatabase.addMessageDatabaseBCM(g.getPlayers()[0].getUsername(), g.getGameName()); // when running/testing this remove hardcode in MessageController
            masterDatabase.addGameBCM(g.getPlayers()[0].getUsername(), g.getGameName());

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
                getGame(gameName).addPlayer(masterDatabase.getMasterPlayerDatabase().getPlayer(username));

                for (int i = 0; i < currentNumPlayers; i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                masterDatabase.addGameBCM(username, gameName);
                masterDatabase.addMessageDatabaseBCM(username, gameName);
                return JoinGameResponses.JOIN_GAME_SUCCESS;
            } else {
                return JoinGameResponses.ERROR_GAME_FULL;
            }
        }
    }

    public ArrayList<Game> getAllGames() {
        return games;
    }

    public void leavePregame(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        masterDatabase.removeGameBCM(username);
        masterDatabase.removeMessageDatabaseBCM(username);

        if (getGame(gameName).getCurrentNumPlayers() == 1) {
            games.remove(getGame(gameName));
            masterDatabase.deleteMessageDatabase(gameName);
        } else {
            getGame(gameName).removePlayer(username);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
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
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }
            return IsReadyResponses.IS_READY_SUCCESS;
        }
    }

    public SelectHeroResponses playerSelectHero(String gameName, String username, HeroClass hero) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName).getSinglePlayer(username).getHero() == null) {
            for (HeroClass h : getGame(gameName).getAllHeroes()) {
                if (h.equals(hero)) {
                    return SelectHeroResponses.ERROR_DUPLICATE_HERO;
                }
            }

            getGame(gameName).getSinglePlayer(username).setHero(new Hero(hero));

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return SelectHeroResponses.SELECT_HERO_SUCCESS;
        } else {
            return SelectHeroResponses.ERROR_HERO_ALREADY_SELECTED;
        }
    }

    public StartGameResponses startGame(String gameName, String username) {
        if (getGame(gameName).getCurrentNumPlayers() == 1) {
            return StartGameResponses.ERROR_NOT_ENOUGH_PLAYERS;
        }
        if (!getGame(gameName).getPlayers()[0].getUsername().equals(username)) {
            return StartGameResponses.ERROR_NOT_HOST;
        }
        if (getGame(gameName).allReady()) {
            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            getGame(gameName).setActive(true);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            masterDatabase.addRegionDatabase(gameName, new RegionDatabase());

            return StartGameResponses.START_GAME_SUCCESS;
        } else {
            return StartGameResponses.ERROR_PLAYER_NOT_READY;
        }
    }

    public void distributeItems(String gameName, ItemDistribution itemDistribution) {
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            if (i == 0) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer1Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer1Items());
            } else if (i == 1) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer2Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer2Items());
            } else if (i == 2) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer3Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer3Items());
            } else { // i = 3
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer4Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer4Items());
            }
        }
        getGame(gameName).setItemsDistributed(true);


        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }
}
