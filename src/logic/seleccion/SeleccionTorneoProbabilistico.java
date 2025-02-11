package logic.seleccion;

public class SeleccionTorneoProbabilistico extends Seleccion {

    private double p = 0.5;

    public SeleccionTorneoProbabilistico(double[] fitness, int tamPoblacion, boolean min) {
        this.fitness = fitness;
        this.tamPoblacion = tamPoblacion;
        this.probabilidad = new double[this.tamPoblacion];
        this.seleccion = new int[this.tamPoblacion];
        this.min = min;
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
        if ((min && fitness[ind1] > fitness[ind2]) || (!min && fitness[ind1] < fitness[ind2])) {
            mejor = ind1;
            segundo = ind2;
        } else {
            mejor = ind2;
            segundo = ind1;
        }

        if ((min && fitness[mejor] > fitness[ind3]) || (!min && fitness[mejor] < fitness[ind3])) {
            segundo = mejor;
            mejor = ind3;
        } else if ((min && fitness[segundo] > fitness[ind3]) || (!min && fitness[segundo] < fitness[ind3])) {
            segundo = ind3;
        }

        return (x > p) ? mejor : segundo;
    }
}
