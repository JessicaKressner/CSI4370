import java.awt.GridLayout;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
public class ATMSystem extends User {

    // class for the ATM system
        // variable for the UI
        private JFrame frame;
        // variable for the UI
        private JPanel panel;
        // stores users in the system
        private Map<String, User> users;
        // variable for the user that is logged in
        private User currentUser;
        // file where user data is stored
        private static final String STORAGE_FILE = "users.dat";

        // constructor for ATM system
        public ATMSystem() {
            super();
            users = loadUsers();
            createLoginScreen();
        }

        // load users from file
        private Map<String, User> loadUsers() {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(STORAGE_FILE))) {
                return (Map<String, User>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                return new HashMap<>();
            }
        }

        // saves users to the file
        private void saveUsers() {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(STORAGE_FILE))) {
                oos.writeObject(users);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Login GUI
        private void createLoginScreen() {
            frame = new JFrame("ATM System");
            panel = new JPanel();
            panel.setLayout(new GridLayout(5, 1));

            JLabel userLabel = new JLabel("Username:");
            JTextField userField = new JTextField();
            JLabel pinLabel = new JLabel("PIN:");
            JPasswordField pinField = new JPasswordField();
            JButton loginButton = new JButton("Login");
            JButton registerButton = new JButton("Register");

            // event listener for the login button
            loginButton.addActionListener(e -> {
                String username = userField.getText();
                String pin = new String(pinField.getPassword());
                // checks if the username field is empty
                if (username.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username is blank. Enter a username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if the PIN field is empty
                if (pin.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "PIN is blank. Enter a pin", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if the credentials are valid
                if (users.containsKey(username) && users.get(username).authenticate(username, pin)) {
                    currentUser = users.get(username);
                    createMainMenu();
                } else {
                    JOptionPane.showMessageDialog(frame, "Invalid Credentials", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            // event listener for the register button
            registerButton.addActionListener(e -> {
                String newUsername = JOptionPane.showInputDialog(frame, "Enter new username:");
                String newPin = JOptionPane.showInputDialog(frame, "Enter new PIN:");
                // checks if the username field is empty
                if (newUsername == null || newUsername.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Username is blank. Enter a username", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if the pin field is empty
                if (newPin == null || newPin.trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "PIN is blank. Enter a pin", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if username input is valid
                if (!isValidUsername(newUsername)) {
                    JOptionPane.showMessageDialog(frame, "Invalid username. Only letters and numbers are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if PIN input is valid
                if (!isValidPin(newPin)) {
                    JOptionPane.showMessageDialog(frame, "Invalid PIN. Only numbers are allowed.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                // checks if the username is available as the username must be unique
                if (!users.containsKey(newUsername)) {
                    users.put(newUsername, new User(newUsername, newPin, 0.0));
                    saveUsers();
                    JOptionPane.showMessageDialog(frame, "User registered successfully", "Success", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame, "Username already exists", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            panel.add(userLabel);
            panel.add(userField);
            panel.add(pinLabel);
            panel.add(pinField);
            panel.add(loginButton);
            panel.add(registerButton);

            frame.setContentPane(panel);
            frame.setSize(400, 300);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        }

        // function that verifies if the username is valid
        private boolean isValidUsername(String username) {
            return Pattern.matches("^[a-zA-Z0-9]+$", username);
        }

        // function that verifies if the PIN is valid
        private boolean isValidPin(String pin) {
            return Pattern.matches("^[0-9]+$", pin);
        }

        // Main Menu GUI
        private void createMainMenu() {
            panel.removeAll();
            panel.setLayout(new GridLayout(5, 1));

            JButton balanceButton = new JButton("View Balance");
            JButton depositButton = new JButton("Deposit");
            JButton withdrawButton = new JButton("Withdraw");
            JButton historyButton = new JButton("Transaction History");
            JButton logoutButton = new JButton("Logout");

            // event listener for the balance button
            balanceButton.addActionListener(e ->
                    JOptionPane.showMessageDialog(frame, "Balance: $" + User.formatAmount(currentUser.getBalance()), "Balance", JOptionPane.INFORMATION_MESSAGE)
            );

            // event listener for the deposit button
            depositButton.addActionListener(e -> {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter deposit amount:");
                try {
                    // checks if the amount to deposit is greater than 0
                    double amount = validateAmount(amountStr);
                    if (amount >= 0) {
                        currentUser.deposit(amount);
                        saveUsers();
                        JOptionPane.showMessageDialog(frame, "Deposited $" + User.formatAmount(amount), "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(frame, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // event listener for the withdraw button
            withdrawButton.addActionListener(e -> {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter withdrawal amount:");
                try {
                    double amount = validateAmount(amountStr);
                    // checks if the amount to withdraw is greater than 0
                    if (amount >= 0) {
                        if (currentUser.withdraw(amount)) {
                            saveUsers();
                            JOptionPane.showMessageDialog(frame, "Withdrew $" + User.formatAmount(amount), "Success", JOptionPane.INFORMATION_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(frame, "Insufficient funds", "Error", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(frame, "Amount must be greater than zero", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Invalid amount. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });

            // event listener for the history button
            historyButton.addActionListener(e -> {
                ArrayList<String> history = currentUser.getTransactionHistory();
                String message = history.isEmpty() ? "No transactions yet." : String.join("\n", history);
                JOptionPane.showMessageDialog(frame, message, "Transaction History", JOptionPane.INFORMATION_MESSAGE);
            });

            // event listener for the logout button
            logoutButton.addActionListener(e -> {
                currentUser = null;
                createLoginScreen();
            });

            panel.add(balanceButton);
            panel.add(depositButton);
            panel.add(withdrawButton);
            panel.add(historyButton);
            panel.add(logoutButton);

            frame.setContentPane(panel);
            frame.revalidate();
            frame.repaint();
        }

        // function that checks if the amount is valid
        private double validateAmount(String amountStr) throws NumberFormatException {
            if (amountStr == null || amountStr.isEmpty()) {
                throw new NumberFormatException("Amount cannot be empty.");
            }
            try {
                double amount = Double.parseDouble(amountStr);
                if (amount < 0) {
                    throw new NumberFormatException("Amount cannot be negative.");
                }
                return amount;
            } catch (NumberFormatException e) {
                throw new NumberFormatException("Invalid amount. Please enter a valid number.");
            }
        }

        // function to run the system
        public static void main(String[] args) {
            SwingUtilities.invokeLater(ATMSystem::new);
        }
    }
