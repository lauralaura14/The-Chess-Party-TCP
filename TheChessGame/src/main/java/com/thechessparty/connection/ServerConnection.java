package com.thechessparty.connection;

import java.io.*;
import java.net.Socket;

public class ServerConnection implements Runnable {

    // instance variables
    private Socket server;
    private BufferedReader input;
    private PrintWriter output;
    private String clientID;

    //constructor
    public ServerConnection(Socket s, String id) throws IOException {
        this.server = s;
        this.input = new BufferedReader(new InputStreamReader(server.getInputStream()));
        this.output = new PrintWriter(server.getOutputStream(), true);
        this.clientID = id;
    }

    //--------------- public methods ----------------------------

    @Override
    public void run() {
        try {
            while (true) {
                String serverResponse = input.readLine();

                //if the server had terminated the connection
                if(serverResponse == null) break;

                System.out.println("[" + getClientID() + "] " + serverResponse);
            }
        } catch (IOException e) {
            System.err.println("Exception occurred in ServerConnection run method...");
            e.printStackTrace();
        } finally {
            closeConnection();
        }
    }

    //----------------- private helper methods -----------------------

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

    //------------ getters and setters -----------------------

    public Socket getServer() {
        return server;
    }

    public void setServer(Socket server) {
        this.server = server;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setInput(BufferedReader input) {
        this.input = input;
    }

    public PrintWriter getOutput() {
        return output;
    }

    public void setOutput(PrintWriter output) {
        this.output = output;
    }

    public String getClientID() {
        return clientID;
    }

    public void setClientID(String clientID) {
        this.clientID = clientID;
    }
}