package logic.mutacion;

import java.util.HashMap;

public class MutacionFactory {

    private static final HashMap<Integer,Mutate> mutations = new HashMap<>();

    static {
        mutations.put(0,new MutacionInsercion());
        mutations.put(1,new MutacionIntercambio());
        mutations.put(2,new MutacionInversion());
        mutations.put(3,new MutacionHeuristica());
        mutations.put(4,new MutacionInversion());
    }

    public static Mutate getMutation(int index){
        return mutations.getOrDefault(index,new MutacionInsercion());
    }
}
