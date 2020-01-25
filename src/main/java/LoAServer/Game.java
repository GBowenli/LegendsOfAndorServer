package LoAServer;

public class Game {
    private int maxNumPlayers;
    private int currentNumPlayers;
    private Player[] players;
    private String gameName;
    private boolean isActive;

    public Game(Player p, int maxNumPlayers, String gameName){
        this.gameName = gameName;
        this.maxNumPlayers = maxNumPlayers;
        this.currentNumPlayers = 1;
        this.players = new Player[maxNumPlayers];
        this.players[0] = p;
    }


    public String getGameName() {
        return gameName;
    }
    public void setGameName(String gameName) {
        this.gameName = gameName;
    }


}
