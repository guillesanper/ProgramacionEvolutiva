package logic.cruce;

import java.util.*;

public class CruceERX extends Cruce<Integer> {
    Random rand;
    private final List<Set<Integer>> conectividades;

    public CruceERX(int tamCromosoma) {
        super(tamCromosoma);
        this.rand = new Random();

        conectividades = new ArrayList<>();
        for (int i = 0; i < tamCromosoma; i++) {
            conectividades.add(new HashSet<>());
        }
    }

    private void conectar(Integer[] c, int o, int d) {
        conectividades.get(c[o]-1).add(c[d]);
        conectividades.get(c[d]-1).add(c[o]);
    }

    private int escoger(int num, Set<Integer> met) {
        Set<Integer> ady = conectividades.get(num-1);

        int min = tamCromosoma;
        Set<Integer> mins = new HashSet<>();
        for (Integer el : ady) {
            if (met.contains(el)) continue;

            if (conectividades.get(el-1).size() < min) {
                min = conectividades.get(el-1).size();
                mins = new HashSet<>();
                mins.add(el);
            } else if (conectividades.get(el-1).size() == min) {
                mins.add(el);
            }
        }

        return mins.stream().skip(this.rand.nextInt(mins.size())).findFirst().orElse(null);
    }

    private void crearTabla(Integer[] c1, Integer[] c2) {
        for (int i = 0; i < tamCromosoma-1; i++) {
            conectar(c1, i, i+1);
            conectar(c2, i, i+1);
        }

        conectar(c1, tamCromosoma-1, 0);
        conectar(c2, tamCromosoma-1, 0);
    }

    private Integer escogerAleatorio(Set<Integer> met) {
        int ret;

        do {
            ret = this.rand.nextInt(tamCromosoma)+1;
        } while (met.contains(ret));

        return ret;
    }

    private boolean generaBloqueo(int el, Set<Integer> met) {
        Set<Integer> vecinos = conectividades.get(el-1);
        return met.containsAll(vecinos);
    }

    private Integer[] crearHijo(Integer[] c) {
        Integer[] hijo = new Integer[tamCromosoma];
        Set<Integer> met = new HashSet<>();

        hijo[0] = c[0];
        met.add(c[0]);
        int metidos = 1;

        int escogido;
        boolean bloqueo = false;

        while (metidos < tamCromosoma) {
            if (bloqueo) {
                bloqueo = false;
                escogido = escogerAleatorio(met);
            } else escogido = escoger(hijo[metidos-1], met);

            hijo[metidos] = escogido;
            met.add(escogido);
            metidos++;

            if (generaBloqueo(escogido, met)) bloqueo = true;
        }

        return hijo;
    }

    @Override
    public void cross(Integer[] c1, Integer[] c2) {
        crearTabla(c1, c2);

        Integer[] h1 = crearHijo(c1);
        Integer[] h2 = crearHijo(c2);

        // Copy offspring back into the parents array (in-place modification).
        System.arraycopy(h1, 0, c1, 0, tamCromosoma);
        System.arraycopy(h2, 0, c2, 0, tamCromosoma);
    }
}
