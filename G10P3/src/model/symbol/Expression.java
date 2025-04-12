package model.symbol;

import java.util.ArrayList;
import java.util.List;

public abstract class Expression {
    protected int x;
    protected int y;
    protected int childrenCount;
    protected String operation;
    protected List<Expression> children;
    protected int height;

    public Expression() {
        this.children = new ArrayList<>();
        this.height = 1;
    }

    public Expression(String operation) {
        this();
        this.operation = operation;
    }

    public abstract Object execute(boolean isThereFood);

    public void addChild(Expression child) {
        this.children.add(child);
        if (this.height == 0) this.height++;
    }

    public Expression getChild(int index) {
        if (index < children.size()) {
            return children.get(index);
        }
        return null;
    }

    public List<Expression> getChildren() {
        return children;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public int getHeight() {
        return this.height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getChildrenCount() {
        return childrenCount;
    }

    public int countNodes() {
        int count = 1; // Contar este nodo
        for (Expression child : children) {
            count += child.countNodes();
        }
        return count;
    }

    public Expression copy() {
        try {
            Expression copy = this.getClass().getDeclaredConstructor().newInstance();
            copy.setX(this.x);
            copy.setY(this.y);
            copy.childrenCount = this.childrenCount;
            copy.setOperation(this.operation);

            for (Expression child : children) {
                copy.addChild(child.copy());
            }

            return copy;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("(").append(operation);

        for (Expression child : children) {
            sb.append(" ").append(child.toString());
        }

        sb.append(")");
        return sb.toString();
    }

    public void collectTerminals(List<Expression> terminals) {
        for (Expression child : this.children) child.collectTerminals(terminals);
    }

    public void collectFunctions(List<Expression> functions) {
        functions.add(this);
        for (Expression child : this.children) child.collectFunctions(functions);
    }

    public abstract void mutate();

    public Expression findParent(Expression node) {
        if (this.childrenCount == 0) return null;

        for (Expression child : this.children) {
            if (child == node) return this;
        }

        Expression ret = null;

        for (Expression child: this.children) {
            if (ret != null) return ret;
            ret = child.findParent(node);
        }

        return ret;
    }

    public void changeChild(Expression original, Expression replacement) {
        for (int i = 0; i < this.getChildrenCount(); i++) {
            if (this.children.get(i) == original) {
                this.children.set(i, replacement);
                return;
            }
        }
    }
}