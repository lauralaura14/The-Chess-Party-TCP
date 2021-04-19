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
    };

    public abstract Player nextPlayer(final BlackPlayer blackPlayer, final WhitePlayer whitePlayer);
    public abstract int getDirection();
}
