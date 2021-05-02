package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public final class NormalMove extends Move {

    //constructor
    public NormalMove(final GameBoard board, 
    	              final Piece piece, 
    	              final int destination) {
        super(board, piece, destination);
    }
/**
    public GameBoard execute() {

        //creates a Builder for constructing a new board
        final GameBoard.Builder builder = new GameBoard.Builder();

        // place all of the current players unmoved pieces into position
        for (final Piece p : this.board.getCurrentPlayer().getActivePieces()) {
            if (!this.movedPosition.equals(p)) {
                builder.setPiece(p);
            }
        }

        //place all of the adversary's pieces into position
        for (final Piece p : this.board.getCurrentPlayer().getAdversary().getActivePieces()) {
            builder.setPiece(p);
        }

        // sets builder to take in updated Piece with new coordinates based on this
        // Move passed to the Piece that is being moved
        builder.setPiece(this.movedPosition.movePiece(this));

        //sets the boards current player to the next player
        builder.setNextMove(this.board.getCurrentPlayer().getAdversary().getTeam());

        return builder.build();
    }
**/
    //-------------- public methods ------------------

    @Override
    public String toString() {
        return "-";
    }

}
