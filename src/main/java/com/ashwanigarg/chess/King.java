package com.ashwanigarg.chess;


public class King extends Piece {
    public King(boolean white)
    {
        super(white);
    }

    @Override
    public char name() {
        return 'K';
    }

    @Override
    public boolean canMove(Board board, Spot start, Spot end)
    {
        int x = Math.abs(start.getX() - end.getX());
        int y = Math.abs(start.getY() - end.getY());
        if (x + y == 1) {
            return true;
        }

        return false;
    }
}