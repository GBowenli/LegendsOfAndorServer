package LoAServer;

import LoAServer.ReturnClasses.*;
import eu.kartoffelquadrat.asyncrestlib.ResponseGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.ArrayList;

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
    public GetAvailableRegionsRC getAvailableRegions (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getAvailableRegions(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/move")
    public MoveRC move(@PathVariable String gameName, @PathVariable String username, @RequestBody Integer targetRegion) { // this is the one to call after they click a region after clicking "Move"
        return MasterDatabase.getInstance().getMasterGameDatabase().move(gameName, username, targetRegion);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getFarmers")
    public ArrayList<Farmer> getFarmers(@PathVariable String gameName, @PathVariable String username) {
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
    public ActivateFogRC activateFog(@PathVariable String gameName, @PathVariable String username) {
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

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getPossibleCreaturesToFight")
    public GetPossibleCreaturesToFightRC getPossibleCreaturesToFight(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getPossibleCreaturesToFight(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/fight")
    public Fight fight(@PathVariable String gameName, @PathVariable String username, @RequestBody Integer targetRegion) {
        return MasterDatabase.getInstance().getMasterGameDatabase().fight(gameName, username, targetRegion);
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

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/calculateBattleValue")
    public Integer calculateBattleValue(@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Integer> diceRolls) {
        return MasterDatabase.getInstance().getMasterGameDatabase().calculateBattleValue(gameName, username, diceRolls);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getCreatureDice")
    public ArrayList<Die> getCreatureDice(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getCreatureDice(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/calculateCreatureBattleValue")
    public Integer calculateCreatureBattleValue(@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Integer> diceRolls) {
        return MasterDatabase.getInstance().getMasterGameDatabase().calculateCreatureBattleValue(gameName, username, diceRolls);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/endBattleRound")
    public EndBattleRoundResponses calculateCreatureBattleValue(@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().endBattleRound(gameName, username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/activateHelm")
    public ActivateHelmResponses activateHelm(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().activateHelm(gameName, username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/activateShieldFight")
    public ActivateShieldFightResponses activateShieldFight(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().activateShieldFight(gameName, username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/activateWitchesBrewFight")
    public ActivateWitchesBrewFightResponses activateWitchesBrewFight(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().activateWitchesBrewFight(gameName, username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/activateMedicinalHerbFight")
    public ActivateMedicinalHerbFightResponses activateMedicinalHerbFight(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().activateMedicinalHerbFight(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/buyFromMerchant")
    public BuyFromMerchantResponses buyFromMerchant (@PathVariable String gameName, @PathVariable String username, @RequestBody MerchantPurchase merchantPurchase) {
        return MasterDatabase.getInstance().getMasterGameDatabase().buyFromMerchant(gameName, username, merchantPurchase);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/dropGold")
    public void dropGold (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer gold) {
        MasterDatabase.getInstance().getMasterGameDatabase().dropGold(gameName, username, gold);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getGold")
    public Integer getGold (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getGold(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/pickUpGold")
    public void pickUpGold (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer gold) {
        MasterDatabase.getInstance().getMasterGameDatabase().pickUpGold(gameName, username, gold);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/dropFarmers")
    public void dropFarmers (@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Farmer> farmers) {
        MasterDatabase.getInstance().getMasterGameDatabase().dropFarmers(gameName, username, farmers);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getMyFarmers")
    public ArrayList<Farmer> getMyFarmers (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getMyFarmers(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/distributeAfterFight")
    public void distributeAfterFight (@PathVariable String gameName, @PathVariable String username, @RequestBody FightDistribution fightDistribution) {
        MasterDatabase.getInstance().getMasterGameDatabase().distributeAfterFight(gameName, username, fightDistribution);
    }

   //remove the game
    @RequestMapping(method = RequestMethod.DELETE, value = "/{gameName}/gameOver")
    public void gameOver (@PathVariable String gameName){
        MasterDatabase.getInstance().getMasterGameDatabase().gameOver(gameName);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/saveGame")
    public void saveGame (@PathVariable String gameName) {
        MasterDatabase.getInstance().getMasterGameDatabase().saveGame(gameName);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateLegendCardC")
    public void activateLegendCardC (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer dieRoll) {
        MasterDatabase.getInstance().getMasterGameDatabase().activateLegendCardC(gameName, username, dieRoll);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateLegendCardG")
    public void activateLegendCardG (@PathVariable String gameName, @PathVariable String username) {
        MasterDatabase.getInstance().getMasterGameDatabase().activateLegendCardG(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getPrinceThoraldMoves")
    public GetPrinceThoraldMovesRC getPrinceThoraldMoves (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().getPrinceThoraldMoves(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/movePrinceThorald")
    public void movePrinceThorald (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer targetRegion) {
        MasterDatabase.getInstance().getMasterGameDatabase().movePrinceThorald(gameName, username, targetRegion);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/endMovePrinceThorald")
    public EndMovePrinceThoraldResponses endMovePrinceThorald (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getMasterGameDatabase().endMovePrinceThorald(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/setRuneStonesLegendCard")
    public void setRuneStonesLegendCard (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer dieRoll) {
        MasterDatabase.getInstance().getMasterGameDatabase().setRuneStonesLegendCard(gameName, username, dieRoll);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateLegendCardRuneStones")
    public void activateLegendCardRuneStones (@PathVariable String gameName, @PathVariable String username, @RequestBody ArrayList<Integer> runeStonePositions) {
        MasterDatabase.getInstance().getMasterGameDatabase().activateLegendCardRuneStones(gameName, username, runeStonePositions);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateLegendCardTheWitch")
    public void activateLegendCardTheWitch (@PathVariable String gameName, @PathVariable String username, @RequestBody Integer dieRoll) {
        MasterDatabase.getInstance().getMasterGameDatabase().activateLegendCardTheWitch(gameName, username, dieRoll);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/activateLegendCardN")
    public void activateLegendCardN (@PathVariable String gameName, @PathVariable String username) {
        MasterDatabase.getInstance().getMasterGameDatabase().activateLegendCardN(gameName, username);
    }

    @RequestMapping(method=RequestMethod.GET, value="/{gameName}/{username}/getSavedGames")
    public ArrayList<Game> getSavedGames (@PathVariable String gameName, @PathVariable String username) {
        return MasterDatabase.getInstance().getSavedGames(gameName, username);
    }

    @RequestMapping(method=RequestMethod.POST, value="/{gameName}/{username}/loadGame")
    public LoadGameResponses loadGame (@PathVariable String gameName, @PathVariable String username, @RequestBody Game g) {
        return MasterDatabase.getInstance().getMasterGameDatabase().loadGame(gameName, username, g);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{gameName}/{username}/getItems")
    public ArrayList<Item> getItems(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().getItems(gameName,username);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/{gameName}/{username}/getRunestones")
    public ArrayList<RuneStone> getRunestones(@PathVariable String gameName, @PathVariable String username){
        return MasterDatabase.getInstance().getMasterGameDatabase().getRunestones(gameName,username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/addItem")
    public AddDropItemResponses addItem(@PathVariable String gameName, @PathVariable String username, @RequestBody ItemType itemType){
        return MasterDatabase.getInstance().getMasterGameDatabase().addItem(username,gameName,itemType);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/addRunestone")
    public AddDropItemResponses addRunestone(@PathVariable String gameName, @PathVariable String username, @RequestBody Colour colour){
        return MasterDatabase.getInstance().getMasterGameDatabase().addRunestone(username,gameName,colour);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/dropItem")
    public AddDropItemResponses dropItem(@PathVariable String gameName, @PathVariable String username, @RequestBody ItemType itemType){
        return MasterDatabase.getInstance().getMasterGameDatabase().dropItem(username,gameName,itemType);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/dropRunestone")
    public AddDropItemResponses dropRunestone(@PathVariable String gameName, @PathVariable String username, @RequestBody Colour colour){
        return MasterDatabase.getInstance().getMasterGameDatabase().dropRunestone(username,gameName, colour);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/sendFalconTradeRequest")
    public PendingFalconTradeResponses sendFalconTradeRequest(@PathVariable String gameName,@PathVariable String username, @RequestBody HeroClass hero){
        return MasterDatabase.getInstance().getMasterGameDatabase().sendFalconTradeRequest(gameName,username,hero);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/joinFalconTrade")
    public AcceptFalconTradeResponses joinFalconTrade(@PathVariable String gameName, @PathVariable String username, @RequestBody HeroClass hero){
        return MasterDatabase.getInstance().getMasterGameDatabase().joinFalconTrade(gameName,username,hero);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/updateFalconTradeObject")
    public void updateFalconTradeObject(@PathVariable String gameName, @PathVariable String username, @RequestBody FalconTradeObject falconTradeObject){
        MasterDatabase.getInstance().getMasterGameDatabase().updateFalconTradeObject(gameName,username,falconTradeObject);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/leaveFalconTrade")
    public void leaveFalconTrade(@PathVariable String gameName, @PathVariable String username, @RequestBody HeroClass hero){
        MasterDatabase.getInstance().getMasterGameDatabase().leaveFalconTrade(gameName,username,hero);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/processFalconTrade")
    public ProcessFalconTradeResponses processFalconTrade(@PathVariable String gameName, @RequestBody FalconTradeObject falconTradeObject){
        return MasterDatabase.getInstance().getMasterGameDatabase().processFalconTrade(gameName,falconTradeObject);
    }


    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/foundWitch")
    public void foundWitch(@PathVariable String gameName, @PathVariable String username){
        MasterDatabase.getInstance().getMasterGameDatabase().foundWitch(gameName, username);
    }

    @RequestMapping(method = RequestMethod.DELETE, value = "/{gameName}/{username}/leaveGame")
    public void leaveGame(@PathVariable String gameName, @PathVariable String username){
        MasterDatabase.getInstance().getMasterGameDatabase().leaveGame(gameName, username);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/buyWitchBrew")
    public BuyWitchBrewResponses buyWitchBrew(@PathVariable String gameName, @PathVariable String username, @RequestBody Integer quantity){
        return MasterDatabase.getInstance().getMasterGameDatabase().buyWitchBrew(gameName, username, quantity);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{gameName}/{username}/activateWizardAbility")
    public ActivateWizardAbilityResponses activateWizardAbility(@PathVariable String gameName, @PathVariable String username, @RequestBody ActivateWizardTarget activateWizardTarget){
        return MasterDatabase.getInstance().getMasterGameDatabase().activateWizardAbility(gameName, username, activateWizardTarget);
    }


    // to do.. move prince
    // wardraks reward is gold + willpwoer = 6
    // add turnoptions movePrinceThorald to android and
}
