package LoAServer;

public class ActivateWizardTarget {
    private HeroClass heroClass;
    private Integer dieIndex;
    private Integer newDieValue;

    public ActivateWizardTarget() {}

    public ActivateWizardTarget(HeroClass heroClass, Integer dieIndex, Integer newDieValue) {
        this.heroClass = heroClass;
        this.dieIndex = dieIndex;
        this.newDieValue = newDieValue;
    }

    public HeroClass getHeroClass() {
        return heroClass;
    }

    public void setHeroClass(HeroClass heroClass) {
        this.heroClass = heroClass;
    }

    public Integer getDieIndex() {
        return dieIndex;
    }

    public void setDieIndex(Integer dieIndex) {
        this.dieIndex = dieIndex;
    }

    public Integer getNewDieValue() {
        return newDieValue;
    }

    public void setNewDieValue(Integer newDieValue) {
        this.newDieValue = newDieValue;
    }
}
