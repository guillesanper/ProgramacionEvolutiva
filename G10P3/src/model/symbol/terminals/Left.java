package model.symbol.terminals;

import model.symbol.Expression;

public class Left extends Terminal {
    public Left() {
        super("Left");
    }

    @Override
    public Expression clone() {
        return new Left();
    }
}
