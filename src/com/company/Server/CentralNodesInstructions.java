package com.company.Server;

import com.company.Models.Client;
import com.google.gson.Gson;

import java.net.Socket;
import java.util.regex.Pattern;

public class CentralNodesInstructions {

    CentralNode centralNode = new CentralNode();

    public String setInstruction(Socket clientSocket, String serverMsg) {
        String msg = null;

        Gson gson = new Gson();
        Client client = gson.fromJson(serverMsg, Client.class);

        switch (client.getCommand()) {
            case "LOGOUT":
                msg = this.centralNode.saveUserInfo(clientSocket, client);
                break;

            case "BOTÃO COVID":
                msg = centralNode.testCovid(clientSocket);
                break;

            case "ADICIONAR CONTACTOS":
                if (Pattern.matches("^[0-9]+(;[0-9]+)*$", client.getListContact())) {
                    msg = centralNode.addCloseContact(clientSocket, client.getListContact());
                } else {
                    msg = "Introdução de Id's inválida";
                }
                break;
        }

        return msg;
    }

}
