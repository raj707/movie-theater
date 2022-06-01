package com.jpmc.theater;

import java.util.Objects;

public class Customer {

    private String name;
    private String id;

    /**
     * @param name customer name
     * @param id customer id
     */
    public Customer(String name, String id) {
        validate(name, id);
        this.id = id; // NOTE - id is not used anywhere at the moment
        this.name = name;

    }

    private void validate(String name, String id) {
        if(name == null || name.trim().isEmpty() || id == null || id.trim().isEmpty()) {
            throw new IllegalStateException("Customer cannot be created");
        }
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Customer)) return false;
        Customer customer = (Customer) o;
        return Objects.equals(name, customer.name) && Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, id);
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
