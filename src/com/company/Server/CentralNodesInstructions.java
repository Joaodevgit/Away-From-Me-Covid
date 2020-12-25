package com.company.Server;

import com.company.Models.Client;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CentralNodesInstructions {

    CentralNode centralNode = new CentralNode();

    public String setInstruction(Socket clientSocket, String serverMsg) {
        String msg = null;

        System.out.println("Server message: " + serverMsg);

//            System.out.println(contentMessage);
        Gson gson = new Gson();
        Client client = gson.fromJson(serverMsg, Client.class);

        System.out.println("Cheguei: " + client.toString());


//        if (Integer.parseInt(msg.split(";")[0]) > 47 && Integer.parseInt(msg.split(";")[0]) < 58) {
//            msg = centralNode.addCloseContact(clientSocket, msg);
//        if (isNumeric(msg.split(";")[0])) {
//            msg = centralNode.addCloseContact(clientSocket, msg);
//        } else {
        switch (client.getCommand()) {
            case "BOTÃO COVID":
                msg = centralNode.testCovid(clientSocket);
                break;
        }
//        }
        return msg;
    }

    private boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }
}
