package logic.cruce;

import model.Tree;
import model.symbol.Expression;

public class TreeCross implements Cruce<Tree> {
    @Override
    public void cross(Tree c1, Tree c2) {
        // arbol random c1
        Expression st1 = c1.getRandomSubtree();
        Expression parent1 = c1.findParent(st1);

        // arbol random c2
        Expression st2 = c2.getRandomSubtree();
        Expression parent2 = c2.findParent(st2);

        parent1.changeChild(st1, st2);
        parent2.changeChild(st2, st1);
    }
}
