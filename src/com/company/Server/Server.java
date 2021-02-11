package com.company.Server;

import java.net.*;
import java.io.*;

/**
 * Class responsible for
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
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

        // Server Multicast Thread
        new MulticastServerSenderThread(clientsConnected).start();
        // Broadcast Server Thread
        new BroadcastServerSenderThread(clientsConnected).start();


        while (listening) {
            System.out.println("[SERVER] Waiting for a client connection...");
            Socket client = serverSocket.accept();
            System.out.println("[SERVER] Client " + client.getRemoteSocketAddress().toString() + " has connected!");

            WorkerThread clientThread = new WorkerThread(client, clientsConnected);
            clientsConnected.add(clientThread);
            clientThread.start();
        }

        serverSocket.close();
    }
}
