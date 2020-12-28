package com.company.Server;

import com.company.Models.Client;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class WorkerThread extends Thread {

    private Socket clientSocket;
    protected PrintWriter out;
    private BufferedReader in;
    private CentralNodeInstructions centralNodeInstructions;
    private SynchronizedArrayList<WorkerThread> clientsConnected;
    private SynchronizedArrayList<Client> listClient;

    public WorkerThread(Socket clientSocket, SynchronizedArrayList<WorkerThread> clientsConnected, SynchronizedArrayList<Client> listClient) throws IOException {
        super("WorkerThread");
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.centralNodeInstructions = new CentralNodeInstructions();
        this.clientsConnected = clientsConnected;
        this.listClient = listClient;
    }


    public void run() {
        try {
            String inputLine;

            while ((inputLine = in.readLine()) != null) {
                System.out.println("inputLine: " + inputLine);
                System.out.println("Resposta do cliente " + clientSocket.getRemoteSocketAddress().toString() + ": " + inputLine);

                Gson gson = new Gson();
                Client client = gson.fromJson(inputLine, Client.class);

                // Verifica se o cliente já se encontra na lista de cliente na plataforma
                if (client.getCommand().equals("") && !this.listClient.get().contains(client)) {
                    this.listClient.add(client);
                }

                // Verifica se vai notificar a todos os clientes ou executa certos comandos devido a ação do botão
                if (!client.getListContact().equals("")) {
                    this.centralNodeInstructions.sendToAll(client.getCommand(), this.clientsConnected, this.listClient);
                } else {
                    System.out.println("Resposta do nó central: " + this.centralNodeInstructions.setInstruction(clientSocket, client));
                    out.println(this.centralNodeInstructions.setInstruction(clientSocket, client));
                }

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

