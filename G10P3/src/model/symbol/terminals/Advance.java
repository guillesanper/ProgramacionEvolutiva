package model.symbol.terminals;

import model.symbol.Expression;

public class Advance extends Terminal {
    public static final String OP = "ADVANCE";

    public Advance() {
        super(Advance.OP);
    }

    @Override
    public Expression clone() {
        return new Advance();
    }
}
