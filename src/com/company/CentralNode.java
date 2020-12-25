package com.company;

import com.company.Client.AlertUserBox;
import com.company.Client.MenuPage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Random;

public class CentralNode {

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

    public String addCloseContact(Socket clientSocket, String contacts) {
        String res = "";
        for (int i = 0; i < contacts.split(";").length; i++) {
            res += contacts.split(";")[i] + " ";
        }
        return "Contacto(s) " + res + " adicinado(s) com sucesso";
    }


}
