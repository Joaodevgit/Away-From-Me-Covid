package com.company.Models;


/**
 * Class that will represent the client object model
 *
 * @author João Pereira
 * @author Paulo da Cunha
 */
public class Client {

    private int id;
    private String name;
    private boolean isInfected;
    private boolean isNotified;
    private String county;
    private String command;
    private String listContact;

    public Client(int id, String name, boolean isInfected, boolean isNotified, String county) {
        this.id = id;
        this.name = name;
        this.isInfected = isInfected;
        this.isNotified = isNotified;
        this.county = county;
        this.command = "";
        this.listContact = "";
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isInfected() {
        return isInfected;
    }

    public void setInfected(boolean infected) {
        isInfected = infected;
    }

    public String getCounty() {
        return county;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getListContact() {
        return listContact;
    }

    public void setListContact(String listContact) {
        this.listContact = listContact;
    }

    public boolean isNotified() {
        return isNotified;
    }

    public void setNotified(boolean notified) {
        isNotified = notified;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isInfected=" + isInfected +
                ", isNotified=" + isNotified +
                ", county='" + county + '\'' +
                ", command='" + command + '\'' +
                ", listContact='" + listContact + '\'' +
                '}';
    }
}
