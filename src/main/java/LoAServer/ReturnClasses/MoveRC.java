package LoAServer.ReturnClasses;

import LoAServer.Farmer;
import LoAServer.PublicEnums.MoveResponses;

import java.util.ArrayList;

public class MoveRC {
    private ArrayList<Farmer> farmers;
    private MoveResponses moveResponses;

    public MoveRC() {}

    public MoveRC(ArrayList<Farmer> farmers, MoveResponses moveResponses) {
        this.farmers = farmers;
        this.moveResponses = moveResponses;
    }

    public ArrayList<Farmer> getFarmers() {
        return farmers;
    }

    public void setFarmers(ArrayList<Farmer> farmers) {
        this.farmers = farmers;
    }

    public MoveResponses getMoveResponses() {
        return moveResponses;
    }

    public void setMoveResponses(MoveResponses moveResponses) {
        this.moveResponses = moveResponses;
    }
}
