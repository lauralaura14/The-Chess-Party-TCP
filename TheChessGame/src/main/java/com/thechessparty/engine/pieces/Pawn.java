package com.thechessparty.engine.pieces;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

import java.util.List;

public class Pawn extends Piece{

    private finale static int[] PAWN_MOVES={8, 16, 7, 9};

    public Pawn(final int position, final Team team){
        super(PieceIdentifiers.PAWN, position, team);
    }

    @Override
    public List<Move> listLegalMoves(GameBoard board) {

        final List<Move> legalMoves = new ArrayList<>();

        for (final int current : PAWN_MOVES){
           final int destination = getPosition() + (getTeam().getDirection() * current);

            if(!BoardUtilities.isValidMove(destination)){
                continue;

            }
             if(current==8 && !board.getTile(destination).isTileOccupied()){
                //more work to be done deal with promotions
                legalMoves.add(new NormalMove(board, this, destination));
             }else if(current == 16 && this.isFirstMove() && 
                (BoardUtilities.SECOND_ROW [getPosition()] && getTeam().isBlack()) || 
                (BoardUtilities.SEVENTH_ROW [getPosition()] && getTeam().isWhite())){
                final int behindDestination = getPosition() + (getTeam().getDirection() * 8);
                if(!board.getTile(behindDestination).isTileOccupied() &&
                 !board.getTile(destination).isTileOccupied()){
                    legalMoves.add(new NormalMove(board, this, destination));
                   }

             }else if(current = 7 
                !((BoardUtilities.EIGHTH_COLUMN[getPosition()] && getTeam().isWhite() ||
                (BoardUtilities.FIRST_COLUMN[getPosition()] && getTeam().isBlack())) )){
                if(board.getTile(destination).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(destination).getPiece();
                    if(getTeam() != pieceOnCandidate.getTeam()){
                      legalMoves.add(new NormalMove(board, this, destination));  
                    }
                }
                
             }else if (current = 9 &&
                !((BoardUtilities.FIRST_COLUMN[getPosition()] && getTeam().isWhite() ||
                (BoardUtilities.EIGHTH_COLUMN[getPosition()] && getTeam().isBlack())) )){
                if(board.getTile(destination).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(destination).getPiece();
                    if(getTeam() != pieceOnCandidate.getTeam()){
                      legalMoves.add(new NormalMove(board, this, destination));  
                    }
                }

             }
        } 
        return ImmutableList.copyOf(legalMoves);      
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
