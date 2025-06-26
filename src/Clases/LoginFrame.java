/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> userTypeCombo;

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Usuario:"));
        usernameField = new JTextField();
        panel.add(usernameField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        panel.add(new JLabel("Tipo:"));
        userTypeCombo = new JComboBox<>(new String[]{"Usuario", "Administrador", "Técnico"});
        panel.add(userTypeCombo);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(e -> validateLogin());
        panel.add(loginButton);

        add(panel);
    }

    private void validateLogin() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = (String) userTypeCombo.getSelectedItem();

        if (username.equals("admin") && password.equals("admin123") && userType.equals("Administrador")) {
            new AdminFrame().setVisible(true);
            dispose();
        } else if (username.equals("user") && password.equals("user123") && userType.equals("Usuario")) {
            new UserFrame().setVisible(true);
            dispose();
        } else if (username.equals("tech") && password.equals("tech123") && userType.equals("Técnico")) {
            new TechnicianFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}