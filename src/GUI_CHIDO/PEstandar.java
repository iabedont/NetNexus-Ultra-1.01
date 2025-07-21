package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
import javax.swing.JFrame;

public class PEstandar extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PEstandar.class.getName());
    private JFrame parentFrame;
    private BackgroundPanel backgroundPanel;
    private int currentClienteId; // Ahora se recibirá del constructor

    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JButton jButton2;

    public PEstandar() {
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        this.setContentPane(backgroundPanel);
        backgroundPanel.setLayout(null);

        // Inicializar currentClienteId con valor por defecto
        this.currentClienteId = 1; // Valor por defecto para testing

        initComponents();
        this.setSize(800, 500);
        this.setLocationRelativeTo(null);
    }

    public PEstandar(JFrame parentFrame, int clienteId) { // Constructor con clienteId
        this();
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asignar el ID del cliente
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel8.setText("📶 Router Wi-Fi DUAL BAND incluido");
        jLabel8.setBounds(353, 289, 266, 16);
        jLabel8.setForeground(Color.WHITE);

        jLabel9.setText("🟡 Ideal para familias con varios dispositivos y fanáticos del streaming.");
        jLabel9.setBounds(175, 363, 393, 16);
        jLabel9.setForeground(Color.WHITE);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.setBounds(467, 307, 251, 67);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Plan Estandar promo.png")));
        jLabel1.setBounds(18, 101, 300, 256);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 36));
        jLabel2.setText("Plan Estándar – 500 Mbps");
        jLabel2.setBounds(331, 26, 447, 57);
        jLabel2.setForeground(Color.WHITE);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setText("Promoción “Velocidad sin comparación”");
        jLabel3.setBounds(436, 83, 333, 25);
        jLabel3.setForeground(Color.WHITE);

        jLabel5.setText("🔁 Cambia de proveedor y te damos 1 mes GRATIS");
        jLabel5.setBounds(353, 169, 279, 16);
        jLabel5.setForeground(Color.WHITE);

        jLabel6.setText("🎁 Incluye 1 mes de acceso a Netflix o Disney+");
        jLabel6.setBounds(353, 203, 266, 16);
        jLabel6.setForeground(Color.WHITE);

        jLabel7.setText("📞 Soporte técnico prioritario sin costo adicional");
        jLabel7.setBounds(353, 237, 266, 16);
        jLabel7.setForeground(Color.WHITE);

        jLabel10.setText("💰 $35/mes – La combinación ideal entre potencia, estabilidad y ahorro");
        jLabel10.setBounds(126, 403, 465, 16);
        jLabel10.setForeground(Color.WHITE);

        jLabel11.setText("💨 500 Mbps de velocidad");
        jLabel11.setBounds(353, 135, 266, 16);
        jLabel11.setForeground(Color.WHITE);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 36));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setBounds(30, 30, 297, 60);

        backgroundPanel.add(jLabel8);
        backgroundPanel.add(jLabel9);
        backgroundPanel.add(jToggleButton1);
        backgroundPanel.add(jLabel1);
        backgroundPanel.add(jLabel2);
        backgroundPanel.add(jLabel3);
        backgroundPanel.add(jLabel5);
        backgroundPanel.add(jLabel6);
        backgroundPanel.add(jLabel7);
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
        Facturación facturacionFrame = new Facturación("Plan Estándar", 35.00, currentClienteId);
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
        java.awt.EventQueue.invokeLater(() -> new PEstandar().setVisible(true));
    }
}
