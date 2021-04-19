package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public class EnPassant extends PawnAttackMove {

    //constructor
    public EnPassant(final GameBoard board, final Piece piece, final int destination, final Piece attackedPiece) {
        super(board, piece, destination, attackedPiece);
    }

    //-------------- public methods ------------------

    @Override
    public String toString() {
        return "-";
    }

}