package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;

public class BankController {

    @FXML private TextField nameField;
    @FXML private TextField initialBalanceField;
    @FXML private ComboBox<String> accountTypeBox;
    @FXML private TextField transactionAmountField;

    @FXML private TableView<TransactionNode> transactionTable;
    @FXML private TableColumn<TransactionNode, String> dateColumn;
    @FXML private TableColumn<TransactionNode, String> typeColumn;
    @FXML private TableColumn<TransactionNode, Double> amountColumn;
    @FXML private TableColumn<TransactionNode, Double> balanceColumn;

    private BankAccount currentAccount;

    @FXML
    public void initialize() {
        accountTypeBox.setItems(FXCollections.observableArrayList("Savings", "Current", "Fixed Deposit"));

        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDateString()));
        typeColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().type));
        amountColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().amount));
        balanceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().balanceAfter));
    }

    @FXML
    public void handleCreateAccount() {
        String name = nameField.getText().trim();
        String type = accountTypeBox.getValue();

        // Validate name and account type
        if (name.isEmpty()) {
            showAlert("Error", "Please enter a valid name.", Alert.AlertType.WARNING);
            return;
        }
        if (type == null) {
            showAlert("Error", "Please select an account type.", Alert.AlertType.WARNING);
            return;
        }

        double balance = 0;
        try {
            balance = Double.parseDouble(initialBalanceField.getText().trim());
            if (balance < 0) {
                throw new NumberFormatException("Balance cannot be negative.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid initial balance.", Alert.AlertType.WARNING);
            return;
        }

        // Account creation logic
        switch (type) {
            case "Savings":
                currentAccount = new SavingsAccount("S" + System.currentTimeMillis(), name, balance);
                SavingsAccount sa = (SavingsAccount) currentAccount;

                double saInterest = sa.calculateInterest(); // Assuming this method exists
                showAlert("Account creation",
                        "Savings Account created for " + name + "\n" +
                                "Interest Earned (est.): " + saInterest + "\n" +
                                "Projected Balance: " + (balance + saInterest),
                        Alert.AlertType.INFORMATION);
                break;

            case "Current":
                currentAccount = new CurrentAccount("C" + System.currentTimeMillis(), name, balance);
                break;
            case "Fixed Deposit":
                currentAccount = new FixedDepositAccount("F" + System.currentTimeMillis(), name, balance, 30, 5.0);
                FixedDepositAccount fd = (FixedDepositAccount) currentAccount;

                double fdInterest = fd.calculateMaturityAmount() - balance;
                showAlert("Account creation",
                        "Fixed Deposit Account created for " + name + "\n" +
                                "Interest Earned: " + fdInterest + "\n" +
                                "Maturity Amount: " + fd.calculateMaturityAmount() + "\n" +
                                "Maturity Date: " + fd.getMaturityDate(),
                        Alert.AlertType.INFORMATION);
                break;

            default:
                showAlert("Error", "Invalid account type.", Alert.AlertType.WARNING);
                return;
        }

        // Show confirmation for non-Fixed accounts
        if (!(currentAccount instanceof FixedDepositAccount)) {
            showAlert("Account Creation", "Account created for " + name, Alert.AlertType.INFORMATION);
        }

        refreshTransactionTable();
    }

    @FXML
    public void handleDeposit() {
        if (currentAccount == null) {
            showAlert("Error", "No account found. Please create an account first.", Alert.AlertType.WARNING);
            return;
        }

        double amount = 0;
        try {
            amount = Double.parseDouble(transactionAmountField.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException("Deposit amount must be positive.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid deposit amount.", Alert.AlertType.WARNING);
            return;
        }

        currentAccount.deposit(amount);
        refreshTransactionTable();
    }

    @FXML
    public void handleWithdraw() {
        if (currentAccount == null) {
            showAlert("Error", "No account found. Please create an account first.", Alert.AlertType.WARNING);
            return;
        }

        double amount = 0;
        try {
            amount = Double.parseDouble(transactionAmountField.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException("Withdrawal amount must be positive.");
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid withdrawal amount.", Alert.AlertType.WARNING);
            return;
        }

        if (currentAccount.getBalance() < amount) {
            showAlert("Error", "Insufficient funds for withdrawal.", Alert.AlertType.WARNING);
            return;
        }

        currentAccount.withdraw(amount);
        refreshTransactionTable();
    }

    @FXML
    public void handleCheckBalance() {
        // Simply display the balance for the current account
        if (currentAccount != null) {
            showAlert("Balance", "Your current balance is: " + currentAccount.getBalance(), Alert.AlertType.INFORMATION);
        } else {
            showAlert("Error", "No account found. Please create an account first.", Alert.AlertType.WARNING);
        }
    }

    private void refreshTransactionTable() {
        ObservableList<TransactionNode> transactions = FXCollections.observableArrayList();
        TransactionNode current = currentAccount.transactionHead;
        while (current != null) {
            transactions.add(current);
            current = current.next;
        }
        transactionTable.setItems(transactions);
    }

    public static void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);

        // Set the title and message of the alert
        alert.setTitle(title);
        alert.setHeaderText(null);  // Optional, if you want to hide the header
        alert.setContentText(message);

        // Show the alert
        alert.showAndWait();
    }

}
