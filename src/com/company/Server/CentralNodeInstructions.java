package com.company.Server;

import com.company.Models.Client;

import java.net.Socket;
import java.util.regex.Pattern;


/**
 * Class responsible for
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class CentralNodeInstructions {

    private CentralNode centralNode = new CentralNode();
    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();


    /**
     * Method responsible for receiving a given action (in string) from the client in the application
     *
     * @param clientSocket     client associated socket
     * @param client           client object (model)
     * @param clientsConnected array of clients currently connected to the application
     * @return the message resulting from the client’s click action
     */
    public String setInstruction(Socket clientSocket, Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {
        String msg = null;

        switch (client.getCommand()) {
            case "LOGIN BUTTON":
                if (client.isNotified()) {
                    msg = "You were in contact with an infected person ... You need to take the covid-19 test";
                } else {
                    msg = "Welcome " + client.getName();
                }
                break;

            case "LOGOUT BUTTON":
                msg = this.centralNode.saveUserInfo(client, clientsConnected);
                break;

            case "COVID TEST BUTTON":
                msg = this.centralNode.testCovid(clientSocket, client);
                break;
        }

        return msg;
    }

    /**
     * Method responsible for sending to the client, who added the nearby contacts, the following message: "All contacts have been
     * successfully alerted!" and is also responsible for sending to the nearby contacts ,that have been added, the following message:
     * "You have been in contact with an infected person! It is necessary to take the test!"
     *
     * @param client           client object (model)
     * @param clientsConnected array of clients currently connected to the application
     */
    public synchronized void sendToAll(Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {

        if (Pattern.matches("^[0-9]{9}(;[0-9]{9})*$", client.getListContact())) {
            String[] listContact = client.getListContact().split(";");
            int idConvInt = Integer.parseInt(listContact[0]);
            boolean found = false;

            for (int i = 0; !found && i < clientsConnected.get().size(); i++) {
                if (idConvInt == clientsConnected.get().get(i).client.getId()) {
                    found = true;
                    clientsConnected.get().get(i).out.println("All contacts have been successfully alerted!");
                }
            }

            for (int i = 1; i < listContact.length; i++) {
                found = false;
                idConvInt = Integer.parseInt(listContact[i]);
                //System.out.println("ID CONTACT: " + idConvInt);

                for (int j = 0; !found && j < clientsConnected.get().size(); j++) {
                    // If the user is online
                    if (idConvInt == clientsConnected.get().get(j).client.getId()) {
                        found = true;
                        clientsConnected.get().get(i).out.println("You have been in contact with an infected person. " +
                                "It is necessary to take the covid-19 test!");
                    }
                }
                // If the user is offline
                if (!found) {
                    // If the user is registered in the application
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
                    clientsConnected.get().get(i).out.println("Invalid nearby contacts entry");
                }
            }
        }
    }
}
