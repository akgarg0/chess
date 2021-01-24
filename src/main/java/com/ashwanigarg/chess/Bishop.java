package com.ashwanigarg.chess;

public class Bishop extends Piece{
    public Bishop(boolean white)
    {
        super(white);
    }

    @Override
    public char name(){
        return 'B';
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end){
        int moveX = end.getX() - start.getX();
        int moveY = end.getY() - start.getY();

        if (moveX != moveY)
            return false;

        int initializer = moveX > 0 ? 1 : -1;

        for(int i = initializer; i < moveX; i++){
            if (board.board[start.getX() + i][start.getY() + i].getPiece() != null)
                return false;
        }

        return true;
    }
}
