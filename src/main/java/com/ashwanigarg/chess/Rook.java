package com.ashwanigarg.chess;

public class Rook extends Piece {
    private boolean firstMove;
    public Rook(boolean white)
    {
        super(white);
        firstMove = true;
    }

    boolean canCastle(){
        return firstMove;
    }

    @Override
    public char name(){
        return 'R';
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end){
        int moveX = end.getX() - start.getX();
        int moveY = end.getY() - start.getY();

        if (moveX == 0){
            int initializer = moveY > 0 ? 1 : -1;
            for(int i = initializer; i < moveY; i++){
                if (board.board[start.getX()][start.getY() + i].getPiece() != null)
                    return false;
            }
        } else if (moveY == 0){
            int initializer = moveX > 0 ? 1 : -1;
            for(int i = initializer; i < moveX; i++){
                if (board.board[start.getX() + i][start.getY()].getPiece() != null)
                    return false;
            }
        } else {
            return false;
        }

        firstMove = false;
        return true;
    }
}