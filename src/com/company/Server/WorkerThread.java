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
    private Gson gson;
    protected Client client;

    public WorkerThread(Socket clientSocket, SynchronizedArrayList<WorkerThread> clientsConnected) throws IOException {
        super("WorkerThread");
        this.clientSocket = clientSocket;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.centralNodeInstructions = new CentralNodeInstructions();
        this.clientsConnected = clientsConnected;
        this.gson = new Gson();
    }

    /**
     * Método responsável por executar a thread que irá tratar de receber a mensagem do nó central e de a enviar para o
     * cliente
     */
    public void run() {
        try {
            if (!this.clientsConnected.get().isEmpty()) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("inputLine: " + inputLine);
                    System.out.println("Resposta do cliente " + clientSocket.getRemoteSocketAddress().toString() + ": " + inputLine);

                    this.client = this.gson.fromJson(inputLine, Client.class);

                    // Verifica se vai notificar a todos os clientes ou executa certos comandos devido a ação do botão
                    if (!client.getListContact().equals("")) {
                        this.centralNodeInstructions.sendToAll(client, this.clientsConnected);
                    } else {
                        String msg = this.centralNodeInstructions.setInstruction(clientSocket, client, this.clientsConnected);
                        System.out.println("Resposta do nó central: " + msg);
                        out.println(msg);
                    }

                    if (inputLine.equals("Bye"))
                        break;
                }
                out.close();
                in.close();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

