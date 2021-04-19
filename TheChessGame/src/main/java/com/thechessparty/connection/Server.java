package com.thechessparty.connection;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5000;

    //initialize socket and input stream
    private Socket socket;
    private ServerSocket server;
    private DataInputStream input;

    // Server class variables
    private static final ArrayList<ClientHandler> clientList = new ArrayList<>();

    // 256 is maximum number of threads that each JDK can handle per IBM
    private static final ExecutorService pool = Executors.newFixedThreadPool(256);

    public Server(){}

    // constructor with port
    public Server(int port) {
        // starts server and waits for a connection
//        try {
//            setServer(new ServerSocket(port));
//            System.out.println("Server started");
//
//            System.out.println("Waiting for a client ...");
//
//            setSocket(server.accept());
//            System.out.println("Client accepted");
//
//            // takes input from the client socket
//            setIn(new DataInputStream(
//                    new BufferedInputStream(socket.getInputStream())));
//
//            String line = "";
//
//            // reads message from client until "Over" is sent
//            while (!line.equals("TERMINATE")) {
//                try {
//                    line = getIn().readUTF();
//                    System.out.println(line);
//
//                } catch (IOException i) {
//                    System.out.println(i.getMessage());
//                }
//            }
//            System.out.println("Closing connection");
//
//            // close connection
//            getSocket().close();
//            getIn().close();
//        } catch (IOException i) {
//            System.out.println(i.getMessage());
//        }
    }

    //-------------- main access method -----------------------

    public static void main(String args[]) throws IOException {
        ServerSocket listener = new ServerSocket(getPORT());

        //accepts connections and creates new ClientHandler threads to manage them
        // these threads are stored in the pool
        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket client = listener.accept();
            System.out.println("[Server] Connected to client");
            ClientHandler clientThread = new ClientHandler(client, clientList);
            clientList.add(clientThread);
            pool.execute(clientThread);
        }

    }

    //-------------- Getters and Setters ----------------------

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public ServerSocket getServer() {
        return server;
    }

    public void setServer(ServerSocket server) {
        this.server = server;
    }

    public DataInputStream getInput() {
        return input;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public static int getPORT() {
        return PORT;
    }
}