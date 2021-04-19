package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class Queen extends Piece{

    public Queen(int position, final Team team){
        super(PieceIdentifiers.QUEEN, position,team);
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {
        return null;
    }

    /**
     * Creates a new Queen with updated position of Move
     * @param m the next Move of the Bishop
     * @return a new Queen with position of Move
     */
    @Override
    public Queen movePiece(Move m) {
        return new Queen(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public String toString(){
        return PieceIdentifiers.QUEEN.toString();
    }
}
