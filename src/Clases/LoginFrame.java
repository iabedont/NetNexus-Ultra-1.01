package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton userRadioButton;
    private JRadioButton adminRadioButton;
    private JRadioButton techRadioButton;

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión");
        setSize(600,250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(5, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Usuario (idCliente):"));
        usernameField = new JTextField();
        panel.add(usernameField);

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

        panel.add(new JLabel("Tipo:"));
        panel.add(typePanel);

        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.addActionListener(e -> validateLogin());
        panel.add(loginButton);

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            new Bienvenida().setVisible(true);
            dispose();
        });
        panel.add(backButton);

        add(panel);
    }

    private void validateLogin() {
        String idCliente = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String userType = userRadioButton.isSelected() ? "Usuario" : 
                         adminRadioButton.isSelected() ? "Administrador" : 
                         techRadioButton.isSelected() ? "Técnico" : null;

        if (idCliente.isEmpty() || password.isEmpty() || userType == null) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos y seleccione un tipo de usuario", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM cliente WHERE idCliente = ? AND password = ? AND tipo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCliente);
            stmt.setString(2, password);
            stmt.setString(3, userType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                if (userType.equals("Administrador")) {
                    // Tu lógica para administrador aquí
                    JOptionPane.showMessageDialog(this,
                        "Inicio de sesión como Administrador.",
                        "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Ejemplo: new AdminFrame().setVisible(true); dispose();
                } else if (userType.equals("Usuario")) {
                    // MODIFICADO: Crear el objeto Cliente y abrir User_1 con ese objeto
                    Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                    new GUI_CHIDO.User_1(cliente).setVisible(true);
                    dispose();
                } else if (userType.equals("Técnico")) {
                    // Tu lógica para técnico aquí
                    JOptionPane.showMessageDialog(this,
                        "Inicio de sesión como Técnico.",
                        "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE);
                    // Ejemplo: new TechFrame().setVisible(true); dispose();
                }
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Credenciales incorrectas o tipo de usuario incorrecto.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con la base de datos.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}