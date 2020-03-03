package LoAServer.ReturnClasses;

import LoAServer.PublicEnums.GetAvailableRegionsReponses;

import java.util.ArrayList;

public class GetAvailableRegionsRC {
    private ArrayList<Integer> adjacentRegions;
    private GetAvailableRegionsReponses getAvailableRegionsResponses;

    public GetAvailableRegionsRC() {}

    public GetAvailableRegionsRC(ArrayList<Integer> adjacentRegions, GetAvailableRegionsReponses getAvailableRegionsResponses) {
        this.adjacentRegions = adjacentRegions;
        this.getAvailableRegionsResponses = getAvailableRegionsResponses;
    }
}
