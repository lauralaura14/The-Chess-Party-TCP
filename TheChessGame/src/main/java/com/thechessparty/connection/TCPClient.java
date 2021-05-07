/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.thechessparty.connection;

import com.google.gson.Gson;
import static com.thechessparty.connection.Client.*;
import com.thechessparty.connection.ServerConnection;
import com.thechessparty.engine.pieces.Knight;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Admin
 */
public class TCPClient {
    
    public static void main(String args[]) throws IOException {
        Socket socket = new Socket("127.0.0.1", 5001);
        


        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);



        try {
            while (true) {
                String serverResponse = input.readLine();

                //if the server had terminated the connection
                if(serverResponse == null) break;
                Gson json = new Gson();
                System.out.println("The JSON of knight is as follows:"+serverResponse +"\n");
                Knight knight1 = json.fromJson(serverResponse, Knight.class);
                
                //40knight1.Display();
                break;
                
            }
        } catch (IOException e) {
            System.err.println("Exception occurred in ServerConnection run method...");
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
    
}
