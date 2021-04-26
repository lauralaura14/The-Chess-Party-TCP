package com.thechessparty.engine.player;

import com.google.common.collect.ImmutableList;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.pieces.King;
import com.thechessparty.engine.pieces.Piece;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {

    // instance variables
    private final GameBoard board;
    private final King king;
    private final List<Move> legalMoves;
    private final boolean inCheck;

    // constructor
    public Player(final GameBoard board, final List<Move> playerMoves, List<Move> adversaryMoves) {
        this.board = board;
        this.king = kingify();
        this.legalMoves = playerMoves;
        this.inCheck = !Player.tileAttacks(this.king.getPosition(), adversaryMoves).isEmpty();
    }

    //------------------- public methods ----------------------------

    /**
     * Takes in a King Piece and a List of Moves from the adversary to calculate if any of the moves that the
     * adversary could possibly make correspond to the position of the King.
     *
     * @param position       the position of the target Piece (king)
     * @param adversaryMoves the List of possible attack Moves that the adversary can make
     * @return true if any of the moves listed instersect with the Kings position
     */
    public static List<Move> tileAttacks(int position, List<Move> adversaryMoves) {
        final List<Move> attackList = new ArrayList<>();
        for (final Move m : adversaryMoves) {
            if (position == m.getDestination()) {
                attackList.add(m);
            }
        }
        return ImmutableList.copyOf(attackList);
    }

    /**
     * Iterates through the immutable list of active pieces on the GameBoard to find the king
     *
     * @return the King Piece that is found
     */
    public King kingify() {
        for (final Piece p : getActivePieces()) {
            if (p.getType().isKing()) {
                return (King) p;
            }
        }
        throw new RuntimeException("Invalid GameBoard no King type found");
    }

    /**
     * Looks to see if the Move that is passed in is a move that is listed in the List of legal moves.
     *
     * @param move The Move that is being tested
     * @return true if the move is is found in the list of legal moves
     */
    public boolean isLegalMove(final Move move) {
        return this.legalMoves.contains(move);
    }

    /**
     * looks to see if the player is already in check and if there are no possible escape moves.
     *
     * @return true if Player is in check and there are no possible escape Moves.
     */
    public boolean isCheckMate() {
        return this.inCheck && !hasEscape();
    }

    /**
     * Looks to see if the player is not in check and also does not have any possible escape moves.
     *
     * @return true if the conditions of a draw are met on the board
     */
    public boolean isDraw() {
        return !getIsInCheck() && !hasEscape();
    }

    public boolean isCastled() {
        return false;
    }

    /**
     *
     * @param m
     * @return
     */
    public Transition move(final Move m) {

        // if the move is illegal return the same GameBoard and Status
        if (!isLegalMove(m)) {
            return new Transition(this.board, m, Status.ILLEGAL_MOVE);
        }

        // execute the which will return a GameBoard
        final GameBoard transferBoard = m.execute();

        // gets the transfer board and looks at the adversary's King and current Player Moves
        final List<Move> kingMoves = Player.tileAttacks(transferBoard.getCurrentPlayer().getAdversary().getKing().getPosition(),
                transferBoard.getCurrentPlayer().getLegalMoves());

        // if the current players king is still in check
        if(!kingMoves.isEmpty()){
            return new Transition(this.board, m, Status.PLAYER_STILL_CHECK);
        }

        // if the current player made a NormalMove
        return new Transition(transferBoard, m, Status.FINISHED);
    }

    /**
     * Calculates all of the possible player Moves to see if there is a move that will break the condition of check
     *
     * @return true if there is at least one Move that will negate the isCheck condition
     */
    public boolean hasEscape() {
        for (final Move m : this.legalMoves) {
            final Transition transition = move(m);
            if (transition.getStatus().isFinished()) {
                return true;
            }
        }
        return false;
    }

    public abstract List<Move> kingCastleMoves(List<Move> playerMoves, List<Move> adversaryMoves);

    //------------------- getters and setters --------------------

    // abstract methods
    public abstract List<Piece> getActivePieces();

    public abstract Team getTeam();

    public abstract Player getAdversary();

    public GameBoard getBoard() {
        return board;
    }

    public King getKing() {
        return king;
    }

    public List<Move> getLegalMoves() {
        return legalMoves;
    }

    public boolean getIsInCheck() {
        return this.inCheck;
    }
}
