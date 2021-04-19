package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Pawn;
import com.thechessparty.engine.pieces.Piece;

public final class PawnFirstMove extends Move {

    //constructor
    public PawnFirstMove(final GameBoard board, final Piece piece, final int destination) {
        super(board, piece, destination);
    }

    //-------------- public methods ------------------

    /**
     * Utilizes the GameBoard Builder sub-class to configure GameBoard variables for board creation.
     *
     * @return
     */
    @Override
    public GameBoard execute(){

        //creates a Builder for constructing a new board
        final GameBoard.Builder builder = new GameBoard.Builder();

        // place all of the current players unmoved pieces into position
        for(final Piece p : getBoard().getCurrentPlayer().getActivePieces()){
            if(!getBoard().equals(p)){
                builder.setPiece(p);
            }
        }

        //place all of the adversary's pieces into position
        for (final Piece p : getBoard().getCurrentPlayer().getAdversary().getActivePieces()){
            builder.setPiece(p);
        }

        // The generic Piece type that is cast to Pawn class which is the Piece that is making the Move
        final Pawn pawn = (Pawn) getMovedPosition().movePiece(this);

        //configure the moving Piece in the builder
        builder.setPiece(pawn);

        //TODO: create EnPassant logic
        //builder.setEnPassant(pawn);

        //set the next player as the adversary Team
        builder.setNextMove(getBoard().getCurrentPlayer().getAdversary().getTeam());
        return builder.build();
    }

    @Override
    public String toString() {
        return "-";
    }

}
