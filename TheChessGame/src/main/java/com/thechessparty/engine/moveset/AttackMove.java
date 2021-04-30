package com.thechessparty.engine.moveset;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.pieces.Piece;

public class AttackMove extends Move{

    // instance variables
    final Piece attackedPiece;

    // Constructor
    public AttackMove(final GameBoard board, 
                     final Piece piece, 
                     final int destination, 
                     final Piece attackedPiece) {
        super(board, piece, destination);
        this.attackedPiece = attackedPiece;
    }

    @Override
    public GameBoard execute() {
        return null;
    }

    @Override
    public boolean isAttackMove(){
        return true;
    }

    @Override
    public Piece getAttackedPiece(){
        return this.attackedPiece;
    }

    @Override
    public int hashCode(){
        return this.attackedPiece.hashCode();
    }

    @Override
    public boolean equals(final Object o){
        if(this == o){
            return true;
        }
        if(!(o instanceof AttackMove)){
            return false;
        }
        final AttackMove move = (AttackMove) o;
        return super.equals(move) && getAttackedPiece().equals(move.getAttackedPiece());
    }
}
