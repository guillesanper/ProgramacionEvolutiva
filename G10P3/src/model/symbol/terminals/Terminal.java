package model.symbol.terminals;

import model.symbol.Expression;

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
}
