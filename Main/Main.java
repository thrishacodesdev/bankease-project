package Main;

import service.BankSystem;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        BankSystem bank = new BankSystem();
        bank.loadDataFromFiles();

        Scanner scan = new Scanner(System.in);
        boolean running = true;
        while (running) {
            System.out.println("\n=============================");
            System.out.println("Welcome to BankEase 💸");
            System.out.println("=============================");
            System.out.println("1. Create New Account");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            int choice=scan.nextInt();
scan.nextLine();
            switch (choice) {
                case 1: bank.createAccount(); break;
                case 2:
    if (bank.login()) {
        bank.handleUserMenu(); // Only when login is successful
    }
    break;

                case 3:
                    bank.saveDataToFiles();
                    System.out.println("🙏 Thank you for using BankEase!");
                    running = false;
                    break;
                default:
                    System.out.println("❌ Invalid choice. Try again.");
            }
        }

        scan.close();
    }
}
