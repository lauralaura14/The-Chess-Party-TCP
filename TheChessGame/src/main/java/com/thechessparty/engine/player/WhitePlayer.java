package com.thechessparty.engine.player;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public class WhitePlayer extends Player{

    public WhitePlayer(GameBoard board, List<Move> whiteMoves, List<Move> blackMoves){
        super(board, whiteMoves, blackMoves);
    }

    //-------------------- public methods ------------------------

    @Override
    public List<Move> kingCastleMoves(List<Move> playerMoves, List<Move> adversaryMoves) {
        final List<Move> kingCastleMoves = new ArrayList<>();
        return null;
    }

    //------------------- getters and setters --------------------

    @Override
    public List<Piece> getActivePieces() {
        return getBoard().getWhite();
    }

    @Override
    public Team getTeam() {
        return Team.WHITE;
    }

    @Override
    public Player getAdversary() {
        return getBoard().getBlackPlayer();
    }
}
