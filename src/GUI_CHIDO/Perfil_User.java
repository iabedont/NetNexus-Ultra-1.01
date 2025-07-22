package GUI_CHIDO;

import Clases.BackgroundPanel;
import Clases.DatabaseConnection; // Importar BackgroundPanel
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.SwingConstants; // Importar java.awt.Image

public class Perfil_User extends JFrame {

    private JFrame parentFrame;
    private String currentUserId;
    private String currentUserPassword;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Perfil_User.class.getName());
    private javax.swing.JTextField jTextFieldEmail;
    private JLabel jLabelIdCliente;
    private javax.swing.JPasswordField jPasswordField2;

    // Componentes que ahora se añadirán directamente al BackgroundPanel
    private BackgroundPanel backgroundPanel; // El panel de fondo principal
    private javax.swing.JButton jButton2; // Regresar
    private javax.swing.JLabel jLabel3; // Título "Usuario"
    private javax.swing.JLabel jLabel6; // Apellido
    private javax.swing.JLabel jLabel7; // Correo
    private javax.swing.JLabel jLabel8; // Telefono
    private javax.swing.JLabel jLabel9; // Contraseña
    private javax.swing.JLabel jLabel10; // Nombre
    private javax.swing.JTextField jTextField1; // ID Cliente
    private javax.swing.JTextField jTextField3; // Telefono
    private javax.swing.JTextField jTextField4; // Apellido
    private javax.swing.JTextField jTextField5; // Nombre
    private javax.swing.JButton jButton1; // Editar/Guardar
    private javax.swing.JLabel jLabel2; // Imagen de perfil

    public Perfil_User(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        this.setSize(800, 800); // Establecer el tamaño primero
        this.setLocationRelativeTo(null); // Luego centrar la ventana
        setFieldsEditable(false);
        jPasswordField2.setEchoChar('*');

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentPositions();
            }
        });

        // adjustComponentPositions(); // Se llamará automáticamente con el primer redimensionamiento o al ejecutar
    }

    public void setUserData(String idCliente, String nombre, String apellido, String telefono, String email, String password) {
        this.currentUserId = idCliente;
        this.currentUserPassword = password;

        jTextField1.setText(idCliente);
        jTextField5.setText(nombre);
        jTextField4.setText(apellido);
        jTextField3.setText(telefono);
        jTextFieldEmail.setText(email);
        jPasswordField2.setText(password);
    }

    private void setFieldsEditable(boolean editable) {
        jTextField1.setEditable(false); // ID Cliente siempre no editable
        jTextField5.setEditable(editable);
        jTextField4.setEditable(editable);
        jTextField3.setEditable(editable);
        jTextFieldEmail.setEditable(editable);
        jPasswordField2.setEditable(editable);

        // Cambiar el color de fondo para indicar editabilidad
        Color editableBg = new Color(245, 250, 255); // Azul cielo muy claro, casi blanco
        Color nonEditableBg = new Color(220, 235, 250); // Azul pastel claro

        jTextField1.setBackground(nonEditableBg);
        jTextField5.setBackground(editable ? editableBg : nonEditableBg);
        jTextField4.setBackground(editable ? editableBg : nonEditableBg);
        jTextField3.setBackground(editable ? editableBg : nonEditableBg);
        jTextFieldEmail.setBackground(editable ? editableBg : nonEditableBg);
        jPasswordField2.setBackground(editable ? editableBg : nonEditableBg);

        if (editable) {
            jPasswordField2.setEchoChar((char) 0); // Mostrar caracteres
        } else {
            jPasswordField2.setEchoChar('*'); // Ocultar caracteres
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Inicialización del BackgroundPanel como content pane
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        backgroundPanel.setLayout(null); // Usamos layout nulo para posicionamiento absoluto

        // Establecer el BackgroundPanel como el panel de contenido del JFrame
        this.setContentPane(backgroundPanel);

        // Inicialización de componentes (ahora se añadirán a backgroundPanel)
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel(); // Imagen de perfil
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelIdCliente = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Perfil de Usuario"); // Título de la ventana

        // Botón Regresar
        jButton2.setBackground(new Color(90, 150, 200)); // Azul suave
        jButton2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton2.setForeground(Color.WHITE);
        jButton2.setText("Regresar");
        jButton2.setFocusPainted(false);
        jButton2.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        backgroundPanel.add(jButton2); // Añadir a backgroundPanel

        // Título "Usuario"
        jLabel3.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jLabel3.setForeground(new Color(30, 50, 70)); // Azul oscuro-gris
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("PERFIL DE USUARIO");
        backgroundPanel.add(jLabel3); // Añadir a backgroundPanel

        // Imagen de perfil (placeholder)
        try {
            ImageIcon profileIcon = new ImageIcon(getClass().getResource("/Imagenes/Perfil_U.png"));
            if (profileIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                // Escalar la imagen si es necesario para que encaje en 220x220
                Image img = profileIcon.getImage();
                Image scaledImg = img.getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                jLabel2.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen de perfil no encontrada en /Imagenes/Perfil_U.png. Usando texto de marcador.");
                jLabel2.setText("[Imagen de Perfil no encontrada]");
                jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
                jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
                jLabel2.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230))); // Borde pastel
                jLabel2.setForeground(new Color(50, 80, 100)); // Color de texto para placeholder
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen de perfil: /Imagenes/Perfil_U.png", e);
            jLabel2.setText("[Error al cargar imagen]");
            jLabel2.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
            jLabel2.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230))); // Borde pastel
            jLabel2.setForeground(new Color(50, 80, 100)); // Color de texto para placeholder
        }
        backgroundPanel.add(jLabel2); // Añadir a backgroundPanel

        // Etiquetas y Campos de Texto
        // ID Cliente
        jLabelIdCliente.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelIdCliente.setForeground(new Color(50, 80, 100)); // Azul oscuro-gris para etiquetas
        jLabelIdCliente.setText("ID Cliente:");
        backgroundPanel.add(jLabelIdCliente); // Añadir a backgroundPanel
        jTextField1.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextField1.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230))); // Borde pastel
        jTextField1.setOpaque(true); // Asegurar que el fondo del campo se vea
        jTextField1.setBackground(new Color(220, 235, 250)); // Fondo para no editable
        backgroundPanel.add(jTextField1); // Añadir a backgroundPanel

        // Nombre
        jLabel10.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel10.setForeground(new Color(50, 80, 100));
        jLabel10.setText("Nombre:");
        backgroundPanel.add(jLabel10); // Añadir a backgroundPanel
        jTextField5.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextField5.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230)));
        jTextField5.setOpaque(true);
        backgroundPanel.add(jTextField5); // Añadir a backgroundPanel

        // Apellido
        jLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel6.setForeground(new Color(50, 80, 100));
        jLabel6.setText("Apellido:");
        backgroundPanel.add(jLabel6); // Añadir a backgroundPanel
        jTextField4.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextField4.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230)));
        jTextField4.setOpaque(true);
        backgroundPanel.add(jTextField4); // Añadir a backgroundPanel

        // Telefono
        jLabel8.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel8.setForeground(new Color(50, 80, 100));
        jLabel8.setText("Teléfono:");
        backgroundPanel.add(jLabel8); // Añadir a backgroundPanel
        jTextField3.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextField3.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230)));
        jTextField3.setOpaque(true);
        backgroundPanel.add(jTextField3); // Añadir a backgroundPanel

        // Correo
        jLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel7.setForeground(new Color(50, 80, 100));
        jLabel7.setText("Correo:");
        backgroundPanel.add(jLabel7); // Añadir a backgroundPanel
        jTextFieldEmail.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldEmail.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230)));
        jTextFieldEmail.setOpaque(true);
        backgroundPanel.add(jTextFieldEmail); // Añadir a backgroundPanel

        // Contraseña
        jLabel9.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabel9.setForeground(new Color(50, 80, 100));
        jLabel9.setText("Contraseña:");
        backgroundPanel.add(jLabel9); // Añadir a backgroundPanel
        jPasswordField2.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jPasswordField2.setBorder(BorderFactory.createLineBorder(new Color(180, 210, 230)));
        jPasswordField2.setOpaque(true);
        backgroundPanel.add(jPasswordField2); // Añadir a backgroundPanel

        // Botón Editar/Guardar
        jButton1.setBackground(new Color(120, 190, 240)); // Azul pastel vibrante para Editar
        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton1.setForeground(Color.WHITE);
        jButton1.setText("Editar");
        jButton1.setFocusPainted(false);
        jButton1.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20)); // Padding
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        backgroundPanel.add(jButton1); // Añadir a backgroundPanel

        pack(); // Ajusta el tamaño de la ventana a los componentes
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        if (jButton1.getText().equals("Editar")) {
            JPasswordField pf = new JPasswordField();
            int okCxl = JOptionPane.showConfirmDialog(null, pf, "Ingrese su contraseña actual para editar", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (okCxl == JOptionPane.OK_OPTION) {
                String enteredPassword = new String(pf.getPassword());
                if (enteredPassword.equals(currentUserPassword)) {
                    setFieldsEditable(true);
                    jButton1.setText("Guardar");
                    jButton1.setBackground(new Color(140, 220, 180)); // Verde suave para Guardar
                    JOptionPane.showMessageDialog(this, "Ahora puede editar sus datos.");
                } else {
                    JOptionPane.showMessageDialog(this, "Contraseña incorrecta. No se puede editar.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else if (jButton1.getText().equals("Guardar")) {
            try (Connection conn = DatabaseConnection.getConnection()) {
                String newPassword = new String(jPasswordField2.getPassword());
                String sql = "UPDATE cliente SET nombre = ?, apellido = ?, telefono = ?, email = ?, password = ? WHERE idCliente = ?";
                PreparedStatement stmt = conn.prepareStatement(sql);
                stmt.setString(1, jTextField5.getText());
                stmt.setString(2, jTextField4.getText());
                stmt.setString(3, jTextField3.getText());
                stmt.setString(4, jTextFieldEmail.getText());
                stmt.setString(5, newPassword);
                stmt.setString(6, currentUserId);
                
                int rowsAffected = stmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Datos actualizados correctamente.");
                    currentUserPassword = newPassword;
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo actualizar los datos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al actualizar datos en la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, "Error al actualizar datos", e);
            } finally {
                setFieldsEditable(false);
                jButton1.setText("Editar");
                jButton1.setBackground(new Color(120, 190, 240)); // Azul pastel vibrante para Editar
            }
        }
    }

    private void adjustComponentPositions() {
        int newWidth = getContentPane().getWidth();
        int newHeight = getContentPane().getHeight();

        // Los componentes se posicionan directamente sobre el backgroundPanel
        // que es el content pane.

        int centerX = newWidth / 2;

        // Botón Regresar
        int regresarButtonWidth = 180;
        int regresarButtonHeight = 50;
        jButton2.setBounds(30, 30, regresarButtonWidth, regresarButtonHeight);

        // Título "PERFIL DE USUARIO"
        int titleWidth = 400;
        jLabel3.setBounds(centerX - titleWidth / 2, 30, titleWidth, 40);

        // Imagen de perfil (ahora 220x220)
        int profileImageWidth = 220; // Nuevo tamaño
        int profileImageHeight = 220; // Nuevo tamaño
        jLabel2.setBounds(centerX - profileImageWidth / 2, 90, profileImageWidth, profileImageHeight);

        int fieldHeight = 30; // Altura unificada para campos
        int labelHeight = 25; // Altura unificada para etiquetas
        int verticalGap = 15; // Espacio entre campos

        int labelColWidth = 150;
        int fieldColWidth = 300;
        int totalFieldBlockWidth = labelColWidth + fieldColWidth + 20; // Espacio entre label y field

        int newLabelColX = centerX - totalFieldBlockWidth / 2;
        int newFieldColX = newLabelColX + labelColWidth + 20;

        int currentY = jLabel2.getY() + jLabel2.getHeight() + 30; // Debajo de la imagen de perfil

        // ID Cliente
        jLabelIdCliente.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField1.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        // Nombre
        jLabel10.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField5.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        // Apellido
        jLabel6.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField4.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        // Teléfono
        jLabel8.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField3.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        // Correo
        jLabel7.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextFieldEmail.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        // Contraseña
        jLabel9.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jPasswordField2.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        // Botón Editar/Guardar
        int buttonWidth = 180;
        int buttonHeight = 50;
        int buttonY = newHeight - buttonHeight - 40; // 40px desde el borde inferior
        jButton1.setBounds(centerX - buttonWidth / 2, buttonY, buttonWidth, buttonHeight);

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Perfil_User(null).setVisible(true));
    }
}
