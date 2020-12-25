package com.company.Models;

public class Client {

    private int id;
    private String name;
    private boolean isInfected;
    private String county;
    private String command;

    public Client(int id, String name, boolean isInfected, String county) {
        this.id = id;
        this.name = name;
        this.isInfected = isInfected;
        this.county = county;
        this.command = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isInfected=" + isInfected +
                ", county='" + county + '\'' +
                ", command='" + command + '\'' +
                '}';
    }
}
