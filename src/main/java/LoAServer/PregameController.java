package LoAServer;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PregameController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.POST, value="/login")
    public LoginResponses login(@RequestBody Player p) {
        System.out.println(p.getUsername() + p.getPassword());
        return masterDatabase.getMasterPlayerDatabase().login(p);
    }
}