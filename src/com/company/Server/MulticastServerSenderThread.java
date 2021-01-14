package com.company.Server;

import java.io.IOException;
import java.net.*;

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

    /**
     * Método responsável por executar a thread que irá tratar de enviar as mensagens por multicast aos clientes, a
     * informar o nº de infetados do seu respetivo concelho pertencente à sub-região Tâmega e Vale do Sousa
     */
    @Override
    public void run() {
        while (listening) {
            // Intervalo de tempo para a notificação ser lançada (30s em 30s)
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (!this.clientsConnected.get().isEmpty()) {
                System.out.println("Servidor multicast a ouvir...");
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
                                    int infectedCountyNo = this.readWriteFiles.getCountyTotalInfected(this.clientsConnected.get().get(j).client.getCounty());
                                    if (infectedCountyNo != 0) {
                                        serverMsg = "O nº de infetados no concelho " + this.clientsConnected.get().get(j).client.getCounty() +
                                                " é: " + infectedCountyNo;
                                    } else {
                                        serverMsg = "O concelho " + this.clientsConnected.get().get(j).client.getCounty() + " não tem infetados";
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