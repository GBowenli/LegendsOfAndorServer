package LoAServer;

import java.util.HashMap;

enum NarratorSpace {
    A,B,C,D,E,F,G,H,I,J,K,L,M,N
}
public class Narrator {
    private NarratorSpace slot;
    private HashMap<NarratorSpace,Integer[]> map = new HashMap<>();

    public Narrator(){
        this.slot = NarratorSpace.A;
        map.put(NarratorSpace.A,new Integer[]{1725,961});
        map.put(NarratorSpace.B,new Integer[]{1725,885});
        map.put(NarratorSpace.C,new Integer[]{1725,828});
        map.put(NarratorSpace.D,new Integer[]{1725,750});
        map.put(NarratorSpace.E,new Integer[]{1725,674});
        map.put(NarratorSpace.F,new Integer[]{1725,607});
        map.put(NarratorSpace.G,new Integer[]{1725,539});
        map.put(NarratorSpace.H,new Integer[]{1725,453});
        map.put(NarratorSpace.I,new Integer[]{1725,375});
        map.put(NarratorSpace.J,new Integer[]{1725,313});
        map.put(NarratorSpace.K,new Integer[]{1725,240});
        map.put(NarratorSpace.L,new Integer[]{1725,172});
        map.put(NarratorSpace.M,new Integer[]{1725,93});
        map.put(NarratorSpace.N,new Integer[]{1725,28});
    }
    //setNarrator is not provided,
    //It only has one operation "increment"
    //When need to increment call this function.
    public void incrementNarrator(){
        if (this.slot ==NarratorSpace.A){
            this.slot =NarratorSpace.B;
        }
        else if (this.slot ==NarratorSpace.B){
            this.slot =NarratorSpace.C;
        }
        else if (this.slot ==NarratorSpace.C){
            this.slot =NarratorSpace.D;
        }
        else if (this.slot ==NarratorSpace.D){
            this.slot =NarratorSpace.E;
        }
        else if (this.slot ==NarratorSpace.E){
            this.slot =NarratorSpace.F;
        }
        else if (this.slot ==NarratorSpace.F){
            this.slot =NarratorSpace.G;
        }
        else if (this.slot ==NarratorSpace.G){
            this.slot =NarratorSpace.H;
        }
        else if (this.slot ==NarratorSpace.H){
            this.slot =NarratorSpace.I;
        }
        else if (this.slot ==NarratorSpace.I){
            this.slot =NarratorSpace.J;
        }
        else if (this.slot ==NarratorSpace.J){
            this.slot =NarratorSpace.K;
        }
        else if (this.slot ==NarratorSpace.K){
            this.slot =NarratorSpace.L;
        }
        else if (this.slot ==NarratorSpace.L){
            this.slot =NarratorSpace.M;
        }
        else if (this.slot ==NarratorSpace.M){
            this.slot =NarratorSpace.N;
        }
    }

    public HashMap<NarratorSpace,Integer[]> getMap(){
        return this.map;
    }

    public NarratorSpace getSlot(){
        return this.slot;
    }
}
