package model;
import java.util.Date;

// Linked List Node for Transactions
public class TransactionNode {
    public String type;
    public double amount;
    public double balanceAfter;
    Date date;
    public TransactionNode next;

    public TransactionNode(String type, double amount, double balanceAfter) {
        this.type = type;
        this.amount = amount;
        this.balanceAfter = balanceAfter;
        this.date = new Date();
    }

    @Override
    public String toString() {
        return date + " - " + type + ": GHS " + amount + ", Balance: GHS " + balanceAfter;
    }

    public String getDateString() {
        return date.toString();
    }

}
