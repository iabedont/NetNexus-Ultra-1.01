package GUI_CHIDO;

import Clases.DatabaseConnection;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

public class Perfil_User extends JFrame {

    private JFrame parentFrame;
    private String currentUserId;
    private String currentUserPassword;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Perfil_User.class.getName());
    private javax.swing.JTextField jTextFieldEmail;
    private JLabel jLabelIdCliente;
    private javax.swing.JPasswordField jPasswordField2;

    public Perfil_User(JFrame parentFrame) {
        this.parentFrame = parentFrame;
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(800, 700);
        setFieldsEditable(false);
        jPasswordField2.setEchoChar('*');

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentPositions();
            }
        });

        adjustComponentPositions();
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
        jTextField1.setEditable(false);
        jTextField5.setEditable(editable);
        jTextField4.setEditable(editable);
        jTextField3.setEditable(editable);
        jTextFieldEmail.setEditable(editable);
        jPasswordField2.setEditable(editable);

        if (editable) {
            jPasswordField2.setEchoChar((char) 0);
        } else {
            jPasswordField2.setEchoChar('*');
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanel1 = new javax.swing.JPanel();
        jButton2 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jTextFieldEmail = new javax.swing.JTextField();
        jLabelIdCliente = new javax.swing.JLabel();
        jPasswordField2 = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jPanel1.setLayout(null);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setBounds(10, 20, 230, 60);
        jPanel1.add(jButton2);

        jLabel3.setBackground(new java.awt.Color(0, 204, 204));
        jLabel3.setFont(new java.awt.Font("ROG Fonts", 0, 48));
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("Usuario");
        jLabel3.setBounds(300, 20, 530, 50);
        jPanel1.add(jLabel3);

        jLabel6.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("Apellido:");
        jLabel6.setBounds(70, 370, 100, 25);
        jPanel1.add(jLabel6);

        jLabel7.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("Correo:");
        jLabel7.setBounds(70, 410, 100, 25);
        jPanel1.add(jLabel7);

        jLabel8.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("Telefono:");
        jLabel8.setBounds(70, 450, 100, 25);
        jPanel1.add(jLabel8);

        jLabel9.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("Contraseña");
        jLabel9.setBounds(70, 490, 100, 25);
        jPanel1.add(jLabel9);

        jLabel10.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("Nombre:");
        jLabel10.setBounds(70, 330, 100, 25);
        jPanel1.add(jLabel10);

        jLabelIdCliente.setFont(new java.awt.Font("ROG Fonts", 0, 18));
        jLabelIdCliente.setForeground(new java.awt.Color(0, 0, 0));
        jLabelIdCliente.setText("ID Cliente:");
        jLabelIdCliente.setBounds(70, 290, 100, 25);
        jPanel1.add(jLabelIdCliente);

        jTextField1.setText("jTextField1");
        jTextField1.setBounds(240, 290, 270, 26);
        jPanel1.add(jTextField1);

        jTextField5.setText("jTextField1");
        jTextField5.addActionListener(evt -> jTextField5ActionPerformed(evt));
        jTextField5.setBounds(240, 330, 270, 26);
        jPanel1.add(jTextField5);

        jTextField4.setText("jTextField1");
        jTextField4.setBounds(240, 370, 270, 26);
        jPanel1.add(jTextField4);

        jTextField3.setText("jTextField1");
        jTextField3.setBounds(240, 450, 270, 26);
        jPanel1.add(jTextField3);

        jTextFieldEmail.setText("jTextFieldEmail");
        jTextFieldEmail.setBounds(240, 410, 270, 26);
        jPanel1.add(jTextFieldEmail);

        jPasswordField2.setText("jPasswordField2");
        jPasswordField2.setBounds(240, 490, 270, 26);
        jPanel1.add(jPasswordField2);

        jButton1.setBackground(new java.awt.Color(0, 0, 0));
        jButton1.setFont(new java.awt.Font("ROG Fonts", 0, 12));
        jButton1.setText("Editar");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        jButton1.setBounds(480, 550, 110, 30);
        jPanel1.add(jButton1);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Perfil_U.png")));
        jLabel2.setBounds(180, 120, 220, 170);
        jPanel1.add(jLabel2);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/fondo.png")));
        jLabel1.setBounds(0, 0, 800, 700);
        jPanel1.add(jLabel1);

        jPanel1.setBounds(0, 0, 800, 700);
        getContentPane().add(jPanel1);

        pack();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
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
            }
        }
    }

    private void adjustComponentPositions() {
        int newWidth = getContentPane().getWidth();
        int newHeight = getContentPane().getHeight();

        jPanel1.setBounds(0, 0, newWidth, newHeight);

        int centerX = newWidth / 2;

        int regresarButtonWidth = 230;
        int regresarButtonHeight = 60;
        jButton2.setBounds(20, 30, regresarButtonWidth, regresarButtonHeight);

        int titleWidth = 320;
        int minTitleX = jButton2.getX() + jButton2.getWidth() + 20;
        int calculatedTitleX = centerX - titleWidth / 2;
        jLabel3.setBounds(Math.max(minTitleX, calculatedTitleX), 30, titleWidth, 50);

        int profileImageWidth = 200;
        jLabel2.setBounds(centerX - profileImageWidth / 2, 90, profileImageWidth, 150);

        int fieldHeight = 26;
        int labelHeight = 25;
        int verticalGap = 20;
        int initialYForFields = jLabel2.getY() + jLabel2.getHeight() + 30;

        int labelColWidth = 150;
        int fieldColWidth = 300;
        int totalFieldBlockWidth = labelColWidth + fieldColWidth + 10;

        int newLabelColX = centerX - totalFieldBlockWidth / 2;
        int newFieldColX = newLabelColX + labelColWidth + 10;

        int currentY = initialYForFields;

        jLabelIdCliente.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField1.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        jLabel10.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField5.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        jLabel6.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField4.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        jLabel8.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextField3.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        jLabel7.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jTextFieldEmail.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        currentY += fieldHeight + verticalGap;
        jLabel9.setBounds(newLabelColX, currentY, labelColWidth, labelHeight);
        jPasswordField2.setBounds(newFieldColX, currentY, fieldColWidth, fieldHeight);

        int buttonWidth = 150;
        int buttonHeight = 40;
        int buttonPadding = 50;
        jButton1.setBounds(newWidth - buttonWidth - buttonPadding, newHeight - buttonHeight - buttonPadding, buttonWidth, buttonHeight);

        jPanel1.revalidate();
        jPanel1.repaint();
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

    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
}