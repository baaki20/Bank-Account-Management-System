package model;

import controller.BankController;
import javafx.scene.control.Alert;

// Abstract class for all account types
public abstract class BankAccount implements BankOperations {
    protected String accountNumber;
    protected String accountHolderName;
    protected double balance;
    public TransactionNode transactionHead;

    public BankAccount(String accountNumber, String accountHolderName, double initialBalance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = initialBalance;
        this.transactionHead = null;
        recordTransaction("Account Created", initialBalance);
    }

    protected void recordTransaction(String type, double amount) {
        TransactionNode newNode = new TransactionNode(type, amount, balance);
        newNode.next = transactionHead;
        transactionHead = newNode;
    }

    @Override
    public void checkBalance() {
        BankController.showAlert("Message", "Balance: GHS " + balance, Alert.AlertType.INFORMATION);
    }

    @Override
    public void viewTransactionHistory(int n) {
        TransactionNode current = transactionHead;
        int count = 0;
        while (current != null && count < n) {
            System.out.println(current);
            current = current.next;
            count++;
        }
    }

    // Overloaded deposit method with description
    public void deposit(double amount, String source) {
        this.balance += amount;
        recordTransaction("Deposit from " + source, amount);
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public double getBalance() {
        return this.balance;
    }
}