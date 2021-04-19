package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public abstract class Piece {

    // instance variables
    private final PieceIdentifiers type;
    private final int position;
    private final Team team;
    private final boolean firstMove;

    //constructor
    Piece(final PieceIdentifiers type, final int position, final Team team) {
        this.type = type;
        this.position = position;
        this.team = team;
        this.firstMove = false;
    }

    //----------------- public methods ----------------------

    @Override
    public boolean equals(final Object o) {
        boolean equality;
        if (this == o) {
            equality = true;
        }
        if (!(o instanceof Piece)) {
            equality = false;
        }
        final Piece piece = (Piece) o;
        return (position == piece.getPosition()) && (type == piece.getType()) && team == piece.getTeam() &&
                this.firstMove == piece.isFirstMove();
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + team.hashCode();
        result = 31 * result + position;
        if(firstMove){
            result = 31 * result + 1;
        }else{
            result = 31 * result + 0;
        }
        return result;
    }

    //--------------- abstract methods ----------------------

    public abstract List<Move> listLegalMoves(final GameBoard board);

    public abstract Piece movePiece(Move m);

    //--------------- getters and setters -------------------

    public PieceIdentifiers getType() {
        return type;
    }

    public Team getTeam() {
        return this.team;
    }

    public int getPosition() {
        return position;
    }

    public boolean isFirstMove() {
        return firstMove;
    }
}
