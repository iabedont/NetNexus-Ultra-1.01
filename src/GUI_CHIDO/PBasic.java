package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
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
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
    }

    public PBasic(JFrame parentFrame, int clienteId) { // Constructor con clienteId
        this();
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
    }

    @SuppressWarnings("unchecked")
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

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Plan Basico 300 mb promo.png")));
        jLabel1.setBounds(49, 107, 300, 256);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 36));
        jLabel2.setText("Plan Básico – 300 Mbps");
        jLabel2.setBounds(375, 35, 405, 57);
        jLabel2.setForeground(Color.WHITE);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setText("Promoción “Empieza sin límites”");
        jLabel3.setBounds(435, 92, 283, 25);
        jLabel3.setForeground(Color.WHITE);

        jLabel5.setText("📅 Primer mes a solo $10");
        jLabel5.setBounds(375, 169, 145, 16);
        jLabel5.setForeground(Color.WHITE);

        jLabel6.setText("📶 Router Wi-Fi GRATIS");
        jLabel6.setBounds(375, 203, 145, 16);
        jLabel6.setForeground(Color.WHITE);

        jLabel7.setText("💥 Instalación en menos de 24 horas SIN COSTO");
        jLabel7.setBounds(375, 237, 266, 16);
        jLabel7.setForeground(Color.WHITE);

        jLabel8.setText("🎁 3 meses de soporte prioritario");
        jLabel8.setBounds(375, 271, 266, 16);
        jLabel8.setForeground(Color.WHITE);

        jLabel9.setText("🟢 Perfecto para estudiantes, hogares pequeños o quienes recién empiezan en el mundo digital.");
        jLabel9.setBounds(95, 363, 521, 16);
        jLabel9.setForeground(Color.WHITE);

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
