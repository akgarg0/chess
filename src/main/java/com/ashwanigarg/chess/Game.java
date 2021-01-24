package com.ashwanigarg.chess;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
class Game {
    Board chessBoard;

    @GetMapping("/")
    String Info() {
        return "Would you like to have a game of CHESS";
    }

    @PostMapping(value = "/", consumes = "text/plain", produces = "text/plain")
    String Play(@RequestBody String body) {
        boolean kCastling = false;
        boolean qCastling = false;

        switch (body) {
            case "START":
                chessBoard = new Board();
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
                if (pawnPromotion) {
                    promotedPieceChar = character;
                } else {
                    pieceChar = character;
                }
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

        if (pieceChar == ' ') {
            pieceChar = 'P';
        }

        boolean valid = chessBoard.newMove(pieceChar, capture, xChar, y, check, pawnPromotion, promotedPieceChar, ambiquityResolveChar);

        return valid ? "Valid" : "Invalid";
    }
}