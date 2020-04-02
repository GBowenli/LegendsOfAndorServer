package LoAServer;

public class Witch {
    private int currentPosition;
    private int costOfWitchBrew;
    private int costOfWitchBrewArcher;

    public Witch() {}

    public Witch(int currentPosition, int currentNumPlayers) {
        this.currentPosition = currentPosition;
        if (currentNumPlayers == 2) {
            costOfWitchBrew = 3;
        } else if (currentNumPlayers == 3) {
            costOfWitchBrew = 4;
        } else { // == 4
            costOfWitchBrew = 5;
        }
        costOfWitchBrewArcher = costOfWitchBrew-1;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(int currentPosition) {
        this.currentPosition = currentPosition;
    }

    public int getCostOfWitchBrew() {
        return costOfWitchBrew;
    }

    public void setCostOfWitchBrew(int costOfWitchBrew) {
        this.costOfWitchBrew = costOfWitchBrew;
    }

    public int getCostOfWitchBrewArcher() {
        return costOfWitchBrewArcher;
    }

    public void setCostOfWitchBrewArcher(int costOfWitchBrewArcher) {
        this.costOfWitchBrewArcher = costOfWitchBrewArcher;
    }
}
