package com.thechessparty.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    //instance variables
    private Socket client;
    private BufferedReader input;
    private PrintWriter output;
    private ArrayList<ClientHandler> clientList;


    // constructor
    public ClientHandler(Socket clientSocket, ArrayList<ClientHandler> clientList) throws IOException {
        this.client = clientSocket;
        this.clientList = clientList;
        this.input = new BufferedReader(new InputStreamReader(client.getInputStream()));
        this.output = new PrintWriter(client.getOutputStream(), true);
    }

    //------------------- public methods ----------------------------

    @Override
    public void run() {
        try {
            while (true) {
                String request = input.readLine();
                System.out.println(request);
                String rq = request.substring(request.indexOf(": ") + 2);
                if (rq.startsWith("name")) {
                    getOut().println("connection was made in handler");
                } else if (true/*rq.startsWith("request")*/) {

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

    //------------- private helper methods ------------------

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
}
