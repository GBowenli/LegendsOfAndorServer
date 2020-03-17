package LoAServer.ReturnClasses;

import LoAServer.PublicEnums.GetPrinceThoraldMovesResponses;

import java.util.ArrayList;

public class GetPrinceThoraldMovesRC {
    private GetPrinceThoraldMovesResponses getPrinceThoraldMovesResponses;
    private ArrayList<Integer> moves;

    public GetPrinceThoraldMovesRC() {}

    public GetPrinceThoraldMovesRC(ArrayList<Integer> moves, GetPrinceThoraldMovesResponses getPrinceThoraldMovesResponses) {
        this.moves = moves;
        this.getPrinceThoraldMovesResponses = getPrinceThoraldMovesResponses;
    }

    public GetPrinceThoraldMovesResponses getGetPrinceThoraldMovesResponses() {
        return getPrinceThoraldMovesResponses;
    }

    public void setGetPrinceThoraldMovesResponses(GetPrinceThoraldMovesResponses getPrinceThoraldMovesResponses) {
        this.getPrinceThoraldMovesResponses = getPrinceThoraldMovesResponses;
    }

    public ArrayList<Integer> getMoves() {
        return moves;
    }

    public void setMoves(ArrayList<Integer> moves) {
        this.moves = moves;
    }
}
