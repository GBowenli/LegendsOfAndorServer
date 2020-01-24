package LoAServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoAServer {
    public static void main (String[] args) {
        // the following tests only work if the registered players are User 1 and User 2
        // will change this after we add the Join Game/Create Game options
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        masterDatabase.addMessageDatabase("game1"); // TEST DELETE AFTER!!!!!!!!!!!!!!!!!!!!!
        masterDatabase.addMessageDatabaseBCM("User 1", "game1");
        masterDatabase.addMessageDatabaseBCM("User 2", "game1");

        SpringApplication.run(LoAServer.class, args);
    }
}


