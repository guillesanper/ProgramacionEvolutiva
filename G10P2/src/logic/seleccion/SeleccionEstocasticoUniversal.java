package logic.seleccion;

public class SeleccionEstocasticoUniversal extends Seleccion{
    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];
        int metidos = 0;

        double r = this.rand.nextDouble()*((double) 1 /tamPoblacion);

        double[] marcas = new double[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) marcas[i] = list[i].getAccProb();

        double seleccionado;

        for (int i = 0; i < tamPoblacion; i++){
            seleccionado = (r+i)/tamPoblacion;
            int j = 0;
            while (j < tamPoblacion - 1 && seleccionado > marcas[j]) j++;
            seleccion[metidos] = Math.max(j - 1, 0); 

            metidos++;
        }

        return seleccion;
    }

}
