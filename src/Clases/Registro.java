package Clases;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class Registro extends JFrame {
    private JTextField idClienteField;
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;

    public Registro() {
        setTitle("Registro - Net Nexus Ultra");
        setSize(600, 300); // Reducido el alto porque eliminamos la selección de tipo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(8, 2, 5, 5)); // Reducido a 8 filas
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Título
        JLabel titleLabel = new JLabel("Registro de Usuario");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);
        panel.add(new JLabel("")); // Espacio vacío

        panel.add(new JLabel("idCliente:"));
        idClienteField = new JTextField();
        panel.add(idClienteField);

        panel.add(new JLabel("Nombre:"));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Apellido:"));
        lastNameField = new JTextField();
        panel.add(lastNameField);

        panel.add(new JLabel("Teléfono:"));
        phoneField = new JTextField();
        panel.add(phoneField);

        panel.add(new JLabel("Correo electrónico:"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        JButton submitButton = new JButton("Registrar Usuario");
        submitButton.setBackground(new Color(70, 130, 180));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
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
        String idCliente = idClienteField.getText();
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = "Usuario"; // Siempre registra como Usuario

        if (idCliente.isEmpty() || name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (idExists(idCliente)) {
            JOptionPane.showMessageDialog(this, 
                "El idCliente ya existe", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "INSERT INTO cliente (idCliente, nombre, apellido, telefono, email, password, tipo) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCliente);
            stmt.setString(2, name);
            stmt.setString(3, lastName);
            stmt.setString(4, phone);
            stmt.setString(5, email);
            stmt.setString(6, password);
            stmt.setString(7, userType);
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, 
                "Registro exitoso como Usuario. Su ID de Cliente es: " + idCliente + ".\nAhora puede iniciar sesión.", 
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

    private boolean idExists(String id) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT COUNT(*) FROM cliente WHERE idCliente = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}