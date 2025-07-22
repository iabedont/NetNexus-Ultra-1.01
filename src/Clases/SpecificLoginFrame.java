package Clases;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class SpecificLoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private String userType;

    public SpecificLoginFrame(String userType) {
        this.userType = userType;
        setTitle("Login " + userType + " - Sistema de Gestión");
        setSize(500, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(5, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Título específico del tipo de usuario
        JLabel titleLabel = new JLabel("Iniciar Sesión como " + userType);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(titleLabel);
        panel.add(new JLabel("")); // Espacio vacío

        // Campo de usuario
        JLabel userLabel = new JLabel("Usuario (idCliente):");
        userLabel.setForeground(Color.WHITE);
        panel.add(userLabel);
        usernameField = new JTextField();
        panel.add(usernameField);

        // Campo de contraseña
        JLabel passLabel = new JLabel("Contraseña:");
        passLabel.setForeground(Color.WHITE);
        panel.add(passLabel);
        passwordField = new JPasswordField();
        panel.add(passwordField);

        // Botones
        JButton loginButton = new JButton("Iniciar Sesión");
        loginButton.setBackground(getButtonColor(userType));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> validateLogin());
        panel.add(loginButton);

        JButton backButton = new JButton("Atrás");
        backButton.addActionListener(e -> {
            new UserTypeSelectionFrame().setVisible(true);
            dispose();
        });
        panel.add(backButton);

        // Botón para recuperar contraseña (solo visible para usuarios)
        if (userType.equals("Usuario")) {
            JButton recoverPasswordButton = new JButton("Recuperar Contraseña");
            recoverPasswordButton.addActionListener(e -> showRecoverPasswordDialog());
            panel.add(recoverPasswordButton);
            panel.add(new JLabel("")); // Espacio vacío
        } else {
            panel.add(new JLabel("")); // Espacio vacío
            panel.add(new JLabel("")); // Espacio vacío
        }

        add(panel);
    }

    private Color getButtonColor(String userType) {
        switch (userType) {
            case "Usuario":
                return new Color(70, 130, 180);
            case "Administrador":
                return new Color(220, 20, 60);
            case "Técnico":
                return new Color(34, 139, 34);
            default:
                return Color.GRAY;
        }
    }

    private void validateLogin() {
        String idCliente = usernameField.getText();
        String inputPassword = new String(passwordField.getPassword());

        if (idCliente.isEmpty() || inputPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor complete todos los campos", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT * FROM cliente WHERE idCliente = ? AND password = ? AND tipo = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, idCliente);
            stmt.setString(2, inputPassword);
            stmt.setString(3, userType);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Extraer datos del ResultSet antes de cerrar la conexión
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                int clienteId = rs.getInt("idCliente");
                String telefono = rs.getString("telefono");
                String email = rs.getString("email");
                String dbPassword = rs.getString("password");
                
                // Cerrar este frame primero
                dispose();
                
                // Mostrar mensaje de bienvenida
                JOptionPane.showMessageDialog(null,
                    "Inicio de sesión exitoso como " + userType + ".\nBienvenido " + nombre + " " + apellido,
                    "Login Exitoso",
                    JOptionPane.INFORMATION_MESSAGE);

                // Abrir la interfaz correspondiente según el tipo de usuario
                SwingUtilities.invokeLater(() -> {
                    switch (userType) {
                        case "Administrador":
                            try {
                                AdminFrame adminFrame = new AdminFrame();
                                adminFrame.setVisible(true);
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                JOptionPane.showMessageDialog(null, 
                                    "Error al abrir el panel de administrador: " + ex.getMessage(), 
                                    "Error", 
                                    JOptionPane.ERROR_MESSAGE);
                            }
                            break;
                        case "Usuario":
                            Cliente cliente = new Cliente(
                                clienteId,
                                nombre,
                                apellido,
                                telefono,
                                email,
                                dbPassword
                            );
                            new GUI_CHIDO.User_1(cliente).setVisible(true);
                            break;
                        case "Técnico":
                            new TechnicianFrame().setVisible(true);
                            break;
                    }
                });
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Credenciales incorrectas para " + userType + ".", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Error al conectar con la base de datos: " + ex.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showRecoverPasswordDialog() {
        JTextField idField = new JTextField(10);
        JTextField emailField = new JTextField(20);

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Ingrese su idCliente:"));
        panel.add(idField);
        panel.add(new JLabel("Ingrese su email:"));
        panel.add(emailField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Recuperar Contraseña",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String idCliente = idField.getText().trim();
            String email = emailField.getText().trim();

            if (idCliente.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor complete todos los campos.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            try (Connection conn = DatabaseConnection.getConnection()) {
                String sql = "SELECT password FROM cliente WHERE idCliente = ? AND email = ? AND tipo = 'Usuario'";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idCliente);
                stmt.setString(2, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String recoveredPassword = rs.getString("password");
                    JOptionPane.showMessageDialog(this, 
                        "Su contraseña es: " + recoveredPassword + "\nGuárdela en un lugar seguro.", 
                        "Recuperación Exitosa", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "No se encontró un usuario con ese idCliente y email.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Error al recuperar la contraseña: " + ex.getMessage(), 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
