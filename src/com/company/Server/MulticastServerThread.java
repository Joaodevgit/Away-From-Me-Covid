package com.company.Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;

public class MulticastServerThread extends Thread {

    protected DatagramSocket datagramSocket;
    protected boolean listening = true;
    private SynchronizedArrayList<WorkerThread> clientsConnected;
    private CentralNode centralNode = new CentralNode();
    private SynchronizedArrayList<MulticastSocket> countyMulticastSockets;
//    MulticastSocket multicastSocket = new MulticastSocket(4446);
    //InetAddress groupMulticast = InetAddress.getByName("230.0.0.1");
//    multicastSocket.joinGroup(groupMulticast);

    public MulticastServerThread(SynchronizedArrayList<WorkerThread> clientsConnected) throws IOException {
        super("ServerHandlerThread");
        this.datagramSocket = new DatagramSocket(4445);
        this.clientsConnected = clientsConnected;
        this.countyMulticastSockets = new SynchronizedArrayList<>();
    }

    @Override
    public void run() {
        while (listening) {
            if (!this.clientsConnected.get().isEmpty()) {
                System.out.println("Servidor multicast a ouvir...");
                // Intervalo de tempo para a notificação ser lançada
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//              O getClientCountyPort está a retornar bem o nº da porta
//                System.out.println("Portas");
//                for (int i = 0; i < this.clientsConnected.get().size(); i++) {
//                    System.out.println(centralNode.getClientCountyPort(this.clientsConnected.get().get(i).client));
//                }
//              O isMulticastGroupPortExists está a retornar bem se o nº da porta existe ou não no array
//                for (int i = 0; i < this.clientsConnected.get().size(); i++) {
//                    System.out.println(centralNode.isMulticastGroupPortExists(this.clientsConnected.get().get(i).client,countyMulticastSockets));
//                }

                System.out.println("Antes");
                for (int i = 0; i < this.countyMulticastSockets.get().size(); i++) {
                    System.out.println(this.countyMulticastSockets.get().get(i).getLocalPort());
                }
                centralNode.updateMulticastGroups(this.clientsConnected, this.countyMulticastSockets);
                System.out.println("Depois");
                for (int i = 0; i < this.countyMulticastSockets.get().size(); i++) {
                    System.out.println(this.countyMulticastSockets.get().get(i).getLocalPort());
                }
//            try {
//                InetAddress group = InetAddress.getByName("230.0.0.1");
//                byte[] buf = new byte[1024];
//
//                //receive request
//                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
//                datagramSocket.receive(datagramPacket);
//
//                if (datagramPacket != null) {
//                    String question = "Qual é o teu tempo local?";
//                    buf = question.getBytes();
//                    datagramPacket = new DatagramPacket(buf, buf.length, group, 4446);
//                    this.datagramSocket.send(datagramPacket);
//                }
//                datagramPacket = new DatagramPacket(buf, buf.length);
//                this.datagramSocket.receive(datagramPacket);
//
//                String msgRcvd = new String(datagramPacket.getData(), 0, datagramPacket.getLength());
//                System.out.println("O server recebeu do cliente " + datagramPacket.getAddress() + ":" + datagramPacket.getPort() + " a mensagem: " + msgRcvd);
//
//                long total = System.nanoTime() - Long.parseLong(msgRcvd);
//                String str = "O valor é: " + total;
//                buf = str.getBytes();
//                datagramPacket = new DatagramPacket(buf, buf.length, group, 4446);
//                this.datagramSocket.send(datagramPacket);
//            } catch (IOException e) {
//                e.printStackTrace();
//                this.listening = false;
//            }
            }
            this.datagramSocket.close();
        }
    }
}
