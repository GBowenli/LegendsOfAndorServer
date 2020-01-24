package LoAServer;

import java.util.ArrayList;

enum LoginResponses {
    LOGIN_SUCCESS, NEW_LOGIN_CREATED, LOGIN_ERROR_INCORRECT_PASSWORD;
}

public class PlayerDatabase {
    private ArrayList<Player> players;

    public PlayerDatabase() {
        players = new ArrayList<Player>();
    }

    public LoginResponses login(Player player) {
        for (Player p: players) {
            if (player.getUsername().equals(p.getUsername())) {
                if (player.getPassword().equals(p.getPassword())) {
                    return LoginResponses.LOGIN_SUCCESS;
                } else {
                    return LoginResponses.LOGIN_ERROR_INCORRECT_PASSWORD;
                }
            }
        }
        players.add(player);
        return LoginResponses.NEW_LOGIN_CREATED;
    }

    public Player getPlayer(String username){
        for(Player p : this.players){
            if (p.getUsername().equals(username)){
                return p;
            }
        }
        return null;
    }
}
