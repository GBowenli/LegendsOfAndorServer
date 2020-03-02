package LoAServer;

import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;
import java.util.List;

@RestController
public class GameController {
    private MasterDatabase masterDatabase = MasterDatabase.getInstance();

    @RequestMapping(method=RequestMethod.GET, value="/{username}/getGameUpdate")
    public DeferredResult<ResponseEntity<String>> getGameUpdate(@PathVariable String username) {
        return ResponseGenerator.getAsyncUpdate(5000, masterDatabase.getMasterGameBCM().get(username));
    }

    @RequestMapping(method = RequestMethod.GET, value="/{username}/isGameStarted")
    public GameStartedResponses isGameStarted(@PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().isGameStarted(username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{username}/getGameByUsername")
    public Game getGameByUsername(@PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().getGameByUsername(username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getAvailableRegions") // this is the one to call when you click the button "Move"
    public List<Object> getAvailableRegions (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getAvailableRegions(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/move")
    public List<Object> move(@PathVariable String gameName, @PathVariable String username, @RequestBody Integer targetRegion) { // this is the one to call after they click a region after clicking "Move"
        return MasterDatabase.getInstance().getMasterGameDatabase().move(gameName, username, targetRegion);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getFarmers")
    public ArrayList<Farmer> getFarmers(@PathVariable String gameName, String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getFarmers(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/pickUpFarmers")
    public PickUpFarmersResponses pickUpFarmers(@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Farmer> farmers) {
        return MasterDatabase.getInstance().getMasterGameDatabase().pickUpFarmers(gameName, username, farmers);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/endMove")
    public EndMoveResponses endMove(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().endMove(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/emptyWell")
    public EmptyWellResponses emptyWellResponses(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().emptyWell(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateFog")
    public List<Object> activateFog(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().activateFog(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/endDay")
    public EndDayResponses endDay(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().endDay(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/pass")
    public PassResponses pass(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().pass(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/fight")
    public FightResponses fight(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().fight(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/joinFight")
    public void joinFight(@PathVariable String gameName, @PathVariable String username) {
        MasterDatabase.getInstance().getMasterGameDatabase().joinFight(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/leaveFight")
    public LeaveFightResponses leaveFight(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().leaveFight(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getDice")
    public ArrayList<Die> getDice(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getDice(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/calculateBattleValue")
    public Integer calculateBattleValue(@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Integer> diceRolls) {
        return MasterDatabase.getInstance().getMasterGameDatabase().calculateBattleValue(gameName, username, diceRolls);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getCreatureDice")
    public ArrayList<Die> getCreatureDice(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getCreatureDice(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/calculateCreatureBattleValue")
    public Integer calculateCreatureBattleValue(@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Integer> diceRolls) {
        return MasterDatabase.getInstance().getMasterGameDatabase().calculateCreatureBattleValue(gameName, username, diceRolls);
    }
}
