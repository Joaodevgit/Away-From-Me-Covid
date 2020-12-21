package com.company.Threads;

import com.company.Client.CentralNodesInstructions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class WorkerThread extends Thread {

    private Socket clientSocket;
    ArrayList<WorkerThread> clients;
    PrintWriter out;
    BufferedReader in;
    CentralNodesInstructions centralNodesInstructions;


    public WorkerThread(Socket clientSocket, ArrayList<WorkerThread> clients) throws IOException {
        super("WorkerThread");
        this.clientSocket = clientSocket;
        this.clients = clients;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.centralNodesInstructions = new CentralNodesInstructions();
    }


    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Client " + clientSocket.getRemoteSocketAddress().toString() + " Response: " + inputLine);
                System.out.println("Resposta do nó central" + this.centralNodesInstructions.setInstruction(clientSocket, inputLine));
                out.println(this.centralNodesInstructions.setInstruction(clientSocket, inputLine));
                if (inputLine.equals("Bye"))
                    break;
            }
            out.close();
            in.close();
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void sendMessageToAllClients(String msg, Socket client) {
//        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
//        LocalDateTime now = LocalDateTime.now();
//        for (WorkerThread aClient : clients) {
//            aClient.out.println(dtf.format(now) + " O Cliente: " + client.getRemoteSocketAddress().toString() + " diz: " + msg);
//        }
//    }
}

