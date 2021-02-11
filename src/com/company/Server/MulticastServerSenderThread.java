package com.company.Server;

import java.io.IOException;
import java.net.*;

/**
 * Class responsible for executing the thread that will send multicast messages to clients, informing the
 * number of infected in their respective county belonging to the Tâmega and Vale do Sousa sub-region
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class MulticastServerSenderThread extends Thread {

    protected DatagramSocket datagramSocket;
    protected boolean listening = true;
    private SynchronizedArrayList<WorkerThread> clientsConnected;
    private CentralNode centralNode = new CentralNode();
    private SynchronizedArrayList<MulticastSocket> countyMulticastSockets;
    private ReadWriteFiles readWriteFiles = new ReadWriteFiles();

    public MulticastServerSenderThread(SynchronizedArrayList<WorkerThread> clientsConnected) throws IOException {
        super("ServerHandlerThread");
        this.datagramSocket = new DatagramSocket(4445);
        this.clientsConnected = clientsConnected;
        this.countyMulticastSockets = new SynchronizedArrayList<>();
    }

    @Override
    public void run() {
        while (listening) {
            // Time interval for the notification to be launched (30 sec in 30 sec)
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.clientsConnected.get().isEmpty()) {
                System.out.println("Multicast Server listening...");
                try {
                    InetAddress group = InetAddress.getByName("230.0.0.1");
                    byte[] buf = new byte[1024];
                    DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);

                    SynchronizedArrayList<MulticastSocket> clientPorts = this.centralNode.
                            updateMulticastGroups(this.clientsConnected, this.countyMulticastSockets);

                    for (int i = 0; i < clientPorts.get().size(); i++) {
                        for (int j = 0; j < this.clientsConnected.get().size(); j++) {
                            int clientPort = this.readWriteFiles.getClientCountyPort(this.clientsConnected.get().get(j).client);
                            if (clientPort == clientPorts.get().get(i).getLocalPort()) {
                                if (datagramPacket != null) {
                                    String serverMsg;
                                    int infectedCountyNo = this.readWriteFiles.
                                            getCountyTotalInfected(this.clientsConnected.get().get(j).client.getCounty());
                                    if (infectedCountyNo != 0) {
                                        serverMsg = "The number of people infected in " + this.clientsConnected.get().get(j).client.getCounty() +
                                                " is: " + infectedCountyNo;
                                    } else {
                                        serverMsg = "Your County " + this.clientsConnected.get().get(j).client.getCounty() + " has no infected people";
                                    }
                                    buf = serverMsg.getBytes();
                                    datagramPacket = new DatagramPacket(buf, buf.length, group, clientPorts.get().get(i).getLocalPort());
                                    this.datagramSocket.send(datagramPacket);
                                }
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    this.listening = false;
                }
            }
        }
        this.datagramSocket.close();
    }
}