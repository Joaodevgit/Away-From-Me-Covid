package com.company.Server;

import com.company.Client.AlertUserBox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class MultiCastClientMsgReceiverThread extends Thread{

    MulticastSocket multicastSocket;
    private boolean listening = true;


    public MultiCastClientMsgReceiverThread(MulticastSocket multicastSocket) {
        super("MultiCastClientMsgReceiver");
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
                    AlertUserBox.display("Aviso Concelho", serverMsgRcvd);
                    //System.out.println("Resposta do servidor: " + serverMsgRcvd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
