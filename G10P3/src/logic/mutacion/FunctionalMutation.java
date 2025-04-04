package logic.mutacion;

import model.Tree;
import model.symbol.Expression;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FunctionalMutation implements Mutacion {
    @Override
    public Tree mutate(Tree t) {
        List<Expression> functions = new ArrayList<>();
        t.collectFunctions(functions);

        int choice = new Random().nextInt(functions.size());
        functions.get(choice).mutate();

        return t;
    }
}
