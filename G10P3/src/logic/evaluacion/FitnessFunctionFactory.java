package logic.evaluacion;

import utils.Pair;


public abstract class FitnessFunctionFactory {
    private static FitnessFunctionFactory instance;

    public static synchronized FitnessFunctionFactory getInstance(){
        if(instance == null){
            instance = new FitnessFunctionFactoryImp();
        }
        return instance;
    }

    public abstract FitnessFunction getFunction(Integer index, boolean bloating_controller, double avgPopSize);
    public abstract Pair<Double,Double> getInterval();

}