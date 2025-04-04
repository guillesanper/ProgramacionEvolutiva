package model.symbol.functions;

import model.symbol.Expression;

public class IfFood extends Expression {
    public static final String OP = "SICOMIDA";

    public IfFood() {
        super();
        this.childrenCount = 2;
        this.operation = IfFood.OP;
    }

    public IfFood(Expression a, Expression b) {
        this();

        this.children.add(a);
        this.children.add(b);
    }

    @Override
    public Object execute(boolean isThereFood) {
        int child = isThereFood ? 0 : 1;
        return this.children.get(child).execute(isThereFood);
    }

    @Override
    public void mutate() {
        this.operation = Prog2.OP;
    }
}
