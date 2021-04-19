package com.thechessparty.engine;

import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.MoveFactory;
import com.thechessparty.engine.moveset.NormalMove;
import com.thechessparty.engine.pieces.*;
import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.Transition;
import com.thechessparty.engine.player.WhitePlayer;

import java.util.List;
import java.util.Scanner;

public class GameManager {

    public static void main(String[] args) {
        GameBoard board = GameBoard.createInitialBoard();
        WhitePlayer w = new WhitePlayer(board, board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
        BlackPlayer b = new BlackPlayer(board, board.getWhitePlayer().getLegalMoves(), board.getBlackPlayer().getLegalMoves());
        Scanner scan = new Scanner(System.in);

        Player current = board.getCurrentPlayer();
        while(true){
            System.out.println(board);
            List<Move> allLegalMoves = board.getAllMoves();
            int start = 0;
            int destination = 0;
            if(current.getTeam().equals("WHITE")){
                System.out.println("WHITE PLAYER: enter coordinate for move");
                start = scan.nextInt();
                System.out.println("WHITE PLAYER: enter the Piece to move");
                destination = scan.nextInt();
            }else{
                System.out.println("BLACK PLAYER: enter coordinate for move");
                start = scan.nextInt();
                System.out.println("WHITE PLAYER: enter the Piece to move");
                destination = scan.nextInt();
            }

            Move m = MoveFactory.createMove(board, start, destination);
            Transition transition = current.move(m);
            if(transition.getStatus().isFinished()){
                System.out.println(current.getTeam() + " player is finished");
                board = transition.getBoardState();
                current = board.getCurrentPlayer().getAdversary();
            }
        }
    }

    public static Piece selectPiece(String selection, int position, Team team){
        switch (selection){
            case "R":
                return new Rook(position, team);
            case "K":
                return new King(position, team);
            case "P":
                return new Pawn(position, team);
            case "B":
                return new Bishop(position, team);
            case "N":
                return new Knight(position,team);
            case "Q":
                return new Queen(position,team);
            default:
                throw new RuntimeException("Invalid piece type cannot select this type of Piece");
        }
    }
}
