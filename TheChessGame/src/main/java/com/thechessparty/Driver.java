package com.thechessparty;

import com.thechessparty.connection.Client;

import java.util.Scanner;

public class Driver {

    //class variables
    private static Scanner scanner = new Scanner(System.in);
    private static Client client;

    public Driver(){}

    public static void main(String[] args) {
        client = new Client();
        client.run();
    }

    //---------------- getters and setters ------------------

    public static Scanner getScanner() {
        return scanner;
    }

    public static void setScanner(Scanner scanner) {
        Driver.scanner = scanner;
    }

}
