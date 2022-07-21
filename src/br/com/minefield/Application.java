package br.com.minefield;

import br.com.minefield.model.Board;
import br.com.minefield.view.BoardConsole;

public class Application {

    public static void main(String[] args) {

        Board board = new Board(6, 6, 6);
        BoardConsole boardConsole = new BoardConsole(board);

    }
}
