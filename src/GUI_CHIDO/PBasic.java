package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class PBasic extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PBasic.class.getName());
    private JFrame parentFrame;
    private BackgroundPanel backgroundPanel;
    private int currentClienteId; // Ahora se recibirá del constructor

    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JButton jButton2;

    public PBasic() {
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        this.setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        // Inicializar currentClienteId con valor por defecto
        this.currentClienteId = 1; // Valor por defecto para testing

        initComponents();
        this.setSize(1050, 470); // Tamaño consistente con Plan Ultra
        this.setLocationRelativeTo(null);
    }

    public PBasic(JFrame parentFrame, int clienteId) { // Constructor con clienteId
        this();
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Configurar botón Regresar con estilo mejorado
        try {
            ImageIcon icon = new ImageIcon(getClass().getResource("/Imagenes/Atras.png"));
            if (icon.getIconWidth() > 0) {
                Image img = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                jButton2.setIcon(new ImageIcon(img));
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

        // Título principal con estilo mejorado
        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 52));
        jLabel2.setText("Plan Básico – 300 Mbps");
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setBounds(450, 5, 550, 70);

        // Subtítulo promocional
        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setText("Promoción \"Empieza sin límites\"");
        jLabel3.setForeground(Color.WHITE);
        jLabel3.setBounds(450, 75, 300, 25);

        // Imagen del plan
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Plan Basico 300 mb promo.png")));
        jLabel1.setBounds(50, 100, 350, 300);

        // Características con posicionamiento mejorado
        int featureStartX = 450;
        int featureStartY = 130;
        int featureLineHeight = 25;

        jLabel10.setText("🏠 Ideal para hogares pequeños, estudiantes y navegación básica");
        jLabel10.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel10.setForeground(Color.BLACK);
        jLabel10.setBounds(featureStartX, featureStartY, 550, 20);

        jLabel5.setText("📅 Primer mes a solo $10 - ¡Aprovecha la oferta!");
        jLabel5.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel5.setForeground(Color.BLACK);
        jLabel5.setBounds(featureStartX, featureStartY + featureLineHeight, 450, 20);

        jLabel6.setText("📶 Router Wi-Fi GRATIS incluido en la instalación");
        jLabel6.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel6.setForeground(Color.BLACK);
        jLabel6.setBounds(featureStartX, featureStartY + (2 * featureLineHeight), 450, 20);

        jLabel7.setText("💥 Instalación en menos de 24 horas SIN COSTO");
        jLabel7.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel7.setForeground(Color.BLACK);
        jLabel7.setBounds(featureStartX, featureStartY + (3 * featureLineHeight), 450, 20);

        jLabel8.setText("🎁 3 meses de soporte técnico prioritario incluido");
        jLabel8.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel8.setForeground(Color.BLACK);
        jLabel8.setBounds(featureStartX, featureStartY + (4 * featureLineHeight), 450, 20);

        jLabel9.setText("� Perfecto para redes sociales, clases en línea y entretenimiento básico");
        jLabel9.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jLabel9.setForeground(Color.BLACK);
        jLabel9.setBounds(featureStartX, featureStartY + (5 * featureLineHeight), 550, 20);

        jToggleButton1.setBackground(new java.awt.Color(255, 255, 255));
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setBounds(496, 305, 223, 70);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));

        jLabel10.setText("💨 300 Mbps de velocidad");
        jLabel10.setBounds(375, 135, 266, 16);
        jLabel10.setForeground(Color.WHITE);

        jLabel11.setText("Contrata ahora por $10 el primer mes, luego $25/mes.");
        jLabel11.setBounds(181, 403, 319, 16);
        jLabel11.setForeground(Color.WHITE);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 36));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setBounds(30, 30, 270, 60);

        backgroundPanel.add(jLabel1);
        backgroundPanel.add(jLabel2);
        backgroundPanel.add(jLabel3);
        backgroundPanel.add(jLabel5);
        backgroundPanel.add(jLabel6);
        backgroundPanel.add(jLabel7);
        backgroundPanel.add(jLabel8);
        backgroundPanel.add(jLabel9);
        backgroundPanel.add(jToggleButton1);
        backgroundPanel.add(jLabel10);
        backgroundPanel.add(jLabel11);
        backgroundPanel.add(jButton2);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        Facturación facturacionFrame = new Facturación("Plan Básico", 20.00, currentClienteId);
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
        java.awt.EventQueue.invokeLater(() -> new PBasic().setVisible(true));
    }
}
