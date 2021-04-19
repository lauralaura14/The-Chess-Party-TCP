package com.thechessparty.engine.board;

import com.google.common.collect.ImmutableList;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.pieces.*;
import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.WhitePlayer;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameBoard {

    // instance constants
    private final List<Tile> gameBoard;
    private final List<Piece> white;
    private final List<Piece> black;
    private final WhitePlayer whitePlayer;
    private final BlackPlayer blackPlayer;
    private final Player currentPlayer;

    // constructor
    private GameBoard(Builder builder) {
        this.gameBoard = createBoard(builder);
        this.white = listBoardPieces(this.gameBoard, Team.WHITE);
        this.black = listBoardPieces(this.gameBoard, Team.BLACK);

        final List<Move> whiteMoves = legalMoves(this.white);
        final List<Move> blackMoves = legalMoves(this.black);

        this.whitePlayer = new WhitePlayer(this, whiteMoves, blackMoves);
        this.blackPlayer = new BlackPlayer(this, whiteMoves, blackMoves);

        this.currentPlayer = builder.nextMove.nextPlayer(this.blackPlayer, this.whitePlayer);
    }

    //-------------- public methods ----------------------------

    public Tile getTile(final int coordinate) {
        return null;
    }

    /**
     * Takes in a List of Pieces that represent the active pieces of a team and will generate a list of
     * Move objects that will represent the possible legal moves that the specified team can make.
     *
     * @param pieces the List of Pieces that represent the Teams active pieces
     * @return an immutable List of Moves that correspond to the possible legal moves.
     */
    private List<Move> legalMoves(final List<Piece> pieces) {
        final List<Move> moves = new ArrayList<>();

        for (final Piece piece : pieces) {
            //moves.addAll(piece.listLegalMoves(this));
        }
        return ImmutableList.copyOf(moves);
    }

    /**
     * Creates all of the Piece objects that will be needed for the game and sets their initial position. These Pieces
     * are then mapped to the Builder objects board configurations.
     *
     * @return an instance of the configured Builder object.
     */
    public static GameBoard createInitialBoard() {
        final Builder b = new Builder();

        // The black teams pieces at their initial position
        Rook blkRRook = new Rook(0, Team.BLACK);
        Knight blkRKnight = new Knight(1, Team.BLACK);
        Bishop blkRBishop = new Bishop(2, Team.BLACK);
        Queen blkQueen = new Queen(3, Team.BLACK);
        King blkKing = new King(4, Team.BLACK);
        Bishop blkLBishop = new Bishop(5, Team.BLACK);
        Knight blkLKnight = new Knight(6, Team.BLACK);
        Rook blkLRook = new Rook(7, Team.BLACK);
        Pawn blkPawn0 = new Pawn(8, Team.BLACK);
        Pawn blkPawn1 = new Pawn(9, Team.BLACK);
        Pawn blkPawn2 = new Pawn(10, Team.BLACK);
        Pawn blkPawn3 = new Pawn(11, Team.BLACK);
        Pawn blkPawn4 = new Pawn(12, Team.BLACK);
        Pawn blkPawn5 = new Pawn(13, Team.BLACK);
        Pawn blkPawn6 = new Pawn(14, Team.BLACK);
        Pawn blkPawn7 = new Pawn(15, Team.BLACK);

        // The white teams pieces at their initial position
        Rook whtRRook = new Rook(56, Team.WHITE);
        Knight whtRKnight = new Knight(57, Team.WHITE);
        Bishop whtRBishop = new Bishop(58, Team.WHITE);
        Queen whtQueen = new Queen(59, Team.WHITE);
        King whtKing = new King(60, Team.WHITE);
        Bishop whtLBishop = new Bishop(61, Team.WHITE);
        Knight whtLKnight = new Knight(62, Team.WHITE);
        Rook whtLRook = new Rook(63, Team.WHITE);
        Pawn whtPawn0 = new Pawn(48, Team.WHITE);
        Pawn whtPawn1 = new Pawn(49, Team.WHITE);
        Pawn whtPawn2 = new Pawn(50, Team.WHITE);
        Pawn whtPawn3 = new Pawn(51, Team.WHITE);
        Pawn whtPawn4 = new Pawn(52, Team.WHITE);
        Pawn whtPawn5 = new Pawn(53, Team.WHITE);
        Pawn whtPawn6 = new Pawn(54, Team.WHITE);
        Pawn whtPawn7 = new Pawn(55, Team.WHITE);

        //sets the black pieces to the board
        b.setPiece(blkRRook);
        b.setPiece(blkRKnight);
        b.setPiece(blkRBishop);
        b.setPiece(blkQueen);
        b.setPiece(blkKing);
        b.setPiece(blkLBishop);
        b.setPiece(blkLKnight);
        b.setPiece(blkLRook);
        b.setPiece(blkPawn0);
        b.setPiece(blkPawn1);
        b.setPiece(blkPawn2);
        b.setPiece(blkPawn3);
        b.setPiece(blkPawn4);
        b.setPiece(blkPawn5);
        b.setPiece(blkPawn6);
        b.setPiece(blkPawn7);

        //sets the white pieces to the board
        b.setPiece(whtRRook);
        b.setPiece(whtRKnight);
        b.setPiece(whtRBishop);
        b.setPiece(whtQueen);
        b.setPiece(whtKing);
        b.setPiece(whtLBishop);
        b.setPiece(whtLKnight);
        b.setPiece(whtLRook);
        b.setPiece(whtPawn0);
        b.setPiece(whtPawn1);
        b.setPiece(whtPawn2);
        b.setPiece(whtPawn3);
        b.setPiece(whtPawn4);
        b.setPiece(whtPawn5);
        b.setPiece(whtPawn6);
        b.setPiece(whtPawn7);

        // The team to go first White as is chess standard
        b.setNextMove(Team.WHITE);

        //returns the built board
        return b.build();
    }

    /**
     * joins the two lists of Moves of both the whitePlayer and the blackPlayer.
     * @return A List of all possible Moves between both Players
     */
    public List<Move> getAllMoves(){
        return Stream.concat(this.blackPlayer.getLegalMoves().stream(), this.whitePlayer.getLegalMoves().stream())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < BoardUtilites.BOARD_DIMENSIONS; i++) {
            final String tileText = this.gameBoard.get(i).toString();
            sb.append(String.format("%3s", tileText));
            if ((i + 1) % BoardUtilites.ROW_DIMENSIONS == 0) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    //------------------- private methods -----------------------

    /**
     * Populates an array of tiles 0 - 63 to represent the game board. This is done by take the values that are mapped
     * in the builder boardConfigurations and generating a linear immutable List of tiles that will represent the board
     *
     * @param builder A builder object that will give us the needed configurations.
     * @return an immutable List of tiles that represent the game board.
     */
    private static List<Tile> createBoard(final Builder builder) {
        final Tile[] tiles = new Tile[BoardUtilites.BOARD_DIMENSIONS];
        for (int i = 0; i < BoardUtilites.BOARD_DIMENSIONS; i++) {
            tiles[i] = Tile.createTile(i, builder.boardConfiguration.get(i));
        }
        return ImmutableList.copyOf(tiles);
    }

    /**
     * Takes in a List of Tiles that represent the active gameBoard and a Team and will generate a List of Pieces
     * that are currently at play of that team.
     *
     * @param boardList a List of Tiles that represents the current gameBoard
     * @param team      the target Team to have its Pieces Listed
     * @return an immutable List of Pieces for the specified Team
     */
    private static List<Piece> listBoardPieces(final List<Tile> boardList, final Team team) {
        final List<Piece> active = new ArrayList<>();

        for (final Tile tile : boardList) {
            if (tile.isTileOccupied()) {
                final Piece piece = tile.getPiece();
                if (piece.getTeam() == team) {
                    active.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(active);
    }

    //-------------- getters and setters --------------

    public List<Piece> getWhite() {
        return white;
    }

    public List<Piece> getBlack() {
        return black;
    }

    public WhitePlayer getWhitePlayer() {
        return whitePlayer;
    }

    public BlackPlayer getBlackPlayer() {
        return blackPlayer;
    }

    public Player getCurrentPlayer() {
        return this.currentPlayer;
    }

//---------------- nested class -------------------

    /**
     * Utilizes the builder pattern to decouple the complexity of constructing a board object.
     */
    public static class Builder {

        // instance variables
        Map<Integer, Piece> boardConfiguration;
        Team nextMove;

        // constructor
        public Builder() {
            this.boardConfiguration = new HashMap<>();
        }

        //-------------- public methods ---------------------

        /**
         * gets the position of a piece and maps it to the this boardConfiguration map. It will then return this class
         * of the builder.
         *
         * @param piece a Piece that is to be mapped
         * @return this class of the builder
         */
        public Builder setPiece(final Piece piece) {
            this.boardConfiguration.put(piece.getPosition(), piece);
            return this;
        }

        /**
         * Sets the team that is to go next.
         *
         * @param team a Team object that will be the team to go next
         * @return this class of the builder
         */
        public Builder setNextMove(final Team team) {
            this.nextMove = team;
            return this;
        }

        /**
         * standard builder pattern build method.
         *
         * @return this class of the Builder.
         */
        public GameBoard build() {
            return new GameBoard(this);
        }
    }// end of builder class
}// end of GameBoard class
