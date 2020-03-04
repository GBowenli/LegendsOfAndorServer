//added changes
package LoAServer;

import LoAServer.PublicEnums.*;
import LoAServer.ReturnClasses.*;

import java.util.*;

enum HostGameResponses {
    HOST_GAME_SUCCESS, ERROR_GAME_ALREADY_EXISTS
}

enum GameStartedResponses{
    GAME_STARTED, GAME_NOT_STARTED
}

enum JoinGameResponses {
    JOIN_GAME_SUCCESS, ERROR_GAME_FULL, ERROR_GAME_DNE
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
    WON_ROUND, LOST_ROUND, CREATURE_DEFEATED, BATTLE_LOST, PLAYERS_NO_BATTLE_VALUE, CREATURE_NO_BATTLE_VALUE, WAITING_FOR_PLAYERS_TO_JOIN
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

    public void leavePregame(String gameName, String username) {
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

        if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.MOVE) {
            getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(username));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            if (!h.isMoved()) {
                return EndMoveResponses.MUST_MOVE_TO_END_MOVE;
            }

            if (regionDatabase.getRegion(h.getCurrentSpace()).isMerchant()) {
                return EndMoveResponses.BUY_FROM_MERCHANT;
            } else if (regionDatabase.getRegion(h.getCurrentSpace()).isFountain() && regionDatabase.getRegion(h.getCurrentSpace()).isFountainStatus()) {
                return EndMoveResponses.EMPTY_WELL;
            } else if (regionDatabase.getRegion(h.getCurrentSpace()).getFog() != FogKind.NONE) {
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
        FogKind f = regionDatabase.getRegion(h.getCurrentSpace()).getFog();

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
            } else if (f == FogKind.WITCHBREW) { // add Witch Brew object to do this

            } else { // event
                // return the EventCard here
            }

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

                for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                    getGame(gameName).getPlayers()[i].getHero().setHasEndedDay(false);
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
                    return EndDayResponses.NEW_DAY; // Legend Card here
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

