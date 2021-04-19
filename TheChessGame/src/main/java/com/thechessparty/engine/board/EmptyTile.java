package com.thechessparty.engine.board;

import com.thechessparty.engine.pieces.Piece;

public final class EmptyTile extends Tile {

    // constructor makes call to Tile constructor
    EmptyTile(int coordinate) {
        super(coordinate);
    }

    /**
     * Tells if tile is occupied will be false since it is empty
     *
     * @return false
     */
    @Override
    public boolean isTileOccupied() {
        return false;
    }

    /**
     * gets the Piece that is on tile will be null since it is empty
     *
     * @return null
     */
    public Piece getPiece() {
        return null;
    }

    @Override
    public String toString(){
        return "-";
    }
}//end EmptyTile class
