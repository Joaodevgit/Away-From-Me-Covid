package com.company.Server;

import com.company.Models.Client;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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

    public String testCovid(Socket clientSocket) {
        String result;
        int randNumber = new Random().nextInt(2);

        if (randNumber == 0) {
            result = "O teste do cliente" + clientSocket.getRemoteSocketAddress().toString() + " deu negativo";
        } else {
            result = "O teste do cliente" + clientSocket.getRemoteSocketAddress().toString() + " deu positivo";
        }

        return result;
    }

    public String saveUserInfo(Socket clientSocket, Client userInfo) {
        this.readWriteFiles.writeJSONFile(userInfo);

        return "Siga as recomendações da DGS e fique em casa !";
    }

    public void printClientsConnected(SynchronizedArrayList<WorkerThread> clientsConnected) {

        if (clientsConnected.get().size() != 0) {
            System.out.println("Os clientes conetados são: ");
            for (int i = 0; i < clientsConnected.get().size(); i++) {
                System.out.println(clientsConnected.get().get(i).client.toString());
            }
        } else {
            System.out.println("Atualmente não existem clientes conetados!");
        }
    }


    // Método responsável por atualizar a lista de portas dos grupos de multicast
    public SynchronizedArrayList<MulticastSocket> updateMulticastGroups(SynchronizedArrayList<WorkerThread> clientsConnected, SynchronizedArrayList<MulticastSocket> multicastGroups) {

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

    // Método responsável por verificar se uma dada porta já existe (ou não) nos grupos de multicast
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
