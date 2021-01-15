package com.company.Server;

import com.company.Models.Client;

import java.io.IOException;
import java.net.MulticastSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

public class CentralNode {

    ReadWriteFiles readWriteFiles;

    public CentralNode() {
        this.readWriteFiles = new ReadWriteFiles();
    }

    /**
     * Método responsável por determinar o resultado do teste covid-19 quando o cliente clica em "Fazer Teste"
     *
     * @param clientSocket socket associado ao cliente
     * @param client       objeto do cliente (modelo)
     * @return uma string a dizer se o teste à covid-19 do cliente deu positivo ou negativo
     */
    public String testCovid(Socket clientSocket, Client client) {
        String result;
        int randNumber = new Random().nextInt(2);

        if (randNumber == 0) {
            result = "Caro(a) "+ client.getName() + ", o resultado do seu teste deu negativo";
        } else {
            result = "Caro(a) "+ client.getName() + ", o resultado do seu teste deu positivo";
            this.readWriteFiles.addInfectedCounty(client.getCounty());
        }

        return result;
    }

    /**
     * Método responsável por guardar as informações do cliente
     *
     * @param client           objeto do cliente (modelo)
     * @param clientsConnected array de clientes atualmente conetados na aplicação
     * @return mensagem : Siga as recomendações da DGS e fique em casa !
     */
    public String saveUserInfo(Client client, SynchronizedArrayList<WorkerThread> clientsConnected) {

        int i = 0;
        boolean found = false;
        while (!found && i < clientsConnected.get().size()) {
            if (client.getId() == clientsConnected.get().get(i).client.getId()) {
                found = true;
            } else {
                i++;
            }
        }

        clientsConnected.removeElement(clientsConnected.get().get(i));

        this.readWriteFiles.writeJSONFile(client);

        return "Siga as recomendações da DGS e fique em casa !";
    }

    /**
     * Método responsável por atualizar a lista de portas dos grupos de multicast dos clientes
     *
     * @param clientsConnected array de clientes atualmente conetados na aplicação
     * @param multicastGroups  array de multicastsockets dos clientes
     * @return array de multicastsockets dos clientes atualizado
     */
    public SynchronizedArrayList<MulticastSocket> updateMulticastGroups(SynchronizedArrayList<WorkerThread> clientsConnected,
                                                                        SynchronizedArrayList<MulticastSocket> multicastGroups) {

        for (int i = 0; i < clientsConnected.get().size(); i++) {
            if (!isMulticastGroupPortExists(clientsConnected.get().get(i).client, multicastGroups)) {
                try {
                    int port = this.readWriteFiles.getClientCountyPort(clientsConnected.get().get(i).client);
                    multicastGroups.add(new MulticastSocket(port));
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return multicastGroups;
    }


    /**
     * Método responsável por verificar se uma dada porta já existe (ou não) nos grupos de multicast
     *
     * @param client          objeto do cliente (modelo)
     * @param multicastGroups array de multicastsockets dos clientes
     * @return true se a porta do multicastsocket existe, caso contrário retorna false
     */
    public boolean isMulticastGroupPortExists(Client client, SynchronizedArrayList<MulticastSocket> multicastGroups) {
        int i = 0;
        boolean found = false;
        while (!found && i < multicastGroups.get().size()) {
            if (multicastGroups.get().get(i).getLocalPort() == this.readWriteFiles.getClientCountyPort(client)) {
                found = true;
            }
            i++;
        }

        return found;
    }
}
