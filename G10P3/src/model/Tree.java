package model;

import model.symbol.Expression;
import model.symbol.ExpressionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Tree implements Cloneable{
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

    // Métodos auxiliares para los inicializadores externos
    public Expression growExpression(int depth, int maxDepth) {
        // Si estamos en la profundidad máxima o aleatoriamente decidimos usar un terminal
        if (depth >= maxDepth || (depth > 0 && random.nextDouble() < 0.5)) {
            // Crear nodo terminal aleatorio
            return createRandomTerminal();
        } else {
            // Crear nodo función aleatorio
            return createRandomFunction(depth, maxDepth);
        }
    }

    public Expression fullExpression(int depth, int maxDepth) {
        if (depth >= maxDepth) {
            // En la profundidad máxima, crear nodo terminal
            return createRandomTerminal();
        } else {
            // Crear nodo función
            return createRandomFunction(depth, maxDepth);
        }
    }

    private Expression createRandomTerminal() {
        return ExpressionFactory.createRandomTerminal();
    }

    private Expression createRandomFunction(int depth, int maxDepth) {
        int choice = random.nextInt(3);
        Expression a;
        Expression b;

        switch (choice) {
            case 0:
                a = growExpression(depth + 1, maxDepth);
                b = growExpression(depth + 1, maxDepth);
                return ExpressionFactory.createIfFood(a, b);
            case 1:
                a = growExpression(depth + 1, maxDepth);
                b = growExpression(depth + 1, maxDepth);
                return ExpressionFactory.createProg2(a, b);
            case 2:
                a = growExpression(depth + 1, maxDepth);
                b = growExpression(depth + 1, maxDepth);
                Expression c = growExpression(depth + 1, maxDepth);
                return ExpressionFactory.createProg3(a, b, c);
            default:
                a = growExpression(depth + 1, maxDepth);
                b = growExpression(depth + 1, maxDepth);
                return ExpressionFactory.createProg2(a, b);
        }
    }

    // Nuevo método para establecer directamente la raíz y actualizar nodos
    public void setRoot(Expression root) {
        this.root = root;
        this.nodes = root.countNodes();
    }

    // Método para obtener un nodo aleatorio del árbol
    public Expression getRandomNode() {
        List<Expression> allNodes = new ArrayList<>();
        collectNodes(root, allNodes);

        int index = random.nextInt(allNodes.size());
        return allNodes.get(index);
    }

    public Expression getRandomSubtree() {
        List<Expression> allNodes = new ArrayList<>();
        for (Expression child : root.getChildren()) collectNodes(child, allNodes);

        int index = random.nextInt(allNodes.size());
        return allNodes.get(index);
    }


    public Expression findParent(Expression node) {
        if (node == this.root) return this.root;

        return this.root.findParent(node);
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

    // Método auxiliar para recolectar todos los nodos terminales del árbol
    public void collectTerminals(List<Expression> terminals) {
        this.root.collectTerminals(terminals);
    }

    // Método auxiliar para recolectar todos los nodos terminales del árbol
    public void collectFunctions(List<Expression> functions) {
        this.root.collectFunctions(functions);
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

    public int getNumberOfNodes() {
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

    @Override
    public Object clone() throws CloneNotSupportedException {
        Tree clonedTree = (Tree) super.clone();
        clonedTree.root = this.root.copy();
        return clonedTree;
    }
}