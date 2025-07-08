/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/*
 * Click nargs://netbeans/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nargs://netbeans/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Clases;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import GUI_CHIDO.Perfil_User; // Importar Perfil_User
import GUI_CHIDO.User_1; // Importar User_1

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
                // Obtener los datos del usuario
                int id = rs.getInt("idCliente"); // Assuming idCliente is an int in Cliente class
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                String storedPassword = rs.getString("password"); // Obtener la contraseña almacenada

                // Crear un objeto Cliente con los datos obtenidos, incluyendo la contraseña
                Cliente loggedInUser = new Cliente(id, nombre, apellido, telefono, email, storedPassword);

                if (userType.equals("Usuario")) {
                    // Pasar el objeto Cliente a User_1
                    User_1 user1Frame = new User_1(loggedInUser); // Pasa el objeto Cliente
                    user1Frame.setVisible(true);
                } else if (userType.equals("Administrador")) {
                    new AdminFrame().setVisible(true);
                } else if (userType.equals("Técnico")) {
                    new TechnicianFrame().setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error al validar credenciales: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
