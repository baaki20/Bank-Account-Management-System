package model;

// Interface for common banking operations
interface BankOperations {
    void deposit(double amount);
    void withdraw(double amount);
    void checkBalance();
    void viewTransactionHistory(int n);
}
