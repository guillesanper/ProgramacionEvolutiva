package model.symbol.functions;

import model.symbol.Expression;

public class Prog2 extends Expression {
    public Prog2() {
        super();
        this.childrenCount = 2;
    }

    public Prog2(Expression a, Expression b) {
        this();
        this.children.add(a);
        this.children.add(b);
    }

    @Override
    public Object execute(boolean isThereFood) {
        this.children.get(0).execute(isThereFood);
        return this.children.get(1).execute(isThereFood);
    }
}
