package LoAServer;

import com.google.gson.Gson;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;

@RestController
public class PregameController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.POST, value="/login")
    public LoginResponses login(@RequestBody Player p) {
        System.out.println(p.getUsername() + p.getPassword());
        return masterDatabase.getMasterPlayerDatabase().login(p);
    }

    @RequestMapping(method=RequestMethod.POST, value="/logout")
    public LogoutResponses logout(@RequestBody Player p) {
        System.out.println("LOGGING OUT: " +p.getUsername() + p.getPassword());
        return masterDatabase.getMasterPlayerDatabase().logout(p);
    }

    @RequestMapping(method=RequestMethod.GET, value="/getAllGames")
    public ArrayList<Game> getAllGames() {
        return masterDatabase.getMasterGameDatabase().getAllGames();
    }

    @RequestMapping(method=RequestMethod.POST, value="/hostGame") // assume everything is already put in the parameter Game g
    public HostGameResponses hostGame(@RequestBody Game g) {
        return masterDatabase.getMasterGameDatabase().hostGame(g);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/joinGame")
    public JoinGameResponses joinGame(@PathVariable String gameName, @PathVariable String username) {
        return masterDatabase.getMasterGameDatabase().joinGame(gameName, username);
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/{gameName}/{username}/leavePregame")
    public LeavePregameResponses leavePregame(@PathVariable String gameName, @PathVariable String username) {
        return masterDatabase.getMasterGameDatabase().leavePregame(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{username}/getPregameUpdate")
    public DeferredResult<ResponseEntity<String>> getPregameUpdate(@PathVariable String username) {
        System.out.println("k");
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterGameBCM().get(username));
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/isReady")
    public IsReadyResponses isRead(@PathVariable String gameName, @PathVariable String username) {
        return masterDatabase.getMasterGameDatabase().playerIsReady(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/selectHero")
    public SelectHeroResponses selectHero(@PathVariable String gameName, @PathVariable String username, @RequestBody HeroClass hero) {
        return masterDatabase.getMasterGameDatabase().playerSelectHero(gameName, username, hero);
    }

    @RequestMapping(method=RequestMethod.POST, value = "/{gameName}/{username}/startGame")
    public StartGameResponses startGame(@PathVariable String gameName, @PathVariable String username) {
        return masterDatabase.getMasterGameDatabase().startGame(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value = "/{gameName}/distributeItems")
    public DistributeItemsResponses distributeItems(@PathVariable String gameName, @RequestBody ItemDistribution itemDistribution) {
        return masterDatabase.getMasterGameDatabase().distributeItems(gameName, itemDistribution);
    }
}