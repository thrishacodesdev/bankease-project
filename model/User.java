package model;

public class User {
    private String fullName;
    private int age;
    private String username;
    private String pin;
    private int accountNumber;

    // Constructor
    public User(String fullName, int age, String username, String pin, int accountNumber) {
        this.fullName = fullName;
        this.age = age;
        this.username = username;
        this.pin = pin;
        this.accountNumber = accountNumber;
    }

    // Getters
    public String getFullName() {
        return fullName;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public String getPin() {
        return pin;
    }

    public int getAccNumber() {
        return accountNumber;
    }
}
