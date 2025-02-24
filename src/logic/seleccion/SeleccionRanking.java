package logic.seleccion;

public class SeleccionRanking extends Seleccion {
    private void calculateProbs(Seleccionable[] list, int tamPoblacion) {
        double _beta = 1.5;

        double accProb = 0.0;
        for (int i = 0; i < tamPoblacion; ++i) {
            double probOfIth = (double) i / tamPoblacion;
            probOfIth *= 2 * (_beta - 1);
            probOfIth = _beta - probOfIth;
            probOfIth = probOfIth * ((double) 1 / tamPoblacion);

            list[i].setAccProb(accProb);
            list[i].setProb(probOfIth);
            accProb += probOfIth;
        }
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        this.calculateProbs(list, tamPoblacion);

        return SeleccionFactory
                .getMetodoSeleccion("Ruleta")
                .getSeleccion(list, tamPoblacion);
    }
}
