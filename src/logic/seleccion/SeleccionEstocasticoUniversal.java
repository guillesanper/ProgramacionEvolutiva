package logic.seleccion;

public class SeleccionEstocasticoUniversal extends Seleccion{

    public SeleccionEstocasticoUniversal(Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);
    }

    @Override
    public int[] getSeleccion() {
        int metidos = 0;

        double r = this.rand.nextDouble()*((double) 1 /tamPoblacion);

        double[] marcas = new double[tamPoblacion];
        for (int i = 0; i < tamPoblacion; i++) marcas[i] = this.list[i].getAccProb();

        double seleccionado;

        for (int i = 1; i <= tamPoblacion; i++){
            seleccionado = (r+i-1)/tamPoblacion;
            int j = 0;
            while (seleccionado < marcas[j]) j++;
            this.seleccion[metidos] = j-1;
            metidos++;
        }

        return this.seleccion;
    }

}
