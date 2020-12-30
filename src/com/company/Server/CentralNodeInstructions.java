package com.company.Server;

import com.company.Models.Client;

import java.net.Socket;
import java.util.regex.Pattern;

public class CentralNodeInstructions {

    private CentralNode centralNode = new CentralNode();
    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();

    public String setInstruction(Socket clientSocket, Client clientModel) {
        String msg = null;

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
    public synchronized void sendToAll(String clientCommand, SynchronizedArrayList<WorkerThread> clientsConnected) {
        String[] listContact = clientCommand.split(";");
        boolean found = false;

        for (WorkerThread aClient : clientsConnected.get()) {

            for (int i = 0; i < listContact.length; i++) {

                System.out.println("Posição " + i + ": " + clientsConnected.get().get(i).client.getId());

                if (listContact[i].equals(clientsConnected.get().get(i).client.getId())) {
                    found = true;

                    if (clientsConnected.get().get(i).client.getListContact().equals(""))
                        clientsConnected.get().get(i).out.println("Esteve em contacto com uma pessoa infetada! É necessario fazer o teste!");
                    else
                        clientsConnected.get().get(i).out.println("Todos os contactos foram alertados com sucesso!");
                }

            }

            if (!found) {
                if (this.readWriteFiles.userExists(listContact[0])) {
                    // Caso que existe
                    System.out.println("Actualizou a notificação !");
                } else {
                    // Caso que não existe
                }
            }

        }

    }

}
