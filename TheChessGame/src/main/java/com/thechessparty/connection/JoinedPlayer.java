package com.thechessparty.connection;

import java.net.Socket;

public class JoinedPlayer {

    private Socket client;
    private String playerName;
    private String color;

    public JoinedPlayer(Socket client, String playerName, String color) {
        this.client = client;
        this.playerName = playerName;
        this.color = color;
    }

    public String getName() {
        return this.playerName;
    }

    public String getColor() {
        return this.color;
    }

}
