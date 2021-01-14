package com.company.Server;

import com.company.Client.AlertUserBox;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class UDPClientMsgReceiverThread extends Thread {

    MulticastSocket multicastSocket;
    private boolean listening = true;

    public UDPClientMsgReceiverThread(MulticastSocket multicastSocket) {
        super("UDPClientMsgReceiverThread");
        this.multicastSocket = multicastSocket;
    }

    /**
     * Método responsável por executar a thread que irá tratar de receber os datagramsocket (mensagem) da parte do servidor
     */
    @Override
    public void run() {

        while (listening) {
            try {
                byte[] buf = new byte[1024];
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                this.multicastSocket.receive(datagramPacket);

                if (datagramPacket != null) {
                    String serverMsgRcvd = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
                    AlertUserBox.display("Alerta Número Infetados", serverMsgRcvd);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        this.multicastSocket.close();
        this.interrupt();
    }
}
