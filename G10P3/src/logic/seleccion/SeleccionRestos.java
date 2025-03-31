package logic.seleccion;

public class SeleccionRestos extends Seleccion{
    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        int metidos = 0;

        for (int i = 0; i < tamPoblacion; i++) {
            if (metidos == tamPoblacion) break;
            long apariciones = Math.round(list[i].getProb()*tamPoblacion);

            if(apariciones <= tamPoblacion-metidos)
                for (int j = 0; j < apariciones; j++) seleccion[metidos++] = i;


        }

        if (metidos != tamPoblacion) {

            int[] nuevaSeleccion = SeleccionFactory.getMetodoSeleccion(
                    "Torneo Deterministico"
            ).getSeleccion(list, tamPoblacion-metidos);

            if (tamPoblacion - metidos >= 0)
                System.arraycopy(nuevaSeleccion, 0, seleccion, metidos, tamPoblacion - metidos);
        }
        return seleccion;
    }
}
