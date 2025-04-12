package logic.evaluacion;

import model.Mapa;
import utils.Pair;

public class FitnessFunctionFactoryImp extends FitnessFunctionFactory{


    private static Mapa map;
    private Pair<Double,Double> interval;

    public FitnessFunctionFactoryImp(){
        map = new Mapa();
    }
    public FitnessFunction getFunction(Integer index){
        FitnessFunction ff;
        switch (index){
            default:
                ff = new FitnessHormiga(map);
                interval = ff.getLimits();
                return ff;
        }
    }

    public FitnessFunction getBloatingController(FitnessFunction ff, double averagePopSize) {
        return new TarpeianBloating(ff, averagePopSize);
    }


    public Pair<Double,Double> getInterval() {
        return interval;
    }

}
