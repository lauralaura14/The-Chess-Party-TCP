package com.thechessparty.engine;

import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.WhitePlayer;

public enum Team {
    WHITE{
        @Override
        public Player nextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return whitePlayer;
        }

        @Override
        public int getDirection() {
            return -1;
        }

        @Override
        public boolean isWhite() {
            return true;
        }

        @Override
        public boolean isBlack() {
            return false;
        }
    },
    BLACK{
        @Override
        public Player nextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer) {
            return blackPlayer;
        }

        @Override
        public int getDirection() {
            return 1;
        }

        @Override
        public boolean isWhite() {
            return false;
        }

        @Override
        public boolean isBlack() {
            return true;
        }
    };

    public abstract Player nextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer);
    public abstract int getDirection();
    public abstract boolean isWhite();
    public abstract boolean isBlack();
}
