package com.ashwanigarg.chess;

public class Pawn extends Piece{
    private boolean firstMove;

    public Pawn(boolean white)
    {
        super(white);
        firstMove = true;
    }

    @Override
    public char name(){
        return 'P';
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end){
        int moveX = end.getX() - start.getX();
        int moveY = end.getY() - start.getY();

        if (moveX != 0){
            return false;
        } else if (moveY != (isWhite()? 1:-1)) {
            if (firstMove && moveY == (isWhite()? 2:-2))
                return true;
            return false;
        }

        firstMove = false;
        return true;
    }
}