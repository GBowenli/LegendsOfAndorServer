package LoAServer;

import java.lang.reflect.Array;
import java.util.ArrayList;

enum FogKind {
    None, Monster, Wineskin, TwoWP, ThreeWP, SP, Gold, WitchBrew, Event
}

public class Region {
    private int number;
    private FogKind fog;
    private boolean fogRevealed;
    private boolean fountain;
    private boolean fountainStatus;
    private boolean merchant;
    private boolean bridge;
    private ArrayList<Region> adjacentRegions;
    private Region bridgeAdjacentRegion;
    private Region nextRegion;
    private Region bridgeNextRegion;

    public Region(int number, FogKind fog, boolean fogRevealed, boolean fountain, boolean fountainStatus, boolean merchant, boolean bridge) {
        this.number = number;
        this.fog = fog;
        this.fogRevealed = fogRevealed;
        this.fountain = fountain;
        this.fountainStatus = fountainStatus;
        this.merchant = merchant;
        this.bridge = bridge;
        adjacentRegions = new ArrayList<>();
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public FogKind getFog() {
        return fog;
    }

    public void setFog(FogKind fog) {
        this.fog = fog;
    }

    public boolean isFogRevealed() {
        return fogRevealed;
    }

    public void setFogRevealed(boolean fogRevealed) {
        this.fogRevealed = fogRevealed;
    }

    public boolean isFountain() {
        return fountain;
    }

    public void setFountain(boolean fountain) {
        this.fountain = fountain;
    }

    public boolean isFountainStatus() {
        return fountainStatus;
    }

    public void setFountainStatus(boolean fountainStatus) {
        this.fountainStatus = fountainStatus;
    }

    public boolean isMerchant() {
        return merchant;
    }

    public void setMerchant(boolean merchant) {
        this.merchant = merchant;
    }

    public boolean isBridge() {
        return bridge;
    }

    public void setBridge(boolean bridge) {
        this.bridge = bridge;
    }

    public ArrayList<Region> getAdjacentRegions() {
        return adjacentRegions;
    }

    public void setAdjacentRegions(ArrayList<Region> adjacentRegions) {
        this.adjacentRegions = adjacentRegions;
    }

    public void setBridgeAdjacentRegion(Region bridgeAdjacentRegion) {
        this.bridgeAdjacentRegion = bridgeAdjacentRegion;
    }

    public Region getNextRegion() {
        return nextRegion;
    }

    public void setNextRegion(Region nextRegion) {
        this.nextRegion = nextRegion;
    }

    public void setBridgeNextRegion(Region bridgeNextRegion) {
        this.bridgeNextRegion = bridgeNextRegion;
    }
}
