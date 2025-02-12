package model.factoria;

import model.*;

public class IndividuoFactory {
    public static Individuo<?> createIndividuo(int func_id, double valError) {
        switch (func_id) {
            case 0:
                return new IndividuoFuncion1(valError);
            case 1:
                return new IndividuoFuncion2(valError);
            case 2:
                return new IndividuoFuncion3(valError);
            case 3:
                return new IndividuoFuncion4(valError);
            case 4:
                return new IndividuoFuncion5(valError);
            default:
                return null;
        }
    }
}
