package com.ashwanigarg.chess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
class Game2{
    @RequestMapping(method=RequestMethod.GET, value = "/")
    public String Info() {
        return "HomePage";
    }
}

@RestController
class Game {
    /**
     * Game Class holds rest mappings exposed for interacting with server
     */
    Board chessBoard;
    Player playerOnBlack;
    Player playerOnWhite;

    @PostMapping(value = "/", consumes = "text/plain", produces = "text/plain")
    String Play(@RequestBody String body) {
        boolean kCastling = false;
        boolean qCastling = false;
        Player currentPlayer = playerOnBlack;

        // Controlling move and chessboard strategy as per body
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

        //Checking if game has been started
        if (chessBoard == null || chessBoard.isWin) {
            return "Kindly Start Game!";
        }

        if (chessBoard.whiteTurn)
            currentPlayer = playerOnWhite;

        // valid boolean holds value for move
        boolean valid;

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
            char ambiguityResolveChar = ' ';

            // Parsing each character of move as per notations of chess
            // as mentioned on https://www.ichess.net/blog/chess-notation/
            for (int i = 0; i < body.length(); i++) {
                char character = body.charAt(i);

                if (Character.isUpperCase(character)) {
                    // Upper case character can be perceived as pieces/characters on chess board
                    if (pawnPromotion)
                        promotedPieceChar = character;
                    else
                        pieceChar = character;

                } else if (Character.isLowerCase(character)) {
                    // Lower case character can be perceived as file/x(graph) on board
                    if (xChar != ' ') {
                        ambiguityResolveChar = xChar;
                    }
                    if (character == 'x') {
                        capture = true;
                    } else {
                        xChar = character;
                    }
                } else if (Character.isDigit(character)) {
                    // Digits notate ranks/y(graph) on a board
                    y = Character.getNumericValue(character);
                } else if (character == '+') {
                    // Positive sign notates a check
                    check = true;
                } else if (character == '=') {
                    // Equals to sign notates to promote a pawn
                    pawnPromotion = true;
                }
            }

            // Empty character for piece, notated piece to be a Pawn
            if (pieceChar == ' ')
                pieceChar = 'P';

            // Validating and making a move on chessboard as per request and player turn
            valid = chessBoard.newMove(pieceChar, capture, xChar, y, check, pawnPromotion, promotedPieceChar, ambiguityResolveChar);
        }
        if (chessBoard.isWin)
            return "Win";
        return valid ? "Valid" : "Invalid";
    }
}