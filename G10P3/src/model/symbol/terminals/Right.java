package model.symbol.terminals;

import model.symbol.Expression;

public class Right extends Terminal {
    public static final String OP = "RIGHT";

    public Right() {
        super(Right.OP);
    }

    @Override
    public Expression clone() {
        return new Right();
    }
}
