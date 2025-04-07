import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class User implements Serializable {
        // username of the user
        private String username;
        // pin of the user's account
        private String pin;
        // balance of the user's account
        private double balance;
        // list of transactions
        private ArrayList<String> transactionHistory;

        // constructor for the user class
        public User(String username, String pin, double balance) {
            this.username = username;
            this.pin = pin;
            this.balance = balance;
            this.transactionHistory = new ArrayList<>();
        }

    public User() {

    }


    // function that verifies the username and pin are in the system
        public boolean authenticate(String username, String pin) {
            return this.username.equals(username) && this.pin.equals(pin);
        }

        // function returns the user's balance
        public double getBalance() {
            return balance;
        }

    // function returns the user's username
    public String getUsername() {
            return username;
    }

        // function that adds money to the user's account
        public void deposit(double amount) {
            balance += amount;
            transactionHistory.add("Deposited: $" + formatAmount(amount));
        }

        // function that withdraws money from the user's account
        public boolean withdraw(double amount) {
            if (amount > balance) {
                return false;
            }
            balance -= amount;
            transactionHistory.add("Withdrew: $" + formatAmount(amount));
            return true;
        }

        // returns a list of the user's transactions
        public ArrayList<String> getTransactionHistory() {
            return transactionHistory;
        }

        // Format numbers to 2 decimal places
        public static String formatAmount(double amount) {
            DecimalFormat df = new DecimalFormat("0.00");
            return df.format(amount);
        }
    }
