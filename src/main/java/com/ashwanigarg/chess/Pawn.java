package com.ashwanigarg.chess;

public class Pawn extends Piece{

    public Pawn(boolean white)
    {
        super(white);
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
            return false;
        }
        return true;
    }
}