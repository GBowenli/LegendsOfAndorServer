//added changes
package LoAServer;

import LoAServer.PublicEnums.*;
import LoAServer.ReturnClasses.*;
import com.google.gson.Gson;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

enum HostGameResponses {
    HOST_GAME_SUCCESS, ERROR_GAME_ALREADY_EXISTS
}

enum GameStartedResponses{
    GAME_STARTED, GAME_NOT_STARTED
}

enum JoinGameResponses {
    JOIN_GAME_SUCCESS, ERROR_GAME_FULL, ERROR_GAME_DNE, ERROR_GAME_LOADED
}

enum LeavePregameResponses {
    ERROR_GAME_LOADED, LEAVE_SUCCESS
}

enum IsReadyResponses {
    IS_READY_SUCCESS, ERROR_NO_SELECTED_HERO
}

enum SelectHeroResponses {
    SELECT_HERO_SUCCESS, ERROR_HERO_ALREADY_SELECTED, ERROR_DUPLICATE_HERO
}

enum StartGameResponses {
    START_GAME_SUCCESS, ERROR_PLAYER_NOT_READY, ERROR_NOT_HOST, ERROR_NOT_ENOUGH_PLAYERS
}

enum DistributeItemsResponses{
    DISTRIBUTE_ITEMS_SUCCESS, DISTRIBUTE_ITEMS_FAILURE
}

enum PickUpFarmersResponses {
    FARMERS_DIED, NO_FARMERS, FARMERS_PICKED_UP
}

enum EndMoveResponses {
    BUY_FROM_MERCHANT, EMPTY_WELL, ACTIVATE_FOG, MOVE_ALREADY_ENDED, MUST_MOVE_TO_END_MOVE, NONE
}

enum EmptyWellResponses {
    SUCCESS, WELL_ALREADY_EMPTY, WELL_DNE
}

enum EndDayResponses {
    DAY_ALREADY_ENDED, NOT_CURRENT_TURN, NEW_DAY, GAME_OVER, SUCCESS
}

enum PassResponses {
    PASS_SUCCESSFUL, MUST_END_DAY, ONLY_PLAYER_LEFT, NOT_CURRENT_TURN, DAY_ENDED, PASS_SUCCESSFUL_WP_DEDUCTED
}

enum LeaveFightResponses {
    CANNOT_LEAVE_AFTER_ROLLING, CANNOT_LEAVE_WITHOUT_FIGHTING, SUCCESS
}

enum EndBattleRoundResponses {
    WON_ROUND, LOST_ROUND, TIE_ROUND, CREATURE_DEFEATED, BATTLE_LOST, PLAYERS_NO_BATTLE_VALUE, CREATURE_NO_BATTLE_VALUE, WAITING_FOR_PLAYERS_TO_JOIN
}

enum BuyFromMerchantResponses {
    SUCCESS, NOT_ENOUGH_GOLD, MERCHANT_DNE
}

enum EndMovePrinceThoraldResponses {
    MUST_MOVE_PRINCE_TO_END_MOVE, MOVE_PRINCE_ALREADY_ENDED, SUCCESS
}

enum LoadGameResponses {
    ERROR_NOT_ALL_PLAYERS_SELECTED_HEROES, ERROR_PLAYER_NUM_MISMATCH, ERROR_HERO_MISMATCH, ERROR_DIFFICULTY_MISMATCH, LOAD_GAME_SUCCESS
}

enum AddDropItemResponses{
    ITEM_ADDED,ITEM_DROPPED,ADD_DROP_FAILURE, MAX_ITEMS
}

enum ActivateHelmResponses {
    ERROR_DOES_NOT_OWN_HELM, ERROR_ARCHER, ERROR_WIZARD, ERROR_BOW_USER, ERROR_NO_DICE_ROLLS, HELM_ACTIVATED
}

enum ActivateShieldFightResponses {
    ERROR_DOES_NOT_OWN_SHIELD, ERROR_SHIELD_ALREADY_ACTIVATED, ERROR_INAPPROPIRATE_SHIELD_ACTIVATION, SHIELD_ACTIVATED
}


public class GameDatabase {
    private ArrayList<Game> games;

    public GameDatabase(){
        games = new ArrayList<Game>();
    }

    public Game getGame(String gameName){
        for(Game g : games){
            if (g.getGameName().equals(gameName)){
                return g;
            }
        }
        return null;
    }

