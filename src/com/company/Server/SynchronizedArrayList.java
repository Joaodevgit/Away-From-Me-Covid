package com.company.Server;

import java.util.ArrayList;

public class SynchronizedArrayList<T> {
    private ArrayList<T> list;

    public SynchronizedArrayList() {
        this.list = new ArrayList<>();
    }

    public synchronized void add(T o) {
        this.list.add(o);
    }

    public synchronized ArrayList<T> get() {
        return this.list;
    }

    public synchronized boolean removeElement(T object){
        return this.list.remove(object);
    }
}
