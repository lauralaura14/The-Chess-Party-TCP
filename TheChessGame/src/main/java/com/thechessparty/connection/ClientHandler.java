package com.thechessparty.connection;

import com.thechessparty.engine.Team;
import com.thechessparty.engine.board.GameBoard;
import com.thechessparty.engine.board.Tile;
import com.thechessparty.engine.moveset.Move;
import com.thechessparty.engine.moveset.MoveFactory;
import com.thechessparty.engine.pieces.Piece;
import com.thechessparty.engine.player.BlackPlayer;
import com.thechessparty.engine.player.Player;
import com.thechessparty.engine.player.Transition;
import com.thechessparty.engine.player.WhitePlayer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    protected static final String WAIT_HEADER = "-WAIT-";
    protected static final String EXIT_HEADER = "-EXIT-";

    //instance variables
    private Socket client;
    private final List<ClientHandler> clientList;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private String status;
    private JoinedPlayer whitePlayer;
    private JoinedPlayer blackPlayer;
    private JoinedPlayer currentPlayer;
    private final List<String> nameStatus = new ArrayList<>();
    private boolean statChange = false;
    private static String inputMsg;
    private static String msg;
    private static String receiverName;

    // game variables
    private String clientName;
    private String adversaryName;
    private static Player current;
    private static GameBoard board;
    private static WhitePlayer w;
    private static BlackPlayer b;
    private static Move m;
    private static Piece piece;
    private static Transition transition;
    private static Tile tile;


    // constructor
    public ClientHandler(Socket clientSocket, String clientName, ArrayList<ClientHandler> clientList, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        this.client = clientSocket;
        this.clientList = clientList;
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.clientName = clientName;
    }

    //------------------- public methods ----------------------------

    /**
     * Sends out welcome message to newly connected client along with list of other available connected clients
     *
     * @throws IOException will throw exception if the connection is terminated.
     */
    public void welcomeClientConnection() throws IOException {
        outputStream.writeUTF("Welcome " + getClientName() + ".\n");

        this.setStatus("available");
        nameStatusAdd();

        for (ClientHandler each : clientList) {
            if (each.getStatus().equals("available"))
                each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());
        }

        outputStream.writeUTF("\nPlease message player you want to play with strictly following this format." +
                "\n'game req' to make request, 'no req' to reject, 'yes req' to accept. For example: " +
                "\nname: game req | name: no req | name: yes req\n");
    }

    /**
     * @throws IOException
     */
    public void getClientAvailability() throws IOException {
        if (inputMsg.equals("disconnect")) {
            setStatus("disconnected");
            nameStatus.clear();
            nameStatusAdd();

            for (ClientHandler each : clientList) {
                if (each.getStatus().equals("available"))
                    each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());
            }
            inputStream.close();
            outputStream.close();
            closeConnection();
        }

        //incoming msg separated into receiver name and message itself
        msg = inputMsg.substring(inputMsg.indexOf(": ") + 2).toLowerCase();  // msg after :
        receiverName = inputMsg.substring(0, inputMsg.indexOf(":")).toLowerCase();  //name of person receiving msg
    }

    /**
     * @param each
     * @return
     * @throws IOException
     */
    public int readHeader(final ClientHandler each) throws IOException {
        int i = 0;
        //person requesting automatically heads
        switch (msg) {
            case "game req":
                return 0;
            case "no req":
                each.outputStream.writeUTF("\n" + getClientName() + " denied your request. Choose another available player.\n");
                return 0;
            case "yes req":
                each.outputStream.writeUTF("\n" + getClientName() + " accepted your request. Please type '" + getClientName() + ": starting game'.\n");
                return 0; // break out from outer to join messaging with player who made request
            case "starting game":
                this.outputStream.writeUTF("\nSince you requested, you are automatically heads, " + this.clientName + ".\n...");
                each.outputStream.writeUTF("\nPlayer who accepts is automatically tails, " + each.clientName + ".\n...");

                if (coinToss() == "heads") {
                    this.whitePlayer = new JoinedPlayer(this.client, this.clientName, Team.WHITE, each.getClientName(), Team.BLACK);  //the one who made request (auto heads)
                    this.blackPlayer = new JoinedPlayer(each.client, each.getClientName(), Team.BLACK, this.clientName, Team.WHITE);  //the one who accepted

                    outputStream.writeUTF("\nBased on coin toss, you are first (white piece), " + this.whitePlayer.getName() + ".");
                    each.outputStream.writeUTF("\nBased on coin toss, you are second (black piece), " + this.blackPlayer.getName() + ".");
                } else {
                    this.whitePlayer = new JoinedPlayer(each.client, each.getClientName(), Team.WHITE, this.clientName, Team.BLACK);  //the one who accepted
                    this.blackPlayer = new JoinedPlayer(this.client, this.clientName, Team.BLACK, each.getClientName(), Team.WHITE);  //the one who made request

                    each.outputStream.writeUTF("\nBased on coin toss, you are first (white piece), " + this.whitePlayer.getName() + ".");
                    outputStream.writeUTF("\nBased on coin toss, you are second (black piece), " + this.blackPlayer.getName() + ".");
                }
                this.currentPlayer = this.whitePlayer;
                each.setStatus("playing");
                this.setStatus("playing");
                statChange = true; //used for broadcasting new list of available players
                return 1; //break from outer to join messaging with player who accepted
        }
        return i;
    }

    /**
     * @throws IOException
     */
    public void messaging() throws IOException {
        String inputMsg = "", receiverName = "", msg = "";
        try {
            while (true) {
                inputMsg = inputStream.readUTF();
                if (inputMsg == null) continue;
                System.out.println("inputMsg = " + inputMsg);
                if (inputMsg.equals("disconnect")) {  //haven't found a way to let opponent know other player disconnected
                    setStatus("disconnected");
                    inputStream.close();
                    outputStream.close();
                    closeConnection();
                }

                if (inputMsg.contains(":")) {
                    receiverName = inputMsg.substring(0, inputMsg.indexOf(":")).toLowerCase();  //name of person receiving msg
                    msg = inputMsg.substring(inputMsg.indexOf(": ") + 2).toLowerCase();  // msg after :
                }

                if (receiverName.equals(getClientName())) {
                    outputStream.writeUTF("\nYou cannot message yourself. Try again.\n");
                } else {
                    for (ClientHandler each : clientList) {  //see if name of person receiving msg exists
                        if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase())) {
                            each.outputStream.writeUTF(getClientName() + " says " + "'" + msg + "'");
                            break;
                        }
                    }
                }
            }
        } catch (SocketException e) {
            System.err.println("[METHOD: messaging()] The client disconnected prematurely\n[CLASS: ClientHandler] " + e.getStackTrace());
        }
    }

    /**
     * Allows for the ClientHandler to target another specific ClientHandler for direct messaging
     *
     * @param name    name of target client
     * @param message the message to be sent
     * @throws IOException will throw exception if connection is terminated
     */
    public void messaging(final String name, final String message) throws IOException {
        try {
            for (ClientHandler each : clientList) {  //see if name of person receiving msg exists
                if (each.getClientName().equalsIgnoreCase(name)) {
                    each.outputStream.writeUTF(message);
                }
            }
        } catch (SocketException e) {
            System.err.println("[METHOD: messaging()] The client disconnected prematurely\n[CLASS: ClientHandler] " + e.getStackTrace());
        }
    }

    @Override
    public void run() {
        try {
            welcomeClientConnection();

            //messaging client to client
            boolean outter = true;
            while (outter) {
                inputMsg = inputStream.readUTF(); //msg typed in from client

                if (!inputMsg.contains(":")) continue;
                getClientAvailability();

                if (receiverName.equals(getClientName())) {
                    outputStream.writeUTF("\nYou cannot message yourself. Try again.\n");
                } else {
                    for (ClientHandler each : clientList) {  //see if name of person receiving msg exists
                        if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase()) && !each.getStatus().equals("available")) {
                            outputStream.writeUTF(each.clientName + " is unavailable and cannot chat. Make request to an available client.\n");
                            break;
                        } else if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase()) && each.getStatus().equals("available")) {
                            each.outputStream.writeUTF(getClientName() + " says " + "'" + msg + "'");

                            int boolVal = readHeader(each);
                            if (boolVal == 0) {
                                break;
                            } else if (boolVal == 1) {
                                outter = false;
                            }
                        }
                    }
                }
            }

            if (statChange == true) {
                nameStatus.clear();
                nameStatusAdd();
                for (ClientHandler each : clientList) {
                    if (each.getStatus().equals("available"))
                        each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());
                }
            }

            // messaging for the players who joined game
