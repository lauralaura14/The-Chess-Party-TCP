package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class Bishop extends Piece {

    //class variables
    // offsets relative to the Bishop's position on the board
    private final static int[] BISHOP_MOVES = {-9, -7, 7, 9};

    public Bishop(int position, final Team team) {
        super(PieceIdentifiers.BISHOP, position, team);
    }

    @Override
    public List<Move> listLegalMoves(final GameBoard board) {
       
        final List<Move> legalMoves = new ArrayList<>();
       // return null;
        for (final int current : BISHOP_MOVES) {
            int destination = getPosition();
              while (BoardUtilites.isValidMove(destination)) {
                   if (isFirstColumn(destination, current) 
                    || isEighthColumn(destination, current)) {
                    break;
                }
                  destination += current;
               
                if (BoardUtilites.isValidMove(destination)) {
                   final Tile destinationTile = board.getTile(current);

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
                    break; 
                }
            }
        }
    }

        return ImmutableList.copyOf(legalMoves);   
}

//----------- private helper methods ---------------------

    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Bishop
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isFirstColumn(final int currentPosition, final int offset) {
        return BoardUtilites.FIRST_COLUMN[currentPosition] && ((offset == -9) || (offset == 7));
    }


    /**
     * Utilizes the constant boolean array that tracks the
     *
     * @param currentPosition current coordinates of the Bishop
     * @param offset          the offset position to be qualified
     * @return true if the move can be made false if there is edge case exclusion
     */
    private static boolean isEighthColumn(final int currentPosition, final int offset) {
        return BoardUtilites.EIGHTH_COLUMN[currentPosition] && ((offset == -7) || (offset == 9));
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
