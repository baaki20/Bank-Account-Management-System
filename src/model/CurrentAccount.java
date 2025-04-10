package model;

import controller.BankController;
import javafx.scene.control.Alert;

// Current Account
public class CurrentAccount extends BankAccount {
    private final double OVERDRAFT_LIMIT = -500.0;

    public CurrentAccount(String accNum, String name, double initBal) {
        super(accNum, name, initBal);
    }

    @Override
    public void deposit(double amount) {
        this.balance += amount;
        recordTransaction("Deposit", amount);
    }

    @Override
    public void withdraw(double amount) {
        if (this.balance - amount < OVERDRAFT_LIMIT) {
            BankController.showAlert("Warning", "Withdrawal denied: Overdraft limit exceeded.", Alert.AlertType.WARNING);
        } else {
            this.balance -= amount;
            recordTransaction("Withdrawal", amount);
        }
    }
}
