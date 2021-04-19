package com.thechessparty.engine.pieces;

public enum PieceIdentifiers {

    PAWN("P"){
        @Override
        public boolean isKing(){ return false;}
    },
    ROOK("R"){
        @Override
        public boolean isKing(){ return false;}
    },
    KNIGHT("N"){
        @Override
        public boolean isKing(){ return false;}
    },
    BISHOP("B"){
        @Override
        public boolean isKing(){ return false;}
    },
    QUEEN("Q"){
        @Override
        public boolean isKing(){ return false;}
    },
    KING("K"){
        @Override
        public boolean isKing(){ return true;}
    };

    private String pieceName;

    PieceIdentifiers(final String name){
        this.pieceName = name;
    }

    @Override
    public String toString(){
        return this.pieceName;
    }

    public abstract boolean isKing();
}
