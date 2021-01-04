package com.company.Client;

import javafx.application.Application;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class MsgReceiverThread extends Thread {

    Socket socket;
    BufferedReader in;

    public MsgReceiverThread(Socket socket) throws IOException {
        super("MsgReceiverThread");
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void run() {
        try {
            String inputLine;
            boolean isLogout = false;
            while ((inputLine = in.readLine()) != null && !isLogout) {
                //System.out.println("Server Response: " + inputLine);
                if (inputLine.contains("Bem vindo") || inputLine.contains("infetada")) {
                    AlertUserBox.display("Bem vindo", inputLine);
                } else if (inputLine.contains("negativo")) {
                    AlertUserBox.display("Resultado Teste Covid", inputLine);
                    CovidTest.client.setNotified(false);
                } else if (inputLine.contains("positivo")) {
                    AlertUserBox.display("Resultado Teste Covid", inputLine);
                    CovidTest.covidTestButton.setDisable(true);
                    CovidTest.client.setInfected(true);
                    CovidTest.client.setNotified(false);
                } else if (inputLine.contains("Siga as recomendações da DGS e fique em casa !")) {
                    AlertUserBox.display("Recomendação", inputLine);
                } else if(inputLine.contains("Esteve em contacto com uma pessoa infetada...")){
                    AlertUserBox.display("NOTIFY ALL", inputLine);
                    AddCloseContact.client.setListContact("");
                }else{
                    isLogout = true;
                }
            }
        } catch (UnknownHostException e) {
            System.out.println("Unknown Host.");
            System.exit(1);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        try {
            in.close();
            socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
