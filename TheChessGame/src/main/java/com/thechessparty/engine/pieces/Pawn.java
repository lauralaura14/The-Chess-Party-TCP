package com.thechessparty.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.BoardUtilites;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.NormalMove;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Piece {

    //class variables
    private final static int[] PAWN_MOVES = {8, 16, 7, 9};

    //constructor
    public Pawn(final int position, final Team team) {
        super(PieceIdentifiers.PAWN, position, team);
    }

    //--------------- public methods --------------------

    @Override
    public List<Move> listLegalMoves(GameBoard board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (int current : PAWN_MOVES) {
            final int destination = getPosition() + (getTeam().getDirection() * current);

            if (!BoardUtilites.isValidMove(destination)) {
                continue;

            }
            if (current == 8 && !board.getTile(destination).isTileOccupied()) {
                //more work to be done deal with promotions
                legalMoves.add(new NormalMove(board, this, destination));
            } else if (current == 16 && this.isFirstMove() &&
                    (BoardUtilites.SECOND_ROW[getPosition()] && getTeam().isBlack()) ||
                    (BoardUtilites.SEVENTH_ROW[getPosition()] && getTeam().isWhite())) {
                final int behindDestination = getPosition() + (getTeam().getDirection() * 8);
                if (!board.getTile(behindDestination).isTileOccupied() &&
                        !board.getTile(destination).isTileOccupied()) {
                    legalMoves.add(new NormalMove(board, this, destination));
                }

            } else if (current == 7 &&
            !((BoardUtilites.EIGHTH_COLUMN[getPosition()] && getTeam().isWhite() ||
                    (BoardUtilites.FIRST_COLUMN[getPosition()] && getTeam().isBlack())))){
                if (board.getTile(destination).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(destination).getPiece();
                    if (getTeam() != pieceOnCandidate.getTeam()) {
                        legalMoves.add(new NormalMove(board, this, destination));
                    }
                }

            }else if (current == 9 &&
                    !((BoardUtilites.FIRST_COLUMN[getPosition()] && getTeam().isWhite() ||
                            (BoardUtilites.EIGHTH_COLUMN[getPosition()] && getTeam().isBlack())))) {
                if (board.getTile(destination).isTileOccupied()) {
                    final Piece pieceOnCandidate = board.getTile(destination).getPiece();
                    if (getTeam() != pieceOnCandidate.getTeam()) {
                        legalMoves.add(new NormalMove(board, this, destination));
                    }
                }

            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    /**
     * Creates a new Pawn with updated position of Move
     *
     * @param m the next Move of the Pawn
     * @return a new Bishop with position of Move
     */
    @Override
    public Pawn movePiece(Move m) {
        return new Pawn(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public String toString() {
        return PieceIdentifiers.PAWN.toString();
    }
}
