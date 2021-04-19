package com.thechessparty.engine.player;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;

public class Transition {

    //instance variables
    private final GameBoard boardState;
    private final Move move;
    private final Status status;

    //constructor
    public Transition(final GameBoard board,
                      final Move move, final Status status){
        this.boardState = board;
        this.move = move;
        this.status = status;
    }

    //----------- getters and setters -------------------

    public Status getStatus(){
        return this.status;
    }

    public GameBoard getBoardState() {
        return boardState;
    }

    public Move getMove() {
        return move;
    }
}
