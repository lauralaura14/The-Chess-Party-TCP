package com.thechessparty.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.BoardUtilites;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.board.Tile;
import com.thechessparty.engine.moveset.AttackMove;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.NormalMove;

import java.util.ArrayList;
import java.util.List;

public class King extends Piece {

    private final static int[] KING_MOVES = {-9, -8, -7, -1, 1, 7, 8, 9};

    //constructor
    public King(int position, final Team team) {
        super(PieceIdentifiers.KING, position, team);
    }

    //------------------- public methods ---------------------

    /**
     * Creates a new King with the updated position after a move is made
     *
     * @param m the move of the King
     * @return a new King with position of move
     */
    @Override
    public King movePiece(Move m) {
        return new King(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {

        final List<Move> legalMoves = new ArrayList<>();

        int destination;

        for (final int current : KING_MOVES) {

            destination = getPosition() + current;

            if (isFirstColumn(getPosition(), current)
                    || isEighthColumn(getPosition(), current)) {
                continue;
            }

            if (BoardUtilites.isValidMove(destination)) {
                final Tile destinationTile = board.getTile(destination);

                // if destination Tile is not occupied get NormalMove
                if (!destinationTile.isTileOccupied()) {
                    legalMoves.add(new NormalMove(board, this, destination));
                } else {
                    final Piece occupyingPiece = destinationTile.getPiece();
                    final Team team = occupyingPiece.getTeam();

                    // if the Tile is occupied get AttackMove
                    if (getTeam() != team) {
                        legalMoves.add(new AttackMove(board, this, destination, occupyingPiece));
                    }
                }
            }

        }


        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString() {
        return PieceIdentifiers.KING.toString();
    }

    //----------- private helper methods ---------------------

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Knight
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isFirstColumn(final int currentPosition, final int offset) {
        return BoardUtilites.FIRST_COLUMN[currentPosition] && ((offset == -9) || (offset == -1) || (offset == 7));
    }

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Knight
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isEighthColumn(final int currentPosition, final int offset) {
        return BoardUtilites.EIGHTH_COLUMN[currentPosition] && ((offset == -7) || (offset == 1) || (offset == 9));
    }
}
