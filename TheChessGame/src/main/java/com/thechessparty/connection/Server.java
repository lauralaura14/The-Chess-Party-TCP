package com.thechessparty.connection;

import java.net.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 5001;

    private static Scanner scan = new Scanner(System.in);
    private static boolean connected = false;
    //initialize socket and input stream
    private Socket socket;
    private ServerSocket server;
    private DataInputStream input;
    private static String clientName;

    private static ArrayList<String> checkNameList = new ArrayList<>();

    // Server class variables
    private static final ArrayList<ClientHandler> clientList = new ArrayList<>();

    // 256 is maximum number of threads that each JDK can handle per IBM
    private static final ExecutorService pool = Executors.newFixedThreadPool(256);

    //-------------- main access method -----------------------

    public static void main(String args[]) throws IOException {
        ServerSocket listener = new ServerSocket(getPORT());

        //accepts connections and creates new ClientHandler threads to manage them
        // these threads are stored in the pool
        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            Socket client = listener.accept();
            DataInputStream inputClient = new DataInputStream(client.getInputStream()); // from client
            DataOutputStream outputClient = new DataOutputStream(client.getOutputStream());
            System.out.println("[Server] Connected to client\n");
            connected = true;

            //System.out.println("Current List of Clients Connected to Server: " + checkNameList + "\n");
            try {
                if (connected) {
                    System.out.println("Waiting for client username.\n");
                    while (true) {
                        //clientName = getScan().next().toLowerCase();
                        clientName = inputClient.readUTF();
                        if (checkNameList.contains(clientName.toLowerCase())) {
                            outputClient.writeUTF("no");
                        } else {
                            outputClient.writeUTF("ok");
                            System.out.println(clientName + " has connected.\n");
                            break;
                        }
                    }

                    ClientHandler clientThread = new ClientHandler(client, clientName, clientList, inputClient, outputClient);
                    clientList.add(clientThread);
                    //pool.execute(clientThread);
                    checkNameList.add(clientName);
                    Thread threadClient = new Thread(clientThread);
                    threadClient.start();
                }
            } catch (SocketException e){
                System.err.println("connection from client was unexpectedly terminated: " + e.getStackTrace());
            }
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

    public static Scanner getScan() {
        return scan;
    }
}