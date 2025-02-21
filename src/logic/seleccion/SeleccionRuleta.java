package logic.seleccion;

public class SeleccionRuleta extends Seleccion{
    public SeleccionRuleta (Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);
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
                if(this.list[i].getAccProb() >= x ) {
                    this.seleccion[seleccionados] = i;
                    seleccionados++;
                    break;
                }
            }
        }
        return this.seleccion;
    }
}
