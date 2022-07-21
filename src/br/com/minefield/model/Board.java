package br.com.minefield.model;

import br.com.minefield.exception.ExplosionException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Board {

    private int lines;
    private int columns;
    private int mines;

    private final List<Field> fields = new ArrayList<>();

    public Board(int lines, int columns, int mines) {
        this.lines = lines;
        this.columns = columns;
        this.mines = mines;

        generateFields();
        associateNeighbors();
        sortMines();
    }

    public void open(int line, int column) {
        try{
            fields.parallelStream()
                    .filter(f -> f.getLine() == line && f.getColumn() == column)
                    .findFirst()
                    .ifPresent(Field::open);
        } catch (ExplosionException e) {
            fields.forEach(f -> f.setOpened(true));
            throw e;
        }
    }

    public void switchMarker(int line, int column) {
        fields.parallelStream()
                .filter(f -> f.getLine() == line && f.getColumn() == column)
                .findFirst()
                .ifPresent(Field::switchMarker);
    }

    private void generateFields() {
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < columns; j++) {
                fields.add(new Field(i, j));
            }
        }
    }
    private void associateNeighbors() {
        for (Field f1: fields) {
            for (Field f2: fields) {
                f1.addNeighbor(f2);
            }
        }
    }

    private void sortMines() {
        long armedMines = 0;
        Predicate<Field> mined = Field::isMined;

        do {
            int random = (int) (Math.random() * fields.size());
            armedMines = fields.stream()
                    .filter(mined)
                    .count();
            fields.get(random).addMine();
        } while(armedMines < mines);
    }

    public boolean goalAchieved() {
        return fields.stream().allMatch(Field::goalAchieved);
    }

    public void resetGame() {
        fields.forEach(Field::resetGame);
        sortMines();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

            sb.append("  ");
        for (int i = 0; i < columns; i++) {
            sb.append(" ");
            sb.append(i);
            sb.append(" ");
        }
        sb.append("\n");

        int aux = 0;

        for (int i = 0; i < lines; i++) {
            sb.append(i);
            sb.append(" ");
            for (int j = 0; j < columns; j++) {
                sb.append(" ");
                sb.append(fields.get(aux));
                sb.append(" ");
                aux++;
            }
            sb.append("\n");
        }
        
        return sb.toString();
    }

}
