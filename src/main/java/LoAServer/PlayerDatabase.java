package LoAServer;

import java.util.ArrayList;

enum LoginResponses {
    LOGIN_SUCCESS, NEW_LOGIN_CREATED, LOGIN_ERROR_INCORRECT_PASSWORD, LOGIN_ERROR_ALREADY_LOGGED_IN
}

public class PlayerDatabase {
    private ArrayList<Player> players;

    public PlayerDatabase() {
        players = new ArrayList<Player>();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public LoginResponses login(Player player) {
        for (Player p: players) {
            if (player.getUsername().equals(p.getUsername())) {
                if (player.getPassword().equals(p.getPassword())) {
                    if (p.isLoggedIn()) {
                        return LoginResponses.LOGIN_ERROR_ALREADY_LOGGED_IN;
                    } else {
                        p.setLoggedIn(true);
                        return LoginResponses.LOGIN_SUCCESS;
                    }
                } else {
                    return LoginResponses.LOGIN_ERROR_INCORRECT_PASSWORD;
                }
            }
        }
        player.setLoggedIn(true);
        players.add(player);
        return LoginResponses.NEW_LOGIN_CREATED;
    }
}
