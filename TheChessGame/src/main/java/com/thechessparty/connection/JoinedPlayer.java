package com.thechessparty.connection;

import java.net.Socket;

public class JoinedPlayer {

    private static Socket client;
    private static String clientName;

    public JoinedPlayer(Socket client, String clientName) {
        this.client = client;
        this.clientName = clientName;
    }

    //add color determined later

}