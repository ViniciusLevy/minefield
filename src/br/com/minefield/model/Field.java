package br.com.minefield.model;

import br.com.minefield.exception.ExplosionException;

import java.util.ArrayList;
import java.util.List;

public class Field {

    private final int line;
    private final int column;

    private boolean opened;
    private boolean mined;
    private boolean marked;

    private List<Field> neighbors = new ArrayList<>();

    public Field(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public boolean addNeighbor(Field neighbor) {
        boolean differentLine = line != neighbor.line;
        boolean differentColumn = column != neighbor.column;
        boolean diagonal = differentLine && differentColumn;

        int lineDelta = Math.abs(line - neighbor.line);
        int columnDelta = Math.abs(column - neighbor.column);
        int totalDelta = lineDelta + columnDelta;

        if(totalDelta == 1 && !diagonal) {
            neighbors.add(neighbor);
            return true;
        } else if (totalDelta == 2 && diagonal) {
            neighbors.add(neighbor);
            return true;
        } else {
            return false;
        }
    }

    public void switchMarker() {
        if(!opened) {
            marked = !marked;
        }
    }

    public boolean open() {

        if(!opened && !marked) {
            opened = true;

            if(mined) {
                throw new ExplosionException();
            }

            if(safeNeighbors()) {
                neighbors.forEach(n -> n.open());
            }

            return true;
        } else {
            return false;
        }
    }

    public boolean safeNeighbors() {
        return neighbors.stream()
                .noneMatch(n -> n.mined);
    }

    public void addMine() {
        if (!mined) {
            mined = true;
        }
    }

    public boolean isMined() {
        return mined;
    }
    public boolean isMarked() {
        return marked;
    }

    public void setOpened(boolean opened) {
        this.opened = opened;
    }

    public boolean isOpened() {
        return opened;
    }

    public boolean isClosed() {
        return !isOpened();
    }

    public int getLine() {
        return line;
    }

    public int getColumn() {
        return column;
    }

    public boolean goalAchieved() {
        boolean unraveled = !mined && opened;
        boolean secure = mined && marked;
        return unraveled || secure;
    }

    public long minesAtNeighborhood() {
        return neighbors.stream().filter(n -> n.mined).count();
    }

    public void resetGame() {
        opened = false;
        mined = false;
        marked = false;
    }

    @Override
    public String toString() {
        if (marked) {
            return "x";
        } else if (opened && mined) {
            return "*";
        } else if (opened && minesAtNeighborhood() > 0) {
            return Long.toString(minesAtNeighborhood());
        } else if (opened) {
            return " ";
        } else {
            return "?";
        }
    }
}
