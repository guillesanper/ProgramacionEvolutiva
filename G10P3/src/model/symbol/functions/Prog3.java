package model.symbol.functions;

import model.symbol.Expression;

public class Prog3 extends Expression {
    public static final String OP = "PROG3";

    public Prog3() {
        super();
        this.childrenCount = 3;
        this.operation = Prog3.OP;
    }

    public Prog3(Expression a, Expression b, Expression c) {
        this();
        this.children.add(a);
        this.children.add(b);
        this.children.add(c);
    }

    @Override
    public Object execute(boolean isThereFood) {
        this.children.get(0).execute(isThereFood);
        this.children.get(1).execute(isThereFood);
        return this.children.get(2).execute(isThereFood);
    }

    @Override
    public void mutate() {

    }
}
