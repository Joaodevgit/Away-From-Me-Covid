package com.company.Client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Class responsible for starting a thread that will receive messages from the server
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class MsgReceiverThread extends Thread {

    private Socket socket;
    private BufferedReader in;

    public MsgReceiverThread(Socket socket) throws IOException {
        super("MsgReceiverThread");
        this.socket = socket;
        this.in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
    }

    public void run() {
        try {
            String inputLine;
            boolean isLogout = false;

            while ((inputLine = in.readLine()) != null && !isLogout) {
                if (inputLine.contains("Welcome") || inputLine.contains("infected")) {
                    AlertUserBox.display("Welcome", inputLine);
                } else if (inputLine.contains("negative")) {
                    AlertUserBox.display("Covid-19 Test Result", inputLine);
                    CovidTest.client.setNotified(false);
                } else if (inputLine.contains("positive")) {
                    AlertUserBox.display("Covid-19 Test Result", inputLine);
                    CovidTest.covidTestButton.setDisable(true);
                    CovidTest.client.setInfected(true);
                    CovidTest.client.setNotified(false);
                } else if (inputLine.contains("Follow the recommendations of your local government and stay at home!")) {
                    AlertUserBox.display("Recommendation", inputLine);
                } else if (inputLine.contains("You have been in contact with an infected person.")
                        || inputLine.contains("All contacts have been successfully alerted!")) {
                    AlertUserBox.display("Nearby Contacts", inputLine);
                    AddCloseContact.client.setListContact("");
                } else if (inputLine.contains("(Negative Health User no.)")) {
                    AlertUserBox.display("Error Nearby Contacts", inputLine);
                } else {
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
            this.in.close();
            this.socket.close();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