    public HostGameResponses hostGame(Game g) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(g.getGameName()) == null) {
            games.add(g);
            masterDatabase.addMessageDatabase(g.getGameName());
            masterDatabase.addMessageDatabaseBCM(g.getPlayers()[0].getUsername(), g.getGameName()); // when running/testing this remove hardcode in MessageController
            masterDatabase.addGameBCM(g.getPlayers()[0].getUsername(), g.getGameName());

            return HostGameResponses.HOST_GAME_SUCCESS;
        } else {
            return HostGameResponses.ERROR_GAME_ALREADY_EXISTS;
        }
    }


    public JoinGameResponses joinGame(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName) == null) {
            return JoinGameResponses.ERROR_GAME_DNE;
        } else {
            if (getGame(gameName).isGameLoaded()) {
                return JoinGameResponses.ERROR_GAME_LOADED;
            }

            int currentNumPlayers = getGame(gameName).getCurrentNumPlayers();
            int maxNumPlayers = getGame(gameName).getMaxNumPlayers();

            if (currentNumPlayers < maxNumPlayers) {
                getGame(gameName).addPlayer(masterDatabase.getMasterPlayerDatabase().getPlayer(username));

                for (int i = 0; i < currentNumPlayers; i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                masterDatabase.addGameBCM(username, gameName);
                masterDatabase.addMessageDatabaseBCM(username, gameName);
                return JoinGameResponses.JOIN_GAME_SUCCESS;
            } else {
                return JoinGameResponses.ERROR_GAME_FULL;
            }
        }
    }

    public ArrayList<Game> getAllGames() {
        ArrayList<Game> response = new ArrayList<>();
        for(Game g : games){
            if(!g.isActive()){
                response.add(g);
            }
        }
        return response;
    }

    public LeavePregameResponses leavePregame(String gameName, String username) {
        if (getGame(gameName).isGameLoaded()) {
            return LeavePregameResponses.ERROR_GAME_LOADED;
        } else {
            MasterDatabase masterDatabase = MasterDatabase.getInstance();

            masterDatabase.removeGameBCM(username);
            masterDatabase.removeMessageDatabaseBCM(username);

            if (getGame(gameName).getCurrentNumPlayers() == 1) {
                games.remove(getGame(gameName));
                masterDatabase.deleteMessageDatabase(gameName);
            } else {
                getGame(gameName).removePlayer(username);

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }
            }
            return LeavePregameResponses.LEAVE_SUCCESS;
        }
    }

    public IsReadyResponses playerIsReady(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName).getSinglePlayer(username).getHero() == null) {
            return IsReadyResponses.ERROR_NO_SELECTED_HERO;
        } else {
            getGame(gameName).getSinglePlayer(username).setReady(!getGame(gameName).getSinglePlayer(username).isReady());

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }
            return IsReadyResponses.IS_READY_SUCCESS;
        }
    }

    public SelectHeroResponses playerSelectHero(String gameName, String username, HeroClass hero) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (getGame(gameName).getSinglePlayer(username).getHero() == null) {
            for (HeroClass h : getGame(gameName).getAllHeroes()) {
                if (h.equals(hero)) {
                    return SelectHeroResponses.ERROR_DUPLICATE_HERO;
                }
            }

            getGame(gameName).getSinglePlayer(username).setHero(new Hero(hero));

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return SelectHeroResponses.SELECT_HERO_SUCCESS;
        } else {
            return SelectHeroResponses.ERROR_HERO_ALREADY_SELECTED;
        }
    }

    public StartGameResponses startGame(String gameName, String username) {
        if (getGame(gameName).getCurrentNumPlayers() == 1) {
            return StartGameResponses.ERROR_NOT_ENOUGH_PLAYERS;
        }
        if (!getGame(gameName).getPlayers()[0].getUsername().equals(username)) {
            return StartGameResponses.ERROR_NOT_HOST;
        }
        if (getGame(gameName).allReady()) {
            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            getGame(gameName).setActive(true);
            determineWhoStarts(gameName);
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            if (getGame(gameName).getCurrentNumPlayers() == 2) {
                getGame(gameName).setGoldenShields(3);
            } else if (getGame(gameName).getCurrentNumPlayers() == 3) {
                getGame(gameName).setGoldenShields(2);
            } else { // equals 4
                getGame(gameName).setGoldenShields(1);
            }

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return StartGameResponses.START_GAME_SUCCESS;
        } else {
            return StartGameResponses.ERROR_PLAYER_NOT_READY;
        }
    }



    public DistributeItemsResponses distributeItems(String gameName, ItemDistribution itemDistribution) {
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            if(getGame(gameName).getPlayers()[i].getHero().getHeroClass()== HeroClass.WARRIOR){
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getWarriorGold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getWarriorItems());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getWarriorGold() + " Gold and " + itemDistribution.getWarriorItems().size() + " Wineskins");
            }
            if(getGame(gameName).getPlayers()[i].getHero().getHeroClass()== HeroClass.WIZARD){
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getWizardGold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getWizardItems());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getWizardGold() + " Gold and " + itemDistribution.getWizardItems().size() + " Wineskins");
            }
            if(getGame(gameName).getPlayers()[i].getHero().getHeroClass()== HeroClass.DWARF){
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getDwarfGold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getDwarfItems());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getDwarfGold() + " Gold and " + itemDistribution.getDwarfItems().size() + " Wineskins");
            }
            if(getGame(gameName).getPlayers()[i].getHero().getHeroClass()== HeroClass.ARCHER){
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getArcherGold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getArcherItems());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getArcherGold() + " Gold and " + itemDistribution.getArcherItems().size() + " Wineskins");
            }
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        System.out.println(getGame(gameName).getItemsDistributedMessage());
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        getGame(gameName).setItemsDistributed(true);
        return DistributeItemsResponses.DISTRIBUTE_ITEMS_SUCCESS;
    }

    public void determineWhoStarts(String gameName){
        Game currentGame = getGame(gameName);
        Hero startingHero = currentGame.getPlayers()[0].getHero();
        for(int i = 0; i < currentGame.getCurrentNumPlayers(); i++){
            System.out.println(currentGame.getPlayers()[i].getHero().getHeroClass() + " AND " + startingHero.getHeroClass());
            System.out.println(currentGame.getPlayers()[i].getHero().getHeroClass() + "RANK: " + currentGame.getPlayers()[i].getHero().getRank());
            System.out.println(startingHero.getHeroClass() + "RANK: " + startingHero.getRank());
            if(currentGame.getPlayers()[i].getHero().getRank() < startingHero.getRank()){
                startingHero = currentGame.getPlayers()[i].getHero();
            }
        }
        currentGame.setCurrentHero(startingHero);
        System.out.println("CURRENT HERO IS: " + currentGame.getCurrentHero().getHeroClass());
    }

    public GameStartedResponses isGameStarted(String username){
        for(Game g : games){
            for(int i = 0; i < g.getCurrentNumPlayers(); i++){
                if(g.getPlayers()[i].getUsername().equals(username)){
                    if(g.isItemsDistributed()){
                        return GameStartedResponses.GAME_STARTED;
                    }
                }
            }
        }
        return GameStartedResponses.GAME_NOT_STARTED;
    }

    public Game getGameByUsername(String username){
        for(Game g : games){
            for(int i = 0; i < g.getCurrentNumPlayers(); i++){
                if(g.getPlayers()[i].getUsername().equals(username)){
                    return g;
                }
            }
        }
        return null;
    }


    public GetAvailableRegionsRC getAvailableRegions (String gameName, String username) {
        Player p = getGame(gameName).getSinglePlayer(username);

        if (getGame(gameName).getCurrentHero().equals(p.getHero())) {
            if (p.getHero().getCurrentHour() == 10) {
                return new GetAvailableRegionsRC(new ArrayList<>(), GetAvailableRegionsReponses.CURRENT_HOUR_MAXED);
            } else {
                if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.FIGHT) {
                    return new GetAvailableRegionsRC(new ArrayList<>(), GetAvailableRegionsReponses.CANNOT_MOVE_AFTER_FIGHT);
                } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE_PRINCE) {
                    return new GetAvailableRegionsRC(new ArrayList<>(), GetAvailableRegionsReponses.CANNOT_MOVE_AFTER_MOVE_PRINCE);
                } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.NONE) {
                    getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.MOVE);
                }

                if (p.getHero().getCurrentHour() >= 7 && p.getHero().getWillPower() <= 2) {
                    return new GetAvailableRegionsRC(new ArrayList<>(), GetAvailableRegionsReponses.NOT_ENOUGH_WILLPOWER);
                }

                ArrayList<Integer> adjacentRegions = new ArrayList<>();
                RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

                adjacentRegions.addAll(regionDatabase.getRegion(p.getHero().getCurrentSpace()).getAdjacentRegions());

                if (regionDatabase.getRegion(p.getHero().getCurrentSpace()).isBridge()) {
                    adjacentRegions.add(regionDatabase.getRegion(p.getHero().getCurrentSpace()).getBridgeAdjacentRegion());
                }

                if (p.getHero().getCurrentHour() >= 7) {
                    return new GetAvailableRegionsRC(adjacentRegions, GetAvailableRegionsReponses.DEDUCT_WILLPOWER);
                } else {
                    return new GetAvailableRegionsRC(adjacentRegions, GetAvailableRegionsReponses.SUCCESS);
                }
            }
        } else {
            return new GetAvailableRegionsRC(new ArrayList<>(), GetAvailableRegionsReponses.NOT_CURRENT_TURN);
        }
    }

    public MoveRC move(String gameName, String username, Integer targetRegion) { // do the verification on android (checking if is feasible adjacent)
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        h.setMoved(true);
        h.setCurrentSpace(targetRegion);
        if (h.getCurrentHour() >= 7) {
            h.setWillPower(h.getWillPower()-2);
        }
        h.setCurrentHour(h.getCurrentHour()+1);

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        if (regionDatabase.getRegion(targetRegion).getCurrentCreatures().size() > 0) {
            if (h.getFarmers().size() > 0) {
                h.getFarmers().clear();
                return new MoveRC(new ArrayList<>(), MoveResponses.FARMERS_DIED);
            }
        }
        if (regionDatabase.getRegion(targetRegion).getFarmers().size() > 0) {
            return new MoveRC(regionDatabase.getRegion(targetRegion).getFarmers(), MoveResponses.PICK_UP_FARMER);
        } else {
            return new MoveRC(new ArrayList<>(), MoveResponses.NO_OTHER_ACTIONS);
        }
    }

    public ArrayList<Farmer> getFarmers(String gameName, String username) {
        return getGame(gameName).getRegionDatabase().getRegion(getGame(gameName).getSinglePlayer(username).getHero().getCurrentSpace()).getFarmers();
    }

    public PickUpFarmersResponses pickUpFarmers(String gameName, String username, ArrayList<Farmer> farmers) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (farmers.size() == 0) {
            return PickUpFarmersResponses.NO_FARMERS;
        } else {
            regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().clear();
            for (int i = regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().size()-1; i >= farmers.size(); i--) {
                regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().remove(i);
            }

            if (regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreatures().size() > 0) {
                h.getFarmers().clear();
                return PickUpFarmersResponses.FARMERS_DIED;
            }

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            h.getFarmers().addAll(farmers);
            return PickUpFarmersResponses.FARMERS_PICKED_UP;
        }
    }

    public EndMoveResponses endMove(String gameName, String username) { // display end move button after player clicks Move and gets successful return (keep invisible otherwise)
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (!h.isMoved()) {
            return EndMoveResponses.MUST_MOVE_TO_END_MOVE;
        }

        if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE) {
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(username));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            if (regionDatabase.getRegion(h.getCurrentSpace()).isMerchant()) {
                return EndMoveResponses.BUY_FROM_MERCHANT;
            } else if (regionDatabase.getRegion(h.getCurrentSpace()).isFountain() && regionDatabase.getRegion(h.getCurrentSpace()).isFountainStatus()) {
                return EndMoveResponses.EMPTY_WELL;
            } else if ((regionDatabase.getRegion(h.getCurrentSpace()).getFog() != FogKind.NONE) && (regionDatabase.getRegion(h.getCurrentSpace()).isFogRevealed() == false)) {
                return EndMoveResponses.ACTIVATE_FOG; // display a prompt where user must click activate fog
            } else {
                return EndMoveResponses.NONE;
            }
        } else {
            return EndMoveResponses.MOVE_ALREADY_ENDED;
        }
    }

    public EmptyWellResponses emptyWell(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (regionDatabase.getRegion(h.getCurrentSpace()).isFountain()) {
            if (regionDatabase.getRegion(h.getCurrentSpace()).isFountainStatus()) {
                if (h.getHeroClass() == HeroClass.WARRIOR) {
                    h.setWillPower(h.getWillPower()+5);
                } else {
                    h.setWillPower(h.getWillPower()+3);
                }
                regionDatabase.getRegion(h.getCurrentSpace()).setFountainStatus(false);

                MasterDatabase masterDatabase = MasterDatabase.getInstance();
                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return EmptyWellResponses.SUCCESS;
            } else {
                return EmptyWellResponses.WELL_ALREADY_EMPTY;
            }
        } else {
            return EmptyWellResponses.WELL_DNE;
        }
    }

    public ActivateFogRC activateFog(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        int region = h.getCurrentSpace();
        FogKind f = regionDatabase.getRegion(region).getFog();

        if (f != FogKind.NONE) {
            if (f == FogKind.MONSTER) {
                regionDatabase.getRegion(h.getCurrentSpace()).setCurrentCreatures(new ArrayList<Creature>(Arrays.asList(new Creature(CreatureType.GOR))));
            } else if (f == FogKind.WINESKIN) {
                h.getItems().add(new Item(ItemType.WINESKIN));
            } else if (f == FogKind.TWO_WP) {
                h.setWillPower(h.getWillPower()+2);
            } else if (f == FogKind.THREE_WP) {
                h.setWillPower(h.getWillPower()+3);
            } else if (f == FogKind.SP) {
                h.setStrength(h.getStrength()+1);
            } else if (f == FogKind.GOLD) {
                h.setGold(h.getGold()+1);
            } else if (f == FogKind.WITCHBREW) {
                h.getItems().add(new Item(ItemType.WITCH_BREW));
            } else { // event
                // return the EventCard here
            }
            regionDatabase.getRegion(region).setFogRevealed(true);

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return new ActivateFogRC(f, ActivateFogResponses.SUCCESS);
        } else {
            return new ActivateFogRC(FogKind.NONE, ActivateFogResponses.FOG_DNE);
        }
    }

    public EndDayResponses endDay(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (!getGame(gameName).getCurrentHero().equals(getGame(gameName).getSinglePlayer(username).getHero())) {
            return EndDayResponses.NOT_CURRENT_TURN;
        }

        if (!h.isHasEndedDay()) {
            h.setHasEndedDay(true);
            if (getGame(gameName).getFirstHeroInNextDay() == null) {
                getGame(gameName).setFirstHeroInNextDay(h);
            }
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(username));

            if (getGame(gameName).getCurrentHero() == null) { // new day
                getGame(gameName).setCurrentHero(getGame(gameName).getFirstHeroInNextDay());
                getGame(gameName).setFirstHeroInNextDay(null);

                getGame(gameName).getNarrator().incrementNarrator();

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    getGame(gameName).getPlayers()[i].getHero().setHasEndedDay(false);
                    getGame(gameName).getPlayers()[i].getHero().setCurrentHour(0);
                }

                ArrayList<Region> regionsWithCreatures = regionDatabase.getAllRegionsWithCreatures();
                for (Region r : regionsWithCreatures) { // advance every creature once
                    Creature creature = r.getCurrentCreatures().get(0);
                    r.getCurrentCreatures().clear();
                    int newCreatureSpace;

                    do {
                        if (r.getBridgeNextRegion() != null) {
                            newCreatureSpace = r.getBridgeNextRegion();
                        } else {
                            newCreatureSpace = r.getNextRegion();
                        }
                        r = regionDatabase.getRegion(newCreatureSpace);
                    } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreatures().size() > 0);

                    if (newCreatureSpace == 0) {
                        if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                            regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                        } else {
                            System.out.println("Monster entered castle!");
                            getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                        }
                    } else {
                        regionDatabase.getRegion(newCreatureSpace).setCurrentCreatures(new ArrayList<>(Arrays.asList(creature)));
                    }
                }

                ArrayList<Region> regionsWithWardraks = regionDatabase.getAllRegionsWithWardraks();
                for (Region r : regionsWithWardraks) { // advance every wardrak once again
                    Creature creature = r.getCurrentCreatures().get(0);
                    r.getCurrentCreatures().clear();
                    int newCreatureSpace;

                    do {
                        if (r.getBridgeNextRegion() != null) {
                            newCreatureSpace = r.getBridgeNextRegion();
                        } else {
                            newCreatureSpace = r.getNextRegion();
                        }
                        r = regionDatabase.getRegion(newCreatureSpace);
                    } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreatures().size() > 0);

                    if (newCreatureSpace == 0) {
                        if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                            regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                        } else {
                            getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                        }
                    } else {
                        regionDatabase.getRegion(newCreatureSpace).setCurrentCreatures(new ArrayList<Creature>(Arrays.asList(creature)));
                    }
                }

                ArrayList<Region> regionsWithFountains = regionDatabase.getAllRegionsWithFountain();
                for (Region r : regionsWithFountains) { // refresh every fountain (except the ones with a Hero on it)
                    int regionNumber = r.getNumber();
                    boolean heroOnFountain = false;

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        if (getGame(gameName).getPlayers()[i].getHero().getCurrentSpace() == regionNumber) {
                            heroOnFountain = true;
                            break;
                        }
                    }

                    if (!heroOnFountain) { // refresh all fountains without Hero on it
                        r.setFountainStatus(true);
                    }
                }
                // advance creatures
                // refresh wells
                // narrator advances one step

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                if (getGame(gameName).getGoldenShields() < 0) { // game over
                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) { // reset heroes
                        masterDatabase.getMasterPlayerDatabase().getPlayer(getGame(gameName).getPlayers()[i].getUsername()).setHero(null);
                    }

                    games.remove(getGame(gameName));
                    masterDatabase.removeGameBCM(gameName);
                    masterDatabase.deleteMessageDatabase(gameName);

                    return EndDayResponses.GAME_OVER;
                } else {
                    return EndDayResponses.NEW_DAY;
                }
            } else {
                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return EndDayResponses.SUCCESS;
            }
        } else {
            return EndDayResponses.DAY_ALREADY_ENDED;
        }
    }

    public PassResponses pass (String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (h.isHasEndedDay()) {
            return PassResponses.DAY_ENDED;
        } else if (!getGame(gameName).getCurrentHero().equals(h)) {
            return PassResponses.NOT_CURRENT_TURN;
        } else if (h.getCurrentHour() == 10 || (h.getWillPower() <= 2 && h.getCurrentHour() >= 7)) {
            return PassResponses.MUST_END_DAY;
        } else { // can pass turn
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(username));

            if (getGame(gameName).getCurrentHero() == null) {
                getGame(gameName).setCurrentHero(h);
                return PassResponses.ONLY_PLAYER_LEFT;
            }

            h.setCurrentHour(h.getCurrentHour()+1);
            if (h.getCurrentHour() >= 6) {
                h.setWillPower(h.getWillPower()-2);
                return PassResponses.PASS_SUCCESSFUL_WP_DEDUCTED;
            }

            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return PassResponses.PASS_SUCCESSFUL;
        }
    }

    public GetPossibleCreaturesToFightRC getPossibleCreaturesToFight(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (!getGame(gameName).getCurrentHero().equals(h)) {
            return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.NOT_CURRENT_TURN, new ArrayList<>());
        } else if (h.isHasEndedDay()) {
            return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.DAY_ENDED, new ArrayList<>());
        } else if (regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreatures().size() == 0) {
            return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.NO_CREATURE_FOUND, new ArrayList<>());
        } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE) {
            return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.CANNOT_FIGHT_AFTER_MOVE, new ArrayList<>());
        } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE_PRINCE) {
            System.out.println("wtf");
            return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.CANNOT_FIGHT_AFTER_MOVE_PRINCE, new ArrayList<>());
        } else {
            boolean ownsBow = false;

            for (Item item : h.getItems()) {
                if (item.getItemType() == ItemType.BOW) {
                    ownsBow = true;
                }
            }

            if (h.getHeroClass() == HeroClass.ARCHER || ownsBow) {
                ArrayList<Integer> possibleCreaturesToFight = new ArrayList<>();
                ArrayList<Integer> adjacentRegions = new ArrayList<>();

                adjacentRegions = regionDatabase.getRegion(h.getCurrentSpace()).getAdjacentRegions();
                if (regionDatabase.getRegion(h.getCurrentSpace()).isBridge()) {
                    adjacentRegions.add(regionDatabase.getRegion(h.getCurrentSpace()).getBridgeAdjacentRegion());
                }

                for (Integer space : adjacentRegions) {
                    if (regionDatabase.getRegion(space).getCurrentCreatures().size() > 0) {
                        possibleCreaturesToFight.add(space);
                    }
                }
                return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.SUCCESS, possibleCreaturesToFight);
            } else {
                return new GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses.SUCCESS, new ArrayList<>(Arrays.asList(h.getCurrentSpace())));
            }
        }
    }

    public Fight fight(String gameName, String username, Integer targetRegion) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        boolean ownsBow = false;

        for (Item item : h.getItems()) {
            if (item.getItemType() == ItemType.BOW) {
                ownsBow = true;
            }
        }
        if (ownsBow && h.getCurrentSpace() != targetRegion) {
            h.setBowActivated(true);
        }

        Fight fight = new Fight(targetRegion, h, regionDatabase.getRegion(targetRegion).getCurrentCreatures().get(0));
        getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.FIGHT);

        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            Player p = getGame(gameName).getPlayers()[i];
            ownsBow = false;

            if (!p.getUsername().equals(username)) {
                for (Item item : h.getItems()) {
                    if (item.getItemType() == ItemType.BOW) {
                        ownsBow = true;
                    }
                }

                if (p.getHero().getHeroClass() == HeroClass.ARCHER || ownsBow) {
                    Region region = regionDatabase.getRegion(p.getHero().getCurrentSpace());
                    ArrayList<Integer> adjacentRegions = region.getAdjacentRegions();
                    adjacentRegions.add(p.getHero().getCurrentSpace());

                    if (region.isBridge()) {
                        adjacentRegions.add(region.getBridgeAdjacentRegion());
                    }

                    if (adjacentRegions.contains(h.getCurrentSpace())) {
                        if (h.getCurrentHour() >= 7 && h.getCurrentHour() != 10) {
                            if (h.getWillPower() >= 3) {
                                fight.getPendingInvitedHeroes().add(p.getHero());
                            }
                        } else {
                            if (h.getWillPower() >= 1) {
                                fight.getPendingInvitedHeroes().add(p.getHero());
                            }
                        }
                    }
                } else {
                    if (p.getHero().getCurrentSpace() == h.getCurrentSpace()) {
                        if (h.getCurrentHour() >= 7 && h.getCurrentHour() != 10) {
                            if (h.getWillPower() >= 3) {
                                fight.getPendingInvitedHeroes().add(p.getHero());
                            }
                        } else {
                            if (h.getWillPower() >= 1) {
                                fight.getPendingInvitedHeroes().add(p.getHero());
                            }
                        }
                    }
                }
            }
        }

        getGame(gameName).setCurrentFight(fight);

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
        return fight;
    }

    public void joinFight(String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (h.getCurrentSpace() != getGame(gameName).getCurrentFight().getRegionNumber() && h.getHeroClass() != HeroClass.ARCHER) {
            h.setBowActivated(true);
        }

        getGame(gameName).getCurrentFight().getHeroes().add(h);
        getGame(gameName).getCurrentFight().getHeroesBattleScores().add(0);
        getGame(gameName).getCurrentFight().getPendingInvitedHeroes().remove(h);

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public LeaveFightResponses leaveFight(String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        Fight fight = getGame(gameName).getCurrentFight();
        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        if (fight == null) {
            return LeaveFightResponses.SUCCESS;
        }

        if (fight.getHeroes().size() == 0) {
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(getGame(gameName).getCurrentHero().getHeroClass()));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            getGame(gameName).setCurrentFight(null);

            return LeaveFightResponses.SUCCESS;
        }

        if (h.isFought()) {
            return LeaveFightResponses.CANNOT_LEAVE_WITHOUT_FIGHTING;
        }

        int index = fight.getHeroes().indexOf(h);
        if (index != -1) {
            if (fight.getHeroesBattleScores().get(index) > 0) {
                return LeaveFightResponses.CANNOT_LEAVE_AFTER_ROLLING;
            }
        }

        fight.getHeroes().remove(index);
        fight.getHeroesBattleScores().remove(index);
        h.setFought(false);
        h.setBowActivated(false);

        if (fight.getHeroes().size() == 0) {
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(getGame(gameName).getCurrentHero().getHeroClass()));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);
        }

        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        getGame(gameName).setCurrentFight(null);

        return LeaveFightResponses.SUCCESS;
    }

    public ArrayList<Die> getDice(String gameName, String username) { // DID NOT CHECK FOR BLACK DIE HERE ADD IN FUTURE!!!
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        Fight fight = getGame(gameName).getCurrentFight();
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        boolean greenRuneStoneFound = false;
        boolean blueRuneStoneFound = false;
        boolean yellowRuneStoneFound = false;

        for (RuneStone r : h.getRuneStones()) {
            if (r.getColour() == Colour.GREEN) {
                greenRuneStoneFound = true;
            } else if (r.getColour() == Colour.BLUE) {
                blueRuneStoneFound = true;
            } else { // yellow
                yellowRuneStoneFound = true;
            }
        }

        if (h.getHeroClass() == HeroClass.WIZARD) {
            if (greenRuneStoneFound && blueRuneStoneFound && yellowRuneStoneFound) {
                fight.setWizardDice(new ArrayList<>(Arrays.asList(-1)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                fight.setWizardDice(new ArrayList<>(Arrays.asList(0)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE)));
            }
        } else if (h.getHeroClass() == HeroClass.WARRIOR) {
            if (greenRuneStoneFound && blueRuneStoneFound && yellowRuneStoneFound) {
                fight.setWarriorDice(new ArrayList<>(Arrays.asList(-1)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                if (h.getWillPower() < 7) {
                    fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                } else if (h.getWillPower() < 14) {
                    fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                } else {
                    fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                }
            }
        } else if (h.getHeroClass() == HeroClass.DWARF) {
            if (greenRuneStoneFound && blueRuneStoneFound && yellowRuneStoneFound) {
                fight.setDwarfDice(new ArrayList<>(Arrays.asList(-1)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                if (h.getWillPower() < 7) {
                    fight.setDwarfDice(new ArrayList<>(Arrays.asList(0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE)));
                } else if (h.getWillPower() < 14) {
                    fight.setDwarfDice(new ArrayList<>(Arrays.asList(0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                } else {
                    fight.setDwarfDice(new ArrayList<>(Arrays.asList(0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                }
            }
        } else { // ARCHER
            if (greenRuneStoneFound && blueRuneStoneFound && yellowRuneStoneFound) {
                fight.setArcherDice(new ArrayList<>(Arrays.asList(-1)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                if (h.getWillPower() < 7) {
                    fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                } else if (h.getWillPower() < 14) {
                    fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                } else {
                    fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));

                    for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                        masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                    }

                    return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
                }
            }
        }
    }

    public Integer calculateBattleValue(String gameName, String username, ArrayList<Integer> diceRolls) { // control the way this method can be called error check on client (did not account for doubles)
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        Fight fight = getGame(gameName).getCurrentFight();

        h.setFought(false);

        if (h.getHeroClass().equals(HeroClass.WARRIOR)) {
            fight.setWarriorDice(diceRolls);
        } else if (h.getHeroClass().equals(HeroClass.ARCHER)) {
            fight.setArcherDice(diceRolls);
        } else if (h.getHeroClass().equals(HeroClass.DWARF)) {
            fight.setDwarfDice(diceRolls);
        } else { // wizard
            fight.setWizardDice(diceRolls);
        }

        int max = 0;

        if (h.getHeroClass() == HeroClass.ARCHER || h.isBowActivated()) {
            int indexOfZero = -1;

            for (int i = 0; i < diceRolls.size(); i++) {
                if (diceRolls.get(i) == 0) {
                    indexOfZero = i;
                    break;
                }
            }

            if (indexOfZero == -1) {
                max = diceRolls.get(diceRolls.size()-1);
            } else {
                max = diceRolls.get(indexOfZero-1);
            }
        } else {
            if (h.getHeroClass() != HeroClass.ARCHER) {
                for (int i : diceRolls) {
                    if (i > max) {
                        max = i;
                    }
                }
            } else {
                max = diceRolls.get(diceRolls.size() - 1);
            }
        }

        getGame(gameName).getCurrentFight().getHeroesBattleScores().set(getGame(gameName).getCurrentFight().getHeroes().indexOf(h), max + h.getStrength());

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        return max + h.getStrength();
    }

    public ArrayList<Die> getCreatureDice(String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        Creature creature = getGame(gameName).getCurrentFight().getCreature();
        Fight fight = getGame(gameName).getCurrentFight();

        if (creature.getCreatureType() == CreatureType.WARDRAKS) {
            if (creature.getWillpower() < 7) {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(-1))); // -1 MEANS BLACK DIE!!!!!!!!!!!!!!!!!!!!

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(-1, -1)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE), new Die(DieType.BLACK_DIE)));
            }
        } else {
            if (creature.getWillpower() < 7) {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(0, 0)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(0, 0, 0)));

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            }
        }
    }

    public Integer calculateCreatureBattleValue(String gameName, String username, ArrayList<Integer> diceRolls) {
        Creature creature = getGame(gameName).getCurrentFight().getCreature();

        getGame(gameName).getCurrentFight().setCreatureDice(diceRolls);

        if (creature.getCreatureType() == CreatureType.TROLL) {
            int maxRoll = Collections.max(diceRolls);
            getGame(gameName).getCurrentFight().setCreatureBattleScore(maxRoll + creature.getStrength());

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }
            return maxRoll + creature.getStrength();
        }

        Collections.sort(diceRolls);
        int prevValue = diceRolls.get(0);
        int duplicateCurrent = 0;
        int duplicateMax = 0;
        for (int i : diceRolls) {
            if (i == prevValue) {
                duplicateCurrent += prevValue;
            } else {
                if (i > duplicateCurrent) {
                    duplicateCurrent = i;
                }

                if (duplicateCurrent > duplicateMax) {
                    duplicateMax = duplicateCurrent;
                }
                duplicateCurrent = i;
            }
            prevValue = i;
        }

        if (duplicateMax < duplicateCurrent) {
            duplicateMax = duplicateCurrent;
        }

        getGame(gameName).getCurrentFight().setCreatureBattleScore(duplicateMax + creature.getStrength());

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
        return duplicateMax + creature.getStrength();
    }

    public EndBattleRoundResponses endBattleRound (String gameName, String username) {
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        Fight fight = getGame(gameName).getCurrentFight();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        if (fight.getPendingInvitedHeroes().size() > 0) {
            return EndBattleRoundResponses.WAITING_FOR_PLAYERS_TO_JOIN;
        }

        if (fight.getCreatureBattleScore() == 0) {
            return EndBattleRoundResponses.CREATURE_NO_BATTLE_VALUE;
        }

        for (Integer bv : fight.getHeroesBattleScores()) {
            if (bv == 0) {
                return EndBattleRoundResponses.PLAYERS_NO_BATTLE_VALUE;
            }
        }

        for (Hero h : fight.getHeroes()) { // if doesn't work use getHeroByHC
            if (h.getCurrentHour() >= 7) {
                h.setWillPower(h.getWillPower()-2);
            }
            h.setCurrentHour(h.getCurrentHour()+1);
        }
        int totalHeroesBV = 0;
        for (Integer bv : fight.getHeroesBattleScores()) {
            totalHeroesBV += bv;
        }

        int difference;

        if (getGame(gameName).getPrinceThorald() != null) {
            difference = 4 + totalHeroesBV - fight.getCreatureBattleScore();
        } else {
            difference = totalHeroesBV - fight.getCreatureBattleScore();
        }

        fight.getHeroesBattleScores().clear();
        for (int i = 0; i < fight.getHeroes().size(); i++) { // reset battle values test!!!!!!!!!!!
            fight.getHeroesBattleScores().add(0);
        }
        fight.setCreatureBattleScore(0);
        fight.getCreatureDice().clear();
        fight.getWarriorDice().clear();
        fight.getArcherDice().clear();
        fight.getDwarfDice().clear();
        fight.getWizardDice().clear();

        if (difference < 0) {
            for (Iterator<Hero> it = fight.getHeroes().iterator(); it.hasNext();) {
                Hero h = it.next();

                if (!h.isShieldActivatedFight()) {
                    h.setWillPower(h.getWillPower() + difference);

                    if (h.getWillPower() <= 0) {
                        if (h.getStrength() > 1) {
                            h.setStrength(h.getStrength() - 1);
                        }
                        h.setWillPower(3);
                        int index = fight.getHeroes().indexOf(h);
                        it.remove();
                        fight.getHeroesBattleScores().remove(index);
                        h.setBowActivated(false);
                    } else if (h.getCurrentHour() == 10) {
                        int index = fight.getHeroes().indexOf(h);
                        it.remove();
                        fight.getHeroesBattleScores().remove(index);
                        h.setBowActivated(false);
                    }
                } else {
                    h.setShieldActivatedFight(false);
                }
            }

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            if (fight.getHeroes().size() == 0) { // force client to press leave fight!!!!!
                if (fight.getCreature().getCreatureType() == CreatureType.GOR) { // TEST THIS OUT NOT SURE IF WILL CHANGE CREATURE
                    regionDatabase.getRegion(fight.getRegionNumber()).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
                } else if (fight.getCreature().getCreatureType() == CreatureType.SKRAL) {
                    regionDatabase.getRegion(fight.getRegionNumber()).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.SKRAL))));
                } else if (fight.getCreature().getCreatureType() == CreatureType.TROLL) {
                    regionDatabase.getRegion(fight.getRegionNumber()).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.TROLL))));
                } else { // wardraks
                    regionDatabase.getRegion(fight.getRegionNumber()).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.WARDRAKS))));
                }

                return EndBattleRoundResponses.BATTLE_LOST;
            } else {
                return EndBattleRoundResponses.LOST_ROUND;
            }
        } else if (difference == 0) {
            for (Hero h : fight.getHeroes()) { // if doesn't work use getHeroByHC
                if (h.getWillPower() <= 0 || h.getCurrentHour() == 10) {
                    if (h.getStrength() > 1) {
                        h.setStrength(h.getStrength() - 1);
                    }
                    h.setWillPower(3);
                    int index = fight.getHeroes().indexOf(h);
                    fight.getHeroes().remove(index);
                    fight.getHeroesBattleScores().remove(index);
                }
            }

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            if (fight.getHeroes().size() == 0) { // force client to press leave fight!!!!!
                return EndBattleRoundResponses.BATTLE_LOST;
            } else {
                return EndBattleRoundResponses.TIE_ROUND;
            }
        } else {
            fight.getCreature().setWillpower(fight.getCreature().getWillpower()-difference);
            Hero h = getGame(gameName).getSinglePlayer(username).getHero();

            if (fight.getCreature().getWillpower() <= 0) { // force player to press leave fight!!!!
                regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreatures().clear();
                regionDatabase.getRegion(80).getCurrentCreatures().add(fight.getCreature());

                getGame(gameName).getNarrator().incrementNarrator();

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return EndBattleRoundResponses.CREATURE_DEFEATED;
            } else {
                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return EndBattleRoundResponses.WON_ROUND;
            }
        }
    }

    public ActivateHelmResponses activateHelm(String gameName, String username) {
        Game g = getGame(gameName);
        Fight fight = g.getCurrentFight();
        Hero h = g.getSinglePlayer(username).getHero();
        boolean helmFound = false;
        boolean bowFound = false;

        if (h.getHeroClass() == HeroClass.ARCHER) {
            return ActivateHelmResponses.ERROR_ARCHER;
        } else if (h.getHeroClass() == HeroClass.WIZARD) {
            return ActivateHelmResponses.ERROR_WIZARD;
        }

        for (Item item : h.getItems()) {
            if (item.getItemType() == ItemType.HELM) {
                helmFound = true;
            } else if (item.getItemType() == ItemType.BOW) {
                bowFound = true;
            }
        }

        if (!helmFound) {
            return ActivateHelmResponses.ERROR_DOES_NOT_OWN_HELM;
        }
        if (bowFound && h.isBowActivated()) {
            return ActivateHelmResponses.ERROR_BOW_USER;
        }

        ArrayList<Integer> diceRolls = new ArrayList<>();

        if (h.getHeroClass() == HeroClass.WARRIOR) {
            diceRolls = fight.getWarriorDice();
        } else if (h.getHeroClass() == HeroClass.DWARF) {
            diceRolls = fight.getDwarfDice();
        }

        if (diceRolls.get(0) > 0) {
            Collections.sort(diceRolls);
            int prevValue = diceRolls.get(0);
            int duplicateCurrent = 0;
            int duplicateMax = 0;
            for (int i : diceRolls) {
                if (i == prevValue) {
                    duplicateCurrent += prevValue;
                } else {
                    if (i > duplicateCurrent) {
                        duplicateCurrent = i;
                    }

                    if (duplicateCurrent > duplicateMax) {
                        duplicateMax = duplicateCurrent;
                    }
                    duplicateCurrent = i;
                }
                prevValue = i;
            }

            if (duplicateMax < duplicateCurrent) {
                duplicateMax = duplicateCurrent;
            }

            getGame(gameName).getCurrentFight().getHeroesBattleScores().set(fight.getHeroes().indexOf(h), duplicateMax + h.getStrength());

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < g.getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return ActivateHelmResponses.HELM_ACTIVATED;
        } else {
            return ActivateHelmResponses.ERROR_NO_DICE_ROLLS;
        }
    }

    public ActivateShieldFightResponses activateShieldFight(String gameName, String username) {
        Game g = getGame(gameName);
        Fight fight = g.getCurrentFight();
        Hero h = g.getSinglePlayer(username).getHero();

        if (h.isShieldActivatedFight()) {
            return ActivateShieldFightResponses.ERROR_SHIELD_ALREADY_ACTIVATED;
        }

        int totalHeroesBV = 0;
        for (Integer bv : fight.getHeroesBattleScores()) {
            totalHeroesBV += bv;
            if (bv == 0) {
                return ActivateShieldFightResponses.ERROR_INAPPROPIRATE_SHIELD_ACTIVATION;
            }
        }

        if (fight.getCreatureBattleScore() == 0) {
            return ActivateShieldFightResponses.ERROR_INAPPROPIRATE_SHIELD_ACTIVATION;
        }

        if (getGame(gameName).getPrinceThorald() != null) {
            if (4 + totalHeroesBV - fight.getCreatureBattleScore() >= 0) {
                return ActivateShieldFightResponses.ERROR_INAPPROPIRATE_SHIELD_ACTIVATION;
            }
        } else {
            if (totalHeroesBV - fight.getCreatureBattleScore() >= 0) {
                return ActivateShieldFightResponses.ERROR_INAPPROPIRATE_SHIELD_ACTIVATION;
            }
        }

        for (Iterator<Item> it = h.getItems().iterator(); it.hasNext();) {
            Item item = it.next();

            if (item.getItemType() == ItemType.SHIELD) {
                h.setShieldActivatedFight(true);
                item.setNumUses(item.getNumUses()-1);
                if (item.getNumUses() == 0) {
                    it.remove();
                }
            }
        }

        return ActivateShieldFightResponses.ERROR_DOES_NOT_OWN_SHIELD;
    }


    public BuyFromMerchantResponses buyFromMerchant (String gameName, String username, MerchantPurchase merchantPurchase) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero hero = getGame(gameName).getSinglePlayer(username).getHero();

        if (regionDatabase.getRegion(hero.getCurrentSpace()).isMerchant()) {
            int totalGold = 0;
            Hero h = getGame(gameName).getSinglePlayer(username).getHero();
            MasterDatabase masterDatabase = MasterDatabase.getInstance();

            if (h.getHeroClass() == HeroClass.DWARF && hero.getCurrentSpace() == 71) {
                totalGold += merchantPurchase.getStrength();
            } else {
                totalGold += merchantPurchase.getStrength() * 2;
            }
            totalGold += merchantPurchase.getItems().size() * 2;

            if (h.getGold() >= totalGold) {
                h.setGold(h.getGold() - totalGold);
                h.setStrength(h.getStrength() + merchantPurchase.getStrength());
                h.getItems().addAll(merchantPurchase.getItems());

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
                }

                return BuyFromMerchantResponses.SUCCESS;
            } else {
                return BuyFromMerchantResponses.NOT_ENOUGH_GOLD;
            }
        }
        else{
            return BuyFromMerchantResponses.MERCHANT_DNE;
        }
    }

    public void dropGold(String gameName, String username, Integer gold) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        h.setGold(h.getGold() - gold);
        regionDatabase.getRegion(h.getCurrentSpace()).setGold(regionDatabase.getRegion(h.getCurrentSpace()).getGold() + gold);
    }

    public Integer getGold(String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        return regionDatabase.getRegion(h.getCurrentSpace()).getGold();
    }

    public void pickUpGold(String gameName, String username, Integer gold) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        h.setGold(h.getGold() + gold);
        regionDatabase.getRegion(h.getCurrentSpace()).setGold(regionDatabase.getRegion(h.getCurrentSpace()).getGold() - gold);
    }

    public void dropFarmers(String gameName, String username, ArrayList<Farmer> farmers) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().addAll(farmers);
    }

    public void distributeAfterFight(String gameName, String username, FightDistribution fightDistribution) {
        Game g = getGame(gameName);

        for (int i = 0; i < g.getCurrentNumPlayers(); i++) {
            if (g.getPlayers()[i].getHero().getHeroClass() == HeroClass.DWARF) {
                g.getPlayers()[i].getHero().setGold(g.getPlayers()[i].getHero().getGold() + fightDistribution.getDwarfGold());
                g.getPlayers()[i].getHero().setWillPower(g.getPlayers()[i].getHero().getWillPower() + fightDistribution.getDwarfWillpower());
            } else if (g.getPlayers()[i].getHero().getHeroClass() == HeroClass.WARRIOR) {
                g.getPlayers()[i].getHero().setGold(g.getPlayers()[i].getHero().getGold() + fightDistribution.getWarriorGold());
                g.getPlayers()[i].getHero().setWillPower(g.getPlayers()[i].getHero().getWillPower() + fightDistribution.getWarriorWillpower());
            } else if (g.getPlayers()[i].getHero().getHeroClass() == HeroClass.ARCHER) {
                g.getPlayers()[i].getHero().setGold(g.getPlayers()[i].getHero().getGold() + fightDistribution.getArcherGold());
                g.getPlayers()[i].getHero().setWillPower(g.getPlayers()[i].getHero().getWillPower() + fightDistribution.getArcherWillpower());
            } else { // WIZARD
                g.getPlayers()[i].getHero().setGold(g.getPlayers()[i].getHero().getGold() + fightDistribution.getWizardGold());
                g.getPlayers()[i].getHero().setWillPower(g.getPlayers()[i].getHero().getWillPower() + fightDistribution.getWizardWillpower());
            }
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < g.getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public ArrayList<Farmer> getMyFarmers(String gameName, String username) {
        return getGame(gameName).getSinglePlayer(username).getHero().getFarmers();
    }


    public void gameOver(String gameName){
        if(getGame(gameName) != null){
            games.remove(getGame(gameName));
        }
    }

    public void saveGame(String gameName) {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("savedGames", true));
            writer.write(new Gson().toJson(getGame(gameName)));
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void activateLegendCardC(String gameName, String username, Integer dieRoll) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        int skralStronghold = dieRoll + 50;

        getGame(gameName).setSkralStronghold(skralStronghold);

        regionDatabase.getRegion(skralStronghold).getCurrentCreatures().clear();
        regionDatabase.getRegion(skralStronghold).getCurrentCreatures().add(new Creature(getGame(gameName).getDifficultMode(), getGame(gameName).getCurrentNumPlayers()));

        regionDatabase.getRegion(27).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
        regionDatabase.getRegion(31).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
        regionDatabase.getRegion(29).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.SKRAL))));
        getGame(gameName).setPrinceThorald(new PrinceThorald(72));

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public void activateLegendCardG(String gameName, String username) {
        Game g = getGame(gameName);
        RegionDatabase regionDatabase = g.getRegionDatabase();
        Region r = regionDatabase.getRegion(26);

        if (regionDatabase.getRegion(26).getCurrentCreatures().size() > 0) {
            Creature creature = r.getCurrentCreatures().get(0);
            r.getCurrentCreatures().clear();
            int newCreatureSpace;

            do {
                if (r.getBridgeNextRegion() != null) {
                    newCreatureSpace = r.getBridgeNextRegion();
                } else {
                    newCreatureSpace = r.getNextRegion();
                }
                r = regionDatabase.getRegion(newCreatureSpace);
            } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreatures().size() > 0);

            if (newCreatureSpace == 0) {
                if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                    regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                } else {
                    getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                }
            } else {
                regionDatabase.getRegion(newCreatureSpace).setCurrentCreatures(new ArrayList<>(Arrays.asList(creature)));
            }
        } else {
            regionDatabase.getRegion(26).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.WARDRAKS))));
        }

        r = regionDatabase.getRegion(27);
        if (regionDatabase.getRegion(27).getCurrentCreatures().size() > 0) {
            Creature creature = r.getCurrentCreatures().get(0);
            r.getCurrentCreatures().clear();
            int newCreatureSpace;

            do {
                if (r.getBridgeNextRegion() != null) {
                    newCreatureSpace = r.getBridgeNextRegion();
                } else {
                    newCreatureSpace = r.getNextRegion();
                }
                r = regionDatabase.getRegion(newCreatureSpace);
            } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreatures().size() > 0);

            if (newCreatureSpace == 0) {
                if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                    regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                } else {
                    getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                }
            } else {
                regionDatabase.getRegion(newCreatureSpace).setCurrentCreatures(new ArrayList<>(Arrays.asList(creature)));
            }
        } else {
            regionDatabase.getRegion(27).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.WARDRAKS))));
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public GetPrinceThoraldMovesRC getPrinceThoraldMoves(String gameName, String username) {
        Player p = getGame(gameName).getSinglePlayer(username);

        if (getGame(gameName).getPrinceThorald() == null) {
            return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.PRINCE_DNE);
        }

        if (getGame(gameName).getCurrentHero().equals(p.getHero())) {
            if (p.getHero().getCurrentHour() == 10) {
                return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.CURRENT_HOUR_MAXED);
            } else {
                if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.FIGHT) {
                    return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.CANNOT_MOVE_PRINCE_AFTER_FIGHT);
                } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE) {
                    return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.CANNOT_MOVE_PRINCE_AFTER_MOVE);
                } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.NONE) {
                    getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.MOVE_PRINCE);
                }

                if (p.getHero().getCurrentHour() >= 7 && p.getHero().getWillPower() <= 2) {
                    return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.NOT_ENOUGH_WILLPOWER);
                }

                ArrayList<Integer> moves = new ArrayList<>();
                Game g = getGame(gameName);
                RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

                // add all regions adjacent by 1 space
                moves.addAll(regionDatabase.getRegion(g.getPrinceThorald().getCurrentPosition()).getAdjacentRegions());
                if (regionDatabase.getRegion(g.getPrinceThorald().getCurrentPosition()).isBridge()) {
                    moves.add(regionDatabase.getRegion(g.getPrinceThorald().getCurrentPosition()).getBridgeAdjacentRegion());
                }

                // add all regions adjacent by 2 spaces
                for (Integer move : moves) {
                    moves.addAll(regionDatabase.getRegion(move).getAdjacentRegions());
                    if (regionDatabase.getRegion(move).isBridge()) {
                        moves.add(regionDatabase.getRegion(move).getBridgeAdjacentRegion());
                    }
                }

                // add all regions adjacent by 3 spaces
                for (Integer move : moves) {
                    moves.addAll(regionDatabase.getRegion(move).getAdjacentRegions());
                    if (regionDatabase.getRegion(move).isBridge()) {
                        moves.add(regionDatabase.getRegion(move).getBridgeAdjacentRegion());
                    }
                }

                // add all regions adjacent by 4 spaces
                for (Integer move : moves) {
                    moves.addAll(regionDatabase.getRegion(move).getAdjacentRegions());
                    if (regionDatabase.getRegion(move).isBridge()) {
                        moves.add(regionDatabase.getRegion(move).getBridgeAdjacentRegion());
                    }
                }

                // remove duplicates
                LinkedHashSet<Integer> hashSet = new LinkedHashSet<>(moves);
                ArrayList<Integer> movesWithoutDuplicates = new ArrayList<>(hashSet);

                if (p.getHero().getCurrentHour() >= 7) {
                    return new GetPrinceThoraldMovesRC(movesWithoutDuplicates, GetPrinceThoraldMovesResponses.DEDUCT_WILLPOWER);
                } else {
                    return new GetPrinceThoraldMovesRC(movesWithoutDuplicates, GetPrinceThoraldMovesResponses.SUCCESS);
                }
            }
        } else {
            return new GetPrinceThoraldMovesRC(new ArrayList<>(), GetPrinceThoraldMovesResponses.NOT_CURRENT_TURN);
        }
    }

    public void movePrinceThorald (String gameName, String username, Integer targetRegion) {
        Game g = getGame(gameName);
        Hero h = g.getSinglePlayer(username).getHero();

        h.setMovedPrince(true);

        if (h.getCurrentHour() >= 7) {
            h.setWillPower(h.getWillPower()-2);
        }
        h.setCurrentHour(h.getCurrentHour()+1);

        g.getPrinceThorald().setCurrentPosition(targetRegion);

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public EndMovePrinceThoraldResponses endMovePrinceThorald(String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (!h.isMoved()) {
            return EndMovePrinceThoraldResponses.MUST_MOVE_PRINCE_TO_END_MOVE;
        }

        if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE_PRINCE) {
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(username));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return EndMovePrinceThoraldResponses.SUCCESS;
        } else {
            return EndMovePrinceThoraldResponses.MOVE_PRINCE_ALREADY_ENDED;
        }
    }

    public void setRuneStonesLegendCard(String gameName, String username, Integer dieRoll) {
        if (dieRoll == 1) {
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.B);
        } else if (dieRoll == 2) {
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.D);
        } else if (dieRoll == 3) {
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.E);
        } else if (dieRoll == 4) {
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.F);
        } else if (dieRoll == 5) {
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.F);
        } else { // 6
            getGame(gameName).setRuneStoneLegendCard(NarratorSpace.H);
        }
    }

    public void activateLegendCardRuneStones(String gameName, String username, ArrayList<Integer> runeStonePositions) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        if (getGame(gameName).getDifficultMode()) {
            regionDatabase.getRegion(32).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
            regionDatabase.getRegion(43).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
            regionDatabase.getRegion(39).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.SKRAL))));

            if (getGame(gameName).getWitch() != null) {
                for (int i = 0; i < runeStonePositions.size(); i++) {
                    if (i == 0) {
                        regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.BLUE));
                    } else if (i == 1) {
                        regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.BLUE));
                    } else if (i == 2) {
                        regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.YELLOW));
                    } else if (i == 3) {
                        regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.YELLOW));
                    } else { // i == 4
                        regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.GREEN));
                    }
                }
            }
        } else {
            regionDatabase.getRegion(43).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.GOR))));
            regionDatabase.getRegion(39).setCurrentCreatures(new ArrayList<>(Arrays.asList(new Creature(CreatureType.SKRAL))));

            for (int i = 0; i < runeStonePositions.size(); i++) {
                if (i == 0) {
                    regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.BLUE));
                } else if (i == 1) {
                    regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.BLUE));
                } else if (i == 2) {
                    regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.YELLOW));
                } else if (i == 3) {
                    regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.YELLOW));
                } else { // i == 4
                    regionDatabase.getRegion(runeStonePositions.get(i)).getRuneStones().add(new RuneStone(Colour.GREEN));
                }
            }
        }
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public void activateLegendCardTheWitch(String gameName, String username, Integer dieRoll) {
        Game g = getGame(gameName);
        Hero h = g.getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = g.getRegionDatabase();

        g.setWitch(new Witch(h.getCurrentSpace()));
        h.getItems().add(new Item(ItemType.WITCH_BREW));

        Creature gor = new Creature(CreatureType.GOR);
        gor.setMedicinalHerb(new Item(ItemType.MEDICINAL_HERB));

        if (dieRoll == 1 || dieRoll == 2) {
            regionDatabase.getRegion(37).setCurrentCreatures(new ArrayList<>(Arrays.asList(gor)));
        } else if (dieRoll == 3 || dieRoll == 4) {
            regionDatabase.getRegion(67).setCurrentCreatures(new ArrayList<>(Arrays.asList(gor)));
        } else { // 5 or 6
            regionDatabase.getRegion(61).setCurrentCreatures(new ArrayList<>(Arrays.asList(gor)));
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }
    }

    public void activateLegendCardN(String gameName, String username) {
        Game g = getGame(gameName);
        RegionDatabase regionDatabase = g.getRegionDatabase();
        boolean medicinalHerbInCastle = false;

        for (Item item : regionDatabase.getRegion(0).getItems()) {
            if (item.getItemType() == ItemType.MEDICINAL_HERB) {
                medicinalHerbInCastle = true;
                break;
            }
        }

        if (medicinalHerbInCastle && regionDatabase.getRegion(g.getSkralStronghold()).getCurrentCreatures().size() == 0 && g.getGoldenShields() >= 0) {
            g.setGameStatus(GameStatus.GAME_WON);
        } else {
            g.setGameStatus(GameStatus.GAME_LOST);
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        games.remove(getGame(gameName));
        masterDatabase.removeGameBCM(gameName);
        masterDatabase.deleteMessageDatabase(gameName);
    }

    public AddDropItemResponses addItem(String username, String gameName, ItemType itemType){
        Game game = getGame(gameName);
        Region region = null;
        Hero hero = null;
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
                hero = game.getPlayers()[i].getHero();
            }
        }

        if(region == null || hero == null){
            return AddDropItemResponses.ADD_DROP_FAILURE;
        }

        if(!canAddItem(hero,itemType)){
            return AddDropItemResponses.MAX_ITEMS;
        }

        for(Item item : region.getItems()){
            if(item.getItemType() == itemType){
                region.getItems().remove(item);
                hero.getItems().add(item);
                return AddDropItemResponses.ITEM_ADDED;
            }
        }
        return  AddDropItemResponses.ADD_DROP_FAILURE;
    }

    public AddDropItemResponses addRunestone(String username, String gameName, Colour colour){
        Game game = getGame(gameName);
        Region region = null;
        Hero hero = null;
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
                hero = game.getPlayers()[i].getHero();
            }
        }

        if(region == null || hero == null){
            return AddDropItemResponses.ADD_DROP_FAILURE;
        }

        if(!canAddRunestone(hero)){
            return AddDropItemResponses.MAX_ITEMS;
        }

        for(RuneStone stone : region.getRuneStones()){
            if(stone.getColour() == colour){
                region.getRuneStones().remove(stone);
                hero.getRuneStones().add(stone);
                return AddDropItemResponses.ITEM_ADDED;
            }
        }

        return  AddDropItemResponses.ADD_DROP_FAILURE;
    }

    public AddDropItemResponses dropItem(String username, String gameName, ItemType itemType){
        Game game = getGame(gameName);
        Region region = null;
        Hero hero = null;
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
                hero = game.getPlayers()[i].getHero();
            }
        }

        if(region == null || hero == null){
            return AddDropItemResponses.ADD_DROP_FAILURE;
        }

        for(Item item : hero.getItems()){
            if(item.getItemType() == itemType){
                hero.getItems().remove(item);
                region.getItems().add(item);
                return AddDropItemResponses.ITEM_DROPPED;
            }
        }

        return  AddDropItemResponses.ADD_DROP_FAILURE;
    }

    public AddDropItemResponses dropRunestone(String username, String gameName, Colour colour){
        Game game = getGame(gameName);
        Region region = null;
        Hero hero = null;
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
                hero = game.getPlayers()[i].getHero();
            }
        }

        if(region == null || hero == null){
            return AddDropItemResponses.ADD_DROP_FAILURE;
        }

        for(RuneStone stone : hero.getRuneStones()){
            if(stone.getColour() == colour){
                hero.getRuneStones().remove(stone);
                region.getRuneStones().add(stone);
                return AddDropItemResponses.ITEM_DROPPED;
            }
        }

        return  AddDropItemResponses.ADD_DROP_FAILURE;
    }

    public ArrayList<Item> getItems(String gameName, String username){
        Game game = getGame(gameName);
        Region region = null;
        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
            }
        }

        if(region == null){
            return new ArrayList<Item>();
        }else{
            return region.getItems();
        }
    }

    public ArrayList<RuneStone> getRunestones(String gameName, String username){
        Game game = getGame(gameName);
        Region region = null;

        for(int i = 0; i < game.getCurrentNumPlayers(); i++){
            if(game.getPlayers()[i].getUsername().equals(username)){
                region = game.getRegionDatabase().getRegion(game.getPlayers()[i].getHero().getCurrentSpace());
            }
        }

        if(region == null){
            return new ArrayList<RuneStone>();
        }else{
            return region.getRuneStones();
        }
    }


    public boolean canAddItem(Hero hero, ItemType itemType){
        int smallItems = 0;
        int largeItems = 0;
        int numHelms = 0;

        for(Item item : hero.getItems()){
            if(item.isSmallItem()){
                smallItems++;
                continue;
            }
            if(item.isLargeItem()){
                largeItems++;
                continue;
            }
            if(item.isHelm()){
                numHelms++;
                continue;
            }
        }

        for(RuneStone runeStone : hero.getRuneStones()){
            smallItems++;
        }

        if(smallItems < 3 && (itemType == ItemType.WITCH_BREW || itemType == ItemType.WINESKIN || itemType == ItemType.MEDICINAL_HERB || itemType == ItemType.TELESCOPE)){
            return true;
        }
        if(largeItems < 1 && (itemType == ItemType.BOW || itemType == ItemType.SHIELD || itemType == ItemType.FALCON)){
            return true;
        }
        if(numHelms < 1 && (itemType == ItemType.HELM)){
            return true;
        }
        return false;
    }

    public boolean canAddRunestone(Hero hero){
        int smallItems = 0;

        for(Item item : hero.getItems()){
            if(item.isSmallItem()){
                smallItems++;
            }
        }

        for(RuneStone runeStone : hero.getRuneStones()){
            smallItems++;
        }

        if(smallItems < 3){
            return true;
        }else{
            return false;
        }
    }




    public LoadGameResponses loadGame(String gameName, String username, Game g) {
        Game game = getGame(gameName);
        for (int i = 0; i < game.getCurrentNumPlayers(); i++) {
            if (game.getPlayers()[i].getHero() == null) {
                return LoadGameResponses.ERROR_NOT_ALL_PLAYERS_SELECTED_HEROES;
            }
        }

        if (game.getDifficultMode() != g.getDifficultMode()) {
            return LoadGameResponses.ERROR_DIFFICULTY_MISMATCH;
        }

        if (game.getCurrentNumPlayers() != g.getCurrentNumPlayers()) {
            return LoadGameResponses.ERROR_PLAYER_NUM_MISMATCH;
        }

        boolean found;
        for (HeroClass h : g.getAllHeroes()) {
            found = false;
            for (int i = 0; i < game.getCurrentNumPlayers(); i++) {
                if (game.getPlayers()[i].getHero().getHeroClass() == h) {
                    found = true;
                }
            }
            if (!found) {
                return LoadGameResponses.ERROR_HERO_MISMATCH;
            }
        }

        game.setGoldenShields(g.getGoldenShields());
        for (int i = 0; i < game.getCurrentNumPlayers(); i++) {
            for (int j = 0; i < g.getCurrentNumPlayers(); i++) {
                if (game.getPlayers()[i].getHero().getHeroClass() == g.getPlayers()[j].getHero().getHeroClass()) {
                    game.getPlayers()[i].setHero(g.getPlayers()[j].getHero());
                }
            }
        }
        game.setActive(g.isActive());
        game.setItemsDistributed(g.isItemsDistributed());
        game.setItemsDistributedMessage(g.getItemsDistributedMessage());
        game.setRegionDatabase(g.getRegionDatabase());
        game.setCurrentHero(g.getCurrentHero());
        game.setFirstHeroInNextDay(g.getFirstHeroInNextDay());
        game.setCurrentHeroSelectedOption(g.getCurrentHeroSelectedOption());
        game.setFarmers(g.getFarmers());
        game.setDifficultMode(g.getDifficultMode());
        game.setCurrentFight(g.getCurrentFight());
        game.setGameStatus(g.getGameStatus());
        game.setNarrator(g.getNarrator());
        game.setPrinceThorald(g.getPrinceThorald());
        game.setRuneStoneLegendCard(g.getRuneStoneLegendCard());
        game.setWitch(g.getWitch());
        game.setSkralStronghold(g.getSkralStronghold());
        game.setGameLoaded(true);
        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        return LoadGameResponses.LOAD_GAME_SUCCESS;
    }
}
