package logic.seleccion;

public class SeleccionRestos extends Seleccion{

    public SeleccionRestos(boolean min) {
        super(min);
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        int metidos = 0;

        for (int i = 0; i < tamPoblacion; i++) {
            long apariciones = Math.round(list[i].getProb()*tamPoblacion);

            for (int j = 0; j < apariciones; j++) {
                seleccion[metidos] = i;
                metidos++;
            }
        }

        if (metidos != tamPoblacion) {

            int[] nuevaSeleccion = SeleccionFactory.getMetodoSeleccion(
                    "Torneo Deterministico", min, 0
            ).getSeleccion(list, tamPoblacion-metidos);

            for (int i = 0; i < tamPoblacion-metidos; i++) seleccion[metidos+i] = nuevaSeleccion[i];
        }
        return seleccion;
    }
}
