package LoAServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoAServer {
    public static void main (String[] args) {
        GameDatabase gameDatabase = new GameDatabase();
        gameDatabase.hostGame(new Game(new Player("Bowen", "123", "blue"), 4, "game1", false));
        gameDatabase.saveGame("game1");

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        masterDatabase.loadGames();

        SpringApplication.run(LoAServer.class, args);
    }
}
