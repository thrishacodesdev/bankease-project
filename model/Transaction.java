package model;

import java.time.LocalDateTime;

public class Transaction {
    private String type;
    private double amount;
    private LocalDateTime timestamp;

    // For data loading
    public Transaction(String type, double amount, String timestamp) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.parse(timestamp);
    }

    // For runtime transactions
    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
        this.timestamp = LocalDateTime.now();
    }

    public String getType() { return type; }
    public double getAmount() { return amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
