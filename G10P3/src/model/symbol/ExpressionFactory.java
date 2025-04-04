package model.symbol;

import model.symbol.functions.IfFood;
import model.symbol.functions.Prog2;
import model.symbol.functions.Prog3;
import model.symbol.terminals.Advance;
import model.symbol.terminals.Left;
import model.symbol.terminals.Right;

import java.util.List;
import java.util.Random;

public class ExpressionFactory {
    private static final List<Expression> terminals = List.of(new Advance(), new Left(), new Right());
    public static Expression createIfFood(Expression a, Expression b) {
        return new IfFood(a, b);
    }

    public static Expression createProg2(Expression a, Expression b) {
        return new Prog2(a, b);
    }

    public static Expression createProg3(Expression a, Expression b, Expression c) {
        return new Prog3(a, b, c);
    }

    public static Expression createRandomTerminal() {
        int choice = new Random().nextInt(terminals.size());

        return terminals.get(choice).copy();
    }
}
