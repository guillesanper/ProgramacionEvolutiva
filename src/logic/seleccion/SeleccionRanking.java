package logic.seleccion;

import java.util.Arrays;

public class SeleccionRanking extends Seleccion{
    public SeleccionRanking(boolean min) {
        super(min);
    }

    private void calculateProbs(Seleccionable[] sortedList, int tamPoblacion) {
        double _beta = 1.5;

        double accProb = 0.0;
        for (int i = 0; i <= tamPoblacion; ++i) {
            double probOfIth = (double)i/tamPoblacion;
            probOfIth *= 2*(_beta-1);
            probOfIth = _beta - probOfIth;
            probOfIth = probOfIth * ((double)1/tamPoblacion);

            sortedList[i].setAccProb(accProb);
            sortedList[i].setProb(probOfIth);
            accProb += probOfIth;
        }
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        Seleccionable[] sortedList = new Seleccionable[tamPoblacion];

        for (int i = 0; i < tamPoblacion; i++) sortedList[i] = list[i];

        Arrays.sort(sortedList, (a, b) -> Double.compare(b.getFitness(), a.getFitness()));

        this.calculateProbs(sortedList, tamPoblacion);

       return SeleccionFactory
               .getMetodoSeleccion("Ruleta", min, 0)
               .getSeleccion(sortedList, tamPoblacion);
    }
}
