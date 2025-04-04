package model.symbol.terminals;

import model.symbol.Expression;

public class Advance extends Terminal {
    public Advance() {
        super("Advance");
    }

    @Override
    public Expression clone() {
        return new Advance();
    }
}
