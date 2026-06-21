package views;

import controllers.StaffController;
import models.Staff;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LoginView extends JFrame {
    private StaffController staffController = new StaffController();
    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginView() {
        setTitle("Construction Material Supply System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null); 
        setResizable(true);

        // Create UI Components
        JLabel userLabel = new JLabel("Username:");
        JLabel passLabel = new JLabel("Password:");
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Login");

        // Layout using GridBagLayout 
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Row 0: Username
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(userLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(usernameField, gbc);

        // Row 1: Password
        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(passLabel, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(passwordField, gbc);

        // Row 2: Login Button (Centered)
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        panel.add(loginButton, gbc);

        add(panel);

        // ACTION LISTENER: Connects GUI to CONTROLLER
        loginButton.addActionListener((ActionEvent e) -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            // 1. Call the Controller 
            Staff loggedIn = staffController.authenticateLogin(username, password);

            // 2. Handle the result 
            if (loggedIn != null) {
                JOptionPane.showMessageDialog(this, "Welcome, " + loggedIn.getFullName() + "!");
                new DashboardView(loggedIn).setVisible(true);
                dispose(); 
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials or inactive account.", " Login Failed.", JOptionPane.ERROR_MESSAGE);
                passwordField.setText("");
            }
        });

        getRootPane().setDefaultButton(loginButton);
    }   
}
