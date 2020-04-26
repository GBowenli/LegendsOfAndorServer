package LoAServer.ReturnClasses;

import LoAServer.PublicEnums.FogKind;
import LoAServer.PublicEnums.UseTelescopeResponses;
import LoAServer.RuneStone;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class UseTelescopeRC {
    private FogKind fogKind;
    private UseTelescopeResponses useTelescopeResponses;
    private ArrayList<RuneStone> runeStones;

    public UseTelescopeRC() {}

    public UseTelescopeRC(FogKind fogKind, UseTelescopeResponses useTelescopeResponses, ArrayList<RuneStone> runeStones) {
        this.fogKind = fogKind;
        this.useTelescopeResponses = useTelescopeResponses;
        this.runeStones = runeStones;
    }

    public FogKind getFogKind() {
        return fogKind;
    }

    public void setFogKind(FogKind fogKind) {
        this.fogKind = fogKind;
    }

    public UseTelescopeResponses getUseTelescopeResponses( ){
        return useTelescopeResponses;
    }

    public void setUseTelescopeResponses(UseTelescopeResponses useTelescopeResponses) {
        this.useTelescopeResponses = useTelescopeResponses;
    }

    public ArrayList<RuneStone> getRuneStones( ){
        return runeStones;
    }

    public void setRuneStones(ArrayList<RuneStone> runeStones) {
        this.runeStones = runeStones;
    }
}
