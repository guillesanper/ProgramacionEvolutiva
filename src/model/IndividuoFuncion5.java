package model;

public class IndividuoFuncion5 extends IndividuoDouble {
    public IndividuoFuncion5(int dimenion) {
        super(dimenion);
        this.initGens(null, null);
    }

    @Override
    public double getPhenotype(int n) {
        return 0;
    }

    @Override
    public double getFitness() {
        return 0;
    }
}
