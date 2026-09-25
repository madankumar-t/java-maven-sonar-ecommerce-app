package com.example.ecommerce.common.model;

import java.util.Objects;

public class Customer {

    private final String id;
    private String name;
    private String email;

    public Customer(String id, String name, String email) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("Customer id must not be empty");
        }
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Customer email is invalid");
        }
        this.id = id;
        this.name = name;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Customer email is invalid");
        }
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        Customer customer = (Customer) o;
        return Objects.equals(id, customer.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Customer{id='" + id + "', name='" + name + "', email='" + email + "'}";
    }
}
