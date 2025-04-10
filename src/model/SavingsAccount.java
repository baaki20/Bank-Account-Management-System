package model;

import controller.BankController;
import javafx.scene.control.Alert;

// Savings Account
public class SavingsAccount extends BankAccount {

    public SavingsAccount(String accNum, String name, double initBal) {
        super(accNum, name, initBal);
    }

    @Override
    public void deposit(double amount) {
        this.balance += amount;
        recordTransaction("Deposit", amount);
    }

    @Override
    public void withdraw(double amount) {
        double MIN_BALANCE = 100.0;
        if (this.balance - amount < MIN_BALANCE) {
            BankController.showAlert("Error", "Withdrawal denied: Minimum balance of GHS " + MIN_BALANCE + " required.", Alert.AlertType.ERROR);
        } else {
            this.balance -= amount;
            recordTransaction("Withdrawal", amount);
        }
    }

    // Interest calculation for savings account
    public double calculateInterest() {
        double interestRate = 2.0; // 2% as example
        return (getBalance() * interestRate) / 100;
    }
}
