package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.BroadcastContent;

public class Game implements BroadcastContent {
    private int maxNumPlayers;
    private int currentNumPlayers;
    private Player[] players;
    private String gameName;
    private boolean isActive;

    public Game() {}

    public Game(Player p, int maxNumPlayers, String gameName) {
        this.gameName = gameName;
        this.maxNumPlayers = maxNumPlayers;
        this.currentNumPlayers = 1;
        this.players = new Player[maxNumPlayers];
        this.players[0] = p;
    }

    public int getMaxNumPlayers() {
        return maxNumPlayers;
    }

    public void setMaxNumPlayers(int maxNumPlayers) {
        this.maxNumPlayers = maxNumPlayers;
    }

    public int getCurrentNumPlayers() {
        return currentNumPlayers;
    }

    public void setCurrentNumPlayers(int currentNumPlayers) {
        this.currentNumPlayers = currentNumPlayers;
    }

    public Player[] getPlayers() {
        return players;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Player getSinglePlayer(String username) {
        for (Player p : players) {
            if (p.getUsername().equals(username)) {
                return p;
            }
        }
        return null;
    }

    public void addPlayer(Player p) {
        for (int i = 0; i < maxNumPlayers; i++) {
            if (players[i] == null) {
                players[i] = p;
                currentNumPlayers++;
                break;
            }
        }
    }

    public void removePlayer(String username) {
        for (int i = 0; i < currentNumPlayers; i++) {
            if (players[i].getUsername().equals(username)) {
                for (int j = i; j < maxNumPlayers; j++) {
                    if (j == maxNumPlayers-1) {
                        players[j] = null;
                    } else {
                        players[j] = players[j + 1]; // shift every element down by one unit
                    }
                }
                currentNumPlayers--;
                return;
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return (players.length == 0);
    }
}
