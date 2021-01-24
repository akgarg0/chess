package com.ashwanigarg.chess;

import java.util.stream.IntStream;

public class Board {
    Spot[][] board;
    boolean whiteTurn;

    public static Piece piece(char name, boolean white){
        switch (name){
            case 'K':
                return new King(white);
            case 'Q':
                return new Queen(white);
            case 'B':
                return new Bishop(white);
            case 'N':
                return new Knight(white);
            case 'R':
                return new Rook(white);
            case 'P':
                return new Pawn(white);

        }
        return null;
    }

    public static Piece initPiece(int x,int y){
        boolean white = false;
        boolean pawnRow = false;
        switch (y){
            case 1:
                white = true;
                break;

            case 2:
                white = true;
                pawnRow = true;
                break;

            case 7:
                pawnRow = true;
                break;

            case 8:
                break;

            default:
                return null;
        }

        if (pawnRow) {
            return piece('P', white);
        }

        switch (x){
            case 1:
            case 8:
                return piece('R', white);
            case 2:
            case 7:
                return piece('N', white);
            case 3:
            case 6:
                return piece('B', white);
            case 4:
                return piece('Q', white);
            case 5:
                return piece('K', white);
        }
        return null;
    }

    Board(){
        whiteTurn = true;
        board = new Spot[8][8];
        for(int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Piece piece = initPiece(i + 1, j + 1);
                board[i][j] = new Spot(i + 1, j + 1, piece);
            }
        }
    }

    public Piece getPiece(int x ,int y){
        return board[x][y].getPiece();
    }

    public Spot getSpot(boolean white, char pieceChar, int ambiguityResolverPositionX){
        for (int i = 0 ; i < 8; i++) {
            if (ambiguityResolverPositionX != 0){
                i = ambiguityResolverPositionX - 1;
            }
            for (int j = 0 ; j < 8; j++) {
                if (board[i][j].getPiece().name() == pieceChar && board[i][j].getPiece().isWhite() == white){
                    return board[i][j];
                }
            }
            if (ambiguityResolverPositionX != 0){
               break;
            }
        }
        return null;
    }

    boolean newMove(final int pieceChar, boolean capture, char xChar, int y, boolean check, boolean pawnPromotion, char promotedPieceChar, char ambiguityResolveChar ){
        int[] pieceList = {'K','Q','B','N','R','P'};

        if (!IntStream.of(pieceList).anyMatch(x -> x == pieceChar)){
            return false;
        }

        int x = xChar - 96;
        if (x < 1 || x > 8 ){
            return false;
        }
        if (y < 1 || y > 8 ){
            return false;
        }


        if (promotedPieceChar != ' ' && (!pieceList.toString().contains(Character.toString(promotedPieceChar)))){
            return false;
        }

        int ambiguityResolverPositionX = ambiguityResolveChar - 96;
        if (ambiguityResolverPositionX < 1 || ambiguityResolverPositionX > 8 ){
            ambiguityResolverPositionX = 0;
//            return false;
        }

        Spot startSpot = getSpot(whiteTurn, (char)pieceChar, ambiguityResolverPositionX);

        if (startSpot == null){
            return false;
        }

        Spot endSpot = board[x-1][y-1];
        if (endSpot.getPiece() != null){
            if (endSpot.getPiece().isWhite() == whiteTurn){
                return false;
            }
            if (!capture) {
                return false;
            }
        }else {
            if (capture){
                return false;
            }
        }

        if(pawnPromotion){
            if (y !=8 || pieceChar != 'P'){
                return false;
            }
        }

        if (!startSpot.getPiece().canMove(this, startSpot, endSpot)){
            return false;
        }

        endSpot.setPiece(startSpot.getPiece());
        startSpot.setPiece(null);

        if (pawnPromotion){
            endSpot.setPiece(piece(promotedPieceChar, whiteTurn));
        }

        whiteTurn = whiteTurn ? false: true;

        return true;
    }
}
