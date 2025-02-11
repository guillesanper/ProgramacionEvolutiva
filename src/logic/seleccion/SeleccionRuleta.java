package logic.seleccion;

public class SeleccionRuleta extends Seleccion{

    //Array de probabilidades acumuladas
    private double[] probabilidadAcumulada;
    //Array de valores de fitness ajustados para que funcionen correctamente
    //si es un problema de minimizacion o maximizacion
    private double[] fitnessCorr;

    public SeleccionRuleta (double[] fitness, int tamPoblacion, boolean min, double mejor) {
        double uTotal = 0;
        this.min = min;
        this.fitness = fitness;
        this.tamPoblacion = tamPoblacion;
        this.probabilidad = new double[this.tamPoblacion];
        this.probabilidadAcumulada = new double[this.tamPoblacion];
        this.seleccion = new int[this.tamPoblacion];
        this.fitnessCorr = new double[this.tamPoblacion];
        if(min)
            this.corrigeMinimizar(mejor);
        else
            this.corrigeMaximizar(mejor);

        for(int j = 0; j < this.tamPoblacion; j++)
            uTotal += this.fitnessCorr[j];

        for(int i = 0; i < this.tamPoblacion; i++) {
            this.probabilidad[i] = this.fitnessCorr[i] / uTotal;

            if (i == 0)
                this.probabilidadAcumulada[i] = this.probabilidad[i];
            else
                this.probabilidadAcumulada[i] = this.probabilidad[i] + this.probabilidadAcumulada[i-1];
        }

    }
    /*
    * Selecciona individuos basándose en la ruleta.
    * El primer i donde probAcu[i] sea mayor o igual a x es el individuo seleccionado, se
    * repite el proceso hasta seleccionar los individuos que tuviera la poblacion
    * */
    @Override
    public int[] getSeleccion() {

        int seleccionados = 0;
        double x;
        while (seleccionados < this.tamPoblacion) {
            x = this.rand.nextDouble();
            for(int i = 0; i < this.tamPoblacion; i++) {
                if(this.probabilidadAcumulada[i] >= x ) {
                    this.seleccion[seleccionados] = i;
                    seleccionados++;
                    break;
                }
            }
        }
        return this.seleccion;
    }

    //Convierte un problema de minimización en uno de maximización.
    //Se usa 1.05 * max para evitar valores negativos.
    private void corrigeMinimizar(double max) {
        for(int i = 0; i < this.tamPoblacion; i++)
            this.fitnessCorr[i] = (1.05 * max) - this.fitness[i];
    }

    //Ajusta los valores de fitness para asegurarse de que sean positivos.
    private void corrigeMaximizar(double min) {
        for(int i = 0; i < this.tamPoblacion; i++)
            this.fitnessCorr[i] = this.fitness[i] + Math.abs(min);
    }

}
