package LoAServer;

import LoAServer.Creature.Creature;
import LoAServer.Creature.Gor;
import LoAServer.Item.Wineskin;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

enum HostGameResponses {
    HOST_GAME_SUCCESS, ERROR_GAME_ALREADY_EXISTS
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

enum GetAvailableRegionsReponses {
    NOT_CURRENT_TURN, DEDUCT_WILLPOWER, NOT_ENOUGH_WILLPOWER, CURRENT_HOUR_MAXED, CANNOT_MOVE_AFTER_FIGHT, SUCCESS
}

enum MoveResponses {
    PICK_UP_FARMER, FARMERS_DIED, NO_OTHER_ACTIONS
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

enum ActivateFogResponses {
    SUCCESS, FOG_DNE
}

enum EndDayResponses {
    DAY_ALREADY_ENDED, NOT_CURRENT_TURN, NEW_DAY, GAME_OVER, SUCCESS
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
        return games;
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
            getGame(gameName).setCurrentHero(getGame(gameName).getSinglePlayer(username).getHero());
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
            if (i == 0) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer1Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer1Items());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getPlayer1Gold() + " Gold and " + itemDistribution.getPlayer1Items().size() + " Wineskins");
            } else if (i == 1) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer2Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer2Items());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getPlayer2Gold() + " Gold and " + itemDistribution.getPlayer2Items().size() + " Wineskins");
            } else if (i == 2) {
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer3Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer3Items());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getPlayer3Gold() + " Gold and " + itemDistribution.getPlayer3Items().size() + " Wineskins");
            } else { // i = 3
                getGame(gameName).getPlayers()[i].getHero().setGold(itemDistribution.getPlayer4Gold());
                getGame(gameName).getPlayers()[i].getHero().setItems(itemDistribution.getPlayer4Items());
                getGame(gameName).appendToDistributedItemsMessage(getGame(gameName).getPlayers()[i].getHero().getHeroClass() + ": " + itemDistribution.getPlayer4Gold() + " Gold and " + itemDistribution.getPlayer4Items().size() + " Wineskins");
            }
        }

        MasterDatabase masterDatabase = MasterDatabase.getInstance();

        for (int i = 0; i < getGame(gameName).getCurrentNumPlayers(); i++) {
            masterDatabase.getMasterGameBCM().get(getGame(gameName).getPlayers()[i].getUsername()).touch();
        }

        getGame(gameName).setItemsDistributed(true);
        return DistributeItemsResponses.DISTRIBUTE_ITEMS_SUCCESS;
    }

    public List<Object> getAvailableRegions (String gameName, String username) {
        Player p = getGame(gameName).getSinglePlayer(username);

        if (getGame(gameName).getCurrentHero().equals(p.getHero())) {
            if (p.getHero().getCurrentHour() == 10) {
                return Arrays.asList(null, GetAvailableRegionsReponses.CURRENT_HOUR_MAXED);
            } else {
                if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.FIGHT) {
                    return Arrays.asList(null, GetAvailableRegionsReponses.CANNOT_MOVE_AFTER_FIGHT);
                } else if (getGame(gameName).getCurrentHeroSelectedOption() == TurnOptions.NONE) {
                    getGame(gameName).setCurrentHeroSelectedOption(TurnOptions.MOVE);
                }

                if (p.getHero().getCurrentHour() >= 7 && p.getHero().getWillPower() <= 2) {
                    return Arrays.asList(null, GetAvailableRegionsReponses.NOT_ENOUGH_WILLPOWER);
                }

                ArrayList<Integer> adjacentRegions = new ArrayList<>();
                RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

                adjacentRegions.addAll(regionDatabase.getRegion(p.getHero().getCurrentSpace()).getAdjacentRegions());

                if (regionDatabase.getRegion(p.getHero().getCurrentSpace()).isBridge()) {
                    adjacentRegions.add(regionDatabase.getRegion(p.getHero().getCurrentSpace()).getBridgeAdjacentRegion());
                }

                if (p.getHero().getCurrentHour() >= 7) {
                    return Arrays.asList(adjacentRegions, GetAvailableRegionsReponses.DEDUCT_WILLPOWER);
                } else {
                    return Arrays.asList(adjacentRegions, GetAvailableRegionsReponses.SUCCESS);
                }
            }
        } else {
            return Arrays.asList(null, GetAvailableRegionsReponses.NOT_CURRENT_TURN);
        }
    }

    public List<Object> move(String gameName, String username, Integer targetRegion) { // do the verification on android (checking if is feasible adjacent)
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();

        h.setMoved(true);
        h.setCurrentSpace(targetRegion);
        if (h.getCurrentHour() >= 7) {
            h.setWillPower(h.getWillPower()-2);
        }
        h.setCurrentHour(h.getCurrentHour()+1);

        if (regionDatabase.getRegion(targetRegion).getCurrentCreature() != null) {
            if (h.getFarmers().size() > 0) {
                h.getFarmers().clear();
                return Arrays.asList(null, MoveResponses.FARMERS_DIED);
            }
        }
        if (regionDatabase.getRegion(targetRegion).getFarmers().size() > 0) {
            return Arrays.asList(regionDatabase.getRegion(targetRegion).getFarmers(), MoveResponses.PICK_UP_FARMER);
        } else {
            return Arrays.asList(null, MoveResponses.NO_OTHER_ACTIONS);
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
            int newFarmersCount = regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().size() - farmers.size();
            regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().clear();
            for (int i = 0; i < newFarmersCount; i++) {
                regionDatabase.getRegion(h.getCurrentSpace()).getFarmers().add(Farmer.FARMER);
            }

            if (regionDatabase.getRegion(h.getCurrentSpace()).getCurrentCreature() != null) {
                h.getFarmers().clear();
                return PickUpFarmersResponses.FARMERS_DIED;
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
                return EmptyWellResponses.SUCCESS;
            } else {
                return EmptyWellResponses.WELL_ALREADY_EMPTY;
            }
        } else {
            return EmptyWellResponses.WELL_DNE;
        }
    }

    public List<Object> activateFog(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();
        FogKind f = regionDatabase.getRegion(h.getCurrentSpace()).getFog();

        if (f != FogKind.NONE) {
            if (f == FogKind.MONSTER) {
                regionDatabase.getRegion(h.getCurrentSpace()).setCurrentCreature(new Gor());
            } else if (f == FogKind.WINESKIN) {
                h.getItems().add(new Wineskin());
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
            return Arrays.asList(f, ActivateFogResponses.SUCCESS);
        } else {
            return Arrays.asList(null, ActivateFogResponses.FOG_DNE);
        }
    }

    public List<Object> endDay(String gameName, String username) {
        RegionDatabase regionDatabase = getGame(gameName).getRegionDatabase();
        Hero h = getGame(gameName).getSinglePlayer(username).getHero();

        if (!getGame(gameName).getCurrentHero().equals(getGame(gameName).getSinglePlayer(username).getHero())) {
            return Arrays.asList(null, EndDayResponses.NOT_CURRENT_TURN);
        }

        if (!h.isHasEndedDay()) {
            h.setHasEndedDay(true);
            if (getGame(gameName).getFirstHeroInNextDay() == null) {
                getGame(gameName).setFirstHeroInNextDay(h);
            }

            if (getGame(gameName).getCurrentHero() == null) { // new day
                getGame(gameName).setCurrentHero(getGame(gameName).getFirstHeroInNextDay());
                getGame(gameName).setFirstHeroInNextDay(null);

                ArrayList<Region> regionsWithCreatures = regionDatabase.getAllRegionsWithCreatures();
                for (Region r : regionsWithCreatures) { // advance every creature once
                    Creature creature = r.getCurrentCreature();
                    r.setCurrentCreature(null);
                    int newCreatureSpace;

                    do {
                        if (r.isBridge()) {
                            newCreatureSpace = r.getBridgeNextRegion();
                        } else {
                            newCreatureSpace = r.getNextRegion();
                        }
                    } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreature() != null || newCreatureSpace != 0);

                    if (newCreatureSpace == 0) {
                        if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                            regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                        } else {
                            getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                        }
                    } else {
                        regionDatabase.getRegion(newCreatureSpace).setCurrentCreature(creature);
                    }
                }

                ArrayList<Region> regionsWithWardraks = regionDatabase.getAllRegionsWithWardraks();
                for (Region r : regionsWithWardraks) { // advance every wardrak once again
                    Creature creature = r.getCurrentCreature();
                    r.setCurrentCreature(null);
                    int newCreatureSpace;

                    do {
                        if (r.isBridge()) {
                            newCreatureSpace = r.getBridgeNextRegion();
                        } else {
                            newCreatureSpace = r.getNextRegion();
                        }
                    } while (regionDatabase.getRegion(newCreatureSpace).getCurrentCreature() != null || newCreatureSpace != 0);

                    if (newCreatureSpace == 0) {
                        if (regionDatabase.getRegion(0).getFarmers().size() > 0) {
                            regionDatabase.getRegion(0).getFarmers().remove(regionDatabase.getRegion(0).getFarmers().size()-1);
                        } else {
                            getGame(gameName).setGoldenShields(getGame(gameName).getGoldenShields()-1);
                        }
                    } else {
                        regionDatabase.getRegion(newCreatureSpace).setCurrentCreature(creature);
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
                if (getGame(gameName).getGoldenShields() < 0) {
                    return Arrays.asList(null, EndDayResponses.GAME_OVER);
                } else {
                    return Arrays.asList(new LegendCard(), EndDayResponses.NEW_DAY);
                }
            } else {
                return Arrays.asList(null, EndDayResponses.SUCCESS);
            }
        } else {
            return Arrays.asList(null, EndDayResponses.DAY_ALREADY_ENDED);
        }
    }
}
