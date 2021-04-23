package com.thechessparty.connection;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    //instance variables
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private ArrayList<ClientHandler> clientList;
    private DataInputStream inputStream;
    private DataOutputStream outputStream;
    private String clientName;
    private JoinedPlayer firstPlayer;
    private JoinedPlayer secondPlayer;

    // constructor
    public ClientHandler(Socket clientSocket, String clientName, ArrayList<ClientHandler> clientList, DataInputStream inputStream, DataOutputStream outputStream) throws IOException {
        this.client = clientSocket;
        this.clientList = clientList;
        //this.input = new BufferedReader(new InputStreamReader(inputStream));
        //this.output = new PrintWriter(outputStream, true);
        this.inputStream = inputStream;
        this.outputStream = outputStream;
        this.clientName = clientName;
    }

    //------------------- public methods ----------------------------

    @Override
    public void run() {
        String inputMsg;

        try {
            outputStream.writeUTF(getClientName());

            outputStream.writeUTF("Welcome " + getClientName() + ". " + "\nCurrent Available Waiting Player(s): ");

            String list = "";

            for(int i = 0; i < clientList.size()-1; i++) {
                list += clientList.get(i).getClientName() + " ";
            }

            if(list != "") {
                outputStream.writeUTF(list + "\n\nPlease message player you want to play with strictly following this format and replies." +
                        "\nExample: Bob is player you want to message. 'Request' to request game, 'no' to reject, 'yes' to accept." +
                        "\nbob: request | bob: no | bob: yes");

            } else {
                outputStream.writeUTF("\nYou're the only one waiting.");
            }


            //messaging client to client (please check receiving client to see msgs are coming through properly. sometimes letters disappear/weird symbols pop up).

            while (true) {
                inputMsg = inputStream.readUTF();
                System.out.println(inputMsg);

                //incoming msg broken down below
                String receiverName = inputMsg.substring(0, inputMsg.indexOf(":")).toLowerCase();  //name of person receiving msg
                String msg = inputMsg.substring(inputMsg.indexOf(": ") + 2).toLowerCase();  // msg after :

                if (!msg.equals("request") && !msg.equals("yes") && !msg.equals("no")) {
                    outputStream.writeUTF("Invalid Response. Try again.");
                } else {
                    //check to see if receiverName exists in clientList
                    for (ClientHandler each : clientList) {
                        if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase())) {
                            each.outputStream.writeUTF(this.clientName + " says " + "'" + msg + "'");
                            outputStream.flush();
                            //person requesting automatically heads
                            if (msg.equals("request")) {
                                break;
                            } else if (msg.equals("no")) {
                                each.outputStream.writeUTF(this.clientName + " denied your request. Choose another available player.");
                                outputStream.flush();
                                break;
                            } else if (msg.equals("yes")) {
                                if (coinToss() == "heads") {
                                    firstPlayer = new JoinedPlayer(this.client, this.clientName, "white");  //the one who made request (auto heads)
                                    secondPlayer = new JoinedPlayer(each.client, each.getClientName(), "black");  //the one who accepted
                                } else {
                                    firstPlayer = new JoinedPlayer(each.client, each.getClientName(), "white");  //the one who accepted
                                    secondPlayer = new JoinedPlayer(this.client, this.clientName, "black");  //the one who made request
                                }
                                break;
                            }
                        }
                    }
                }
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }

        /*
        try {
            closeConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
         */

    }

/*
//original
    public void run() {
        try {
            while (true) {
                String request = input.readLine();
                System.out.println(request);
                String rq = request.substring(request.indexOf(": ") + 2);
                if (rq.startsWith("name")) {
                    getOut().println("connection was made in handler");
                } else if (true//rq.startsWith("request")) {
                    //TODO: fix the logic here possibly utilize the Json parser
                    int firstSpace = request.indexOf(" ");
                    if (firstSpace != -1) {
                        clientBroadcast(request.substring(12));
                    }
                } else {
                    getOut().println("Server response: " + request);
                }
            }
        } catch (IOException e) {
            System.err.println("IO Exception in client handler");
            System.err.println(e.getStackTrace());
        } finally {
            getOut().close();
            closeConnection();
        }
    }
*/


    //------------- private helper methods ------------------

    /**
     * work on coin toss for who takes turn first/color in this method
     */
    private String coinToss() {
        String side;

        if(Math.random() <= 0.5) {
            side = "heads";
            return side;
        } else {
            side = "tails";
            return side;
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
}