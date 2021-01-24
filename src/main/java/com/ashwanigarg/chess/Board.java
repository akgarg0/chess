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

    static int fileToNumber(char file){
        int number = file - 96;
        if (number < 1 || number > 8 ){
            return 0;
        }
        return number;
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

    boolean newMove(char pieceChar, boolean capture, char xChar, int y, boolean check, boolean pawnPromotion, char promotedPieceChar, char ambiguityResolveChar ){
        int[] pieceList = {'K','Q','B','N','R','P'};

        if (!IntStream.of(pieceList).anyMatch(x -> x == pieceChar)){
            return false;
        }

        int x = fileToNumber(xChar);
        if (x == 0){
            return false;
        }
        if (y < 1 || y > 8 ){
            return false;
        }

        if (promotedPieceChar != ' ' && (!IntStream.of(pieceList).anyMatch(z -> z == promotedPieceChar)) || promotedPieceChar == 'K'){
            return false;
        }

        int ambiguityResolverPositionX = fileToNumber(xChar);

        if(pawnPromotion){
            if (y !=8 || pieceChar != 'P'){
                return false;
            }
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

        Spot startSpot = board[0][0];
        boolean canMove = false;

        for (int i = 0 ; i < 8; i++) {
            if (ambiguityResolverPositionX != 0){
                i = ambiguityResolverPositionX - 1;
            }
            for (int j = 0 ; j < 8; j++) {
                if ( board[i][j].getPiece()!=null && board[i][j].getPiece().name() == pieceChar && board[i][j].getPiece().isWhite() == whiteTurn){
                    startSpot = board[i][j];
                    if (startSpot.getPiece().canMove(this, startSpot, endSpot)){
                        canMove = true;
                        break;
                    }
                }
            }
            if (ambiguityResolverPositionX != 0){
                break;
            }
        }

        if (!canMove){
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
