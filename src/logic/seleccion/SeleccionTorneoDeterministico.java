package logic.seleccion;

public class SeleccionTorneoDeterministico extends Seleccion{

    public SeleccionTorneoDeterministico(Seleccionable[] list, int tamPoblacion, boolean min) {
        super(list, tamPoblacion, min);
    }

    @Override
    public int[] getSeleccion() {
        if(!this.min) {
            for(int i = 0; i < this.tamPoblacion; i++) {
                int ind1 = this.rand.nextInt(this.tamPoblacion), ind2 = this.rand.nextInt(this.tamPoblacion), ind3 = this.rand.nextInt(this.tamPoblacion);
                if(this.list[ind1].getFitness() < this.list[ind2].getFitness())
                    if(this.list[ind2].getFitness() < this.list[ind3].getFitness())
                        this.seleccion[i] = ind3;
                    else
                        this.seleccion[i] = ind2;
                else
                if(this.list[ind1].getFitness() < this.list[ind3].getFitness())
                    this.seleccion[i] = ind3;
                else
                    this.seleccion[i] = ind1;
            }
        }
        else {
            for(int i = 0; i < this.tamPoblacion; i++) {
                int ind1 = this.rand.nextInt(this.tamPoblacion);
                int ind2 = this.rand.nextInt(this.tamPoblacion);
                int ind3 = this.rand.nextInt(this.tamPoblacion);

                if(this.list[ind1].getFitness() > this.list[ind2].getFitness())
                    if(this.list[ind2].getFitness() > this.list[ind3].getFitness())
                        this.seleccion[i] = ind3;
                    else
                        this.seleccion[i] = ind2;
                else
                if(this.list[ind1].getFitness() > this.list[ind3].getFitness())
                    this.seleccion[i] = ind3;
                else
                    this.seleccion[i] = ind1;
            }
        }
        return this.seleccion;
    }
}
