package Clases;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Registro extends JFrame {
    private JTextField idClienteField;
    private JTextField nameField;
    private JTextField lastNameField;
    private JTextField phoneField;
    private JTextField emailField;
    private JPasswordField passwordField;
    private JRadioButton userRadioButton;
    private JRadioButton adminRadioButton;
    private JRadioButton techRadioButton;

    public Registro() {
        setTitle("Registro - Net Nexus Ultra");
        setSize(600, 350); // Aumentamos el ancho a 400 píxeles
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Bloquea el redimensionamiento de la ventana

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(9, 2, 5, 5)); // Mantenemos el layout actual
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

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

        panel.add(new JLabel("Correo electrónico (Usuario):"));
        emailField = new JTextField();
        panel.add(emailField);

        panel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Panel para los radio buttons
        JPanel typePanel = new JPanel(new FlowLayout());
        typePanel.setOpaque(false);

        ButtonGroup typeGroup = new ButtonGroup();
        userRadioButton = new JRadioButton("Usuario");
        adminRadioButton = new JRadioButton("Administrador");
        techRadioButton = new JRadioButton("Técnico");

        // Establecer fondo transparente
        userRadioButton.setOpaque(false);
        adminRadioButton.setOpaque(false);
        techRadioButton.setOpaque(false);

        typeGroup.add(userRadioButton);
        typeGroup.add(adminRadioButton);
        typeGroup.add(techRadioButton);
        userRadioButton.setSelected(true); // Selecciona "Usuario" por defecto

        typePanel.add(userRadioButton);
        typePanel.add(adminRadioButton);
        typePanel.add(techRadioButton);

        panel.add(new JLabel("Tipo de usuario:"));
        panel.add(typePanel);

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
        String idCliente = idClienteField.getText();
        String name = nameField.getText();
        String lastName = lastNameField.getText();
        String phone = phoneField.getText();
        String email = emailField.getText();
        String password = new String(passwordField.getPassword());
        String userType = userRadioButton.isSelected() ? "Usuario" : 
                         adminRadioButton.isSelected() ? "Administrador" : 
                         techRadioButton.isSelected() ? "Técnico" : null;

        if (idCliente.isEmpty() || name.isEmpty() || lastName.isEmpty() || phone.isEmpty() || email.isEmpty() || password.isEmpty() || userType == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos y seleccione un tipo de usuario", 
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