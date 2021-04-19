package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public final class NormalMove extends Move {

    //constructor
    public NormalMove(final GameBoard board, final Piece piece, final int destination) {
        super(board, piece, destination);
    }

    //-------------- public methods ------------------

    @Override
    public String toString() {
        return "-";
    }

}
