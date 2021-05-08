package com.thechessparty.engine.board;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.pieces.Piece;

public final class OccupiedTile extends Tile {

    // instance variable
    public Piece pieceOccuping;

    // constructor makes call to Tile constructor
    OccupiedTile(final int coordinate, Piece piece) {
        super(coordinate);
        this.pieceOccuping = piece;
    }

    //----------------- public methods ----------------------------

    /**
     * Tells if tile is occupied will be true since it is occupied
     *
     * @return true
     */
    @Override
    public boolean isTileOccupied() {
        return true;
    }

    /**
     * gets the Piece that is on tile.
     *
     * @return the Piece that is located on the Tile
     */
    @Override
    public Piece getPiece() {
        return this.pieceOccuping;
    }

    @Override
    public String toString(){
        if(getPiece().getTeam() == Team.BLACK){
            return getPiece().toString().toLowerCase();
        }else{
            return getPiece().toString();
        }
    }

}// end of OccupiedTile class