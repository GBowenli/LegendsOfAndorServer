package LoAServer.ReturnClasses;

import LoAServer.Fight;
import LoAServer.PublicEnums.EndBattleRoundResponses;

public class EndBattleRoundRC {
    private Fight fight;
    private EndBattleRoundResponses endBattleRoundResponses;

    public EndBattleRoundRC() {}

    public EndBattleRoundRC(Fight fight, EndBattleRoundResponses endBattleRoundResponses) {
        this.fight = fight;
        this.endBattleRoundResponses = endBattleRoundResponses;
    }

    public Fight getFight() {
        return fight;
    }

    public void setFight(Fight fight) {
        this.fight = fight;
    }

    public EndBattleRoundResponses getEndBattleRoundResponses() {
        return endBattleRoundResponses;
    }

    public void setEndBattleRoundResponses(EndBattleRoundResponses endBattleRoundResponses) {
        this.endBattleRoundResponses = endBattleRoundResponses;
    }
}
