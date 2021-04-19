package com.thechessparty.engine.player;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.pieces.Piece;

import java.util.List;

public class BlackPlayer extends Player{

    // constructor
    public BlackPlayer(GameBoard board, List<Move> whiteMoves, List<Move> blackMoves){
        super(board, blackMoves, whiteMoves);
    }

    //------------ public methods ---------------------

    @Override
    public List<Move> kingCastleMoves(List<Move> playerMoves, List<Move> adversaryMoves) {
        return null;
    }

    //--------------- getters and setters --------------

    @Override
    public List<Piece> getActivePieces() {
        return getBoard().getBlack();
    }

    @Override
    public Team getTeam() {
        return Team.BLACK;
    }

    @Override
    public Player getAdversary() {
        return getBoard().getWhitePlayer();
    }
}
