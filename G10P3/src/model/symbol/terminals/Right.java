package model.symbol.terminals;

import model.symbol.Expression;

public class Right extends Terminal {
    public Right() {
        super("Right");
    }

    @Override
    public Expression clone() {
        return new Right();
    }
}
