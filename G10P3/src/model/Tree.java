package model;

import model.symbol.Expression;
import model.symbol.functions.*;
import model.symbol.terminals.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree {
    public Expression root;

    private int maxDepth;
    private int rows;
    private int columns;
    private int nodes;

    private static final Random random = new Random();

    public Tree(int maxDepth, int rows, int columns) {
        this.maxDepth = maxDepth;
        this.rows = rows;
        this.columns = columns;
    }

    // Método para crear un árbol con inicialización Grow
    public void createGrowTree(int maxDepth) {
        this.root = growExpression(0, maxDepth);
        this.nodes = root.countNodes();
    }

    // Método para crear un árbol con inicialización Full
    public void createFullTree(int maxDepth) {
        this.root = fullExpression(0, maxDepth);
        this.nodes = root.countNodes();
    }

    // Método auxiliar para crear una expresión utilizando el método Grow
    private Expression growExpression(int depth, int maxDepth) {
        // Si estamos en la profundidad máxima o aleatoriamente decidimos usar un terminal
        if (depth >= maxDepth || (depth > 0 && random.nextDouble() < 0.5)) {
            // Crear nodo terminal aleatorio
            return createRandomTerminal();
        } else {
            // Crear nodo función aleatorio
            return createRandomFunction(depth, maxDepth);
        }
    }

    // Método auxiliar para crear una expresión utilizando el método Full
    private Expression fullExpression(int depth, int maxDepth) {
        if (depth >= maxDepth) {
            // En la profundidad máxima, crear nodo terminal
            return createRandomTerminal();
        } else {
            // Crear nodo función
            return createRandomFunction(depth, maxDepth);
        }
    }

    private Expression createRandomTerminal() {
        int choice = random.nextInt(3);
        switch (choice) {
            case 0: return new Advance();
            case 1: return new Right();
            case 2: return new Left();
            default: return new Advance();
        }
    }

    private Expression createRandomFunction(int depth, int maxDepth) {
        int choice = random.nextInt(3);
        Expression function;

        switch (choice) {
            case 0:
                function = new IfFood();
                function.addChild(growExpression(depth + 1, maxDepth));
                function.addChild(growExpression(depth + 1, maxDepth));
                break;
            case 1:
                function = new Prog2();
                function.addChild(growExpression(depth + 1, maxDepth));
                function.addChild(growExpression(depth + 1, maxDepth));
                break;
            case 2:
                function = new Prog3();
                function.addChild(growExpression(depth + 1, maxDepth));
                function.addChild(growExpression(depth + 1, maxDepth));
                function.addChild(growExpression(depth + 1, maxDepth));
                break;
            default:
                function = new Prog2();
                function.addChild(growExpression(depth + 1, maxDepth));
                function.addChild(growExpression(depth + 1, maxDepth));
        }

        return function;
    }

    // Método para implementar la inicialización "Ramped and Half"
    public void createRampedHalfAndHalfTree(int maxDepth) {
        boolean useGrow = random.nextBoolean();

        if (useGrow) {
            this.root = growExpression(0, maxDepth);
        } else {
            this.root = fullExpression(0, maxDepth);
        }

        this.nodes = root.countNodes();
    }

    // Método para obtener un nodo aleatorio del árbol
    public Expression getRandomNode() {
        List<Expression> allNodes = new ArrayList<>();
        collectNodes(root, allNodes);

        int index = random.nextInt(allNodes.size());
        return allNodes.get(index);
    }

    // Método auxiliar para recolectar todos los nodos del árbol
    private void collectNodes(Expression node, List<Expression> nodes) {
        if (node != null) {
            nodes.add(node);
            for (Expression child : node.getChildren()) {
                collectNodes(child, nodes);
            }
        }
    }

    // Método para reemplazar un subárbol
    public void replaceSubtree(Expression target, Expression replacement) {
        if (root == target) {
            root = replacement;
            return;
        }

        replaceInChildren(root, target, replacement);
    }

    private boolean replaceInChildren(Expression parent, Expression target, Expression replacement) {
        for (int i = 0; i < parent.getChildren().size(); i++) {
            Expression child = parent.getChildren().get(i);
            if (child == target) {
                parent.getChildren().set(i, replacement);
                return true;
            }
            if (replaceInChildren(child, target, replacement)) {
                return true;
            }
        }
        return false;
    }

    // Método para calcular la profundidad de un nodo
    public int getNodeDepth(Expression node) {
        return calculateNodeDepth(root, node, 0);
    }

    private int calculateNodeDepth(Expression current, Expression target, int depth) {
        if (current == target) {
            return depth;
        }

        for (Expression child : current.getChildren()) {
            int childDepth = calculateNodeDepth(child, target, depth + 1);
            if (childDepth > -1) {
                return childDepth;
            }
        }

        return -1;
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getColumns() {
        return columns;
    }

    public void setColumns(int columns) {
        this.columns = columns;
    }

    public int getNodes() {
        return nodes;
    }

    public Expression getRoot() {
        return root;
    }

    public Tree copy() {
        Tree newTree = new Tree(this.maxDepth, this.rows, this.columns);
        newTree.root = this.root.copy();
        newTree.nodes = this.nodes;
        return newTree;
    }

    @Override
    public String toString() {
        if (root == null) {
            return "Empty Tree";
        }
        return root.toString();
    }
}