package com.company.Server;

import com.company.Models.Client;

import java.net.Socket;
import java.util.regex.Pattern;

public class CentralNodeInstructions {

    private CentralNode centralNode = new CentralNode();
    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();


    /**
     * Método responsável por receber uma determinada ação (em string) do cliente na aplicação
     *
     * @param clientSocket     socket associado ao cliente
     * @param client           objeto do cliente (modelo)
     * @param clientsConnected array de clientes atualmente conetados na aplicação
     * @return a mensagem resultante do clique do cliente
     */
    public String setInstruction(Socket clientSocket, Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {
        String msg = null;

        switch (client.getCommand()) {
            case "BOTÃO LOGIN":
                if (client.isNotified()) {
                    msg = "Esteve em contacto com uma pessoa infetada...É necessário que faça o teste";
                } else {
                    msg = "Bem vindo " + client.getName();
                }
                break;

            case "BOTÃO LOGOUT":
                msg = this.centralNode.saveUserInfo(client, clientsConnected);
                break;

            case "BOTÃO COVID":
                msg = this.centralNode.testCovid(clientSocket, client);
                break;
        }

        return msg;
    }

    /**
     * Método responsável por enviar ao cliente que adicionou os contatos próximos a mensagem: "Todos os contactos foram
     * alertados com sucesso!" e responsável por enviar aos contatos próximos que foram adicionados a mensagem:" Esteve
     * em contacto com uma pessoa infetada! É necessario fazer o teste!"
     *
     * @param client           objeto do cliente (modelo)
     * @param clientsConnected array de clientes atualmente conetados na aplicação
     */
    public synchronized void sendToAll(Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {

        if (Pattern.matches("^[0-9]+(;[0-9]+)*$", client.getListContact())) {
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
                        this.readWriteFiles.updateNotificationContactUser(idConvInt);
                    } else {
                        this.readWriteFiles.writeUnregisteredUsers(idConvInt);
                    }
                }
            }
        } else {
            String[] listContact = client.getListContact().split(";");
            int idConvInt = Integer.parseInt(listContact[0]);
            boolean found = false;

            for (int i = 0; !found && i < clientsConnected.get().size(); i++) {
                if (idConvInt == clientsConnected.get().get(i).client.getId()) {
                    found = true;
                    clientsConnected.get().get(i).out.println("Introdução de contactos inválida (Nº Utente Saúde Negativo)");
                }
            }
        }
    }
}
