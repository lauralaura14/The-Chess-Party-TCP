package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class Bishop extends Piece {

    public Bishop(int position, final Team team) {
        super(PieceIdentifiers.BISHOP, position, team);
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {
        return null;
    }

    /**
     * Creates a new Bishop with updated position of Move
     * @param m the next Move of the Bishop
     * @return a new Bishop with position of Move
     */
    @Override
    public Piece movePiece(Move m) {
        return new Bishop(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public String toString(){
        return PieceIdentifiers.BISHOP.toString();
    }
}
