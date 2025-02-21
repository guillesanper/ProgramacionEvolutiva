package logic.seleccion;

public class SeleccionRestos extends Seleccion{

    public SeleccionRestos(Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);
    }

    @Override
    public int[] getSeleccion() {
        int metidos = 0;

        for (int i = 0; i < this.tamPoblacion; i++) {
            long apariciones = Math.round(this.list[i].getProb()*tamPoblacion);

            for (int j = 0; j < apariciones; j++) {
                this.seleccion[metidos] = i;
                metidos++;
            }
        }

        if (metidos != tamPoblacion) {

            int[] nuevaSeleccion = SeleccionFactory.getMetodoSeleccion(
                    "Torneo Deterministico", list, tamPoblacion-metidos, min, 0
            ).getSeleccion();

            for (int i = 0; i < tamPoblacion-metidos; i++) this.seleccion[metidos+i] = nuevaSeleccion[i];
        }
        return this.seleccion;
    }
}
