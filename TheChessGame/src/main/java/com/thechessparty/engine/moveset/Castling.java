package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public class Castling extends Move {
    Castling(final GameBoard board, final Piece piece, final int destination) {
        super(board, piece, destination);
    }
}
