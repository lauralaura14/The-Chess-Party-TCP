package com.thechessparty.engine;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.thechessparty.connection.MessageDTO;
import com.thechessparty.connection.jsonparsing.JsonConverter;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.board.Tile;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.MoveFactory;
import com.thechessparty.engine.pieces.*;
import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.Transition;
import com.thechessparty.engine.player.WhitePlayer;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameManager implements Runnable {

    private static final String HEADER = "--PLAYERMOVE: ";
    private static Scanner scan;
    private static Gson gson;
    private static String jsonOut = "";
    private static String jsonIn = "";
    private static Team clientTeam;
    private static Team advarsary;
    private static Player current;
    private static GameBoard board;
    private static WhitePlayer w;
    private static BlackPlayer b;
    private static Move m;
    private static Piece piece;
    private static Transition transition;
    private static Tile tile;
    private static volatile boolean isPlaying;

    //constructor
    public GameManager() {}

    public GameManager(Team staringTeam) {
        clientTeam = staringTeam;
        if (staringTeam.isWhite()) {
            advarsary = Team.BLACK;
        } else {
            advarsary = Team.WHITE;
        }
    }

    /**
     * @param selection
     * @param position
     * @param team
     * @return
     */
    public static Piece selectPiece(String selection, int position, Team team) {
        switch (selection) {
            case "R":
                return new Rook(position, team);
            case "K":
                return new King(position, team);
            case "P":
                return new Pawn(position, team);
            case "B":
                return new Bishop(position, team);
            case "N":
                return new Knight(position, team);
            case "Q":
                return new Queen(position, team);
            default:
                throw new RuntimeException("Invalid piece type cannot select this type of Piece");
        }
    }

    /**
     * @return
     */
    public static GameBoard configureGame() {
        scan = new Scanner(System.in);
        board = GameBoard.createInitialBoard();
        current = board.getCurrentPlayer();
        gson = new Gson();

        List<Piece> bp = board.getBlack();
        List<Piece> wp = board.getWhite();
        List<Move> allMoves = new ArrayList<>();

        List<Move> wMoves = new ArrayList<>();
        List<Move> bMoves = new ArrayList<>();

        for (Piece p : wp) {
            if (p.listLegalMoves(board) == null) continue;
            wMoves = p.listLegalMoves(board);
            for (Move m : wMoves) {
                allMoves.add(m);
            }
        }

        for (Piece p : bp) {
            if (p.listLegalMoves(board) == null) continue;
            bMoves = p.listLegalMoves(board);
            for (Move m : bMoves) {
                allMoves.add(m);
            }
        }

        w = new WhitePlayer(board, wMoves, bMoves);
        b = new BlackPlayer(board, wMoves, bMoves);

        return board;
    }

    /**
     * @return
     */
    public int waitMakeMove() {
        int start = 0, destination = 0, x = 0, y = 0;
        m = null;
        piece = null;
        while (m == null) {
            if (board.getCurrentPlayer().getTeam().equals(clientTeam)) {
                setIsPlaying(true);
                System.out.println(clientTeam + " PLAYER: enter x coordinate for starting move");
                x = scan.nextInt();
                System.out.println(clientTeam + " PLAYER: enter y coordinate for the starting move");
                y = scan.nextInt();
                start = moveHelper(x, y);
                System.out.println(clientTeam + " PLAYER: enter x coordinate for destination move");
                x = scan.nextInt();
                System.out.println(clientTeam + " PLAYER: enter y coordinate for the destination move");
                y = scan.nextInt();
                destination = moveHelper(x, y);
            } else {
                setIsPlaying(false);
                System.out.println(advarsary + " PLAYER: is playing wait for them to finish");
            }
            System.out.println(start);
            System.out.println(destination);

            tile = board.getTile(start);
            piece = tile.getPiece();

            m = MoveFactory.createMove(board, start, destination);

            if (m == null) {
                System.out.println(board);
                System.out.println("invalid move");
            }
        }
        return destination;
    }

    public void waitForPlayer() {

        while (jsonIn == null) {

        }
    }

    /**
     *
     */
    @Override
    public void run() {
        String gameState = "";
        board = configureGame();

        boolean draw = false;
        //while(board.getAllMoves() != 0){
        while (!draw) {
            w = board.getWhitePlayer();
            b = board.getBlackPlayer();

            System.out.println(board);

            //players either makes move or waits for other player to finish move
            int destination = waitMakeMove();

            System.out.println("the " + current.getTeam() + " has selected " + piece.toString() + " going to " + destination);
            transition = board.getCurrentPlayer().move(m);
            if (transition.getStatus().isFinished()) {
                System.out.println(current.getTeam() + " player is finished");


                //TODO: go into waiting state for response
                board = transition.getBoardState();
                current = board.getCurrentPlayer();

                gameState = gson.toJson(destination);
                jsonOut = HEADER + gameState;
                draw = isDraw();
            }
        }
    }

    /**
     *
     * @return
     */
    private boolean isDraw() {
        boolean draw =false;
        if(w.inCheck()){
            System.out.println("white players king is in check");
            draw = true;
        } else if(b.inCheck()){
            System.out.println("black players king is in check");
            draw = true;
        }
        return draw;
    }

    /**
     * Makes entering the coordinates simpler by taking in an x and a y coordinate 1 to 8 ana converting it to an index
     * on the GameBoard array
     *
     * @param x the horizontal
     * @param y the vertical
     * @return an index on the array
     */
    private int moveHelper(final int x, final int y) {
        final int c = (((x * 8) + y) - 9) % 63;
        return c;
    }

    //------------ getters and setters ---------------


    public static Gson getGson() {
        return gson;
    }

    public static String getJsonOut() {
        return jsonOut;
    }

    public static void setJsonOut(String jsonOut) {
        GameManager.jsonOut = jsonOut;
    }

    public static String getJsonIn() {
        return jsonIn;
    }

    public static void setJsonIn(String jsonIn) {
        GameManager.jsonIn = jsonIn;
    }

    public static Team getClientTeam() {
        return clientTeam;
    }

    public static void setClientTeam(Team clientTeam) {
        GameManager.clientTeam = clientTeam;
    }

    public static Team getAdvarsary() {
        return advarsary;
    }

    public static void setAdvarsary(Team advarsary) {
        GameManager.advarsary = advarsary;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static void setScan(Scanner scan) {
        GameManager.scan = scan;
    }

    public static Player getCurrent() {
        return current;
    }

    public static void setCurrent(Player current) {
        GameManager.current = current;
    }

    public static GameBoard getBoard() {
        return board;
    }

    public static void setBoard(GameBoard board) {
        GameManager.board = board;
    }

    public static WhitePlayer getW() {
        return w;
    }

    public static void setW(WhitePlayer w) {
        GameManager.w = w;
    }

    public static BlackPlayer getB() {
        return b;
    }

    public static void setB(BlackPlayer b) {
        GameManager.b = b;
    }

    public static Move getM() {
        return m;
    }

    public static void setM(Move m) {
        GameManager.m = m;
    }

    public static Piece getPiece() {
        return piece;
    }

    public static void setPiece(Piece piece) {
        GameManager.piece = piece;
    }

    public static boolean isIsPlaying() {
        return isPlaying;
    }

    public static void setIsPlaying(boolean isPlaying) {
        GameManager.isPlaying = isPlaying;
    }
}