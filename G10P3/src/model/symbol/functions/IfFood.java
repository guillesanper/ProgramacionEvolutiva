package model.symbol.functions;

import model.symbol.Expression;

import java.util.ArrayList;

public class IfFood extends Expression {
    public IfFood() {
        this.childrenCount = 2;
        this.children = new ArrayList<>();
    }

    @Override
    public Object execute(boolean hayComida) {
        return null;
    }

    @Override
    public Expression getChild(int i) {
        return null;
    }

    public Expression clone() {
        return new IfFood();
    }
}
