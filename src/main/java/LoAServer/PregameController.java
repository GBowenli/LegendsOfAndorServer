package LoAServer;

import LoAServer.LoginResponses;
import LoAServer.Player;
import LoAServer.PlayerDatabase;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PregameController {
    PlayerDatabase playerDatabase = new PlayerDatabase();

    @RequestMapping(method=RequestMethod.POST, value="/login")
    public LoginResponses login(@RequestBody Player p) {
        System.out.println(p.getUsername() + p.getPassword());
        return playerDatabase.login(p);
    }
}