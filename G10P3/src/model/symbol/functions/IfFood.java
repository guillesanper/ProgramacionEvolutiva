package model.symbol.functions;

import model.symbol.Expression;

public class IfFood extends Expression {
    public IfFood() {
        this.childrenCount = 2;
        this.children = new Expression[this.childrenCount];
    }


    @Override
    public void setChild(int i, Expression ex) {
        this.children[i] = ex;
    }

    @Override
    public Expression getChild(int i) {
        return null;
    }

    public Expression clone() {
        return new IfFood();
    }
}
