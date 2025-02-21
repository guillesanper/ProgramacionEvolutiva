package logic.seleccion;

public class SeleccionRuleta extends Seleccion{
    public SeleccionRuleta (boolean min) {
        super(min);
    }

    /*
    * Selecciona individuos basándose en la ruleta.
    * El primer i donde probAcu[i] sea mayor o igual a x es el individuo seleccionado, se
    * repite el proceso hasta seleccionar los individuos que tuviera la poblacion
    * */
    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        int seleccionados = 0;
        double x;
        while (seleccionados < tamPoblacion) {
            x = this.rand.nextDouble();
            for(int i = 0; i < tamPoblacion; i++) {
                if(list[i].getAccProb() >= x ) {
                    seleccion[seleccionados] = i;
                    seleccionados++;
                    break;
                }
            }
        }
        return seleccion;
    }
}
