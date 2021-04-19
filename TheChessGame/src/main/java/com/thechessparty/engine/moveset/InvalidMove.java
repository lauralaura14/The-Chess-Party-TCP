package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;

public class InvalidMove extends Move {

    //constructor
    InvalidMove() {
        super(null, null, -1);
    }

    //------------- public methods ------------------

    @Override
    public GameBoard execute(){
        throw new RuntimeException("Invalid Move");
    }
}