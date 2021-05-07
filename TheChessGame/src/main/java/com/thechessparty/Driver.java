package com.thechessparty;

import com.thechessparty.connection.Client;
import com.thechessparty.connection.MessageDTO;

import java.util.Scanner;

public class Driver {

    private static Thread gameManager;
    private static Thread client;
    private static Scanner scanner = new Scanner(System.in);
    private static String clientString;

    public Driver(){
      //   gameManager = new Thread(new GameManager());
    }

    public static void main(String[] args) {
        client = new Thread(new Client());
        client.run();

        while (true){

        }
    }

    //---------------- getters and setters ------------------

    public static Thread getGameManager() {
        return gameManager;
    }

    public static void setGameManager(Thread gameManager) {
        Driver.gameManager = gameManager;
    }

    public static Thread getClient() {
        return client;
    }

    public static void setClient(Thread client) {
        Driver.client = client;
    }

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        Driver.scanner = scanner;
    }

    public static String getClientString() {
        return clientString;
    }

    public static void setClientString(String clientString) {
        Driver.clientString = clientString;
    }
}