//            messaging();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
        runGame(configureGame());
    }//end of run method

    //------------- private helper methods ------------------

    /**
     * work on coin toss for who takes turn first/color in this method
     */
    private String coinToss() {
        String side;

        if (Math.random() <= 0.5) {
            side = "heads";
            return side;
        } else {
            side = "tails";
            return side;
        }
    }

    /**
     * Iterates through the list of clients and adds all of their names and status's to a List
     */
    private void nameStatusAdd() {
        for (ClientHandler each : clientList) {
            if (each.getStatus().equals("available")) {
                nameStatus.add(each.getClientName() + " - " + each.getStatus());
            }
        }
    }

    /**
     * Helper method that closes the connection and encapsulates the
     */
    private void closeConnection() {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------- game methods ----------------------------

    /**
     * Initializes a chess game board by creating all of the objects and configuring all of the settings.
     *
     * @return a GameBoard at turn 0
     */
    public static GameBoard configureGame() {
        board = GameBoard.createInitialBoard();
        current = board.getCurrentPlayer();

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
    public int waitMakeMove() throws IOException{
        int start = 0, destination = 0, x = 0, y = 0;
        m = null;
        piece = null;
        messaging(this.currentPlayer.getAdversaryName(), "please wait for adversary to finish their turn");
        messaging(this.currentPlayer.getAdversaryName(), WAIT_HEADER);
        while (m == null) {
                messaging(this.currentPlayer.getPlayerName(), this.currentPlayer.getTeam() +" PLAYER: enter x coordinate for starting move");
                x = Integer.parseInt(inputStream.readUTF());
                messaging(this.currentPlayer.getPlayerName(), this.currentPlayer.getTeam() +" PLAYER: enter y coordinate for the starting move");
                y = Integer.parseInt(inputStream.readUTF());
                start = moveHelper(x, y);
                messaging(this.currentPlayer.getPlayerName(), this.currentPlayer.getTeam() + " PLAYER: enter x coordinate for destination move");
                x = Integer.parseInt(inputStream.readUTF());
                messaging(this.currentPlayer.getPlayerName(), this.currentPlayer.getTeam() + " PLAYER: enter y coordinate for the destination move");
                y = Integer.parseInt(inputStream.readUTF());
                destination = moveHelper(x, y);
            System.out.println(start);
            System.out.println(destination);

            tile = board.getTile(start);
            piece = tile.getPiece();

            m = MoveFactory.createMove(board, start, destination);

            if (m == null) {
                messaging(this.currentPlayer.getPlayerName(), board.toString());
                messaging(this.currentPlayer.getPlayerName(), currentPlayer.getTeam() + "invalid move");
            }
        }
        messaging(this.currentPlayer.getAdversaryName(), EXIT_HEADER);
        return destination;
    }

    /**
     * runs the game on the server
     */
    public GameBoard runGame(GameBoard gb) {
        board = gb;
        try {
            boolean draw = false;
            while (!draw) {
                w = board.getWhitePlayer();
                b = board.getBlackPlayer();

                messaging(this.currentPlayer.getPlayerName(), board.toString());
                messaging(this.currentPlayer.getAdversaryName(), board.toString());

                //players either makes move or waits for other player to finish move
                int destination = waitMakeMove();

                messaging(this.currentPlayer.getAdversaryName(),"the " + current.getTeam() + " has selected " + piece.toString() + " going to " + destination);
                messaging(this.currentPlayer.getPlayerName(),"the " + current.getTeam() + " has selected " + piece.toString() + " going to " + destination);

                transition = board.getCurrentPlayer().move(m);
                if (transition.getStatus().isFinished()) {
                    messaging(this.currentPlayer.getAdversaryName(),current.getTeam() + " player is finished");


                    //TODO: go into waiting state for response
                    board = transition.getBoardState();
                    current = board.getCurrentPlayer().getAdversary();
                    if(this.currentPlayer.getAdversaryTeam() == this.whitePlayer.getTeam()){
                        this.currentPlayer = this.whitePlayer;
                    }else{
                        this.currentPlayer = this.blackPlayer;
                    }

                    draw = isDraw();
                }
            }
        } catch (IOException e) {
            System.err.println("[EXCEPTION] an exception was thrown in the runGame method in the ClientHandler class " + e.getStackTrace());
        }
        return board;
    }

    /**
     * TODO: not right it should look at available moves list
     *
     * @return
     */
    private boolean isDraw() {
        boolean draw = false;
        if (w.inCheck()) {
            System.out.println("white players king is in check");
            draw = true;
        } else if (b.inCheck()) {
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

    //-------------- getters and setters ---------------------

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}