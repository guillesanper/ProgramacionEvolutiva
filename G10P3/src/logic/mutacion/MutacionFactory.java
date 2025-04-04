package logic.mutacion;

import java.util.HashMap;

public class MutacionFactory {

    private static final HashMap<Integer, Mutacion> mutations = new HashMap<>();

    static {
        mutations.put(0,new TerminalMutation());
    }

    public static Mutacion getMutation(int index){
        return mutations.getOrDefault(index,new TerminalMutation());
    }
}
