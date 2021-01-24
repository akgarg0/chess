package com.ashwanigarg.chess;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Game {
    Board chessBoard;
    Player playerOnBlack;
    Player playerOnWhite;

    @GetMapping("/")
    String Info() {
        return "Would you like to have a game of CHESS.\n" +
                "Application: https://ak-chess.herokuapp.com/\n" +
                "Repository: https://github.com/akgarg0/chess\n" +
                "Online Resume: https://www.ashwanigarg.com\n" +
                "*Note:*\n" +
                "*1. First Request to Application may take some time.*\n" +
                "*2. All the requests are required to sent on 'post' method with body as 'plain text'*\n";

    }

    @PostMapping(value = "/", consumes = "text/plain", produces = "text/plain")
    String Play(@RequestBody String body) {
        boolean kCastling = false;
        boolean qCastling = false;
        Player currentPlayer = playerOnBlack;

        switch (body) {
            case "START":
                chessBoard = new Board();
                playerOnWhite = new Player(chessBoard, true);
                playerOnBlack = new Player(chessBoard, false);
                return "READY";
            case "0-0":
                kCastling = true;
                break;
            case "0-0-0":
                qCastling = true;
                break;
        }

        if (chessBoard == null) {
            return "Kindly Start Game!";
        }

        if (chessBoard.whiteTurn)
            currentPlayer = playerOnWhite;

        boolean valid = false;

        if (kCastling){
            valid = chessBoard.kingSideCastling();
        } else if (qCastling) {
            valid = chessBoard.queenSideCastling();
        } else {

            char pieceChar = ' ';
            boolean capture = false;
            char xChar = ' ';
            int y = 0;
            boolean check = false;
            boolean pawnPromotion = false;
            char promotedPieceChar = ' ';
            char ambiquityResolveChar = ' ';

            for (int i = 0; i < body.length(); i++) {
                char character = body.charAt(i);
                if (Character.isUpperCase(character)) {
                    if (pawnPromotion)
                        promotedPieceChar = character;
                    else
                        pieceChar = character;

                } else if (Character.isLowerCase(character)) {
                    if (xChar != ' ') {
                        ambiquityResolveChar = xChar;
                    }
                    if (character == 'x') {
                        capture = true;
                    } else {
                        xChar = character;
                    }
                } else if (Character.isDigit(character)) {
                    y = Character.getNumericValue(character);
                } else if (character == '+') {
                    check = true;
                } else if (character == '=') {
                    pawnPromotion = true;
                }
            }

            if (pieceChar == ' ')
                pieceChar = 'P';

            valid = chessBoard.newMove(pieceChar, capture, xChar, y, check, pawnPromotion, promotedPieceChar, ambiquityResolveChar);
        }
        return valid ? "Valid" : "Invalid";
    }
}