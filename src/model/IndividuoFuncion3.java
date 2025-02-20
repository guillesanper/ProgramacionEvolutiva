package model;

public class IndividuoFuncion3 extends IndividuoBoolean{

    public IndividuoFuncion3(double errorValue) {
        super(errorValue, 2);

        double[] mins = new double[]{-10, -10};
        double[] maxs = new double[]{10, 10};

        this.initGens(mins, maxs);
    }

    private double calcHalf(double x) {
        double sum = 0;

        for (int i = 1; i <= 5; i++) {
            sum += i*Math.cos((i+1)*x+i);
        }

        return sum;
    }

    @Override
    public double getFitness() {
        return calcHalf(this.getPhenotype(0))*calcHalf(this.getPhenotype(1));
    }

}
