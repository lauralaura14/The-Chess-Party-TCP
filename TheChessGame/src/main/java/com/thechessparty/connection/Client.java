package com.thechessparty.connection;

import com.thechessparty.engine.GameManager;
import com.thechessparty.engine.Team;
import org.checkerframework.checker.units.qual.C;

import java.net.*;
import java.io.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Client implements Runnable {

    private static Thread outgoingPrivateMsg;
    private static Thread incomingPrivateMsg;

    private volatile boolean userScanner = true;
    private static volatile Scanner scanner = new Scanner(System.in);

    // initialize socket and input output streams
    private static final int port = 5001;
    private static String serverIP = "127.0.0.1";
    private static String clientID;
    private static Thread IncomingPrivateMsg;
    private static Thread OutgoingPrivateMsg;
    private static Runnable gameManager;
    private static String msg = "";
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;

    private static Team clientTeam;
    private static boolean isInGame = false;

    // constructor to set ip address and port
    public Client() {
        this("127.0.0.1", 5001);
    }

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

    //------------------- public methods ----------------------

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

    /**
     * @param input
     * @return
     */
    public static String appendHeader(String input) {
        return getClientID() + "--GAMEDATA:" + input;
    }

    /**
     * Starts the incoming data thread
     */
    public void startIncomingThread() {
        incomingPrivateMsg = new Thread(new Runnable() {
            public volatile boolean exit = false;

            public void run() {
                while (!exit) {
                    try {
                        msg = input.readUTF(); // msg that comes from clientHandler
                        System.out.println(msg);
                        if (!isInGame) {
                            if (msg.contains("Based on coin toss, you are")) {
                                System.out.println("the game has started");
                                if (msg.contains("white")) {
                                    clientTeam = Team.WHITE;
                                } else {
                                    clientTeam = Team.BLACK;
                                }
                                isInGame = true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("connection to server was terminated");
                        break;
                    }
                }
            }

            public void stop() {
                exit = true;
            }
        });
        incomingPrivateMsg.start();
    }

    /**
     * starts outgoing data stream thread
     */
    public void startOutgoingThread() {
        outgoingPrivateMsg = new Thread(new Runnable() {
            private volatile boolean exit = false;

            public void run() {

                while (!exit) {

                    if (userScanner && !isInGame) {
                        msg = scanner.nextLine(); //type in from keyboard
                    } else { //if game has been established

                        msg = inputSanatizer();
                    }
                    try {
                        output.writeUTF(msg); //send to clientHandler for parsing msg
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void stop() {
                exit = true;
            }
        });
        outgoingPrivateMsg.start();
    }

    /**
     * Uses a regex pattern to ensure the data is sanitized to be a single digit between 1 and 8 inclusive.
     *
     * @return the input String value
     */
    public String inputSanatizer() {
        String pattern = "\\b[1-8]\\b";
        String userInput = "";
        boolean formated = false;
        while (!formated) {
            userInput = scanner.nextLine();
            if (Pattern.matches(pattern, userInput)) {
                formated = true;
            } else {
                System.out.println("please enter a number between 1 and 8");
            }
        }
        return userInput;
    }

    /**
     * Static test method to for JUnit testing
     * @param s String
     * @return will only return if input is between 1-8 inclusive
     */
    public static String inputSanatizer(String s) {
        String pattern = "\\b[1-8]\\b";
        String userInput = "";
        boolean formated = false;
        while (!formated) {
            userInput = s;
            if (Pattern.matches(pattern, userInput)) {
                formated = true;
            } else {
                System.out.println("please enter a number between 1 and 8");
            }
        }
        return userInput;
    }

    /**
     * Utilizes the socket to establish a connection to the server.
     *
     * @throws IOException will throw exception if server disconnects unexpectedly
     */
    public void initializeConnection() throws IOException {
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());

        String id;

        System.out.print("Enter username: ");

        while (true) {
            id = getScan().nextLine();
            output.writeUTF(id);
            String receive = input.readUTF();
            if (receive.equals("no")) {
                System.out.print("\nUsername " + id + " unavailable. Enter new username: ");
            } else if (receive.equals("ok")) {
                setClientID(id);
                break;
            }
        }
        System.out.println("\nConnection made at ip: " + getServerIp() + " on port: " + getPort() + ".\n");
    }

    @Override
    public void run() {
        try {
            // initialize the data stream connections
            initializeConnection();

            //outgoing message client to client
            startOutgoingThread();

            //incoming message client to client
            startIncomingThread();

        } catch (IOException e) {
            System.err.println("[EXCEPTION] an IOException is being thrown in the Client run method " + e.getStackTrace());

        }
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

    public static void setServerIP(String ip) {
        serverIP = ip;
    }

    public static int getPort() {
        return port;
    }

    public static Scanner getScan() {
        return scanner;
    }

    public static String getClientID() {
        return clientID;
    }

    public static void setClientID(String clientID) {
        Client.clientID = clientID;
    }
}