package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.atomic.AtomicReference;

public class MsgSenderThread extends Thread {
    Socket socket;
    PrintWriter out;
    String msg;

    boolean listening = true;

    public MsgSenderThread(Socket socket, String msg) throws IOException {
        super("MsgSenderThread");
        this.socket = socket;
        out = new PrintWriter(socket.getOutputStream(), true);
        this.msg = msg;
    }

    public void run() {

        //BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        //String userInput;
        //while ((userInput = stdIn.readLine()) != null) {
        out.println(this.msg);
        //}
        out.close();
        //socket.close();
        //stdIn.close();
    }
}
