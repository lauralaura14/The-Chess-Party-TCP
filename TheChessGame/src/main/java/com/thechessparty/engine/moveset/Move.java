package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public abstract class Move {

    //instance constants
    final private GameBoard board;
    final private Piece movedPosition;
    final private int destination;

    // class variable
    final public static Move INVALID_MOVE = new InvalidMove();

    //constructor
    Move(final GameBoard board, final Piece piece, final int destination){
        this.board = board;
        this.movedPosition = piece;
        this.destination = destination;
    }

    //--------------- public methods -----------------------

    /**
     * Utilizes the GameBoard Builder class to construct a new board by placing all of the current players pieces
     * into position if they have not moved then all of the adversary's pieces by setting the values through the
     * builder. The moved piece is then set and finally set the next player to the adversary
     *
     * @return a new GameBoard constructed through the builder
     */
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

    public boolean isAttackMove(){
        return false;
    }

    public boolean isCastling(){
        return false;
    }

    @Override
    public int hashCode(){
        int hash = 1;
        hash = 31 * hash + this.destination;
        hash = 31 * hash + this.movedPosition.hashCode();
        return hash;
    }

    @Override
    public boolean equals(final Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof Move)){
            return false;
        }
        final Move move = (Move) o;
        return getDestination() == move.getDestination() && getMovedPosition().equals(move.getMovedPosition());
    }

    //--------------- getters and setters ------------------

    public int getDestination(){
        return this.destination;
    }

    public GameBoard getBoard() {
        return board;
    }

    public Piece getMovedPosition() {
        return movedPosition;
    }

    public int getCurrent(){
        return this.movedPosition.getPosition();
    }

    public Piece getAttackedPiece(){
        return null;
    }
}
