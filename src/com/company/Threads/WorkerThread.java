package com.company.Threads;

import com.company.Models.Client;
import com.company.Server.CentralNodesInstructions;
import com.company.Server.Server;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

public class WorkerThread extends Thread {

    private Socket clientSocket;
    //    private ArrayList<WorkerThread> clients;
    private PrintWriter out;
    private BufferedReader in;
    private CentralNodesInstructions centralNodesInstructions;
    private ArrayList<Client> clientsList;


    public WorkerThread(Socket clientSocket, ArrayList<Client> clientsList) throws IOException {
        super("WorkerThread");
        this.clientSocket = clientSocket;
//        this.clients = clients;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.centralNodesInstructions = new CentralNodesInstructions();
        this.clientsList = clientsList;

//        String contentMessage = in.readLine();
////            System.out.println(contentMessage);
//        Gson gson = new Gson();
//        Client clientSend = gson.fromJson(contentMessage, Client.class);

        //System.out.println("Server: " + clientSend.toString());
    }


    public void run() {
        try {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("inputLine: " + inputLine);
                System.out.println("Resposta do cliente " + clientSocket.getRemoteSocketAddress().toString() + ": " + inputLine);
                System.out.println("Resposta do nó central: " + this.centralNodesInstructions.setInstruction(clientSocket, inputLine));
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

