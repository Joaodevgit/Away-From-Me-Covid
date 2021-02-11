package com.company.Server;

import com.company.Client.AlertUserBox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

/**
 * Class responsible for executing the thread that will receive the datagramsocket (message) from the server
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class UDPClientMsgReceiverThread extends Thread {

    MulticastSocket multicastSocket;
    private boolean listening = true;

    public UDPClientMsgReceiverThread(MulticastSocket multicastSocket) {
        super("UDPClientMsgReceiverThread");
        this.multicastSocket = multicastSocket;
    }

    @Override
    public void run() {

        while (listening) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                this.multicastSocket.receive(datagramPacket);

                if (datagramPacket != null) {
                    String serverMsgRcvd = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    AlertUserBox.display("Alert Number Infected", serverMsgRcvd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.multicastSocket.close();
        this.interrupt();
    }
}
