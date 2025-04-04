package model.symbol.terminals;

import model.symbol.Expression;

public class Left extends Terminal {
    public static final String OP = "LEFT";

    public Left() {
        super(Left.OP);
    }

    @Override
    public Expression clone() {
        return new Left();
    }
}
