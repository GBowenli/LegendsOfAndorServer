package LoAServer;

import java.util.ArrayList;
import java.util.List;

public class GameDatabase {
    private List<Game> games;

    public GameDatabase(){
        games = new ArrayList<Game>();
    }

    public Game getGame(String gameName){
        for(Game g : this.games){
            if (g.getGameName().equals(gameName)){
                return g;
            }
        }
        return null;
    }

    public void addGame(Game game){
        games.add(game);
    }
}
