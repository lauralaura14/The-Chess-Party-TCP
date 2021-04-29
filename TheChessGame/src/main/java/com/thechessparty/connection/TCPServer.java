/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chesstcp;

import com.google.gson.Gson;
import com.thechessparty.connection.ClientHandler;
import static com.thechessparty.connection.Server.getPORT;
import com.thechessparty.engine.Team;
import com.thechessparty.engine.pieces.Knight;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Admin
 */
public class TCPServer {
    public static void main(String args[]) throws IOException {
        ServerSocket listener = new ServerSocket(5001);
        Socket client = null;
        BufferedReader input;
        PrintWriter output = null;
   
        //accepts connections and creates new ClientHandler threads to manage them
        // these threads are stored in the pool
        while (true) {
            System.out.println("[SERVER] Waiting for client connection...");
            if(client == null)
            {
                client = listener.accept();
                input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                output = new PrintWriter(client.getOutputStream(), true);
                continue;
            }
            Knight knight = new Knight(0, Team.BLACK);
            Gson json = new Gson();
            System.out.println("The data in knight is as follows:\n");
            knight.Display();
            String object = json.toJson(knight);
            System.out.println("The JSON of knight is as follows:"+object +"\n");
            output.println(object);
            break;
            
        }

    }
    
}
