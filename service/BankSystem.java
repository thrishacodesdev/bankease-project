package service;

import model.User;
import model.Account;
import model.Transaction;

import java.io.File;
import java.io.IOException;
import java.io.FileWriter;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class BankSystem implements BankOperation 
{
    private Map<String, User> users = new HashMap<>();
    private Map<Integer, Account> accounts = new HashMap<>();
    private Map<Integer, List<Transaction>> history = new HashMap<>();
    private Scanner scan = new Scanner(System.in);
    private User currentUser;
    private static int accountCounter = 100100;

   @Override
public void createAccount() {
    System.out.println("----- Create New Account -----");

    System.out.print("Enter your full name: ");
    String name = scan.nextLine(); // ✅ only one

    System.out.print("Enter your age: ");
    int age = scan.nextInt();
    scan.nextLine(); // ✅ fix

    System.out.print("Enter username: ");
    String username = scan.nextLine();

    String pin;
    while (true) {
        System.out.print("Create a 4-digit PIN: ");
        pin = scan.nextLine();
        if (pin.matches("\\d{4}")) break;
        else System.out.println("❌ PIN must be exactly 4 digits.");
    }

    int accNo = accountCounter++;
    User user = new User(name, age, username, pin, accNo);
    users.put(username, user);
    accounts.put(accNo, new Account(accNo));

    System.out.println("✅ Account created successfully!");
    System.out.println("🎉 Your Account Number is: " + accNo);
}


   @Override
public boolean login() {
    System.out.println("-------- Login --------");

    System.out.println("Enter Username: ");
String username = scan.nextLine().trim();

System.out.println("Enter 4-digit PIN: ");
String pin = scan.nextLine().trim();


    if (username.equals("admin") && pin.equals("0000")) {
        System.out.println("👑 Welcome, Admin!");
        adminMenu();
        return false; // Don't enter user menu
    }

    User user = users.get(username);
    if (user != null && user.getPin().equals(pin)) {
        currentUser = user;
        System.out.println("✔ Login successful. Welcome, " + user.getFullName() + "!");
        return true;
    } else {
        System.out.println("❌ Login failed! Invalid username or PIN.");
        return false;
    }
}

    @Override
    public void showMenu() {
        System.out.println("\n===== Main Menu =====");
        System.out.println("1. Check Balance");
        System.out.println("2. Deposit");
        System.out.println("3. Withdraw");
        System.out.println("4. View Transaction History");
        System.out.println("5. Transfer Money");
        System.out.println("6. Logout");
    }

    public void handleUserMenu() {
        int accNo = currentUser.getAccNumber();
        boolean running = true;

        while (running) {
            showMenu();
            System.out.print("Enter choice: ");
int choice = scan.nextInt();
scan.nextLine(); // ✅ Fix again here

            switch (choice) {
                case 1:
                    System.out.println("💰 Balance: ₹" + accounts.get(accNo).getBalance());
                    break;
                case 2:
                    System.out.print("Deposit ₹: ");
                    double depositAmt = Double.parseDouble(scan.nextLine().trim());
                    if (depositAmt > 0) {
                        accounts.get(accNo).deposit(depositAmt);
                        addTransaction(accNo, "Deposit", depositAmt);
                        System.out.println("✅ Deposited ₹" + depositAmt);
                    } else {
                        System.out.println("❌ Enter a positive amount.");
                    }
                    break;
                case 3:
                    System.out.print("Withdraw ₹: ");
                    double withdrawAmt = Double.parseDouble(scan.nextLine().trim());
                    if (accounts.get(accNo).withdraw(withdrawAmt)) {
                        addTransaction(accNo, "Withdraw", withdrawAmt);
                        System.out.println("✅ Withdrew ₹" + withdrawAmt);
                    } else {
                        System.out.println("❌ Insufficient funds or invalid amount.");
                    }
                    break;
                case 4:
                    List<Transaction> list = history.get(accNo);
                    if (list != null && !list.isEmpty()) {
                        System.out.println("📝 Transaction History:");
                        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                        list.forEach(t -> System.out.println(t.getTimestamp().format(fmt) +
                                " - " + t.getType() + " ₹" + t.getAmount()));
                    } else {
                        System.out.println("No transactions yet.");
                    }
                    break;
                case 5:
                    System.out.print("Recipient Account # : ");
                    int recAcc = Integer.parseInt(scan.nextLine().trim());

                    if (!accounts.containsKey(recAcc)) {
                        System.out.println("❌ Recipient not found.");
                        break;
                    }

                    System.out.print("Transfer ₹: ");
                    double transferAmt = Double.parseDouble(scan.nextLine().trim());

                    if (transferAmt > 0 && accounts.get(accNo).withdraw(transferAmt)) {
                        accounts.get(recAcc).deposit(transferAmt);
                        addTransaction(accNo, "Transfer to " + recAcc, transferAmt);
                        addTransaction(recAcc, "Received from " + accNo, transferAmt);
                        System.out.println("✅ ₹" + transferAmt + " sent to " + recAcc);
                    } else {
                        System.out.println("❌ Invalid amount or insufficient funds.");
                    }
                    break;
                case 6:
                    System.out.println("👋 Logged out successfully.");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice.");
            }
        }
    }

    private void addTransaction(int accNo, String type, double amount) {
        Transaction txn = new Transaction(type, amount);
        history.putIfAbsent(accNo, new ArrayList<>());
        history.get(accNo).add(txn);
    }

    public void adminMenu() {
        System.out.println("\n📋 Admin Dashboard:");
        users.values().forEach(u ->
            System.out.println("👤 " + u.getFullName()
                    + " | Username: " + u.getUsername()
                    + " | Acc#: " + u.getAccNumber()));
    }

    public void saveDataToFiles() {
    try {
        // Save users
        FileWriter uf = new FileWriter("users.txt");
        for (User u : users.values()) {
            uf.write(u.getUsername() + "," + u.getFullName() + "," +
                     u.getAge() + "," + u.getAccNumber() + "," + u.getPin() + "\n");
        }
        uf.close();

        // Save accounts
        FileWriter af = new FileWriter("accounts.txt");
        for (Account a : accounts.values()) {
            af.write(a.getAccNumber() + "," + a.getBalance() + "\n");
        }
        af.close();

        // Save transactions
        FileWriter tf = new FileWriter("transactions.txt");
        for (Map.Entry<Integer, List<Transaction>> entry : history.entrySet()) {
            for (Transaction t : entry.getValue()) {
                tf.write(entry.getKey() + "," + t.getType() + "," +
                         t.getAmount() + "," + t.getTimestamp() + "\n");
            }
        }
        tf.close();

    } catch (IOException e) {
        System.out.println("❌ Error saving data: " + e.getMessage());
    }
}


    public void loadDataFromFiles() {
        try {
            File file = new File("users.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    users.put(parts[0], new User(parts[1], Integer.parseInt(parts[2]),
                            parts[0], parts[4], Integer.parseInt(parts[3])));
                }
                sc.close();
            }

            file = new File("accounts.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] parts = sc.nextLine().split(",");
                    accounts.put(Integer.parseInt(parts[0]),
                            new Account(Integer.parseInt(parts[0]),
                                        Double.parseDouble(parts[1])));
                }
                sc.close();
            }

            file = new File("transactions.txt");
            if (file.exists()) {
                Scanner sc = new Scanner(file);
                while (sc.hasNextLine()) {
                    String[] p = sc.nextLine().split(",");
                    Transaction txn = new Transaction(p[1], Double.parseDouble(p[2]), p[3]);
                    int accNo = Integer.parseInt(p[0]);
                    history.putIfAbsent(accNo, new ArrayList<>());
                    history.get(accNo).add(txn);
                }
                sc.close();
            }

        } catch (Exception e) {
            System.out.println("❌ Error loading data: " + e.getMessage());
        }
    }

    private void writeLine(FileWriter fw, String line) throws Exception {
        fw.write(line + "\n");
    }
}
