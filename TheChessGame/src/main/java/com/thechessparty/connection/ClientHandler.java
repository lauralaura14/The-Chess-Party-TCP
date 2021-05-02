package com.thechessparty.connection;

import java.io.*;
import java.net.Socket;
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
        try {
            String inputMsg;

            outputStream.writeUTF("Welcome " + getClientName() + ".\n");

            this.setStatus("available");
            nameStatusAdd();

            for (ClientHandler each : clientList) {
                if (each.getStatus().equalsIgnoreCase("available"))
                    each.outputStream.writeUTF("Current List: " + nameStatus.toString());

                if (each.getStatus().equals("available"))
                    each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());

            }

            outputStream.writeUTF("\nPlease message player you want to play with strictly following this format." +
                    "\n'game req' to make request, 'no req' to reject, 'yes req' to accept. For example: " +
                    "\nname: game req | name: no req | name: yes req\n");

            //messaging client to client.
            outer:
            while (true) {
                inputMsg = inputStream.readUTF();

                if (inputMsg.equalsIgnoreCase("disconnect")) {
                    setStatus("disconnected");
                    nameStatus.clear();
                    nameStatusAdd();

                    for (ClientHandler each : clientList) {

                        if (each.getStatus().equalsIgnoreCase("available"))
                            each.outputStream.writeUTF("Current List: " + nameStatus.toString());

                        if (each.getStatus().equals("available"))
                            each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());

                    }
                    inputStream.close();
                    outputStream.close();
                    closeConnection();
                }

                //incoming msg separated into receiver name and message itself
                String receiverName = inputMsg.substring(0, inputMsg.indexOf(":")).toLowerCase();  //name of person receiving msg
                String msg = inputMsg.substring(inputMsg.indexOf(": ") + 2).toLowerCase();  // msg after :

                if (receiverName.equalsIgnoreCase(getClientName())) {
                    outputStream.writeUTF("\nYou cannot message yourself. Try again.\n");
                } else {
                    for (ClientHandler each : clientList) {  //see if name of person receiving msg exists

                        if (each.getClientName().toLowerCase().equalsIgnoreCase(receiverName.toLowerCase()) && !each.getStatus().equalsIgnoreCase("available")) {
                            outputStream.writeUTF(each.clientName + " is unavailable. Make request to an available client.\n");
                            break;
                        } else if (each.getClientName().toLowerCase().equalsIgnoreCase(receiverName.toLowerCase()) && each.getStatus().equalsIgnoreCase("available")) {
                            if (!msg.equalsIgnoreCase("request") && !msg.equalsIgnoreCase("yes") && !msg.equalsIgnoreCase("no")) {
                                outputStream.writeUTF("\nInvalid Response. Try again.\n");
                                break;
                            } else {

                                if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase()) && !each.getStatus().equals("available")) {
                                    outputStream.writeUTF(each.clientName + " is unavailable and cannot chat. Make request to an available client.\n");
                                    break;
                                } else if (each.getClientName().toLowerCase().equals(receiverName.toLowerCase()) && each.getStatus().equals("available")) {
                                    //if (!msg.equals("request") && !msg.equals("yes") && !msg.equals("no")) {
                                    //    outputStream.writeUTF("\nInvalid Response. Try again.\n");
                                    //    break;
                                    //} else {
                                    each.outputStream.writeUTF(getClientName() + " says " + "'" + msg + "'");
                                    //}
                                    //person requesting automatically heads
                                    switch (msg) {
                                        case "game req":
                                            break;
                                        case "no req":
                                            each.outputStream.writeUTF("\n" + getClientName() + " denied your request. Choose another available player.\n");
                                            break;
                                        case "yes req":
                                            each.outputStream.writeUTF("\nSince you requested, you are automatically heads, " + each.clientName + ".\n...");
                                            outputStream.writeUTF("\nPlayer who accepts is automatically tails, " + this.clientName + ".\n...");

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

                                            break outer;
                                    }
                                }
                            }
                        }
                    }

                    nameStatus.clear();
                    nameStatusAdd();

                    for (ClientHandler each : clientList) {
                        if (each.getStatus().equals("available"))
                            each.outputStream.writeUTF("Current Available Waiting Player(s): " + nameStatus.toString());
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