    public FightRC fight(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (!getGame(gameName).getCurrentHero().equals(h)) {
            return new FightRC(new Fight(), FightResponses.NOT_CURRENT_TURN);
        } else if (h.isHasEndedDay()) {
            return new FightRC(new Fight(), FightResponses.DAY_ENDED);
        } else if (regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreatures().size() == 0) {
            return new FightRC(new Fight(), FightResponses.NO_CREATURE_FOUND);
        } else {
            Fight fight = new Fight(h, regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreatures().get(0));
            getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.FIGHT);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                Player p = getGame(gameName).getPlayers()[i];

                if (!p.getUsername().equals(username)) {
                    if (p.getHero().getHeroClass() != HeroClass.ARCHER) {
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
                    } else { // is an archer
                        Region region = regionDatabase.getRegion(p.getHero().getCurrentSpace());
                        ArrayList<Integer> archerAdjacentRegions = region.getAdjacentRegions();

                        if (region.isBridge()) {
                            archerAdjacentRegions.add(region.getBridgeAdjacentRegion());
                        }

                        if (archerAdjacentRegions.contains(h.getCurrentSpace())) {
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
            return new FightRC(fight, FightResponses.JOINED_FIGHT);
        }
    }

    public void joinFight(String gameName, String username) {
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

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

        if (getGame(gameName).getCurrentFight().getHeroesBattleScores().get(getGame(gameName).getCurrentFight().getHeroes().indexOf(h)) > 0) {
            return LeaveFightResponses.CANNOT_LEAVE_AFTER_ROLLING;
        } else if (h.isFought()) {
            return LeaveFightResponses.CANNOT_LEAVE_WITHOUT_FIGHTING;
        } else {
            getGame(gameName).getCurrentFight().getHeroes().remove(h);
            h.setFought(false);

            if (getGame(gameName).getCurrentFight().getHeroes().size() == 0) {
                getGame(gameName).setCurrentFight(null);
                getGame(gameName).setCurrentHero(getGame(gameName).getNextHero(getGame(gameName).getCurrentHero().getHeroClass()));
                getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.NONE);
            } else {
                getGame(gameName).getCurrentFight().getHeroes().remove(h);
            }

            MasterDatabase masterDatabase = MasterDatabase.getInstance();
            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            return LeaveFightResponses.SUCCESS;
        }
    }

    public ArrayList<Die> getDice(String gameName, String username) { // DID NOT CHECK FOR BLACK DIE HERE ADD IN FUTURE!!!
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        Fight fight = getGame(gameName).getCurrentFight();

        if (h.getHeroClass() == HeroClass.WIZARD) {
            fight.setWizardDice(new ArrayList<>(Arrays.asList(0)));
            return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE)));
        } else if (h.getHeroClass() == HeroClass.WARRIOR) {
            if (h.getWillPower() < 7) {
                fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else if (h.getWillPower() < 14) {
                fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else {
                fight.setWarriorDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            }
        } else if (h.getHeroClass() == HeroClass.DWARF) {
            if (h.getWillPower() < 7) {
                fight.setDwarfDice(new ArrayList<>(Arrays.asList(0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE)));
            } else if (h.getWillPower() < 14) {
                fight.setDwarfDice(new ArrayList<>(Arrays.asList(0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else {
                fight.setDwarfDice(new ArrayList<>(Arrays.asList(0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            }
        } else { // ARCHER
            if (h.getWillPower() < 7) {
                fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else if (h.getWillPower() < 14) {
                fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else {
                fight.setArcherDice(new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
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
        for (int i : diceRolls) {
            if (i > max) {
                max = i;
            }
        }

        getGame(gameName).getCurrentFight().getHeroesBattleScores().set(getGame(gameName).getCurrentFight().getHeroes().indexOf(h), max + h.getStrength());

        MasterDatabase masterDatabase = MasterDatabase.getInstance();
        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        for (Integer i : diceRolls) {
            System.out.println(i);
        }
        System.out.println(max+h.getStrength());

        return max + h.getStrength();
    }

    public ArrayList<Die> getCreatureDice(String gameName, String username) {
        Creature creature = getGame(gameName).getCurrentFight().getCreature();
        Fight fight = getGame(gameName).getCurrentFight();

        if (creature.getCreatureType() == CreatureType.WARDRAKS) {
            if (creature.getWillpower() < 7) {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(-1))); // -1 MEANS BLACK DIE!!!!!!!!!!!!!!!!!!!!
                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE)));
            } else {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(-1, -1)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.BLACK_DIE), new Die(DieType.BLACK_DIE)));
            }
        } else {
            if (creature.getWillpower() < 7) {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            } else {
                fight.setCreatureDice(new ArrayList<>(Arrays.asList(0, 0, 0)));
                return new ArrayList<>(Arrays.asList(new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE), new Die(DieType.REGULAR_DIE)));
            }
        }
    }

    public Integer calculateCreatureBattleValue(String gameName, String username, ArrayList<Integer> diceRolls) {
        Creature creature = getGame(gameName).getCurrentFight().getCreature();

        Collections.sort(diceRolls);
        int prevValue = diceRolls.get(0);
        int duplicateCurrent = diceRolls.get(0);
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

        int difference = totalHeroesBV - fight.getCreatureBattleScore();

        for (Integer bv : fight.getHeroesBattleScores()) { // reset battle values
            bv = 0;
        }
        fight.setCreatureBattleScore(0);
        fight.setWizardDice(new ArrayList<>());
        fight.setDwarfDice(new ArrayList<>());
        fight.setArcherDice(new ArrayList<>());
        fight.setWizardDice(new ArrayList<>());

        if (difference > 0) {
            for (Hero h : fight.getHeroes()) { // if doesn't work use getHeroByHC
                h.setWillPower(h.getWillPower()-difference);

                if (h.getWillPower() <= 0 || h.getCurrentHour() == 10) {
                    h.setStrength(h.getStrength()-1);
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
                return EndBattleRoundResponses.LOST_ROUND;
            }
        } else {
            fight.getCreature().setWillpower(fight.getCreature().getWillpower()-difference);

            for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
                masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
            }

            if (fight.getCreature().getWillpower() <= 0) { // force player to press leave fight!!!!
                getGame(gameName).getRegionDatabase().getRegion(80).getCurrentCreatures().add(fight.getCreature());

                return EndBattleRoundResponses.CREATURE_DEFEATED;
            } else {
                return EndBattleRoundResponses.WON_ROUND;
            }
        }
    }

    public void gameOver(String gameName){
        if(getGame(gameName) != null){
            games.remove(getGame(gameName));
        }
    }
}
