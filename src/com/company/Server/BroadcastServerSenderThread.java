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

    /**
     * Method responsible for executing the thread that will send messages by broadcast to users, informing
     * the total number of infected people in the counties belonging to the sub-region Tâmega and Vale do Sousa
     */
    @Override
    public void run() {
        while (listening) {
            // Time interval for the notification to be launched (1 min in 1 min)
            try {
                Thread.sleep(60000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.clientsConnected.get().isEmpty()) {
                System.out.println("Broadcast Server listening...");
                try {
                    String serverMsg = "The total number of infected in the Sub-region of Tâmega and Vale do Sousa is: " +
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
