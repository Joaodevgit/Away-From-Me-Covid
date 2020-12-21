package com.company.Client;

import com.company.CentralNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class CentralNodesInstructions {

    CentralNode centralNode = new CentralNode();

    public String setInstruction(Socket clientSocket, String serverMsg) {
        String msg = serverMsg;
        switch (msg) {
            case "BOTÃO COVID":
                msg = centralNode.testCovid(clientSocket);
                break;
//            case "BOTÃO ADIÇÃO CONTATO":
//                msg = centralNode.addCloseContacts(clientSocket);
        }
        return msg;
    }
}
