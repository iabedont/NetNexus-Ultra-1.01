package Clases;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class LoginFrame extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JRadioButton userRadioButton;
    private JRadioButton adminRadioButton;
    private JRadioButton techRadioButton;

    public LoginFrame() {
        setTitle("Login - Sistema de Gestión");
        setSize(600, 300); // Aumenté el alto para el botón de recuperación
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false); // Bloquea el redimensionamiento de la ventana

        BackgroundPanel panel = new BackgroundPanel("/Imagenes/fondo.png");
        panel.setLayout(new GridLayout(6, 2, 5, 5)); // Añadí una fila para el botón de recuperación
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

        userRadioButton.setOpaque(false);
        adminRadioButton.setOpaque(false);
        techRadioButton.setOpaque(false);

        typeGroup.add(userRadioButton);
        typeGroup.add(adminRadioButton);
        typeGroup.add(techRadioButton);
        userRadioButton.setSelected(true);

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

        // Botón para recuperar contraseña
        JButton recoverPasswordButton = new JButton("Recuperar Contraseña");
        recoverPasswordButton.addActionListener(e -> showRecoverPasswordDialog());
        panel.add(recoverPasswordButton);
        panel.add(new JLabel("")); // Espacio vacío para alinear el layout

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
                    JOptionPane.showMessageDialog(this,
                        "Inicio de sesión como Administrador.",
                        "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE);
                    new AdminFrame().setVisible(true);
                    dispose();
                } else if (userType.equals("Usuario")) {
                    Cliente cliente = new Cliente(
                        rs.getInt("idCliente"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("password")
                    );
                    
                    JOptionPane.showMessageDialog(this,
                        "Inicio de sesión exitoso.\nBienvenido " + cliente.getNombre() + " " + cliente.getApellido(),
                        "Login Exitoso",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Usar versión temporal funcional
                    new GUI_CHIDO.User_1_Temp(cliente).setVisible(true);
                    dispose();
                } else if (userType.equals("Técnico")) {
                    JOptionPane.showMessageDialog(this,
                        "Inicio de sesión como Técnico.",
                        "Bienvenido",
                        JOptionPane.INFORMATION_MESSAGE);
                    new TechnicianFrame().setVisible(true);
                    dispose();
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
                String sql = "SELECT password FROM cliente WHERE idCliente = ? AND email = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, idCliente);
                stmt.setString(2, email);
                ResultSet rs = stmt.executeQuery();

                if (rs.next()) {
                    String password = rs.getString("password");
                    JOptionPane.showMessageDialog(this, 
                        "Su contraseña es: " + password + "\nGuárdela en un lugar seguro.", 
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