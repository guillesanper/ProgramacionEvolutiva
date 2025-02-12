package model;

public class IndividuoFuncion1 extends IndividuoBoolean {
    public IndividuoFuncion1(double errorValue){
        super(errorValue, 2);

        double[] mins = new double[]{-3, 4.1};
        double[] maxs = new double[]{12.1, 5.8};
        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        double x1 = this.getPhenotype(0);
        double x2 = this.getPhenotype(1);

        return 21.5 + x1*Math.sin(4*Math.PI*x1) + x2*Math.sin(20*Math.PI*x2);
    }
}
