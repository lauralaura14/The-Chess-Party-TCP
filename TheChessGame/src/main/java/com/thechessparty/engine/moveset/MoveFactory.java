package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;

public class MoveFactory {

    /**
     * Factory design pattern we use here to help with the creation of an unknown number of sub-class Move objects
     */
    private MoveFactory(){
        throw new RuntimeException("Cannot instantiate MoveBuilder");
    }

    /**
     * Creates a Move type object based on the current and destination coordinates
     *
     * @param board the current GameBoard that is in play
     * @param current the current coordinates of the Move
     * @param destination the destination coordinates of the Move
     * @return a Move type object based on the arguments
     */
    public static Move createMove(final GameBoard board, final int current, final int destination){
        for(final Move m : board.getAllMoves()){
            if(m.getCurrent() == current && m.getDestination() == destination){
                return m;
            }
        }
        return null;
    }

}
