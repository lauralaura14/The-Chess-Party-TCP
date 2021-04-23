package com.thechessparty.connection;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client {

    private static Thread outgoingPrivateMsg;
    private static Thread incomingPrivateMsg;

    // initialize socket and input output streams
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private static final String serverIP = "127.0.0.1";
    private static final int port = 5000;
    private static final Scanner scan = new Scanner(System.in);
    private static String clientID;
    private static Thread IncomingPrivateMsg;
    private static Thread OutgoingPrivateMsg;

    // constructor to set ip address and port
    public Client(String address, int port) {
        // establish a connection
        try {
            setServerIP(address);
            setSocket(new Socket(address, port));
            System.out.println("Connected at address" + Client.getServerIp() + " on port" + getPort());

            // takes input from terminal
            this.input = new DataInputStream(System.in);

            // sends output to the socket
            this.output = new DataOutputStream(socket.getOutputStream());

        } catch (UnknownHostException u) {
            System.out.println(u.getMessage());
        } catch (IOException i) {
            System.out.println(i.getMessage());
        }
    }

    /**
     * Logic for running the client
     * NOTE: currently not in use
     * TODO: possibly try to refactor the logic in the main method to this method
     */
    public void runClient() {
        // string to read message from input
        String line = "";

        // keep reading until "Over" is input
        while (!line.equals("TERMINATE")) {
            try {
                if (getInput() == null) {
                    System.out.println("no server was connected unable to run the client exiting...");
                    return;
                }
                BufferedReader d
                        = new BufferedReader(new InputStreamReader(getInput()));
                line = d.readLine();
                getOutput().writeUTF(line);
            } catch (IOException | NullPointerException i) {
                System.err.println("Error occurred in the runClient method" + i);
            }
        }

        // close the connection
        try {
            getInput().close();
            getOutput().close();
            getSocket().close();
        } catch (IOException i) {
            System.out.println(i);
        }
    }

    /**
     * Allows the user to set up an ip address they wish to connect to.
     *
     * @return String of the IP address
     */
    public static String setUpIp() {
        String input = "";
        String zeroTo255
                = "(\\d{1,2}|(0|1)\\"
                + "d{2}|2[0-4]\\d|25[0-5])";

        // Regex for a digit from 0 to 255 and
        // followed by a dot, repeat 4 times.
        // this is the regex to validate an IP address.
        String regex
                = zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255 + "\\."
                + zeroTo255;

        // Compile the ReGex
        Pattern p = Pattern.compile(regex);
        try {
            System.out.println();
            while (true) {
                System.out.println("Enter the ip address you wish to connect to...");
                input = getScan().nextLine();
                Matcher m = p.matcher(input);

                if (m.matches()) {
                    System.out.println("valid ip!!");
                    break;
                } else {
                    System.out.println("ip is invalid please try again..");
                }
            }
        } catch (Exception e) {
            System.err.println("error occurred in setUpIp method" + Arrays.toString(e.getStackTrace()));
        }
        return input;
    }

    /**
     * Allow the user to enter the port number via input from the keyboard
     * NOTE: May not need to use this method may make port number final
     *
     * @return int of the port number that was entered
     */
    public static int setUpPort() {
        String input;
        int port = 0;
        String portReg = "(\\d{1,5})";
        Pattern p = Pattern.compile(portReg);
        try {
            while (true) {
                System.out.println("Enter the port number you wish to connect with...");
                input = getScan().nextLine();
                Matcher m = p.matcher(input);

                if (m.matches()) {
                    System.out.println("valid port bust out the port!!");
                    port = Integer.parseInt(input);
                    break;
                } else {
                    System.out.println("port is invalid you dummy try again..");
                }
            }
        } catch (Exception e) {
            System.err.println("Error occurred in setUpPort method " + Arrays.toString(e.getStackTrace()));
        }
        return port;
    }

    //-------------- main access method -------------------------

    public static void main(String args[]) throws IOException {
        Socket socket = new Socket(getServerIp(), getPort());

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        //System.out.println("enter username");
        //String id = getScan().nextLine();
        String id = inputStream.readUTF();
        setClientID(id);
        //ServerConnection serverConn = new ServerConnection(socket, getClientID());

        //BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        //PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //new Thread(serverConn).start();

        System.out.println("Connection made at ip: " + getServerIp() + " on port: " + getPort());

        String waitList = inputStream.readUTF();
        System.out.println(waitList);

        String currentStateMsg = inputStream.readUTF();
        System.out.println(currentStateMsg);

        /**
         * outgoing message client to client
         */
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    String msg = getScan().nextLine();
                    try {
                        outputStream.writeUTF(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        outgoingPrivateMsg.start();


        /**
         * incoming message client to client
         */
        new Thread(new Runnable() {
            public void run() {
                while(true) {
                    try {
                        String msg = inputStream.readUTF();
                        System.out.println(msg);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        incomingPrivateMsg.start();
/*
        while (true) {
            System.out.println("[" + getClientID() + "]> ");
            String command = keyboard.readLine();
            if (command.equals("-quit")) break;
            out.println("FROM[" + getClientID() + "]: " + command);
        }
        socket.close();
        System.exit(0);
 */
    }



    //--------- Getters and setters -----------------------------

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public DataInputStream getInput() {
        return input;
    }

    public void setInput(DataInputStream input) {
        this.input = input;
    }

    public DataOutputStream getOutput() {
        return output;
    }

    public void setOutput(DataOutputStream output) {
        this.output = output;
    }

    public static String getServerIp() {
        return serverIP;
    }

    public static void setServerIP(String serverIP) {
        serverIP = serverIP;
    }

    public static int getPort() {
        return port;
    }

    public static Scanner getScan() {
        return scan;
    }

    public static String getClientID() {
        return clientID;
    }

    public static void setClientID(String clientID) {
        Client.clientID = clientID;
    }

}