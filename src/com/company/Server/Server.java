package com.company.Server;

import java.net.*;
import java.io.*;

public class Server {

    private static SynchronizedArrayList<WorkerThread> clientsConnected = new SynchronizedArrayList<>();

    public static void main(String[] args) throws IOException {

        ServerSocket serverSocket = null;
        int port = 2048;
        boolean listening = true;

        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Não foi possível ligar-se à porta " + port + ".");
            System.exit(-1);
        }

        // Thread do server multicast
        new MulticastServerSenderThread(clientsConnected).start();
        // Thread do server broadcast
        new BroadcastServerSenderThread(clientsConnected).start();


        while (listening) {
            System.out.println("[SERVER] Aguardando por uma conexão do cliente...");
            Socket client = serverSocket.accept();
            System.out.println("[SERVER] Cliente " + client.getRemoteSocketAddress().toString() + " conectou-se!");

            WorkerThread clientThread = new WorkerThread(client, clientsConnected);
            clientsConnected.add(clientThread);
            clientThread.start();
        }

        serverSocket.close();
    }
}
