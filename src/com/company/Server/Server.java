package com.company.Server;

import java.net.*;
import java.io.*;
import java.util.ArrayList;

public class Server {

    private static ArrayList<WorkerThread> clients = new ArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        int port = 2048;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port " + port + ".");
            System.exit(-1);
        }

        while (listening) {
            System.out.println("[SERVER] Waiting for a new client connection...");
            Socket client = serverSocket.accept();
            System.out.println("[SERVER] Client "+ client.getRemoteSocketAddress().toString() + " has connected!");
            WorkerThread clientThread = new WorkerThread(client,clients);
            clients.add(clientThread);
            clientThread.start();
        }

        serverSocket.close();
    }
}
