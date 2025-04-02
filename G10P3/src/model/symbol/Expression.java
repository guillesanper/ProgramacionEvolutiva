package model.symbol;

import java.util.List;

public abstract class Expression implements Cloneable {

    protected int x;
    protected int y;
    protected String operation;
    protected List<Expression> children;
    protected int childrenCount;

    public abstract Object execute();
    public abstract void setChild(int i, Expression ex);

    public abstract Expression getChild(int i);

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

    public void setChildrenCount(int childrenCount) {
        this.childrenCount = childrenCount;
    }
}
