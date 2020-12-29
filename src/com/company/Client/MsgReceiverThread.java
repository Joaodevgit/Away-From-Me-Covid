package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgReceiverThread extends Thread {

    Socket socket;
    BufferedReader in;

    public MsgReceiverThread(Socket socket) throws IOException {
        super("MsgReceiverThread");
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //System.out.println("Server Response: " + inputLine);
                AlertUserBox.display("NOTIFY ALL", inputLine + "\nSocket: " + this.socket.getRemoteSocketAddress().toString());
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            in.close();
            //socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
