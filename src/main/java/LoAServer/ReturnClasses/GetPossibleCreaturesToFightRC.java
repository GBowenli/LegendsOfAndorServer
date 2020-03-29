package LoAServer.ReturnClasses;

import LoAServer.PublicEnums.GetPossibleCreaturesToFightResponses;

import java.util.ArrayList;

public class GetPossibleCreaturesToFightRC {
    private GetPossibleCreaturesToFightResponses getPossibleCreaturesToFightResponses;
    private ArrayList<Integer> possibleCreaturesToFight;

    public GetPossibleCreaturesToFightRC() {}

    public GetPossibleCreaturesToFightRC(GetPossibleCreaturesToFightResponses getPossibleCreaturesToFightResponses, ArrayList<Integer> possibleCreaturesToFight) {
        this.getPossibleCreaturesToFightResponses = getPossibleCreaturesToFightResponses;
        this.possibleCreaturesToFight = possibleCreaturesToFight;
    }

    public GetPossibleCreaturesToFightResponses getGetPossibleCreaturesToFightResponses() {
        return getPossibleCreaturesToFightResponses;
    }

    public void setGetPossibleCreaturesToFIghtResponses(GetPossibleCreaturesToFightResponses getPossibleCreaturesToFightResponses) {
        this.getPossibleCreaturesToFightResponses = getPossibleCreaturesToFightResponses;
    }

    public ArrayList<Integer> getPossibleCreaturesToFight() {
        return possibleCreaturesToFight;
    }

    public void setPossibleCreaturesToFight(ArrayList<Integer> possibleCreaturesToFight) {
        this.possibleCreaturesToFight = possibleCreaturesToFight;
    }
}
