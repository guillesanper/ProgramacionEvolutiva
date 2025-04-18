package model.symbol.terminals;

import model.symbol.Expression;

import java.util.List;
import java.util.Objects;
import java.util.Random;

public abstract class Terminal extends Expression {
    public Terminal(String op) {
        this.x = 0;
        this.y = 0;
        this.childrenCount = 0;
        this.operation = op;
    }

    @Override
    public Object execute(boolean isThereFood) {
        return this.operation;
    }

    @Override
    public void mutate() {
        List<String> ops = List.of(Advance.OP, Left.OP, Right.OP);
        Random rand = new Random();
        String newop;
        int choice;
        do {
            choice =rand.nextInt(3);
            newop = ops.get(choice);
        } while (Objects.equals(newop, this.operation));
        this.operation = newop;
    }

    @Override
    public void collectTerminals(List<Expression> terminals) {
        terminals.add(this);
    }

    @Override
    public void collectFunctions(List<Expression> functions) {

    }
}
