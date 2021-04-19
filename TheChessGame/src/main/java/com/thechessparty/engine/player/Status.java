package com.thechessparty.engine.player;

public enum Status {
    FINISHED{
        @Override
        public boolean isFinished(){
            return true;
        }
    },
    ILLEGAL_MOVE{
        @Override
        public boolean isFinished(){
            return false;
        }
    },
    PLAYER_STILL_CHECK{
        @Override
        public boolean isFinished(){
            return false;
        }
    };
    public abstract boolean isFinished();
}
