package com.thechessparty.connection;

import java.util.Scanner;

public class MessageDTO {

    private static volatile String gameMessageIncoming;
    private static volatile String gameMessageOutgoing;
    private static volatile String clientMessageIncoming;
    private static volatile String clientMessageOutgoing;

    public static String getGameMessageIncoming() {
        return gameMessageIncoming;
    }

    public static void setGameMessageIncoming(String gameMessageIncoming) {
        MessageDTO.gameMessageIncoming = gameMessageIncoming;
    }

    public static String getGameMessageOutgoing() {
        return gameMessageOutgoing;
    }

    public static void setGameMessageOutgoing(String gameMessageOutgoing) {
        MessageDTO.gameMessageOutgoing = gameMessageOutgoing;
    }

    public static String getClientMessageIncoming() {
        return clientMessageIncoming;
    }

    public static void setClientMessageIncoming(String clientMessageIncoming) {
        MessageDTO.clientMessageIncoming = clientMessageIncoming;
    }

    public static String getClientMessageOutgoing() {
        return clientMessageOutgoing;
    }

    public static void setClientMessageOutgoing(String clientMessageOutgoing) {
        MessageDTO.clientMessageOutgoing = clientMessageOutgoing;
    }
}
