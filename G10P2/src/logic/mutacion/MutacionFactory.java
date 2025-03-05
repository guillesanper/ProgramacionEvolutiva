package logic.mutacion;

import java.util.HashMap;

public class MutacionFactory {

    private static final HashMap<Integer,Mutate> mutations = new HashMap<>();

    static {
        mutations.put(0,new MutacionInsercion());
    }

    public static Mutate getMutation(int index){
        return mutations.getOrDefault(index,new MutacionInsercion());
    }
}
