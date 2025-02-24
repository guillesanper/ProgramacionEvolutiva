package model;

public class IndividuoFuncion5 extends IndividuoDouble {
    private final int d;
    private final int m;

    public IndividuoFuncion5(int d) {
        super(d);

        this.d = d;
        this.m = 10;

        double[] mins = new double[d];
        double[] maxs = new double[d];

        for (int i = 0; i < d; i++) {
            mins[i] = 0;
            maxs[i] = Math.PI;
        }

        this.initGens(mins, maxs);
    }

    @Override
    public double getFitness() {
        double sum = 0;

        for (int i = 0; i < this.d; i++) {
            double firstSin = Math.sin(this.getPhenotype(i));
            double secondSin = Math.sin(i*Math.pow(this.getPhenotype(i), 2) / Math.PI);
            sum += firstSin*Math.pow(secondSin, 2*this.m);
        }

        return sum*-1;
    }
}
