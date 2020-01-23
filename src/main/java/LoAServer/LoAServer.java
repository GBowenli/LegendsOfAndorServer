package LoAServer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoAServer {
    public static void main (String[] args) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        masterDatabase.addMessageDatabaseBCM("game1"); // TEST DELETE AFTER!!!!!!!!!!!!!!!!!!!!!

        SpringApplication.run(LoAServer.class, args);
    }
}


