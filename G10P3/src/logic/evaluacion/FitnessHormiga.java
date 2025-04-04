package logic.evaluacion;

import model.Tree;
import model.symbol.Expression;
import utils.Pair;
import model.Mapa;

public class FitnessHormiga implements FitnessFunction{

    private static final int MAX_STEPS = 400;
    private static final int MAX_FOOD = 89;

    private final Mapa map;

    public FitnessHormiga(Mapa map) {
        this.map = map;
    }

    @Override
    public double calculateFitness(Tree chromosome) {
        Expression program = chromosome.getRoot();

        int steps = 0;

        // Ejecutar la simulación hasta agotar los pasos o encontrar toda la comida
        while (steps < MAX_STEPS && map.getFoodEaten() < MAX_FOOD) {
            boolean isThereFood = map.isFoodAhead();

            // Ejecutar la expresión actual (el programa genético)
            Object action = program.execute(isThereFood);

            if (action instanceof String) {
                String actionStr = (String) action;
                switch (actionStr) {
                    case "Advance":
                        map.moveForward();
                        break;
                    case "Left":
                        map.turnLeft();
                        break;
                    case "Right":
                        map.turnRight();
                        break;
                }
            }

            // Incrementar el contador de pasos
            steps++;
        }

        // El fitness es la cantidad de comida encontrada
        return map.getFoodEaten();
    }

    @Override
    public Pair<Double, Double> getLimits() {
        // El límite inferior es 0 (ninguna comida encontrada)
        // El límite superior es la cantidad máxima de comida disponible
        return new Pair<>(0.0, (double) MAX_FOOD + 20.0);
    }
}
