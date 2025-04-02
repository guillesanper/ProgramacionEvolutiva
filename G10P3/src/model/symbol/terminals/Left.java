package model.symbol.terminals;

import model.symbol.Expression;

public class Left extends Terminal {
    public Left() {
        super("Left");
    }

    public Expression clone() {
        return new Left();
    }
}
