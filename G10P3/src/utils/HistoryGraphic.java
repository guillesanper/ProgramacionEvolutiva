package utils;

import java.util.Stack;

public class HistoryGraphic {
    private HistoryState currState;
    private Stack<HistoryState> historyStack = new Stack<>();
    private Stack<HistoryState> redoStack = new Stack<>();

    public void saveState(HistoryState state) {
        if (currState != null) {
            historyStack.push(currState); // Guarda el estado actual antes de actualizarlo
        }
        currState = state; // Actualiza el estado actual
        redoStack.clear(); // Limpiar redo al guardar un nuevo estado // Al guardar un nuevo estado, se elimina la posibilidad de rehacer
    }

    public boolean undo() {
        if (!historyStack.isEmpty()) {
            redoStack.push(currState); // Guarda el estado actual en redoStack antes de cambiarlo
            currState = historyStack.pop(); // Recupera el estado anterior
            return true;
        }
        return false;
    }


    public boolean redo() {
        if (!redoStack.isEmpty()) {
            historyStack.push(currState); // Guarda el estado actual en historyStack antes de cambiarlo
            currState = redoStack.pop(); // Recupera el estado que se deshizo
            return true;
        }
        return false;
    }


    private void restoreState(HistoryState state) {
        this.currState = state;
    }

    public HistoryState getState(){
        return currState;
    }

}
