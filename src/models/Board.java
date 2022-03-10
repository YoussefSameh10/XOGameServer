/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;

import static models.Mark.*;

/**
 *
 * @author amin
 */
enum BoardStatus {
    NotDetermined,
    Row0,
    Row1,
    Row2,
    Col0,
    Col1,
    Col2,
    DiagonalTopLeftToBottomRight,
    DiagonalTopRightToBottomLeft,
    Tie
}

public class Board {

    private final Mark[][] board;
    private Mark winningMark;
    private final int BOARD_WIDTH = 3;
    private boolean crossTurn, gameOver;
    private int availableMoves = BOARD_WIDTH * BOARD_WIDTH;
    private BoardStatus status =  BoardStatus.NotDetermined  ; 

    public Board() {
        board = new Mark[BOARD_WIDTH][BOARD_WIDTH];
        crossTurn = true;
        gameOver = false;
        winningMark = BLANK;
        initialiseBoard();
    }

    private void initialiseBoard() {
        for (int row = 0; row < BOARD_WIDTH; row++) {
            for (int col = 0; col < BOARD_WIDTH; col++) {
                board[row][col] = BLANK;
            }
        }
    }

    /**
     * Attempt to mark tile at the given coordinates if they are valid and it is
     * possible to do so, toggle the player and check if the placing the mark
     * has resulted in a win.
     *
     * @param row Row coordinate to attempt to mark
     * @param col Column coordinate to attempt to mark
     * @return true if mark was placed successfully
     */
    public boolean placeMark(int row, int col) {
        if (row < 0 || row >= BOARD_WIDTH || col < 0 || col >= BOARD_WIDTH
                || isTileMarked(row, col) || gameOver) {
            return false;
        }
        availableMoves--;
        board[row][col] = crossTurn ? X : O;
        togglePlayer();
        checkWin(row, col);
        return true;
    }

    /**
     * Check row and column provided and diagonals for win.
     *
     * @param row Row to check
     * @param col Column to check
     */
    private void checkWin(int row, int col) {
        int rowSum = 0;
        // Check row for winner.
        for (int c = 0; c < BOARD_WIDTH; c++) {
            rowSum += getMarkAt(row, c).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on row " + row);
            switch (row) {
                case 0 :
                    status = BoardStatus.Row0;
                    break ;
                case 1 :
                      status = BoardStatus.Row1;
                    break ;
                case 2:
                      status = BoardStatus.Row2;
                    break ;
            }
            return;
        }

        // Check column for winner.
        rowSum = 0;
        for (int r = 0; r < BOARD_WIDTH; r++) {
            rowSum += getMarkAt(r, col).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on column " + col);
                  switch (col) {
                case 0 :
                    status = BoardStatus.Col0;
                    break ;
                case 1 :
                      status = BoardStatus.Col1;
                    break ;
                case 2:
                      status = BoardStatus.Col2;
                    break ;
            }
            return;
        }

        // Top-left to bottom-right diagonal.
        rowSum = 0;
        for (int i = 0; i < BOARD_WIDTH; i++) {
            rowSum += getMarkAt(i, i).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-left to "
                    + "bottom-right diagonal");
            status =   BoardStatus.DiagonalTopLeftToBottomRight ;
            return;
        }

        // Top-right to bottom-left diagonal.
        rowSum = 0;
        int indexMax = BOARD_WIDTH - 1;
        for (int i = 0; i <= indexMax; i++) {
            rowSum += getMarkAt(i, indexMax - i).getMark();
        }
        if (calcWinner(rowSum) != BLANK) {
            System.out.println(winningMark + " wins on the top-right to "
                    + "bottom-left diagonal.");
            status = BoardStatus.DiagonalTopRightToBottomLeft ;
            return;
        }

        if (!anyMovesAvailable()) {
            gameOver = true;
            status = BoardStatus.Tie ;
            System.out.println("Tie!");
        }
    }

    /**
     * Calculates if provided ASCII sum equates to a win for X or O.
     *
     * @param rowSum Sum of characters' ASCII values in a row
     * @return Mark indicating which player won or a space character if neither
     */
    private Mark calcWinner(int rowSum) {
        int Xwin = X.getMark() * BOARD_WIDTH;
        int Owin = O.getMark() * BOARD_WIDTH;
        if (rowSum == Xwin) {
            gameOver = true;
            winningMark = X;
            return X;
        } else if (rowSum == Owin) {
            gameOver = true;
            winningMark = O;
            return O;
        }
        return BLANK;
    }

    private void togglePlayer() {
        crossTurn = !crossTurn;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean anyMovesAvailable() {
        return availableMoves > 0;
    }

    public Mark getMarkAt(int row, int column) {
        return board[row][column];
    }

    public boolean isTileMarked(int row, int column) {
        return board[row][column].isMarked();
    }

    public void setMarkAt(int row, int column, Mark newMark) {
        board[row][column] = newMark;
    }

    @Override
    public String toString() {
        StringBuilder strBldr = new StringBuilder();
        for (Mark[] row : board) {
            for (Mark tile : row) {
                strBldr.append(tile).append(' ');
            }
            strBldr.append("\n");
        }
        return strBldr.toString();
    }

    public boolean isCrossTurn() {
        return crossTurn;
    }

    public int getWidth() {
        return BOARD_WIDTH;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Mark getWinningMark() {
        return winningMark;
    }

    public void printGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                System.out.print(board[i][j].getMark() + "   ");
            }
            System.out.println("\n");
        }
    }
    
      public BoardStatus getBoardStatus(){
        return status ;
    }
    
}
