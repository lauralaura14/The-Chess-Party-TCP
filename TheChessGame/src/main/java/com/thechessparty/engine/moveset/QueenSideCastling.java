package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public class QueenSideCastling extends Castling {
    QueenSideCastling(final GameBoard board, final Piece piece, final int destination) {
        super(board, piece, destination);
    }
}
