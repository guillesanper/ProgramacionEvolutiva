package logic.seleccion;

public class SeleccionTorneoProbabilistico extends Seleccion {

    private final double p;

    public SeleccionTorneoProbabilistico(Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);
        this.p = 0.5;
    }

    @Override
    public int[] getSeleccion() {
        for (int i = 0; i < this.tamPoblacion; i++) {
            this.seleccion[i] = torneoProbabilistico();
        }
        return this.seleccion;
    }

    private int torneoProbabilistico() {
        int ind1 = this.rand.nextInt(this.tamPoblacion);
        int ind2 = this.rand.nextInt(this.tamPoblacion);
        int ind3 = this.rand.nextInt(this.tamPoblacion);
        double x = this.rand.nextDouble();

        // Determinar el mejor y el segundo mejor según si es minimización o maximización
        int mejor, segundo;
        if (
                (min && list[ind1].getFitness() > list[ind2].getFitness()) ||
                (!min && list[ind1].getFitness() < list[ind2].getFitness())
        ) {
            mejor = ind1;
            segundo = ind2;
        } else {
            mejor = ind2;
            segundo = ind1;
        }

        if (
                (min && list[mejor].getFitness() > list[ind3].getFitness()) ||
                (!min && list[mejor].getFitness() < list[ind3].getFitness())
        ) {
            segundo = mejor;
            mejor = ind3;
        } else if (
                (min && list[segundo].getFitness() > list[ind3].getFitness()) ||
                (!min && list[segundo].getFitness() < list[ind3].getFitness())
        ) {
            segundo = ind3;
        }

        return (x > p) ? mejor : segundo;
    }
}
