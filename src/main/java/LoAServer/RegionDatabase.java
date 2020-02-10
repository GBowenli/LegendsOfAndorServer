package LoAServer;

import java.util.ArrayList;
import java.util.Random;

public class RegionDatabase {
    private ArrayList<Region> regionDatabase;

    public RegionDatabase() {
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
        regionDatabase.get(1).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(2).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(3).setNextRegion(regionDatabase.get(1));
        regionDatabase.get(4).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(5).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(6).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(7).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(8).setNextRegion(regionDatabase.get(7));
        regionDatabase.get(9).setNextRegion(regionDatabase.get(7));
        regionDatabase.get(10).setNextRegion(regionDatabase.get(3));
        regionDatabase.get(11).setNextRegion(regionDatabase.get(0));
        regionDatabase.get(12).setNextRegion(regionDatabase.get(11));
        regionDatabase.get(13).setNextRegion(regionDatabase.get(6));
        regionDatabase.get(14).setNextRegion(regionDatabase.get(2));
        regionDatabase.get(15).setNextRegion(regionDatabase.get(7));
        regionDatabase.get(16).setNextRegion(regionDatabase.get(13));
        regionDatabase.get(17).setNextRegion(regionDatabase.get(6));
        regionDatabase.get(18).setNextRegion(regionDatabase.get(14));
        regionDatabase.get(19).setNextRegion(regionDatabase.get(3));
        regionDatabase.get(20).setNextRegion(regionDatabase.get(3));
        regionDatabase.get(21).setNextRegion(regionDatabase.get(4));
        regionDatabase.get(22).setNextRegion(regionDatabase.get(19));
        regionDatabase.get(23).setNextRegion(regionDatabase.get(22));
        regionDatabase.get(24).setNextRegion(regionDatabase.get(21));
        regionDatabase.get(25).setNextRegion(regionDatabase.get(24));
        regionDatabase.get(26).setNextRegion(regionDatabase.get(25));
        regionDatabase.get(27).setNextRegion(regionDatabase.get(25));
        regionDatabase.get(28).setNextRegion(regionDatabase.get(18));
        regionDatabase.get(29).setNextRegion(regionDatabase.get(28));
        regionDatabase.get(30).setNextRegion(regionDatabase.get(29));
        regionDatabase.get(31).setNextRegion(regionDatabase.get(23));
        regionDatabase.get(32).setNextRegion(regionDatabase.get(16));
        regionDatabase.get(33).setNextRegion(regionDatabase.get(30));
        regionDatabase.get(34).setNextRegion(regionDatabase.get(23));
        regionDatabase.get(35).setNextRegion(regionDatabase.get(23));
        regionDatabase.get(36).setNextRegion(regionDatabase.get(16));
        regionDatabase.get(37).setNextRegion(regionDatabase.get(41));
        regionDatabase.get(38).setNextRegion(regionDatabase.get(16));
        regionDatabase.get(39).setBridgeNextRegion(regionDatabase.get(38));
        regionDatabase.get(40).setNextRegion(regionDatabase.get(39));
        regionDatabase.get(41).setNextRegion(regionDatabase.get(40));
        regionDatabase.get(42).setNextRegion(regionDatabase.get(39));
        regionDatabase.get(43).setNextRegion(regionDatabase.get(39));
        regionDatabase.get(44).setNextRegion(regionDatabase.get(42));
        regionDatabase.get(45).setNextRegion(regionDatabase.get(43));
        regionDatabase.get(46).setNextRegion(regionDatabase.get(44));
        regionDatabase.get(47).setBridgeNextRegion(regionDatabase.get(46));
        regionDatabase.get(48).setBridgeNextRegion(regionDatabase.get(16));
        regionDatabase.get(49).setNextRegion(regionDatabase.get(48));
        regionDatabase.get(50).setNextRegion(regionDatabase.get(48));
        regionDatabase.get(51).setNextRegion(regionDatabase.get(48));
        regionDatabase.get(52).setNextRegion(regionDatabase.get(50));
        regionDatabase.get(53).setNextRegion(regionDatabase.get(47));
        regionDatabase.get(54).setNextRegion(regionDatabase.get(47));
        regionDatabase.get(55).setNextRegion(regionDatabase.get(51));
        regionDatabase.get(56).setNextRegion(regionDatabase.get(47));
        regionDatabase.get(57).setNextRegion(regionDatabase.get(54));
        regionDatabase.get(58).setNextRegion(regionDatabase.get(57));
        regionDatabase.get(59).setNextRegion(regionDatabase.get(57));
        regionDatabase.get(60).setNextRegion(regionDatabase.get(59));
        regionDatabase.get(61).setNextRegion(regionDatabase.get(58));
        regionDatabase.get(62).setNextRegion(regionDatabase.get(58));
        regionDatabase.get(63).setNextRegion(regionDatabase.get(45));
        regionDatabase.get(64).setNextRegion(regionDatabase.get(45));
        regionDatabase.get(65).setNextRegion(regionDatabase.get(45));
        regionDatabase.get(66).setNextRegion(regionDatabase.get(65));
        regionDatabase.get(67).setNextRegion(regionDatabase.get(66));
        regionDatabase.get(68).setNextRegion(regionDatabase.get(67));
        regionDatabase.get(69).setNextRegion(regionDatabase.get(68));
        regionDatabase.get(70).setNextRegion(regionDatabase.get(69));
        regionDatabase.get(71).setNextRegion(regionDatabase.get(43));
        regionDatabase.get(72).setNextRegion(regionDatabase.get(18));
        // 73 - 79  regions missing on map
        regionDatabase.get(80).setNextRegion(null);
        regionDatabase.get(81).setNextRegion(regionDatabase.get(70));
        regionDatabase.get(82).setNextRegion(regionDatabase.get(81));
        regionDatabase.get(83).setNextRegion(null);
        regionDatabase.get(84).setNextRegion(regionDatabase.get(82));

        // set adjacent regions and bridge adjacent region
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(7));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(2));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(1));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(0).getAdjacentRegions().add(regionDatabase.get(5));
        regionDatabase.get(1).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(1).getAdjacentRegions().add(regionDatabase.get(2));
        regionDatabase.get(1).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(1).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(2).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(2).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(2).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(2).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(2).getAdjacentRegions().add(regionDatabase.get(1));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(1));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(2));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(10));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(20));
        regionDatabase.get(3).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(1));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(20));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(21));
        regionDatabase.get(4).getAdjacentRegions().add(regionDatabase.get(5));
        regionDatabase.get(5).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(5).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(5).getAdjacentRegions().add(regionDatabase.get(21));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(2));
        regionDatabase.get(6).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(7).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(7).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(7).getAdjacentRegions().add(regionDatabase.get(9));
        regionDatabase.get(7).getAdjacentRegions().add(regionDatabase.get(8));
        regionDatabase.get(7).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(8).getAdjacentRegions().add(regionDatabase.get(7));
        regionDatabase.get(8).getAdjacentRegions().add(regionDatabase.get(9));
        regionDatabase.get(8).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(9).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(9).getAdjacentRegions().add(regionDatabase.get(7));
        regionDatabase.get(9).getAdjacentRegions().add(regionDatabase.get(8));
        regionDatabase.get(10).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(10).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(10).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(10).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(11).getAdjacentRegions().add(regionDatabase.get(0));
        regionDatabase.get(11).getAdjacentRegions().add(regionDatabase.get(8));
        regionDatabase.get(11).getAdjacentRegions().add(regionDatabase.get(12));
        regionDatabase.get(11).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(11).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(12).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(12).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(13).getAdjacentRegions().add(regionDatabase.get(12));
        regionDatabase.get(13).getAdjacentRegions().add(regionDatabase.get(11));
        regionDatabase.get(13).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(13).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(13).getAdjacentRegions().add(regionDatabase.get(16));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(2));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(10));
        regionDatabase.get(14).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(15).getAdjacentRegions().add(regionDatabase.get(9));
        regionDatabase.get(15).getAdjacentRegions().add(regionDatabase.get(7));
        regionDatabase.get(16).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(16).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(16).getAdjacentRegions().add(regionDatabase.get(36));
        regionDatabase.get(16).getAdjacentRegions().add(regionDatabase.get(38));
        regionDatabase.get(16).getAdjacentRegions().add(regionDatabase.get(32));
        regionDatabase.get(16).setBridgeAdjacentRegion(regionDatabase.get(48));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(6));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(13));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(16));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(36));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(17).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(10));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(14));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(36));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(28));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(18).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(10));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(22));
        regionDatabase.get(19).getAdjacentRegions().add(regionDatabase.get(20));
        regionDatabase.get(20).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(20).getAdjacentRegions().add(regionDatabase.get(3));
        regionDatabase.get(20).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(20).getAdjacentRegions().add(regionDatabase.get(22));
        regionDatabase.get(20).getAdjacentRegions().add(regionDatabase.get(21));
        regionDatabase.get(21).getAdjacentRegions().add(regionDatabase.get(5));
        regionDatabase.get(21).getAdjacentRegions().add(regionDatabase.get(4));
        regionDatabase.get(21).getAdjacentRegions().add(regionDatabase.get(20));
        regionDatabase.get(21).getAdjacentRegions().add(regionDatabase.get(22));
        regionDatabase.get(21).getAdjacentRegions().add(regionDatabase.get(24));
        regionDatabase.get(22).getAdjacentRegions().add(regionDatabase.get(20));
        regionDatabase.get(22).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(22).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(22).getAdjacentRegions().add(regionDatabase.get(24));
        regionDatabase.get(22).getAdjacentRegions().add(regionDatabase.get(21));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(22));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(34));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(35));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(31));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(25));
        regionDatabase.get(23).getAdjacentRegions().add(regionDatabase.get(24));
        regionDatabase.get(24).getAdjacentRegions().add(regionDatabase.get(21));
        regionDatabase.get(24).getAdjacentRegions().add(regionDatabase.get(22));
        regionDatabase.get(24).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(24).getAdjacentRegions().add(regionDatabase.get(25));
        regionDatabase.get(25).getAdjacentRegions().add(regionDatabase.get(24));
        regionDatabase.get(25).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(25).getAdjacentRegions().add(regionDatabase.get(31));
        regionDatabase.get(25).getAdjacentRegions().add(regionDatabase.get(27));
        regionDatabase.get(25).getAdjacentRegions().add(regionDatabase.get(26));
        regionDatabase.get(26).getAdjacentRegions().add(regionDatabase.get(25));
        regionDatabase.get(26).getAdjacentRegions().add(regionDatabase.get(27));
        regionDatabase.get(27).getAdjacentRegions().add(regionDatabase.get(25));
        regionDatabase.get(27).getAdjacentRegions().add(regionDatabase.get(31));
        regionDatabase.get(27).getAdjacentRegions().add(regionDatabase.get(26));
        regionDatabase.get(28).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(28).getAdjacentRegions().add(regionDatabase.get(36));
        regionDatabase.get(28).getAdjacentRegions().add(regionDatabase.get(38));
        regionDatabase.get(28).getAdjacentRegions().add(regionDatabase.get(29));
        regionDatabase.get(28).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(29).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(29).getAdjacentRegions().add(regionDatabase.get(28));
        regionDatabase.get(29).getAdjacentRegions().add(regionDatabase.get(30));
        regionDatabase.get(29).getAdjacentRegions().add(regionDatabase.get(34));
        regionDatabase.get(30).getAdjacentRegions().add(regionDatabase.get(33));
        regionDatabase.get(30).getAdjacentRegions().add(regionDatabase.get(35));
        regionDatabase.get(30).getAdjacentRegions().add(regionDatabase.get(34));
        regionDatabase.get(30).getAdjacentRegions().add(regionDatabase.get(29));
        regionDatabase.get(31).getAdjacentRegions().add(regionDatabase.get(27));
        regionDatabase.get(31).getAdjacentRegions().add(regionDatabase.get(25));
        regionDatabase.get(31).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(31).getAdjacentRegions().add(regionDatabase.get(35));
        regionDatabase.get(31).getAdjacentRegions().add(regionDatabase.get(33));
        regionDatabase.get(32).getAdjacentRegions().add(regionDatabase.get(38));
        regionDatabase.get(32).getAdjacentRegions().add(regionDatabase.get(16));
        regionDatabase.get(33).getAdjacentRegions().add(regionDatabase.get(31));
        regionDatabase.get(33).getAdjacentRegions().add(regionDatabase.get(35));
        regionDatabase.get(33).getAdjacentRegions().add(regionDatabase.get(30));
        regionDatabase.get(34).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(34).getAdjacentRegions().add(regionDatabase.get(72));
        regionDatabase.get(34).getAdjacentRegions().add(regionDatabase.get(29));
        regionDatabase.get(34).getAdjacentRegions().add(regionDatabase.get(30));
        regionDatabase.get(34).getAdjacentRegions().add(regionDatabase.get(35));
        regionDatabase.get(35).getAdjacentRegions().add(regionDatabase.get(23));
        regionDatabase.get(35).getAdjacentRegions().add(regionDatabase.get(34));
        regionDatabase.get(35).getAdjacentRegions().add(regionDatabase.get(30));
        regionDatabase.get(35).getAdjacentRegions().add(regionDatabase.get(33));
        regionDatabase.get(35).getAdjacentRegions().add(regionDatabase.get(31));
        regionDatabase.get(36).getAdjacentRegions().add(regionDatabase.get(17));
        regionDatabase.get(36).getAdjacentRegions().add(regionDatabase.get(16));
        regionDatabase.get(36).getAdjacentRegions().add(regionDatabase.get(38));
        regionDatabase.get(36).getAdjacentRegions().add(regionDatabase.get(28));
        regionDatabase.get(36).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(37).getAdjacentRegions().add(regionDatabase.get(41));
        regionDatabase.get(38).getAdjacentRegions().add(regionDatabase.get(28));
        regionDatabase.get(38).getAdjacentRegions().add(regionDatabase.get(36));
        regionDatabase.get(38).getAdjacentRegions().add(regionDatabase.get(16));
        regionDatabase.get(38).getAdjacentRegions().add(regionDatabase.get(32));
        regionDatabase.get(38).setBridgeAdjacentRegion(regionDatabase.get(39));
        regionDatabase.get(39).getAdjacentRegions().add(regionDatabase.get(40));
        regionDatabase.get(39).getAdjacentRegions().add(regionDatabase.get(42));
        regionDatabase.get(39).getAdjacentRegions().add(regionDatabase.get(43));
        regionDatabase.get(39).setBridgeAdjacentRegion(regionDatabase.get(38));
        regionDatabase.get(40).getAdjacentRegions().add(regionDatabase.get(41));
        regionDatabase.get(40).getAdjacentRegions().add(regionDatabase.get(39));
        regionDatabase.get(41).getAdjacentRegions().add(regionDatabase.get(37));
        regionDatabase.get(41).getAdjacentRegions().add(regionDatabase.get(40));
        regionDatabase.get(42).getAdjacentRegions().add(regionDatabase.get(39));
        regionDatabase.get(42).getAdjacentRegions().add(regionDatabase.get(43));
        regionDatabase.get(42).getAdjacentRegions().add(regionDatabase.get(44));
        regionDatabase.get(43).getAdjacentRegions().add(regionDatabase.get(39));
        regionDatabase.get(43).getAdjacentRegions().add(regionDatabase.get(42));
        regionDatabase.get(43).getAdjacentRegions().add(regionDatabase.get(44));
        regionDatabase.get(43).getAdjacentRegions().add(regionDatabase.get(45));
        regionDatabase.get(43).getAdjacentRegions().add(regionDatabase.get(71));
        regionDatabase.get(44).getAdjacentRegions().add(regionDatabase.get(46));
        regionDatabase.get(44).getAdjacentRegions().add(regionDatabase.get(45));
        regionDatabase.get(44).getAdjacentRegions().add(regionDatabase.get(43));
        regionDatabase.get(44).getAdjacentRegions().add(regionDatabase.get(42));
        regionDatabase.get(45).getAdjacentRegions().add(regionDatabase.get(46));
        regionDatabase.get(45).getAdjacentRegions().add(regionDatabase.get(64));
        regionDatabase.get(45).getAdjacentRegions().add(regionDatabase.get(65));
        regionDatabase.get(45).getAdjacentRegions().add(regionDatabase.get(43));
        regionDatabase.get(45).getAdjacentRegions().add(regionDatabase.get(44));
        regionDatabase.get(46).getAdjacentRegions().add(regionDatabase.get(64));
        regionDatabase.get(46).getAdjacentRegions().add(regionDatabase.get(45));
        regionDatabase.get(46).getAdjacentRegions().add(regionDatabase.get(44));
        regionDatabase.get(46).setBridgeAdjacentRegion(regionDatabase.get(47));
        regionDatabase.get(47).getAdjacentRegions().add(regionDatabase.get(48));
        regionDatabase.get(47).getAdjacentRegions().add(regionDatabase.get(53));
        regionDatabase.get(47).getAdjacentRegions().add(regionDatabase.get(54));
        regionDatabase.get(47).getAdjacentRegions().add(regionDatabase.get(56));
        regionDatabase.get(47).setBridgeAdjacentRegion(regionDatabase.get(46));
        regionDatabase.get(48).getAdjacentRegions().add(regionDatabase.get(49));
        regionDatabase.get(48).getAdjacentRegions().add(regionDatabase.get(50));
        regionDatabase.get(48).getAdjacentRegions().add(regionDatabase.get(51));
        regionDatabase.get(48).getAdjacentRegions().add(regionDatabase.get(53));
        regionDatabase.get(48).getAdjacentRegions().add(regionDatabase.get(47));
        regionDatabase.get(48).setBridgeAdjacentRegion(regionDatabase.get(16));
        regionDatabase.get(49).getAdjacentRegions().add(regionDatabase.get(50));
        regionDatabase.get(49).getAdjacentRegions().add(regionDatabase.get(48));
        regionDatabase.get(50).getAdjacentRegions().add(regionDatabase.get(52));
        regionDatabase.get(50).getAdjacentRegions().add(regionDatabase.get(51));
        regionDatabase.get(50).getAdjacentRegions().add(regionDatabase.get(48));
        regionDatabase.get(50).getAdjacentRegions().add(regionDatabase.get(49));
        regionDatabase.get(51).getAdjacentRegions().add(regionDatabase.get(52));
        regionDatabase.get(51).getAdjacentRegions().add(regionDatabase.get(55));
        regionDatabase.get(51).getAdjacentRegions().add(regionDatabase.get(53));
        regionDatabase.get(51).getAdjacentRegions().add(regionDatabase.get(48));
        regionDatabase.get(51).getAdjacentRegions().add(regionDatabase.get(50));
        regionDatabase.get(52).getAdjacentRegions().add(regionDatabase.get(55));
        regionDatabase.get(52).getAdjacentRegions().add(regionDatabase.get(51));
        regionDatabase.get(52).getAdjacentRegions().add(regionDatabase.get(50));
        regionDatabase.get(53).getAdjacentRegions().add(regionDatabase.get(51));
        regionDatabase.get(53).getAdjacentRegions().add(regionDatabase.get(55));
        regionDatabase.get(53).getAdjacentRegions().add(regionDatabase.get(54));
        regionDatabase.get(53).getAdjacentRegions().add(regionDatabase.get(47));
        regionDatabase.get(53).getAdjacentRegions().add(regionDatabase.get(48));
        regionDatabase.get(54).getAdjacentRegions().add(regionDatabase.get(47));
        regionDatabase.get(54).getAdjacentRegions().add(regionDatabase.get(53));
        regionDatabase.get(54).getAdjacentRegions().add(regionDatabase.get(55));
        regionDatabase.get(54).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(54).getAdjacentRegions().add(regionDatabase.get(56));
        regionDatabase.get(55).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(55).getAdjacentRegions().add(regionDatabase.get(54));
        regionDatabase.get(55).getAdjacentRegions().add(regionDatabase.get(53));
        regionDatabase.get(55).getAdjacentRegions().add(regionDatabase.get(51));
        regionDatabase.get(55).getAdjacentRegions().add(regionDatabase.get(52));
        regionDatabase.get(56).getAdjacentRegions().add(regionDatabase.get(47));
        regionDatabase.get(56).getAdjacentRegions().add(regionDatabase.get(54));
        regionDatabase.get(56).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(56).getAdjacentRegions().add(regionDatabase.get(63));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(59));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(63));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(56));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(54));
        regionDatabase.get(57).getAdjacentRegions().add(regionDatabase.get(55));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(61));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(63));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(59));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(60));
        regionDatabase.get(58).getAdjacentRegions().add(regionDatabase.get(62));
        regionDatabase.get(59).getAdjacentRegions().add(regionDatabase.get(60));
        regionDatabase.get(59).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(59).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(60).getAdjacentRegions().add(regionDatabase.get(62));
        regionDatabase.get(60).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(60).getAdjacentRegions().add(regionDatabase.get(59));
        regionDatabase.get(61).getAdjacentRegions().add(regionDatabase.get(64));
        regionDatabase.get(61).getAdjacentRegions().add(regionDatabase.get(63));
        regionDatabase.get(61).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(61).getAdjacentRegions().add(regionDatabase.get(62));
        regionDatabase.get(62).getAdjacentRegions().add(regionDatabase.get(61));
        regionDatabase.get(62).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(62).getAdjacentRegions().add(regionDatabase.get(60));
        regionDatabase.get(63).getAdjacentRegions().add(regionDatabase.get(56));
        regionDatabase.get(63).getAdjacentRegions().add(regionDatabase.get(57));
        regionDatabase.get(63).getAdjacentRegions().add(regionDatabase.get(58));
        regionDatabase.get(63).getAdjacentRegions().add(regionDatabase.get(61));
        regionDatabase.get(63).getAdjacentRegions().add(regionDatabase.get(64));
        regionDatabase.get(64).getAdjacentRegions().add(regionDatabase.get(63));
        regionDatabase.get(64).getAdjacentRegions().add(regionDatabase.get(61));
        regionDatabase.get(64).getAdjacentRegions().add(regionDatabase.get(65));
        regionDatabase.get(64).getAdjacentRegions().add(regionDatabase.get(45));
        regionDatabase.get(64).getAdjacentRegions().add(regionDatabase.get(46));
        regionDatabase.get(65).getAdjacentRegions().add(regionDatabase.get(45));
        regionDatabase.get(65).getAdjacentRegions().add(regionDatabase.get(64));
        regionDatabase.get(65).getAdjacentRegions().add(regionDatabase.get(66));
        regionDatabase.get(66).getAdjacentRegions().add(regionDatabase.get(65));
        regionDatabase.get(66).getAdjacentRegions().add(regionDatabase.get(67));
        regionDatabase.get(67).getAdjacentRegions().add(regionDatabase.get(66));
        regionDatabase.get(67).getAdjacentRegions().add(regionDatabase.get(68));
        regionDatabase.get(68).getAdjacentRegions().add(regionDatabase.get(67));
        regionDatabase.get(68).getAdjacentRegions().add(regionDatabase.get(69));
        regionDatabase.get(69).getAdjacentRegions().add(regionDatabase.get(68));
        regionDatabase.get(69).getAdjacentRegions().add(regionDatabase.get(70));
        regionDatabase.get(70).getAdjacentRegions().add(regionDatabase.get(69));
        regionDatabase.get(70).getAdjacentRegions().add(regionDatabase.get(81));
        regionDatabase.get(71).getAdjacentRegions().add(regionDatabase.get(43));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(19));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(18));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(28));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(29));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(34));
        regionDatabase.get(72).getAdjacentRegions().add(regionDatabase.get(23));
        // 73 - 79 regions missing on map
        // 80 no adjacents
        regionDatabase.get(81).getAdjacentRegions().add(regionDatabase.get(70));
        regionDatabase.get(81).getAdjacentRegions().add(regionDatabase.get(82));
        regionDatabase.get(82).getAdjacentRegions().add(regionDatabase.get(81));
        regionDatabase.get(82).getAdjacentRegions().add(regionDatabase.get(84));
        // 83 no adjacents
        regionDatabase.get(84).getAdjacentRegions().add(regionDatabase.get(82));
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
