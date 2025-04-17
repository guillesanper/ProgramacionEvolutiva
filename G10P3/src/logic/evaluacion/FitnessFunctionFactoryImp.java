package logic.evaluacion;

import model.Mapa;
import utils.Pair;

public class FitnessFunctionFactoryImp extends FitnessFunctionFactory{


    private static Mapa map;
    private Pair<Double,Double> interval;

    public FitnessFunctionFactoryImp(){
        map = new Mapa();
    }

    @Override
    public FitnessFunction getFunction(Integer index, boolean bloating_controller, double avgPopSize) {
        if (!bloating_controller) return this.getFunction(index);
        return this.getBloatingController(index, avgPopSize);
    }

    private  FitnessFunction getFunction(Integer index){
        FitnessFunction ff;
        switch (index){
            default:
                ff = new FitnessHormiga(map);
                interval = ff.getLimits();
                return ff;
        }
    }

    public FitnessFunction getBloatingController(int index, double averagePopSize) {
        return new TarpeianBloating(this.getFunction(index), averagePopSize);
    }

    public Pair<Double,Double> getInterval() {
        return interval;
    }

}
