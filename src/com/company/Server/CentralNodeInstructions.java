package com.company.Server;

import com.company.Models.Client;

import java.net.Socket;
import java.util.regex.Pattern;

public class CentralNodeInstructions {

    CentralNode centralNode = new CentralNode();

    public String setInstruction(Socket clientSocket, Client clientModel) {
        String msg = null;
//
//        Gson gson = new Gson();
//        Client client = gson.fromJson(client, Client.class);

        switch (clientModel.getCommand()) {
            case "LOGIN":
                msg = "Bem vindo " + clientModel.getName();
                break;

            case "LOGOUT":
                msg = this.centralNode.saveUserInfo(clientSocket, clientModel);
                break;

            case "BOTÃO COVID":
                msg = centralNode.testCovid(clientSocket);
                break;

            case "ADICIONAR CONTACTOS":
                if (Pattern.matches("^[0-9]+(;[0-9]+)*$", clientModel.getListContact())) {
                    msg = centralNode.addCloseContact(clientSocket, clientModel.getListContact());
                } else {
                    msg = "Introdução de Id's inválida";
                }
                break;
        }

        return msg;
    }

    //TODO: Acabar o método e testar
    public void sendToAll(String clientCommand, SynchronizedArrayList<WorkerThread> clientsConnected, SynchronizedArrayList<Client> listClient) {
        clientCommand.split(";");

        for (WorkerThread aClient : clientsConnected.get()) {
            aClient.out.println("NOTIFY ALL");
        }
    }

}
