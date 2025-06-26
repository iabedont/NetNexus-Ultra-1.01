/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.Random;

public class Registro extends JFrame {
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public Registro() {
        setTitle("Registro - Net Nexus Ultra");
        setSize(300, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(7, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Apellido:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Teléfono:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Correo electrónico (Usuario):"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton submitButton = new JButton("Registrar");
        submitButton.addActionListener(e -> registerUser());
        panel.add(submitButton);

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            new Bienvenida().setVisible(true);
            dispose();
        });
        panel.add(backButton);

        add(panel);
    }

    private void registerUser() {
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());

        if (name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        String idCliente = generateIdCliente();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, contraseña) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCliente);
            stmt.setString(2, name);
            stmt.setString(3, lastName);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, 
                "Registro exitoso. Su ID de Cliente es: " + idCliente + ". Ahora puede iniciar sesión.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            new Bienvenida().setVisible(true);
            dispose();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error al registrar: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private String generateIdCliente() {
        Random rand = new Random();
        String id;
        do {
            id = String.format("%010d", rand.nextInt(1000000000));
        } while (idExists(id));
        return id;
    }

    private boolean idExists(String id) {
        return false; // Placeholder, needs actual database check
    }
}