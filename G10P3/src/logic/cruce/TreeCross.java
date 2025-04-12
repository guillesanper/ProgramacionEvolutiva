package logic.cruce;

import model.Tree;
import model.symbol.Expression;

public class TreeCross implements Cruce<Tree> {
    @Override
    public void cross(Tree c1, Tree c2) {
        // arbol random c1
        Expression st1 = c1.getRandomNode();
        Expression parent1 = c1.findParent(st1);

        // arbol random c2
        Expression st2 = c2.getRandomNode();
        Expression parent2 = c2.findParent(st2);

        boolean c1Changed = false;
        boolean c2Changed = false;
        // st1 es root de su arbol
        if (parent1 == st1) {
            c1.root = st2;
            c1Changed = true;
        }

        if (parent2 == st2) {
            c2.root = st1;
            c2Changed = true;
        }

        if (!c1Changed) parent1.changeChild(st1, st2);
        if (!c2Changed) parent2.changeChild(st2, st1);
    }
}
