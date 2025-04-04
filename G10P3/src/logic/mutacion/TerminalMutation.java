package logic.mutacion;

import model.Tree;
import model.symbol.Expression;
import model.symbol.ExpressionFactory;
import model.symbol.terminals.Terminal;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class TerminalMutation implements Mutacion {
    @Override
    public Tree mutate(Tree t) {
        Tree mutated = t.copy();

        List<Terminal> terminals = new ArrayList<>();
        mutated.collectTerminals(terminals);

        int choice = new Random().nextInt(terminals.size());
        terminals.get(choice).mutate();

        return t;
    }
}
