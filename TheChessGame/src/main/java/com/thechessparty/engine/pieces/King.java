package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class King extends Piece{

    public King(int position, final Team team){
        super(PieceIdentifiers.KING, position, team);
    }

    /**
     * Creates a new King with the updated position after a move is made
     * @param m the move of the King
     * @return a new King with position of move
     */
    @Override
    public King movePiece(Move m) {
        return new King(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {
        return null;
    }

    @Override
    public String toString(){
        return PieceIdentifiers.KING.toString();
    }
}
