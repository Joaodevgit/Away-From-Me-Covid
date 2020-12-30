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
    public synchronized void sendToAll(Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {
        String[] listContact = client.getListContact().split(";");
        int idConvInt = Integer.parseInt(listContact[0]);
        boolean found = false;

        for (int i = 0; !found && i < clientsConnected.get().size(); i++) {
            if (idConvInt == clientsConnected.get().get(i).client.getId()) {
                found = true;
                clientsConnected.get().get(i).out.println("Todos os contactos foram alertados com sucesso!");
            }
        }

        for (int i = 1; i < listContact.length; i++) {
            found = false;

            idConvInt = Integer.parseInt(listContact[i]);

            System.out.println("ID CONTACT: " + idConvInt);

            for (int j = 0; !found && j < clientsConnected.get().size(); j++) {
                // Caso que o Utilizador esteja conectado
                if (idConvInt == clientsConnected.get().get(j).client.getId()) {
                    found = true;
                    clientsConnected.get().get(i).out.println("Esteve em contacto com uma pessoa infetada! É necessario fazer o teste!");
                }

            }

            // Caso que o Utilizador não esteja conectado
            if (!found) {
                // Se existir um registo do Utilizador
                if (this.readWriteFiles.userExists(idConvInt)) {
                    System.out.println("ENTREI");
                    this.readWriteFiles.UpdateNotificationContactUser(idConvInt);
                } else {
                    //Se não existir um registo do Utilizador
                }
            }
        }

    }

}
