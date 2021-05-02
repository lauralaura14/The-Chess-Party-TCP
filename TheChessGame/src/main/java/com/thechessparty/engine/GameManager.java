package com.thechessparty.engine;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.board.Tile;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.MoveFactory;
import com.thechessparty.engine.pieces.*;
import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.Transition;
import com.thechessparty.engine.player.WhitePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameManager implements Runnable {

    public static void main(String[] args) {
        GameManager gm = new GameManager();
        gm.run();
    }

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

    @Override
    public void run() {
        GameBoard board = GameBoard.createInitialBoard();
        Scanner scan = new Scanner(System.in);

        Player current = board.getCurrentPlayer();

        List<Piece> bp = board.getBlack();
        List<Piece> wp = board.getWhite();
        List<Piece> allPieces = Stream.concat(bp.stream(), wp.stream())
                .collect(Collectors.toList());
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

        WhitePlayer w = new WhitePlayer(board, wMoves, bMoves);
        BlackPlayer b = new BlackPlayer(board, wMoves, bMoves);

        board.getAllMoves();

        //while(board.getAllMoves() != 0){
        while (true) {
            w = board.getWhitePlayer();
            b= board.getBlackPlayer();

            System.out.println(board);
            int start = 0, destination = 0, x = 0, y = 0;
            Move m = null;
            Piece piece = null;
            while(m ==null) {
                if (board.getCurrentPlayer().getTeam().equals(Team.WHITE)) {
                    System.out.println("WHITE PLAYER: enter x coordinate for starting move");
                    x = scan.nextInt();
                    System.out.println("WHITE PLAYER: enter y coordinate for the starting move");
                    y = scan.nextInt();
                    start = moveHelper(x, y);
                    System.out.println("WHITE PLAYER: enter x coordinate for destination move");
                    x = scan.nextInt();
                    System.out.println("WHITE PLAYER: enter y coordinate for the destination move");
                    y = scan.nextInt();
                    destination = moveHelper(x, y);
                } else {
                    System.out.println("BLACK PLAYER: enter x coordinate for starting move");
                    x = scan.nextInt();
                    System.out.println("BLACK PLAYER: enter y coordinate for the starting move");
                    y = scan.nextInt();
                    start = moveHelper(x, y);
                    System.out.println("BLACK PLAYER: enter x coordinate for destination move");
                    x = scan.nextInt();
                    System.out.println("BLACK PLAYER: enter y coordinate for the destination move");
                    y = scan.nextInt();
                    destination = moveHelper(x, y);
                }
                System.out.println(start);
                System.out.println(destination);

                Tile tile = board.getTile(start);
                piece = tile.getPiece();

                m = MoveFactory.createMove(board, start, destination);

                if(m == null){
                    System.out.println(board);
                    System.out.println("invalid move");
                }
        }

            System.out.println("the " + current.getTeam() + " has selected " + piece.toString() + " going to " + destination);
            Transition transition = board.getCurrentPlayer().move(m);
            if (transition.getStatus().isFinished()) {
                System.out.println(current.getTeam() + " player is finished");

                board = transition.getBoardState();
                current = board.getCurrentPlayer();
            }

            if(w.getIsInCheck()){
                System.out.println("white players king is in check");
            } else if(b.getIsInCheck()){
                System.out.println("black players king is in check");
            }

        }
    }

    private int moveHelper(final int x, final int y){
        final int c = (((x * 8)+ y)-9) % 63;
        return c;
    }
}