package com.company.Server;

import java.io.IOException;
import java.net.*;

public class BroadcastServerSenderThread extends Thread {

    private SynchronizedArrayList<WorkerThread> clientsConnected;
    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();
    private DatagramSocket broadcastSocket;
    final String BROADCAST_ADDRESS = "230.0.0.2";

    public BroadcastServerSenderThread(SynchronizedArrayList<WorkerThread> clientsConnected) {
        this.clientsConnected = clientsConnected;
        try {
            this.broadcastSocket = new DatagramSocket();
            this.broadcastSocket.setBroadcast(true);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    protected boolean listening = true;

    @Override
    public void run() {
        while (listening) {
            try {
                Thread.sleep(16000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.clientsConnected.get().isEmpty()) {
                System.out.println("Servidor broadcast a ouvir...");
                // Intervalo de tempo para a notificação ser lançada
                try {
                    String serverMsg = "O nº total de infetados na Sub-região do Tâmega e Vale do Sousa é: " +
                            this.readWriteFiles.getSubRegionTotalInfected();

                    byte[] buf = serverMsg.getBytes();

                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(BROADCAST_ADDRESS), 5000);
                    this.broadcastSocket.send(packet);
                } catch (UnknownHostException | SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        this.broadcastSocket.close();
    }


}
