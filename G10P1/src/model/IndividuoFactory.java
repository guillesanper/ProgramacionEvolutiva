package model;

import utils.Pair;

public class IndividuoFactory {

    private static final Pair<Double, Double> INTERVAL_FUNC1 = new Pair<>(0.0, 40.0);
    private static final Pair<Double, Double> INTERVAL_FUNC2 = new Pair<>(-110.0, 50.0);
    private static final Pair<Double, Double> INTERVAL_FUNC3 = new Pair<>(-200.0, 50.0);
    private static final Pair<Double, Double> INTERVAL_FUNC4 = new Pair<>(-10.0, 5.0);
    private static final Pair<Double, Double> INTERVAL_FUNC5 = new Pair<>(-10.0, 5.0);

    public static Individuo<?> createIndividuo(int func_id, double valError, int d) {
        switch (func_id) {
            case 0:
                return new IndividuoFuncion1(valError);
            case 1:
                return new IndividuoFuncion2(valError);
            case 2:
                return new IndividuoFuncion3(valError);
            case 3:
                return new IndividuoFuncion4(valError, d);
            case 4:
                return new IndividuoFuncion5(d);
            default:
                return null;
        }
    }

    public static Pair<Double, Double> getInterval(int funcIndex) {
        switch (funcIndex) {
            case 0:
                return INTERVAL_FUNC1;
            case 1:
                return INTERVAL_FUNC2;
            case 2:
                return INTERVAL_FUNC3;
            case 3:
                return INTERVAL_FUNC4;
            case 4:
                return INTERVAL_FUNC5;
            default:
                return null;
        }
    }
}
