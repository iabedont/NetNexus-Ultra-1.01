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
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

public class Servicios extends javax.swing.JFrame {

    private JFrame parentFrame;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Servicios.class.getName());
    private int currentClienteId;

    private BackgroundPanel backgroundPanel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;

    public Servicios(JFrame parentFrame) {
        this(parentFrame, -1); // Constructor por defecto, clienteId -1 si no se conoce
    }

    public Servicios(JFrame parentFrame, int clienteId) {
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
        initComponents();
        this.setLocationRelativeTo(null);
        this.setSize(600, 600);

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentPositions();
            }
        });
        adjustComponentPositions();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        this.setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Nuestros Servicios");

        jButton1.setBackground(new Color(90, 150, 200));
        jButton1.setFont(new Font("Segoe UI", Font.BOLD, 22));
        jButton1.setForeground(Color.WHITE);
        try {
            ImageIcon atrasIcon = new ImageIcon(getClass().getResource("/Imagenes/Atras.png"));
            if (atrasIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = atrasIcon.getImage();
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                jButton1.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen 'Atras.png' no encontrada. Usando solo texto para el botón Regresar.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen 'Atras.png': " + e.getMessage());
        }
        jButton1.setText("Regresar");
        jButton1.setContentAreaFilled(false);
        jButton1.setBorderPainted(false);
        jButton1.setFocusPainted(false);
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));
        backgroundPanel.add(jButton1);

        jLabel1.setFont(new Font("ROG Fonts", 0, 36));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("NUESTROS SERVICIOS");
        jLabel1.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel1);

        Font serviceButtonFont = new Font("Segoe UI", Font.BOLD, 18);
        Color serviceButtonFgColor = new Color(30, 50, 70);

        jButton9.setFont(serviceButtonFont);
        jButton9.setForeground(serviceButtonFgColor);
        jButton9.setText("Internet");
        jButton9.setContentAreaFilled(false);
        jButton9.setBorderPainted(false);
        jButton9.setFocusPainted(false);
        jButton9.addActionListener(evt -> jButton9ActionPerformed(evt));
        backgroundPanel.add(jButton9);

        jLabel6.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel6.setForeground(serviceButtonFgColor);
        jLabel6.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel6.setText("Internet");
        backgroundPanel.add(jLabel6);

        jButton8.setFont(serviceButtonFont);
        jButton8.setForeground(serviceButtonFgColor);
        jButton8.setText("Televisión");
        jButton8.setContentAreaFilled(false);
        jButton8.setBorderPainted(false);
        jButton8.setFocusPainted(false);
        jButton8.addActionListener(evt -> jButton8ActionPerformed(evt));
        backgroundPanel.add(jButton8);

        jLabel5.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel5.setForeground(serviceButtonFgColor);
        jLabel5.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel5.setText("Televisión");
        backgroundPanel.add(jLabel5);

        jButton7.setFont(serviceButtonFont);
        jButton7.setForeground(serviceButtonFgColor);
        jButton7.setText("Telefonía Móvil");
        jButton7.setContentAreaFilled(false);
        jButton7.setBorderPainted(false);
        jButton7.setFocusPainted(false);
        jButton7.addActionListener(evt -> jButton7ActionPerformed(evt));
        backgroundPanel.add(jButton7);

        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel3.setForeground(serviceButtonFgColor);
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("Telefonía Móvil");
        backgroundPanel.add(jLabel3);

        jButton4.setFont(serviceButtonFont);
        jButton4.setForeground(serviceButtonFgColor);
        jButton4.setText("Telefonía Fija");
        jButton4.setContentAreaFilled(false);
        jButton4.setBorderPainted(false);
        jButton4.setFocusPainted(false);
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));
        backgroundPanel.add(jButton4);

        jLabel4.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabel4.setForeground(serviceButtonFgColor);
        jLabel4.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel4.setText("Telefonía Fija");
        backgroundPanel.add(jLabel4);
    }

    private void adjustComponentPositions() {
        int newWidth = getContentPane().getWidth();
        int newHeight = getContentPane().getHeight();

        int regresarButtonWidth = 240;
        int regresarButtonHeight = 65;
        jButton1.setBounds(10, 10, regresarButtonWidth, regresarButtonHeight);

        jLabel1.setBounds(0, 70, newWidth, 40);

        int buttonSize = 120;
        int horizontalGap = 60;
        int verticalGap = 40;

        int startX = (newWidth - (2 * buttonSize + horizontalGap)) / 2;
        int startY = jLabel1.getY() + jLabel1.getHeight() + 30;

        jButton9.setBounds(startX, startY, buttonSize, buttonSize);
        jLabel6.setBounds(startX, startY + buttonSize, buttonSize, 20);

        jButton8.setBounds(startX + buttonSize + horizontalGap, startY, buttonSize, buttonSize);
        jLabel5.setBounds(startX + buttonSize + horizontalGap, startY + buttonSize, buttonSize, 20);

        startY += buttonSize + 30 + verticalGap;
        jButton7.setBounds(startX, startY, buttonSize, buttonSize);
        jLabel3.setBounds(startX, startY + buttonSize, buttonSize, 20);

        jButton4.setBounds(startX + buttonSize + horizontalGap, startY, buttonSize, buttonSize);
        jLabel4.setBounds(startX + buttonSize + horizontalGap, startY + buttonSize, buttonSize, 20);

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {
        Internet internetFrame = new Internet(this, currentClienteId); 
        internetFrame.setVisible(true);
        this.setVisible(false);
    }

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        // Asegúrate de que Television.java tiene un constructor Television(JFrame parentFrame, int clienteId)
        Television televisionFrame = new Television(this, currentClienteId);
        televisionFrame.setVisible(true);
        this.setVisible(false);
    }

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        // Asegúrate de que TeleMovil.java tiene un constructor TeleMovil(JFrame parentFrame, int clienteId)
        TeleMovil teleMovilFrame = new TeleMovil(this, currentClienteId);
        teleMovilFrame.setVisible(true);
        this.setVisible(false);
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        // Asegúrate de que TeleFija.java tiene un constructor TeleFija(JFrame parentFrame, int clienteId)
        TeleFija teleFijaFrame = new TeleFija(this, currentClienteId);
        teleFijaFrame.setVisible(true);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }

        java.awt.EventQueue.invokeLater(() -> new Servicios(null).setVisible(true));
    }
}
