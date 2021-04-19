package com.thechessparty.engine.board;

import com.google.common.collect.ImmutableMap;
import com.thechessparty.engine.pieces.Piece;

import java.util.HashMap;
import java.util.Map;

public abstract class Tile {

    // instance variables
    private int tileCoordinate;

    // class variables
    private static final Map<Integer, EmptyTile> EMPTY_TILE_MAP = createAllEmptyTiles();

    // constructor
    Tile(final int tileCoordinate) {
        this.tileCoordinate = tileCoordinate;
    }

    //--------------- public access methods --------------------

    /**
     * creates an immutable map of all the possible empty tiles that could exist.
     *
     * @return an immutable Map<Integer, EmptyTile>
     */
    public static Map<Integer, EmptyTile> createAllEmptyTiles() {
        final Map<Integer, EmptyTile> emptyTileMap = new HashMap<>();

        for (int i = 0; i < BoardUtilites.BOARD_DIMENSIONS; i++) {
            emptyTileMap.put(i, new EmptyTile(i));
        }

        return ImmutableMap.copyOf(emptyTileMap);
    }

    /**
     * Factor method that allows the creation of tiles.
     *
     * @param tileCoordinate the coordinates of the Tile that correspond to the game board location
     * @param piece          the type of Piece that may or may not be on the Tile
     * @return a new Tile or a cached Tile
     */
    public static Tile createTile(final int tileCoordinate, final Piece piece ){
        if (piece != null)
            return new OccupiedTile(tileCoordinate, piece);
        else
            return EMPTY_TILE_MAP.get(tileCoordinate);
    }

    public abstract boolean isTileOccupied();

    public abstract Piece getPiece();

}//end Tile class
