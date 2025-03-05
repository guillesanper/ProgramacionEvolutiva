package model;

public class IndividuoFuncion2 extends IndividuoBoolean {

    public IndividuoFuncion2(double errorValue) {
        super(errorValue, 2);
        double[] mins = new double[]{-10.0, -6.5}; // Rango de x1 y x2
        double[] maxs = new double[]{0.0, 0.0};   // Rango de x1 y x2

        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        double x1 = this.getPhenotype(0);
        double x2 = this.getPhenotype(1);

        double exp1 = Math.exp(1-Math.cos(x1));
        double exp2 = Math.exp(1-Math.sin(x2));

        return Math.sin(x2)*Math.pow(exp1, 2) +
                Math.cos(x1)*Math.pow(exp2, 2) +
                Math.pow(x1-x2, 2);
    }

}
