package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.BorderFactory;

public class Internet extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Internet.class.getName());
    private JFrame parentFrame;
    private int currentClienteId; // Nuevo campo para almacenar el ID del cliente

    private BackgroundPanel backgroundPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;

    public Internet() {
        initComponents();
        this.setSize(720, 650);
        this.setLocationRelativeTo(null);
        this.setResizable(false);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentPositions();
            }
        });
        adjustComponentPositions();
    }

    public Internet(JFrame parentFrame, int clienteId) { // Constructor que recibe el clienteId
        this();
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Planes de Internet");
        backgroundPanel.setLayout(null);
        this.setContentPane(backgroundPanel);

        jButton2.setBackground(new Color(90, 150, 200));
        jButton2.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton2.setForeground(Color.WHITE);
        
        try {
            ImageIcon atrasIcon = new ImageIcon(getClass().getResource("/Imagenes/Atras.png"));
            if (atrasIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = atrasIcon.getImage();
                Image scaledImg = img.getScaledInstance(30, 30, Image.SCALE_SMOOTH);
                jButton2.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen 'Atras.png' no encontrada. Usando solo texto para el botón Regresar.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen 'Atras.png': " + e.getMessage());
        }
        
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.setBorderPainted(false);
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        backgroundPanel.add(jButton2);

        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("¡Tu próxima gran experiencia ");
        jLabel1.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel1);

        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("comienza seleccionando el plan ideal!");
        jLabel2.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel2);

        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("INTERNET DE ALTA VELOCIDAD – ¡Más que solo velocidad!");
        jLabel3.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel3);

        jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel4.setText("💡 Todos nuestros planes incluyen:");
        jLabel4.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel4);

        jLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel5.setText("📶 Router Wi-Fi GRATIS");
        jLabel5.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel5);

        jLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel6.setText("🛠️ Instalación sin costo y en menos de 24 horas");
        jLabel6.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel6);

        jLabel7.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel7.setText("📺 Compatible con Smart TVs, consolas y dispositivos móviles");
        jLabel7.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel7);

        jLabel8.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel8.setText("🔒 Control parental y filtros opcionales incluidos");
        jLabel8.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel8);

        jLabel9.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel9.setText("✅ Cambios de plan sin penalización");
        jLabel9.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel9);

        jLabel10.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel10.setText("✅ Atención personalizada en cada etapa");
        jLabel10.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel10);

        jLabel11.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel11.setText("✅ Facturación clara, sin cobros ocultos");
        jLabel11.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel11);

        jLabel12.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel12.setText("✅ Infraestructura moderna y cobertura nacional");
        jLabel12.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel12);

        jButton3.setText("Plan Básico");
        jButton3.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton3.setForeground(Color.WHITE);
        jButton3.setBackground(new Color(50, 120, 180));
        jButton3.setContentAreaFilled(true);
        jButton3.setBorder(BorderFactory.createLineBorder(new Color(100, 180, 255), 3));
        setupPlanButtonIcon(jButton3, "/Imagenes/PBasico-removebg-preview.png");
        jButton3.addActionListener(this::jButton3ActionPerformed); // Añadir ActionListener
        backgroundPanel.add(jButton3);

        jButton1.setText("Plan Estándar");
        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton1.setForeground(new Color(255, 215, 0));
        jButton1.setBackground(new Color(50, 50, 50));
        jButton1.setContentAreaFilled(true);
        jButton1.setBorder(BorderFactory.createLineBorder(new Color(255, 215, 0), 3));
        setupPlanButtonIcon(jButton1, "/Imagenes/PEstandar-removebg-preview (1).png");
        jButton1.addActionListener(this::jButton1ActionPerformed); // Añadir ActionListener
        backgroundPanel.add(jButton1);

        jButton4.setText("Plan Ultra");
        jButton4.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButton4.setForeground(new Color(200, 0, 200));
        jButton4.setBackground(new Color(50, 50, 50));
        jButton4.setContentAreaFilled(true);
        jButton4.setBorder(BorderFactory.createLineBorder(new Color(200, 0, 200), 3));
        setupPlanButtonIcon(jButton4, "/Imagenes/PUltra-removebg-preview (1).png");
        jButton4.addActionListener(this::jButton4ActionPerformed); // Añadir ActionListener
        backgroundPanel.add(jButton4);

        pack();
    }

    private void setupPlanButtonIcon(JButton button, String imagePath) {
        try {
            ImageIcon planIcon = new ImageIcon(getClass().getResource(imagePath));
            if (planIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = planIcon.getImage();
                Image scaledImg = img.getScaledInstance(100, 100, Image.SCALE_SMOOTH); 
                button.setIcon(new ImageIcon(scaledImg));
                button.setVerticalTextPosition(SwingConstants.BOTTOM);
                button.setHorizontalTextPosition(SwingConstants.CENTER);
            } else {
                logger.log(Level.WARNING, "Imagen '{0}' no encontrada para el botón. Se mostrará solo texto.", imagePath);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen '{0}': {1}", new Object[]{imagePath, e.getMessage()});
        }
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        // Pasar el ID del cliente al constructor de PBasic
        PBasic pBasicFrame = new PBasic(this, currentClienteId);
        pBasicFrame.setVisible(true);
        this.setVisible(false);
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // Pasar el ID del cliente al constructor de PEstandar
        PEstandar pEstandarFrame = new PEstandar(this, currentClienteId);
        pEstandarFrame.setVisible(true);
        this.setVisible(false);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // Pasar el ID del cliente al constructor de PUltra
        PUltra pUltraFrame = new PUltra(this, currentClienteId);
        pUltraFrame.setVisible(true);
        this.setVisible(false);
    }

    private void adjustComponentPositions() {
        int newWidth = getContentPane().getWidth();
        int newHeight = getContentPane().getHeight();

        int regresarButtonWidth = 180;
        int regresarButtonHeight = 50;
        jButton2.setBounds(20, 20, regresarButtonWidth, regresarButtonHeight);

        int titleYStart = 90;
        int titleLineHeight = 35;
        int subTitleLineHeight = 25;

        jLabel1.setBounds(0, titleYStart, newWidth, titleLineHeight);
        jLabel2.setBounds(0, jLabel1.getY() + jLabel1.getHeight(), newWidth, titleLineHeight);
        jLabel3.setBounds(0, jLabel2.getY() + jLabel2.getHeight() + 10, newWidth, subTitleLineHeight);

        int featureCol1X = 50;
        int featureCol2X = newWidth / 2 + 20;
        int featureStartY = jLabel3.getY() + jLabel3.getHeight() + 30;
        int featureLineHeight = 22;

        jLabel4.setBounds(featureCol1X, featureStartY, 400, 20);
        jLabel5.setBounds(featureCol1X + 20, featureStartY + featureLineHeight, 300, 20);
        jLabel6.setBounds(featureCol1X + 20, featureStartY + (2 * featureLineHeight), 300, 20);
        jLabel7.setBounds(featureCol1X + 20, featureStartY + (3 * featureLineHeight), 380, 20);
        jLabel8.setBounds(featureCol1X + 20, featureStartY + (4 * featureLineHeight), 300, 20);
        
        jLabel9.setBounds(featureCol2X, featureStartY + featureLineHeight, 300, 20);
        jLabel10.setBounds(featureCol2X, featureStartY + (2 * featureLineHeight), 300, 20);
        jLabel11.setBounds(featureCol2X, featureStartY + (3 * featureLineHeight), 300, 20);
        jLabel12.setBounds(featureCol2X, featureStartY + (4 * featureLineHeight), 300, 20);

        int planButtonWidth = 180;
        int planButtonHeight = 100;
        int gapBetweenButtons = 20;
        
        int basicButtonY = jLabel8.getY() + jLabel8.getHeight() + 40;
        int basicButtonX = (newWidth - planButtonWidth) / 2;
        jButton3.setBounds(basicButtonX, basicButtonY, planButtonWidth, planButtonHeight);

        int standardUltraY = basicButtonY + planButtonHeight + 20;
        int totalStandardUltraWidth = (2 * planButtonWidth) + gapBetweenButtons;
        int standardUltraStartX = (newWidth - totalStandardUltraWidth) / 2;

        jButton1.setBounds(standardUltraStartX, standardUltraY, planButtonWidth, planButtonHeight);
        jButton4.setBounds(standardUltraStartX + planButtonWidth + gapBetweenButtons, standardUltraY, planButtonWidth, planButtonHeight);

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Internet().setVisible(true));
    }
}
