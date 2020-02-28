package LoAServer;

import LoAServer.Creature.Gor;
import LoAServer.Creature.Skral;
import LoAServer.Creature.Troll;
import LoAServer.Creature.Wardraks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

enum Difficulty {
    EASY, HARD
}

public class RegionDatabase {
    private ArrayList<Region> regionDatabase;

    public RegionDatabase() {}

    public RegionDatabase(Difficulty difficulty) {
        regionDatabase = new ArrayList<>();

        for (int i = 0; i < 85; i++) {
            regionDatabase.add(new Region(i, FogKind.NONE, false, false, false, false, false));
        }
        // set bridges
        regionDatabase.get(48).setBridge(true);
        regionDatabase.get(16).setBridge(true);
        regionDatabase.get(47).setBridge(true);
        regionDatabase.get(46).setBridge(true);
        regionDatabase.get(38).setBridge(true);
        regionDatabase.get(39).setBridge(true);

        // set fountains
        regionDatabase.get(5).setFountain(true);
        regionDatabase.get(5).setFountainStatus(true);
        regionDatabase.get(35).setFountain(true);
        regionDatabase.get(35).setFountainStatus(true);
        regionDatabase.get(55).setFountain(true);
        regionDatabase.get(55).setFountainStatus(true);
        regionDatabase.get(45).setFountain(true);
        regionDatabase.get(45).setFountainStatus(true);

        // set merchants
        regionDatabase.get(18).setMerchant(true);
        regionDatabase.get(57).setMerchant(true);
        regionDatabase.get(71).setMerchant(true);

        // randomize the fogs
        setRandomizedFogs();

        // set next regions and bridge next regions
        regionDatabase.get(0).setNextRegion(null);
        regionDatabase.get(1).setNextRegion(0);
        regionDatabase.get(2).setNextRegion(0);
        regionDatabase.get(3).setNextRegion(1);
        regionDatabase.get(4).setNextRegion(0);
        regionDatabase.get(5).setNextRegion(0);
        regionDatabase.get(6).setNextRegion(0);
        regionDatabase.get(7).setNextRegion(0);
        regionDatabase.get(8).setNextRegion(7);
        regionDatabase.get(9).setNextRegion(7);
        regionDatabase.get(10).setNextRegion(3);
        regionDatabase.get(11).setNextRegion(0);
        regionDatabase.get(12).setNextRegion(11);
        regionDatabase.get(13).setNextRegion(6);
        regionDatabase.get(14).setNextRegion(2);
        regionDatabase.get(15).setNextRegion(7);
        regionDatabase.get(16).setNextRegion(13);
        regionDatabase.get(17).setNextRegion(6);
        regionDatabase.get(18).setNextRegion(14);
        regionDatabase.get(19).setNextRegion(3);
        regionDatabase.get(20).setNextRegion(3);
        regionDatabase.get(21).setNextRegion(4);
        regionDatabase.get(22).setNextRegion(19);
        regionDatabase.get(23).setNextRegion(22);
        regionDatabase.get(24).setNextRegion(21);
        regionDatabase.get(25).setNextRegion(24);
        regionDatabase.get(26).setNextRegion(25);
        regionDatabase.get(27).setNextRegion(25);
        regionDatabase.get(28).setNextRegion(18);
        regionDatabase.get(29).setNextRegion(28);
        regionDatabase.get(30).setNextRegion(29);
        regionDatabase.get(31).setNextRegion(23);
        regionDatabase.get(32).setNextRegion(16);
        regionDatabase.get(33).setNextRegion(30);
        regionDatabase.get(34).setNextRegion(23);
        regionDatabase.get(35).setNextRegion(23);
        regionDatabase.get(36).setNextRegion(16);
        regionDatabase.get(37).setNextRegion(41);
        regionDatabase.get(38).setNextRegion(16);
        regionDatabase.get(39).setBridgeNextRegion(38);
        regionDatabase.get(40).setNextRegion(39);
        regionDatabase.get(41).setNextRegion(40);
        regionDatabase.get(42).setNextRegion(39);
        regionDatabase.get(43).setNextRegion(39);
        regionDatabase.get(44).setNextRegion(42);
        regionDatabase.get(45).setNextRegion(43);
        regionDatabase.get(46).setNextRegion(44);
        regionDatabase.get(47).setBridgeNextRegion(46);
        regionDatabase.get(48).setBridgeNextRegion(16);
        regionDatabase.get(49).setNextRegion(48);
        regionDatabase.get(50).setNextRegion(48);
        regionDatabase.get(51).setNextRegion(48);
        regionDatabase.get(52).setNextRegion(50);
        regionDatabase.get(53).setNextRegion(47);
        regionDatabase.get(54).setNextRegion(47);
        regionDatabase.get(55).setNextRegion(51);
        regionDatabase.get(56).setNextRegion(47);
        regionDatabase.get(57).setNextRegion(54);
        regionDatabase.get(58).setNextRegion(57);
        regionDatabase.get(59).setNextRegion(57);
        regionDatabase.get(60).setNextRegion(59);
        regionDatabase.get(61).setNextRegion(58);
        regionDatabase.get(62).setNextRegion(58);
        regionDatabase.get(63).setNextRegion(45);
        regionDatabase.get(64).setNextRegion(45);
        regionDatabase.get(65).setNextRegion(45);
        regionDatabase.get(66).setNextRegion(65);
        regionDatabase.get(67).setNextRegion(66);
        regionDatabase.get(68).setNextRegion(67);
        regionDatabase.get(69).setNextRegion(68);
        regionDatabase.get(70).setNextRegion(69);
        regionDatabase.get(71).setNextRegion(43);
        regionDatabase.get(72).setNextRegion(18);
        // 73 - 79  regions missing on map
        regionDatabase.get(80).setNextRegion(null);
        regionDatabase.get(81).setNextRegion(70);
        regionDatabase.get(82).setNextRegion(81);
        regionDatabase.get(83).setNextRegion(null);
        regionDatabase.get(84).setNextRegion(82);

        // set adjacent regions and bridge adjacent region
        regionDatabase.get(0).setAdjacentRegions(new ArrayList<>(Arrays.asList(7, 11, 6, 2, 1, 4, 5)));
        regionDatabase.get(1).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 2, 3, 4)));
        regionDatabase.get(2).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 6, 14, 3, 1)));
        regionDatabase.get(3).setAdjacentRegions(new ArrayList<>(Arrays.asList(1, 2, 14, 10, 19, 20, 4)));
        regionDatabase.get(4).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 1, 3, 20, 21, 5)));
        regionDatabase.get(5).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 4, 21)));
        regionDatabase.get(6).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 11, 13, 17, 14, 2)));
        regionDatabase.get(7).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 13, 9, 8, 11)));
        regionDatabase.get(8).setAdjacentRegions(new ArrayList<>(Arrays.asList(7, 9, 11)));
        regionDatabase.get(9).setAdjacentRegions(new ArrayList<>(Arrays.asList(13, 7, 8)));
        regionDatabase.get(10).setAdjacentRegions(new ArrayList<>(Arrays.asList(3, 14, 18, 19)));
        regionDatabase.get(11).setAdjacentRegions(new ArrayList<>(Arrays.asList(0, 8, 12, 13, 6)));
        regionDatabase.get(12).setAdjacentRegions(new ArrayList<>(Arrays.asList(11, 13)));
        regionDatabase.get(13).setAdjacentRegions(new ArrayList<>(Arrays.asList(12, 11, 6, 17, 16)));
        regionDatabase.get(14).setAdjacentRegions(new ArrayList<>(Arrays.asList(2, 6, 17, 18, 10, 3)));
        regionDatabase.get(15).setAdjacentRegions(new ArrayList<>(Arrays.asList(9, 7)));
        regionDatabase.get(16).setAdjacentRegions(new ArrayList<>(Arrays.asList(13, 17, 36, 38, 32)));
        regionDatabase.get(16).setBridgeAdjacentRegion(48);
        regionDatabase.get(17).setAdjacentRegions(new ArrayList<>(Arrays.asList(6, 13, 16, 36, 18, 14)));
        regionDatabase.get(18).setAdjacentRegions(new ArrayList<>(Arrays.asList(10, 14, 17, 36, 28, 72, 19)));
        regionDatabase.get(19).setAdjacentRegions(new ArrayList<>(Arrays.asList(3, 10, 18, 72, 23, 22, 20)));
        regionDatabase.get(20).setAdjacentRegions(new ArrayList<>(Arrays.asList(4, 3, 19, 22, 21)));
        regionDatabase.get(21).setAdjacentRegions(new ArrayList<>(Arrays.asList(5, 4, 20, 22, 24)));
        regionDatabase.get(22).setAdjacentRegions(new ArrayList<>(Arrays.asList(20, 19, 23, 24, 21)));
        regionDatabase.get(23).setAdjacentRegions(new ArrayList<>(Arrays.asList(22, 19, 72, 34, 35, 31, 25, 24)));
        regionDatabase.get(24).setAdjacentRegions(new ArrayList<>(Arrays.asList(21, 22, 23, 25)));
        regionDatabase.get(25).setAdjacentRegions(new ArrayList<>(Arrays.asList(24, 23, 31, 27, 26)));
        regionDatabase.get(26).setAdjacentRegions(new ArrayList<>(Arrays.asList(25, 27)));
        regionDatabase.get(27).setAdjacentRegions(new ArrayList<>(Arrays.asList(25, 31, 26)));
        regionDatabase.get(28).setAdjacentRegions(new ArrayList<>(Arrays.asList(18, 36, 38, 29, 72)));
        regionDatabase.get(29).setAdjacentRegions(new ArrayList<>(Arrays.asList(72, 28, 30, 34)));
        regionDatabase.get(30).setAdjacentRegions(new ArrayList<>(Arrays.asList(33, 35, 34, 29)));
        regionDatabase.get(31).setAdjacentRegions(new ArrayList<>(Arrays.asList(27, 25, 23, 35, 33)));
        regionDatabase.get(32).setAdjacentRegions(new ArrayList<>(Arrays.asList(38, 16)));
        regionDatabase.get(33).setAdjacentRegions(new ArrayList<>(Arrays.asList(31, 35, 30)));
        regionDatabase.get(34).setAdjacentRegions(new ArrayList<>(Arrays.asList(23, 72, 29, 30, 35)));
        regionDatabase.get(35).setAdjacentRegions(new ArrayList<>(Arrays.asList(23, 34, 30, 33, 31)));
        regionDatabase.get(36).setAdjacentRegions(new ArrayList<>(Arrays.asList(17, 16, 38, 28, 18)));
        regionDatabase.get(37).setAdjacentRegions(new ArrayList<>(Arrays.asList(41)));
        regionDatabase.get(38).setAdjacentRegions(new ArrayList<>(Arrays.asList(28, 36, 16, 32)));
        regionDatabase.get(38).setBridgeAdjacentRegion(39);
        regionDatabase.get(39).setAdjacentRegions(new ArrayList<>(Arrays.asList(40, 42, 43)));
        regionDatabase.get(39).setBridgeAdjacentRegion(38);
        regionDatabase.get(40).setAdjacentRegions(new ArrayList<>(Arrays.asList(41, 39)));
        regionDatabase.get(41).setAdjacentRegions(new ArrayList<>(Arrays.asList(37, 40)));
        regionDatabase.get(42).setAdjacentRegions(new ArrayList<>(Arrays.asList(39, 43, 44)));
        regionDatabase.get(43).setAdjacentRegions(new ArrayList<>(Arrays.asList(39, 42, 44, 45, 71)));
        regionDatabase.get(44).setAdjacentRegions(new ArrayList<>(Arrays.asList(46, 45, 43, 42)));
        regionDatabase.get(45).setAdjacentRegions(new ArrayList<>(Arrays.asList(46, 64, 65, 43, 44)));
        regionDatabase.get(46).setAdjacentRegions(new ArrayList<>(Arrays.asList(64, 45, 44)));
        regionDatabase.get(46).setBridgeAdjacentRegion(47);
        regionDatabase.get(47).setAdjacentRegions(new ArrayList<>(Arrays.asList(48, 53, 54, 56)));
        regionDatabase.get(47).setBridgeAdjacentRegion(46);
        regionDatabase.get(48).setAdjacentRegions(new ArrayList<>(Arrays.asList(49, 50, 51, 53, 47)));
        regionDatabase.get(48).setBridgeAdjacentRegion(16);
        regionDatabase.get(49).setAdjacentRegions(new ArrayList<>(Arrays.asList(50, 48)));
        regionDatabase.get(50).setAdjacentRegions(new ArrayList<>(Arrays.asList(52, 51, 48, 49)));
        regionDatabase.get(51).setAdjacentRegions(new ArrayList<>(Arrays.asList(52, 55, 53, 48, 50)));
        regionDatabase.get(52).setAdjacentRegions(new ArrayList<>(Arrays.asList(55, 51, 50)));
        regionDatabase.get(53).setAdjacentRegions(new ArrayList<>(Arrays.asList(51, 55, 54, 47, 48)));
        regionDatabase.get(54).setAdjacentRegions(new ArrayList<>(Arrays.asList(47, 53, 55, 57, 56)));
        regionDatabase.get(55).setAdjacentRegions(new ArrayList<>(Arrays.asList(57, 54, 53, 51, 52)));
        regionDatabase.get(56).setAdjacentRegions(new ArrayList<>(Arrays.asList(47, 54, 57, 63)));
        regionDatabase.get(57).setAdjacentRegions(new ArrayList<>(Arrays.asList(59, 58, 63, 56, 54, 55)));
        regionDatabase.get(58).setAdjacentRegions(new ArrayList<>(Arrays.asList(61, 63, 57, 59, 60, 62)));
        regionDatabase.get(59).setAdjacentRegions(new ArrayList<>(Arrays.asList(60, 58, 57)));
        regionDatabase.get(60).setAdjacentRegions(new ArrayList<>(Arrays.asList(62, 58, 59)));
        regionDatabase.get(61).setAdjacentRegions(new ArrayList<>(Arrays.asList(64, 63, 58, 62)));
        regionDatabase.get(62).setAdjacentRegions(new ArrayList<>(Arrays.asList(61, 58, 60)));
        regionDatabase.get(63).setAdjacentRegions(new ArrayList<>(Arrays.asList(56, 57, 58, 61, 64)));
        regionDatabase.get(64).setAdjacentRegions(new ArrayList<>(Arrays.asList(63, 61, 65, 45, 46)));
        regionDatabase.get(65).setAdjacentRegions(new ArrayList<>(Arrays.asList(45, 64, 66)));
        regionDatabase.get(66).setAdjacentRegions(new ArrayList<>(Arrays.asList(65, 67)));
        regionDatabase.get(67).setAdjacentRegions(new ArrayList<>(Arrays.asList(66, 68)));
        regionDatabase.get(68).setAdjacentRegions(new ArrayList<>(Arrays.asList(67, 69)));
        regionDatabase.get(69).setAdjacentRegions(new ArrayList<>(Arrays.asList(68, 70)));
        regionDatabase.get(70).setAdjacentRegions(new ArrayList<>(Arrays.asList(69, 81)));
        regionDatabase.get(71).setAdjacentRegions(new ArrayList<>(Arrays.asList(43)));
        regionDatabase.get(72).setAdjacentRegions(new ArrayList<>(Arrays.asList(19, 18, 28, 29, 34, 23)));
        // 73 - 79 regions missing on map
        // 80 no adjacents
        regionDatabase.get(81).setAdjacentRegions(new ArrayList<>(Arrays.asList(70, 82)));
        regionDatabase.get(82).setAdjacentRegions(new ArrayList<>(Arrays.asList(81, 84)));
        // 83 no adjacents
        regionDatabase.get(84).setAdjacentRegions(new ArrayList<>(Arrays.asList(82)));

        regionDatabase.get(8).setCurrentCreature(new Gor());
        regionDatabase.get(20).setCurrentCreature(new Gor());
        regionDatabase.get(21).setCurrentCreature(new Gor());
        regionDatabase.get(26).setCurrentCreature(new Gor());
        regionDatabase.get(48).setCurrentCreature(new Gor());
        regionDatabase.get(19).setCurrentCreature(new Skral());

        regionDatabase.get(24).getFarmers().add(Farmer.FARMER);
        if (difficulty == Difficulty.EASY) {
            regionDatabase.get(36).getFarmers().add(Farmer.FARMER);
        }
    }

    public void setRandomizedFogs() {
        ArrayList<FogKind> fogs = new ArrayList<>();
        fogs.add(FogKind.EVENT);
        fogs.add(FogKind.EVENT);
        fogs.add(FogKind.EVENT);
        fogs.add(FogKind.EVENT);
        fogs.add(FogKind.EVENT);
        fogs.add(FogKind.SP);
        fogs.add(FogKind.TWO_WP);
        fogs.add(FogKind.THREE_WP);
        fogs.add(FogKind.GOLD);
        fogs.add(FogKind.GOLD);
        fogs.add(FogKind.GOLD);
        fogs.add(FogKind.MONSTER);
        fogs.add(FogKind.MONSTER);
        fogs.add(FogKind.WINESKIN);
        fogs.add(FogKind.WITCHBREW);

        Random random = new Random();
        for (int i = 0; i < 15; i++) {
            int rand = random.nextInt(fogs.size());
            FogKind fog = fogs.get(rand);
            fogs.remove(rand);

            System.out.println(fog.toString());

            if (i == 0) {
                regionDatabase.get(8).setFog(fog);
            } else if (i == 1) {
                regionDatabase.get(11).setFog(fog);
            } else if (i == 2) {
                regionDatabase.get(12).setFog(fog);
            } else if (i == 3) {
                regionDatabase.get(13).setFog(fog);
            } else if (i == 4) {
                regionDatabase.get(16).setFog(fog);
            } else if (i == 5) {
                regionDatabase.get(32).setFog(fog);
            } else if (i == 6) {
                regionDatabase.get(42).setFog(fog);
            } else if (i == 7) {
                regionDatabase.get(44).setFog(fog);
            } else if (i == 8) {
                regionDatabase.get(46).setFog(fog);
            } else if (i == 9) {
                regionDatabase.get(47).setFog(fog);
            } else if (i == 10) {
                regionDatabase.get(48).setFog(fog);
            } else if (i == 11) {
                regionDatabase.get(49).setFog(fog);
            } else if (i == 12) {
                regionDatabase.get(56).setFog(fog);
            } else if (i == 13) {
                regionDatabase.get(63).setFog(fog);
            } else if (i == 14) {
                regionDatabase.get(64).setFog(fog);
            }
        }
    }

    public Region getRegion(int i) {
        return regionDatabase.get(i);
    }

    public ArrayList<Region> getAllRegionsWithCreatures() { // add in the order of Gor, Skral, Wardrak, Troll
        ArrayList<Region> regionsWithCreatures = new ArrayList<>();

        for (Region region : regionDatabase) {
            if (region.getCurrentCreature() instanceof Gor) {
                regionsWithCreatures.add(region);
            }
        }
        for (Region region : regionDatabase) {
            if (region.getCurrentCreature() instanceof Skral) {
                regionsWithCreatures.add(region);
            }
        }
        for (Region region : regionDatabase) {
            if (region.getCurrentCreature() instanceof Wardraks) {
                regionsWithCreatures.add(region);
            }
        }
        for (Region region : regionDatabase) {
            if (region.getCurrentCreature() instanceof Troll) {
                regionsWithCreatures.add(region);
            }
        }

        return regionsWithCreatures;
    }

    public ArrayList<Region> getAllRegionsWithWardraks() {
        ArrayList<Region> regionsWithWardraks = new ArrayList<>();
        for (Region region : regionDatabase) {
            if (region.getCurrentCreature() instanceof Wardraks) {
                regionsWithWardraks.add(region);
            }
        }

        return regionsWithWardraks;
    }

    public ArrayList<Region> getAllRegionsWithFountain() {
        ArrayList<Region> regionsWithFountains = new ArrayList<>();

        regionsWithFountains.add(regionDatabase.get(5));
        regionsWithFountains.add(regionDatabase.get(35));
        regionsWithFountains.add(regionDatabase.get(45));
        regionsWithFountains.add(regionDatabase.get(55));

        return regionsWithFountains;
    }

    public ArrayList<Region> getRegionDatabase() {
        return regionDatabase;
    }
}
