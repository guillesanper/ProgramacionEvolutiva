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

        for (int i = 1; i <= tamPoblacion; i++){
            seleccionado = (r+i-1)/tamPoblacion;
            int j;
            for (j = 1; j < marcas.length; j++) {
                if (seleccionado >= marcas[j-1] && seleccionado < marcas[j]) break;
            }
            seleccion[metidos] = list[j-1].getIndex();
            metidos++;
        }

        return seleccion;
    }

}
