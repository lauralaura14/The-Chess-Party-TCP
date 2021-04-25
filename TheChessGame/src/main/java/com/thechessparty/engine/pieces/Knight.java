package com.thechessparty.engine.pieces;

import com.google.common.collect.ImmutableList;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.*;
import com.thechessparty.engine.moveset.AttackMove;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.NormalMove;

import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {

    //class variables
    // offsets relative to the Knights position on the board
    private final static int[] KNIGHT_MOVES = {-17, -15 - 10, -6, 6, 10, 15, 17};

    //constructor
    public Knight(int position, final Team team) {
        super(PieceIdentifiers.KNIGHT, position, team);
    }

    //------------- public methods -------------------

    /**
     * Iterates through the list of KNIGHT_MOVES and applies the offset to the relative position of the
     * Knight. These positions are checked against to see if the move is a valid move i.e. inbounds, not occupied by,
     * the same Team, and not a losing move.
     *
     * @param board the GameBoard of the game that is being played
     * @return List<Move> list of Move type objects that correspond to legal moves
     */
    @Override
    public List<Move> listLegalMoves(final GameBoard board) {
        int destination;
        final List<Move> legalMoves = new ArrayList<>();

        for (final int current : KNIGHT_MOVES) {

            //applying the offset to the position;
            destination = getPosition() + current;

            if (BoardUtilites.isValidMove(destination)) {

                //if Knight Piece is near the edges of the board (literal edge cases)
                if (isFirstColumn(getPosition(), current) || isSecondColumn(getPosition(), current) ||
                        isSeventhColumn(getPosition(), current) || isEighthColumn(getPosition(), current)) {
                    continue;
                }

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

    /**
     * Creates a new Knight with updated position after a move is made
     *
     * @param m the Move of the Knight
     * @return a new Knight with position of move
     */
    @Override
    public Knight movePiece(Move m) {
        return new Knight(m.getDestination(), m.getMovedPosition().getTeam());
    }

    @Override
    public String toString() {
        return PieceIdentifiers.KNIGHT.toString();
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
        return BoardUtilites.FIRST_COLUMN[currentPosition] && ((offset == -17) || (offset == -10) || (offset == 6) || (offset == 15));
    }

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Knight
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isSecondColumn(final int currentPosition, final int offset) {
        return BoardUtilites.SECOND_COLUMN[currentPosition] && ((offset == -10) || (offset == 6));
    }

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Knight
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isSeventhColumn(final int currentPosition, final int offset) {
        return BoardUtilites.SEVENTH_COLUMN[currentPosition] && ((offset == 10) || (offset == -6));
    }

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Knight
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isEighthColumn(final int currentPosition, final int offset) {
        return BoardUtilites.EIGHTH_COLUMN[currentPosition] && ((offset == -15) || (offset == -6) || (offset == 10) || (offset == 17));
    }
}
