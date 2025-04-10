package model;
import controller.BankController;
import javafx.scene.control.Alert;

import java.time.LocalDate;

// Fixed Deposit Account
public class FixedDepositAccount extends BankAccount {
    private final LocalDate maturityDate;
    private final double interestRate;

    public FixedDepositAccount(String accNum, String name, double initBal, int maturityDays, double interestRate) {
        super(accNum, name, initBal);
        this.maturityDate = LocalDate.now().plusDays(maturityDays);
        this.interestRate = interestRate;
    }

    public double calculateMaturityAmount() {
        if (isMatured()) {
            return this.balance * (1 + (this.interestRate / 100));
        } else {
            return this.balance;
        }
    }

    @Override
    public void deposit(double amount) {
        BankController.showAlert("Warning", "Cannot deposit into a fixed deposit account after creation.", Alert.AlertType.WARNING);
    }

    @Override
    public void withdraw(double amount) {
        if (LocalDate.now().isAfter(this.maturityDate) || LocalDate.now().isEqual(this.maturityDate)) {
            if (amount <= this.balance) {
                this.balance -= amount;
                recordTransaction("Withdrawal", amount);
            } else {
                BankController.showAlert("Message", "Insufficient balance.", Alert.AlertType.INFORMATION);
            }
        } else {
            BankController.showAlert("Warning", "Withdrawal not allowed before maturity date: " + this.maturityDate, Alert.AlertType.WARNING);
        }
    }

    public boolean isMatured() {
        return !LocalDate.now().isBefore(this.maturityDate);
    }

    public LocalDate getMaturityDate() {
        return this.maturityDate;
    }
}