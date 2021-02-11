package com.company.Server;

import com.company.Models.Client;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class CentralNode {

    ReadWriteFiles readWriteFiles;

    public CentralNode() {
        this.readWriteFiles = new ReadWriteFiles();
    }

    /**
     * Method responsible for determining the result of the covid-19 test when the client clicks "Take Test"
     * @param clientSocket client associated socket
     * @param client       client object (model)
     * @return a string to say whether the client's covid-19 test was positive or negative
     */
    public String testCovid(Socket clientSocket, Client client) {
        String result;
        int randNumber = new Random().nextInt(2);

        if (randNumber == 0) {
            result = "Dear "+ client.getName() + ", your covid-19 test result is negative";
        } else {
            result = "Dear "+ client.getName() + ", your covid-19 test result is positive";
            this.readWriteFiles.addInfectedCounty(client.getCounty());
        }

        return result;
    }

    /**
     * Method responsible for saving client informations
     * @param client           client object (model)
     * @param clientsConnected array of clients currently connected to the application
     * @return message : Follow the recommendations of your government and stay at home!
     */
    public String saveUserInfo(Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {

        int i = 0;
        boolean found = false;
        while (!found && i < clientsConnected.get().size()) {
            if (client.getId() == clientsConnected.get().get(i).client.getId()) {
                found = true;
            } else {
                i++;
            }
        }

        clientsConnected.removeElement(clientsConnected.get().get(i));

        this.readWriteFiles.writeJSONFile(client);

        return "Follow the recommendations of your local government and stay at home!";
    }

    /**
     * Method responsible for updating the port list of client multicast groups
     * @param clientsConnected array of clients currently connected to the application
     * @param multicastGroups  client multicastsocket array
     * @return updated array of multicastsockets clients
     */
    public SynchronizedArrayList<MulticastSocket> updateMulticastGroups(SynchronizedArrayList<WorkerThread> clientsConnected,
                                                                        SynchronizedArrayList<MulticastSocket> multicastGroups) {

        for (int i = 0; i < clientsConnected.get().size(); i++) {
            if (!isMulticastGroupPortExists(clientsConnected.get().get(i).client, multicastGroups)) {
                try {
                    int port = this.readWriteFiles.getClientCountyPort(clientsConnected.get().get(i).client);
                    multicastGroups.add(new MulticastSocket(port));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return multicastGroups;
    }


    /**
     * Method responsible for verifying whether a given port already exists (or not) in multicast groups
     * @param client          client object (model)
     * @param multicastGroups client multicastsocket array
     * @return true se a porta do multicastsocket existe, caso contrário retorna false
     */
    public boolean isMulticastGroupPortExists(Client client, SynchronizedArrayList<MulticastSocket> multicastGroups) {
        int i = 0;
        boolean found = false;
        while (!found && i < multicastGroups.get().size()) {
            if (multicastGroups.get().get(i).getLocalPort() == this.readWriteFiles.getClientCountyPort(client)) {
                found = true;
            }
            i++;
        }

        return found;
    }
}
