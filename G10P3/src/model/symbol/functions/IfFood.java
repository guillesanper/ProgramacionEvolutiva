package model.symbol.functions;

import model.symbol.Expression;

public class IfFood extends Expression {
    public IfFood() {
        super();
        this.childrenCount = 2;
        this.operation = "SICOMIDA";
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
}
