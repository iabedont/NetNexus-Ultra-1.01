package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PUltra extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PUltra.class.getName());
    private JFrame parentFrame;
    private BackgroundPanel backgroundPanel;
    private int currentClienteId; // Ahora se recibirá del constructor

    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JButton jButton2;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel10;

    public PUltra() {
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        this.setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        // Inicializar currentClienteId con valor por defecto
        this.currentClienteId = 1; // Valor por defecto para testing
        
        initComponents();
        this.setSize(1050, 470);
        this.setLocationRelativeTo(null);
    }

    public PUltra(JFrame parentFrame, int clienteId) { // Constructor con clienteId
        this();
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 36));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        
        try {
            ImageIcon atrasIcon = new ImageIcon(getClass().getResource("/Imagenes/Atras.png"));
            if (atrasIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = atrasIcon.getImage();
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                jButton2.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen 'Atras.png' no encontrada. Usando solo texto para el botón Regresar.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen 'Atras.png': " + e.getMessage());
        }
        
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setBounds(30, 20, 270, 60);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 52));
        jLabel2.setText("Plan Ultra – 1 Gbps");
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setBounds(450, 5, 480, 70);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setText("Promoción “Conexión ÉPICA”");
        jLabel3.setForeground(Color.WHITE);
        jLabel3.setBounds(450, 75, 300, 25);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Plan Ultra.png")));
        jLabel1.setBounds(50, 100, 350, 300);

        int featureStartX = 450;
        int featureStartY = 130;
        int featureLineHeight = 25;

        jLabel10.setText("🎮 Juega en línea sin lag, transmite en vivo, descarga películas en segundos");
        jLabel10.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel10.setForeground(Color.BLACK);
        jLabel10.setBounds(featureStartX, featureStartY, 550, 20);

        jLabel7.setText("🎮 Incluye 3 meses de Xbox Game Pass o Steam Wallet $10");
        jLabel7.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel7.setForeground(Color.BLACK);
        jLabel7.setBounds(featureStartX, featureStartY + featureLineHeight, 450, 20);

        jLabel6.setText("🚀 Velocidad garantizada o devolución parcial del pago");
        jLabel6.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel6.setForeground(Color.BLACK);
        jLabel6.setBounds(featureStartX, featureStartY + (2 * featureLineHeight), 450, 20);

        jLabel5.setText("🏆 20% de descuento por 3 meses");
        jLabel5.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel5.setForeground(Color.BLACK);
        jLabel5.setBounds(featureStartX, featureStartY + (3 * featureLineHeight), 450, 20);

        jLabel8.setText("📞 Soporte técnico prioritario sin costo adicional");
        jLabel8.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel8.setForeground(Color.BLACK);
        jLabel8.setBounds(featureStartX, featureStartY + (4 * featureLineHeight), 450, 20);
        
        jLabel4.setText("💵 $60/mes – Tu hogar se transforma en un centro de alto rendimiento digital");
        jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel4.setForeground(Color.BLACK);
        jLabel4.setBounds(featureStartX, featureStartY + (5 * featureLineHeight), 550, 20);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setText("");
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setBorderPainted(false);
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));
        jToggleButton1.setBounds(560, featureStartY + (6 * featureLineHeight) + 10, 223, 70);

        jLabel9.setText("🔴 Para gamers, streamers, y usuarios que necesitan el máximo rendimiento sin límites.");
        jLabel9.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel9.setForeground(Color.BLACK);
        jLabel9.setBounds(100, 400, 700, 20);

        backgroundPanel.add(jLabel5);
        backgroundPanel.add(jLabel6);
        backgroundPanel.add(jLabel7);
        backgroundPanel.add(jLabel8);
        backgroundPanel.add(jLabel9);
        backgroundPanel.add(jButton2);
        backgroundPanel.add(jToggleButton1);
        backgroundPanel.add(jLabel1);
        backgroundPanel.add(jLabel2);
        backgroundPanel.add(jLabel3);
        backgroundPanel.add(jLabel4);
        backgroundPanel.add(jLabel10);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Facturación facturacionFrame = new Facturación("Plan Ultra", 60.00, currentClienteId);
        facturacionFrame.setParentFrame(this);
        facturacionFrame.setVisible(true);
        this.setVisible(false);
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new PUltra().setVisible(true));
    }
}
