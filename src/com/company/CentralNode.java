package com.company;

import com.company.Client.AlertUserBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class CentralNode {


    private String testResult;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;


    public String testCovid(Socket clientSocket) {
        String result = "";
        int randNumber = new Random().nextInt(2);
        if (randNumber == 0) {
            result = "O teste do cliente" + clientSocket.getRemoteSocketAddress().toString() + " deu negativo";
        } else {
            result = "O teste do cliente" + clientSocket.getRemoteSocketAddress().toString() + " deu positivo";
        }
        return result;
    }


}
