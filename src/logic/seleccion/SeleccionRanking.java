package logic.seleccion;

import java.util.Arrays;

public class SeleccionRanking extends Seleccion{

    Seleccionable[] sortedList;

    public SeleccionRanking(Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);


        this.sortedList = new Seleccionable[tamPoblacion];

        for (int i = 0; i < this.tamPoblacion; i++) this.sortedList[i] = list[i];

        Arrays.sort(sortedList, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));
    }

    private void calculateProbs() {
        double _beta = 1.5;

        double accProb = 0.0;
        for (int i = 0; i <= this.tamPoblacion; ++i) {
            double probOfIth = (double)i/this.tamPoblacion;
            probOfIth *= 2*(_beta-1);
            probOfIth = _beta - probOfIth;
            probOfIth = probOfIth * ((double)1/this.tamPoblacion);

            this.sortedList[i].setAccProb(accProb);
            this.sortedList[i].setProb(probOfIth);
            accProb += probOfIth;
        }
    }

    @Override
    public int[] getSeleccion() {
       this.calculateProbs();

       return SeleccionFactory.getMetodoSeleccion("Ruleta", list, tamPoblacion, min, 0).getSeleccion();
    }
}
