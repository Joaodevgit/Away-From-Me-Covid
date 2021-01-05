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
    }

    protected boolean listening = true;

    @Override
    public void run() {
        while (listening) {
            if (!this.clientsConnected.get().isEmpty()) {
                // Intervalo de tempo para a notificação ser lançada
                try {
                    Thread.sleep(45000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    String serverMsg = "O nº total de infetados na Sub-região do Tâmega e Vale do Sousa é: " +
                            this.readWriteFiles.getSubRegionTotalInfected();

                    broadcastSocket = new DatagramSocket();
                    broadcastSocket.setBroadcast(true);

                    byte[] buf = serverMsg.getBytes();

                    DatagramPacket packet = new DatagramPacket(buf, buf.length, InetAddress.getByName(BROADCAST_ADDRESS), 5000);
                    broadcastSocket.send(packet);
                    broadcastSocket.close();
                } catch (UnknownHostException | SocketException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
