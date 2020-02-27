package LoAServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

enum Difficulty {
    Easy, Hard
}

public class RegionDatabase {
    private ArrayList<Region> regionDatabase;

    public RegionDatabase() {}

    public RegionDatabase(Difficulty difficulty) {
        regionDatabase = new ArrayList<>();

        for (int i = 0; i < 85; i++) {
            regionDatabase.add(new Region(i, FogKind.None, false, false, false, false, false));
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
        regionDatabase.get(35).setFountain(true);
        regionDatabase.get(55).setFountain(true);
        regionDatabase.get(45).setFountain(true);

        // set merchants
        regionDatabase.get(18).setMerchant(true);
        regionDatabase.get(57).setMerchant(true);
        regionDatabase.get(71).setMerchant(true);

        // randomize the fogs
        setRandomizedFogs();

        // set next regions and bridge next regions
        regionDatabase.get(0).setNextRegion(null);
        regionDatabase.get(1).setNextRegion(new Integer(0));
        regionDatabase.get(2).setNextRegion(new Integer(0));
        regionDatabase.get(3).setNextRegion(new Integer(1));
        regionDatabase.get(4).setNextRegion(new Integer(0));
        regionDatabase.get(5).setNextRegion(new Integer(0));
        regionDatabase.get(6).setNextRegion(new Integer(0));
        regionDatabase.get(7).setNextRegion(new Integer(0));
        regionDatabase.get(8).setNextRegion(new Integer(7));
        regionDatabase.get(9).setNextRegion(new Integer(7));
        regionDatabase.get(10).setNextRegion(new Integer(3));
        regionDatabase.get(11).setNextRegion(new Integer(0));
        regionDatabase.get(12).setNextRegion(new Integer(11));
        regionDatabase.get(13).setNextRegion(new Integer(6));
        regionDatabase.get(14).setNextRegion(new Integer(2));
        regionDatabase.get(15).setNextRegion(new Integer(7));
        regionDatabase.get(16).setNextRegion(new Integer(13));
        regionDatabase.get(17).setNextRegion(new Integer(6));
        regionDatabase.get(18).setNextRegion(new Integer(14));
        regionDatabase.get(19).setNextRegion(new Integer(3));
        regionDatabase.get(20).setNextRegion(new Integer(3));
        regionDatabase.get(21).setNextRegion(new Integer(4));
        regionDatabase.get(22).setNextRegion(new Integer(19));
        regionDatabase.get(23).setNextRegion(new Integer(22));
        regionDatabase.get(24).setNextRegion(new Integer(21));
        regionDatabase.get(25).setNextRegion(new Integer(24));
        regionDatabase.get(26).setNextRegion(new Integer(25));
        regionDatabase.get(27).setNextRegion(new Integer(25));
        regionDatabase.get(28).setNextRegion(new Integer(18));
        regionDatabase.get(29).setNextRegion(new Integer(28));
        regionDatabase.get(30).setNextRegion(new Integer(29));
        regionDatabase.get(31).setNextRegion(new Integer(23));
        regionDatabase.get(32).setNextRegion(new Integer(16));
        regionDatabase.get(33).setNextRegion(new Integer(30));
        regionDatabase.get(34).setNextRegion(new Integer(23));
        regionDatabase.get(35).setNextRegion(new Integer(23));
        regionDatabase.get(36).setNextRegion(new Integer(16));
        regionDatabase.get(37).setNextRegion(new Integer(41));
        regionDatabase.get(38).setNextRegion(new Integer(16));
        regionDatabase.get(39).setBridgeNextRegion(new Integer(38));
        regionDatabase.get(40).setNextRegion(new Integer(39));
        regionDatabase.get(41).setNextRegion(new Integer(40));
        regionDatabase.get(42).setNextRegion(new Integer(39));
        regionDatabase.get(43).setNextRegion(new Integer(39));
        regionDatabase.get(44).setNextRegion(new Integer(42));
        regionDatabase.get(45).setNextRegion(new Integer(43));
        regionDatabase.get(46).setNextRegion(new Integer(44));
        regionDatabase.get(47).setBridgeNextRegion(new Integer(46));
        regionDatabase.get(48).setBridgeNextRegion(new Integer(16));
        regionDatabase.get(49).setNextRegion(new Integer(48));
        regionDatabase.get(50).setNextRegion(new Integer(48));
        regionDatabase.get(51).setNextRegion(new Integer(48));
        regionDatabase.get(52).setNextRegion(new Integer(50));
        regionDatabase.get(53).setNextRegion(new Integer(47));
        regionDatabase.get(54).setNextRegion(new Integer(47));
        regionDatabase.get(55).setNextRegion(new Integer(51));
        regionDatabase.get(56).setNextRegion(new Integer(47));
        regionDatabase.get(57).setNextRegion(new Integer(54));
        regionDatabase.get(58).setNextRegion(new Integer(57));
        regionDatabase.get(59).setNextRegion(new Integer(57));
        regionDatabase.get(60).setNextRegion(new Integer(59));
        regionDatabase.get(61).setNextRegion(new Integer(58));
        regionDatabase.get(62).setNextRegion(new Integer(58));
        regionDatabase.get(63).setNextRegion(new Integer(45));
        regionDatabase.get(64).setNextRegion(new Integer(45));
        regionDatabase.get(65).setNextRegion(new Integer(45));
        regionDatabase.get(66).setNextRegion(new Integer(65));
        regionDatabase.get(67).setNextRegion(new Integer(66));
        regionDatabase.get(68).setNextRegion(new Integer(67));
        regionDatabase.get(69).setNextRegion(new Integer(68));
        regionDatabase.get(70).setNextRegion(new Integer(69));
        regionDatabase.get(71).setNextRegion(new Integer(43));
        regionDatabase.get(72).setNextRegion(new Integer(18));
        // 73 - 79  regions missing on map
        regionDatabase.get(80).setNextRegion(null);
        regionDatabase.get(81).setNextRegion(new Integer(70));
        regionDatabase.get(82).setNextRegion(new Integer(81));
        regionDatabase.get(83).setNextRegion(null);
        regionDatabase.get(84).setNextRegion(new Integer(82));

        // set adjacent regions and bridge adjacent region
        regionDatabase.get(0).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(7), new Integer(11), new Integer(6), new Integer(2), new Integer(1), new Integer(4), new Integer(5))));
        regionDatabase.get(1).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(2), new Integer(3), new Integer(4))));
        regionDatabase.get(2).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(6), new Integer(14), new Integer(3), new Integer(1))));
        regionDatabase.get(3).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(1), new Integer(2), new Integer(14), new Integer(10), new Integer(19), new Integer(20), new Integer(4))));
        regionDatabase.get(4).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(1), new Integer(3), new Integer(20), new Integer(21), new Integer(5))));
        regionDatabase.get(5).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(4), new Integer(21))));
        regionDatabase.get(6).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(11), new Integer(13), new Integer(17), new Integer(14), new Integer(2))));
        regionDatabase.get(7).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(13), new Integer(9), new Integer(8), new Integer(11))));
        regionDatabase.get(8).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(7), new Integer(9), new Integer(11))));
        regionDatabase.get(9).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(13), new Integer(7), new Integer(8))));
        regionDatabase.get(10).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(3), new Integer(14), new Integer(18), new Integer(19))));
        regionDatabase.get(11).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(0), new Integer(8), new Integer(12), new Integer(13), new Integer(6))));
        regionDatabase.get(12).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(11), new Integer(13))));
        regionDatabase.get(13).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(12), new Integer(11), new Integer(6), new Integer(17), new Integer(16))));
        regionDatabase.get(14).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(2), new Integer(6), new Integer(17), new Integer(18), new Integer(10), new Integer(3))));
        regionDatabase.get(15).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(9), new Integer(7))));
        regionDatabase.get(16).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(13), new Integer(17), new Integer(36), new Integer(38), new Integer(32))));
        regionDatabase.get(16).setBridgeAdjacentRegion(new Integer(48));
        regionDatabase.get(17).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(6), new Integer(13), new Integer(16), new Integer(36), new Integer(18), new Integer(14))));
        regionDatabase.get(18).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(10), new Integer(14), new Integer(17), new Integer(36), new Integer(28), new Integer(72), new Integer(19))));
        regionDatabase.get(19).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(3), new Integer(10), new Integer(18), new Integer(72), new Integer(23), new Integer(22), new Integer(20))));
        regionDatabase.get(20).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(4), new Integer(3), new Integer(19), new Integer(22), new Integer(21))));
        regionDatabase.get(21).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(5), new Integer(4), new Integer(20), new Integer(22), new Integer(24))));
        regionDatabase.get(22).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(20), new Integer(19), new Integer(23), new Integer(24), new Integer(21))));
        regionDatabase.get(23).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(22), new Integer(19), new Integer(72), new Integer(34), new Integer(35), new Integer(31), new Integer(25), new Integer(24))));
        regionDatabase.get(24).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(21), new Integer(22), new Integer(23), new Integer(25))));
        regionDatabase.get(25).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(24), new Integer(23), new Integer(31), new Integer(27), new Integer(26))));
        regionDatabase.get(26).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(25), new Integer(27))));
        regionDatabase.get(27).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(25), new Integer(31), new Integer(26))));
        regionDatabase.get(28).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(18), new Integer(36), new Integer(38), new Integer(29), new Integer(72))));
        regionDatabase.get(29).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(72), new Integer(28), new Integer(30), new Integer(34))));
        regionDatabase.get(30).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(33), new Integer(35), new Integer(34), new Integer(29))));
        regionDatabase.get(31).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(27), new Integer(25), new Integer(23), new Integer(35), new Integer(33))));
        regionDatabase.get(32).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(38), new Integer(16))));
        regionDatabase.get(33).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(31), new Integer(35), new Integer(30))));
        regionDatabase.get(34).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(23), new Integer(72), new Integer(29), new Integer(30), new Integer(35))));
        regionDatabase.get(35).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(23), new Integer(34), new Integer(30), new Integer(33), new Integer(31))));
        regionDatabase.get(36).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(17), new Integer(16), new Integer(38), new Integer(28), new Integer(18))));
        regionDatabase.get(37).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(41))));
        regionDatabase.get(38).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(28), new Integer(36), new Integer(16), new Integer(32))));
        regionDatabase.get(38).setBridgeAdjacentRegion(new Integer(39));
        regionDatabase.get(39).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(40), new Integer(42), new Integer(43))));
        regionDatabase.get(39).setBridgeAdjacentRegion(new Integer(38));
        regionDatabase.get(40).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(41), new Integer(39))));
        regionDatabase.get(41).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(37), new Integer(40))));
        regionDatabase.get(42).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(39), new Integer(43), new Integer(44))));
        regionDatabase.get(43).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(39), new Integer(42), new Integer(44), new Integer(45), new Integer(71))));
        regionDatabase.get(44).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(46), new Integer(45), new Integer(43), new Integer(42))));
        regionDatabase.get(45).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(46), new Integer(64), new Integer(65), new Integer(43), new Integer(44))));
        regionDatabase.get(46).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(64), new Integer(45), new Integer(44))));
        regionDatabase.get(46).setBridgeAdjacentRegion(new Integer(47));
        regionDatabase.get(47).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(48), new Integer(53), new Integer(54), new Integer(56), new Integer(46))));
        regionDatabase.get(47).setBridgeAdjacentRegion(new Integer(46));
        regionDatabase.get(48).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(49), new Integer(50), new Integer(51), new Integer(53), new Integer(47))));
        regionDatabase.get(48).setBridgeAdjacentRegion(new Integer(16));
        regionDatabase.get(49).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(50), new Integer(48))));
        regionDatabase.get(50).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(52), new Integer(51), new Integer(48), new Integer(49))));
        regionDatabase.get(51).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(52), new Integer(55), new Integer(53), new Integer(48), new Integer(50))));
        regionDatabase.get(52).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(55), new Integer(51), new Integer(50))));
        regionDatabase.get(53).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(51), new Integer(55), new Integer(54), new Integer(47), new Integer(48))));
        regionDatabase.get(54).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(47), new Integer(53), new Integer(55), new Integer(57), new Integer(56))));
        regionDatabase.get(55).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(57), new Integer(54), new Integer(53), new Integer(51), new Integer(52))));
        regionDatabase.get(56).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(47), new Integer(54), new Integer(57), new Integer(63))));
        regionDatabase.get(57).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(59), new Integer(58), new Integer(63), new Integer(56), new Integer(54), new Integer(55))));
        regionDatabase.get(58).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(61), new Integer(63), new Integer(57), new Integer(59), new Integer(60), new Integer(62))));
        regionDatabase.get(59).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(60), new Integer(58), new Integer(57))));
        regionDatabase.get(60).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(62), new Integer(58), new Integer(59))));
        regionDatabase.get(61).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(64), new Integer(63), new Integer(58), new Integer(62))));
        regionDatabase.get(62).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(61), new Integer(58), new Integer(60))));
        regionDatabase.get(63).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(56), new Integer(57), new Integer(58), new Integer(61), new Integer(64))));
        regionDatabase.get(64).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(63), new Integer(61), new Integer(65), new Integer(45), new Integer(46))));
        regionDatabase.get(65).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(45), new Integer(64), new Integer(66))));
        regionDatabase.get(66).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(65), new Integer(67))));
        regionDatabase.get(67).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(66), new Integer(68))));
        regionDatabase.get(68).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(67), new Integer(69))));
        regionDatabase.get(69).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(68), new Integer(70))));
        regionDatabase.get(70).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(69), new Integer(81))));
        regionDatabase.get(71).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(43))));
        regionDatabase.get(72).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(19), new Integer(18), new Integer(28), new Integer(29), new Integer(34), new Integer(23))));
        // 73 - 79 regions missing on map
        // 80 no adjacents
        regionDatabase.get(81).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(70), new Integer(82))));
        regionDatabase.get(82).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(81), new Integer(84))));
        // 83 no adjacents
        regionDatabase.get(84).setAdjacentRegions(new ArrayList<Integer>(Arrays.asList(new Integer(82))));
    }

    public void setRandomizedFogs() {
        ArrayList<FogKind> fogs = new ArrayList<>();
        fogs.add(FogKind.Event);
        fogs.add(FogKind.Event);
        fogs.add(FogKind.Event);
        fogs.add(FogKind.Event);
        fogs.add(FogKind.Event);
        fogs.add(FogKind.SP);
        fogs.add(FogKind.TwoWP);
        fogs.add(FogKind.ThreeWP);
        fogs.add(FogKind.Gold);
        fogs.add(FogKind.Gold);
        fogs.add(FogKind.Gold);
        fogs.add(FogKind.Monster);
        fogs.add(FogKind.Monster);
        fogs.add(FogKind.Wineskin);
        fogs.add(FogKind.WitchBrew);

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

    public ArrayList<Region> getRegionDatabase() {
        return regionDatabase;
    }
}
