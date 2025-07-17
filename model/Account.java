package model;

public class Account {
    private int accountNumber;
    private double balance;

    public Account(int accountNumber)
    {
        this.accountNumber=accountNumber;
        this.balance=0.0;
    }
    public Account(int accNo, double balance2) {
this.accountNumber=accNo;
this.balance=balance2;

    }
    public void deposit(double amount)
    {
if(amount>0)
{
    balance+=amount;
}
    }
    public boolean withdraw(double amount)
    {
        if(amount >0&& balance >=amount)
        {
            balance-=amount;
            return true;
        }
        return false;
    }
    public double getBalance()
    {
        return balance;
    }
    public int getAccNumber()
    {
        return accountNumber;
    }
}
