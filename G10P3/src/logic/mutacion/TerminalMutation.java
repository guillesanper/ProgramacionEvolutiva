package logic.mutacion;

import model.Tree;
import model.symbol.Expression;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TerminalMutation implements Mutacion {
    @Override
    public Tree mutate(Tree t) {
        List<Expression> terminals = new ArrayList<>();
        t.collectTerminals(terminals);

        int choice = new Random().nextInt(terminals.size());
        terminals.get(choice).mutate();

        return t;
    }
}
