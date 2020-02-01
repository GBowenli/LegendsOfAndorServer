package LoAServer;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LoAServer {
    public static void main (String[] args) {
        // the following tests only work if the registered players are User 1 and User 2
        // will change this after we add the Join Game/Create Game options
//        MasterDatabase masterDatabase = MasterDatabase.getInstance();
//        masterDatabase.addMessageDatabase("game1"); // TEST DELETE AFTER!!!!!!!!!!!!!!!!!!!!!
//        masterDatabase.addMessageDatabaseBCM("User 1", "game1");
//        masterDatabase.addMessageDatabaseBCM("User 2", "game1");

        // tests

//        MasterDatabase md = MasterDatabase.getInstance();
//        md.getMasterPlayerDatabase().login(new Player("Bowen", "123", "blue", false));
//        md.getMasterPlayerDatabase().login(new Player("Friend", "123", "blue", false));
//        md.getMasterPlayerDatabase().login(new Player("Draven", "123", "blue", false));
//        md.getMasterGameDatabase().hostGame(new Game(new Player("Bowen", "123", "blue", true), 4, "game1"));
//        md.getMasterGameDatabase().playerIsReady("game1", "Bowen");
//        md.getMasterGameDatabase().joinGame("game1", "Friend");
//        md.getMasterGameDatabase().leavePregame("game1", "Bowen");
//        md.getMasterGameDatabase().playerSelectHero("game1", "Friend", Hero.ARCHER);
//        md.getMasterGameDatabase().playerIsReady("game1", "Friend");
//
//
//        System.out.println(new Gson().toJson(md.getMasterGameDatabase().getGame("game1")));


        SpringApplication.run(LoAServer.class, args);
    }
}


