package logic.cruce;

public abstract class Cruce <T>{
    int tamCromosoma;
    public Cruce (int tamCromosoma) {
        this.tamCromosoma = tamCromosoma;
    }

    public abstract void cross(T[] c1, T[] c2);
}