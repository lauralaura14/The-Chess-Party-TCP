package com.thechessparty.connection;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class ClientHandler implements Runnable {

    //instance variables
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private final ArrayList<ClientHandler> clientList;
    private final DataInputStream inputStream;
    private final DataOutputStream outputStream;
    private String clientName;
    private String status;
    private JoinedPlayer firstPlayer;
    private JoinedPlayer secondPlayer;
    private final List<String> nameStatus = new ArrayList<>();
    private boolean statChange = false;
    private static String inputMsg;
    private static String msg;
    private static String receiverName;

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
     *
     * @throws IOException
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
     *
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

    public int readHeader(ClientHandler each) throws IOException {
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
                return 1; // break out from outer to join messaging with player who made request
            case "starting game":
                this.outputStream.writeUTF("\nSince you requested, you are automatically heads, " + this.clientName + ".\n...");
                each.outputStream.writeUTF("\nPlayer who accepts is automatically tails, " + each.clientName + ".\n...");

                if (coinToss() == "heads") {
                    firstPlayer = new JoinedPlayer(this.client, getClientName(), "white");  //the one who made request (auto heads)
                    secondPlayer = new JoinedPlayer(each.client, each.getClientName(), "black");  //the one who accepted

                    outputStream.writeUTF("\nBased on coin toss, you are first (white piece), " + firstPlayer.getName() + ".");
                    each.outputStream.writeUTF("\nBased on coin toss, you are second (black piece), " + secondPlayer.getName() + ".");

                } else {
                    firstPlayer = new JoinedPlayer(each.client, each.getClientName(), "white");  //the one who accepted
                    secondPlayer = new JoinedPlayer(this.client, getClientName(), "black");  //the one who made request

                    each.outputStream.writeUTF("\nBased on coin toss, you are first (white piece), " + firstPlayer.getName() + ".");
                    outputStream.writeUTF("\nBased on coin toss, you are second (black piece), " + secondPlayer.getName() + ".");

                }
                each.setStatus("playing");
                this.setStatus("playing");
                statChange = true; //used for broadcasting new list of available players
                return 1; //break from outer to join messaging with player who accepted
        }
        return i;
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
                            if(boolVal == 0){
                                break;
                            }else if (boolVal == 1){
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
            messaging();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        try {
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }//end of run method

    /**
     * @throws IOException
     */
    public void messaging() throws IOException {
        String inputMsg = "", receiverName = "", msg = "";
        try {
            while (true) {
                inputMsg = inputStream.readUTF();
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
            System.err.println("The client disconnected prematurely [METHOD: messaging()\nCLASS: ClientHandler] " + e.getStackTrace());
        }
    }

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

    private void nameStatusAdd() {
        for (ClientHandler each : clientList) {
            if (each.getStatus().equals("available")) {
                nameStatus.add(each.getClientName() + " - " + each.getStatus());
            }
        }
    }

    /**
     * Iterates through the list of clients and sends a message to each of them
     *
     * @param msg String of the message that is to be broadcast
     */

    private void clientBroadcast(String msg) {
        for (ClientHandler client : clientList) {
            client.output.println(msg);
        }
    }

    /**
     * Helper method that closes the connection and encapsulates the
     */
    private void closeConnection() {
        try {
            getInput().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //-------------- getters and setters ---------------------

    public Socket getClient() {
        return client;
    }

    public void setClient(Socket client) {
        this.client = client;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public PrintWriter getOut() {
        return output;
    }

    public void setOut(PrintWriter out) {
        this.output = out;
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