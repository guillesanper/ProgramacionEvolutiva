package logic.seleccion;

public class SeleccionTorneoDeterministico extends Seleccion{

    public SeleccionTorneoDeterministico(boolean min) {
        super(min);
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        if(!this.min) {
            for(int i = 0; i < tamPoblacion; i++) {
                int ind1 = this.rand.nextInt(tamPoblacion), ind2 = this.rand.nextInt(tamPoblacion), ind3 = rand.nextInt(tamPoblacion);
                if(list[ind1].getFitness() < list[ind2].getFitness())
                    if(list[ind2].getFitness() < list[ind3].getFitness())
                        seleccion[i] = ind3;
                    else
                        seleccion[i] = ind2;
                else
                if(list[ind1].getFitness() < list[ind3].getFitness())
                    seleccion[i] = ind3;
                else
                    seleccion[i] = ind1;
            }
        }
        else {
            for(int i = 0; i < tamPoblacion; i++) {
                int ind1 = rand.nextInt(tamPoblacion);
                int ind2 = rand.nextInt(tamPoblacion);
                int ind3 = rand.nextInt(tamPoblacion);

                if(list[ind1].getFitness() > list[ind2].getFitness())
                    if(list[ind2].getFitness() > list[ind3].getFitness())
                        seleccion[i] = ind3;
                    else
                        seleccion[i] = ind2;
                else
                if(list[ind1].getFitness() > list[ind3].getFitness())
                    seleccion[i] = ind3;
                else
                    seleccion[i] = ind1;
            }
        }
        return seleccion;
    }
}
