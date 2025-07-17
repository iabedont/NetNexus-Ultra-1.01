package GUI_CHIDO;

import javax.swing.JFrame;

public class PGlobal extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PGlobal.class.getName());
    private JFrame parentFrame;

    public PGlobal() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jLabel10.setText("💸 $25/mes – Tu plan premium, sin fronteras ni límites");
        jLabel10.setBounds(280, 300, 390, 20);
        add(jLabel10);

        jLabel11.setText("📡 50 GB de datos móviles");
        jLabel11.setBounds(280, 150, 200, 20);
        add(jLabel11);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("Segoe UI Black", 0, 36));
        jButton2.setForeground(new java.awt.Color(255, 255, 255));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        jButton2.setBounds(0, 26, 297, 60);
        add(jButton2);

        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("✨ Diseñado para quienes viajan, trabajan en línea o usan el móvil como oficina.");
        jLabel9.setBounds(173, 480, 464, 20);
        add(jLabel9);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));
        jToggleButton1.setBounds(300, 350, 251, 67);
        add(jToggleButton1);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PlanGlobal (1).png")));
        jLabel1.setBounds(36, 113, 200, 200);
        add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 36));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Plan Pro Global");
        jLabel2.setBounds(331, 26, 447, 57);
        add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel3.setText("Promoción: Primer mes a $20 + 5 GB extra + eSIM gratis para segunda línea");
        jLabel3.setBounds(36, 100, 500, 20);
        add(jLabel3);

        jLabel5.setText("📞 Llamadas y SMS ilimitados");
        jLabel5.setBounds(280, 200, 279, 20);
        add(jLabel5);

        jLabel6.setText("✈️ Roaming internacional incluido (USA, Canadá, Europa)");
        jLabel6.setBounds(280, 250, 300, 20);
        add(jLabel6);

        jLabel7.setText("🛡️ VPN móvil gratuita para navegación segura");
        jLabel7.setBounds(280, 275, 266, 20);
        add(jLabel7);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
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
        java.awt.EventQueue.invokeLater(() -> new PGlobal().setVisible(true));
    }

    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
}