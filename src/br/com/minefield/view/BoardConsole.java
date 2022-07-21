package br.com.minefield.view;

import br.com.minefield.exception.ExitException;
import br.com.minefield.exception.ExplosionException;
import br.com.minefield.model.Board;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Scanner;

public class BoardConsole {

    private Board board;
    private Scanner entry = new Scanner(System.in);

    public BoardConsole(Board board) {
        this.board = board;
        executeGame();
    }

    private void executeGame() {
        try{
            boolean resume = true;

            while(resume) {
                gameLoop();

                System.out.println("Another Game? (S/n) ");
                String answer = entry.nextLine();

                if ("n".equalsIgnoreCase(answer)) {
                    resume = false;
                } else {
                    board.resetGame();
                }
            }
        }catch (ExitException e) {
            System.out.println("Byeee!!!");
        } finally {
            entry.close();
        }
    }

    private void gameLoop() {
        try {
            while(!board.goalAchieved()) {
                System.out.println(board);
                String typed = scanTyping("Type (x, y): ");

                Iterator<Integer> xy = Arrays.stream(typed.split(","))
                        .map(e -> Integer.parseInt(e.trim())).iterator();

                typed = scanTyping("1 - to Open or 2 - to (un)Mark");
                
                if ("1".equals(typed)) {
                    board.open(xy.next(), xy.next());
                } else if ("2".equals(typed)) {
                    board.switchMarker(xy.next(), xy.next());
                }
            }

            System.out.println(board);
            System.out.println("You Win!!!");
        } catch(ExplosionException e) {
            System.out.println(board);
            System.out.println("You Lose!!!");
        }
    }

    private String scanTyping(String text) {
        System.out.println(text);
        String typed = entry.nextLine();

        if ("exit".equalsIgnoreCase(typed)) {
            throw new ExitException();
        }
        return typed;
    }

}
