package logic.seleccion;

public class SeleccionTorneoProbabilistico extends Seleccion {

    private final double p;

    public SeleccionTorneoProbabilistico() {
        this.p = 0.6;
    }

    private Seleccionable bigger(Seleccionable a, Seleccionable b, Seleccionable c) {
        if (a.compareTo(b) > 0 && a.compareTo(c) > 0) return a;
        if (b.compareTo(c) > 0) return b;
        return c;
    }

    private Seleccionable smaller(Seleccionable a, Seleccionable b, Seleccionable c) {
        if (a.compareTo(b) < 0 && a.compareTo(c) < 0) return a;
        if (b.compareTo(c) < 0) return b;
        return c;
    }

    @Override
    public int[] getSeleccion(Seleccionable[] list, int tamPoblacion) {
        int[] seleccion = new int[tamPoblacion];

        for (int i = 0; i < tamPoblacion; i++) {
            seleccion[i] = torneoProbabilistico(list, tamPoblacion);
        }

        return seleccion;
    }

    private int torneoProbabilistico(Seleccionable[] list, int tamPoblacion) {
        int ind1 = this.rand.nextInt(tamPoblacion);
        int ind2 = this.rand.nextInt(tamPoblacion);
        int ind3 = this.rand.nextInt(tamPoblacion);

        Seleccionable a = list[ind1];
        Seleccionable b = list[ind2];
        Seleccionable c = list[ind3];

        // Corregido: El mejor individuo (mayor fitness) debe ser seleccionado con probabilidad p
        if (this.rand.nextDouble() <= p) return bigger(a, b, c).getIndex();
        return smaller(a, b, c).getIndex();
    }
}