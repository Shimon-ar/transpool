package org.transpool.engine.ds;

public class Customer {
    private String name;


    public Customer(String name, int id) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '}';
    }
}
