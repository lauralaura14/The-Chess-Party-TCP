package com.thechessparty;

import com.thechessparty.connection.Client;
import com.thechessparty.engine.GameManager;

import java.util.Scanner;

public class Driver {

    private static Thread gameManager;
    private static Thread client;
    private static Scanner scanner = new Scanner(System.in);

    public Driver(){
        client = new Thread(new Client(scanner));
      //   gameManager = new Thread(new GameManager());
    }
}
