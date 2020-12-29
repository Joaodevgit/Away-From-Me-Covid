package com.company.Server;

import com.company.Models.Client;
import com.google.gson.Gson;

import java.net.*;
import java.io.*;

public class Server {

    private static SynchronizedArrayList<WorkerThread> clientsConnected = new SynchronizedArrayList<>();
    protected static SynchronizedArrayList<Client> clientsList = new SynchronizedArrayList<>();

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
            System.out.println("[SERVER] Client " + client.getRemoteSocketAddress().toString() + " has connected!");

            WorkerThread clientThread = new WorkerThread(client, clientsConnected);

            clientsConnected.add(clientThread);
            //clientsList.add(clientModel);
            clientThread.start();
        }

        serverSocket.close();
    }
}
