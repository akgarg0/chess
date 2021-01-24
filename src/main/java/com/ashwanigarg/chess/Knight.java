package com.ashwanigarg.chess;

public class Knight extends Piece{
    public Knight(boolean white)
    {
        super(white);
    }

    @Override
    public char name(){
        return 'N';
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end){
        int moveX = Math.abs(end.getX() - start.getX());
        int moveY = Math.abs(end.getY() - start.getY());

        if (Math.max(moveX, moveY) == 2 && Math.min(moveX, moveY) == 1){
            return true;
        }
        return false;
    }
}
