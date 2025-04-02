package model.symbol.terminals;

import model.symbol.Expression;

public class Advance extends Terminal {
    public Advance() {
        super("Advance");
    }

    public Expression clone() {
        return new Advance();
    }
}
