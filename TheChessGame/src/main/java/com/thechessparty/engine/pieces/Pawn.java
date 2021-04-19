package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class Pawn extends Piece{

    public Pawn(int position, final Team team){
        super(PieceIdentifiers.PAWN, position, team);
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {
        return null;
    }

    /**
     * Creates a new Pawn with updated position of Move
     * @param m the next Move of the Pawn
     * @return a new Bishop with position of Move
     */
    @Override
    public Pawn movePiece(Move m) {
        return new Pawn(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public String toString(){
        return PieceIdentifiers.PAWN.toString();
    }
}
