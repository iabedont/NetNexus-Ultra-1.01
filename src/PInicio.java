package GUI_CHIDO;

import javax.swing.JFrame;

public class PInicio extends JFrame {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PInicio.class.getName());
    private JFrame parentFrame;

    public PInicio() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jToggleButton1 = new javax.swing.JToggleButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(null);

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));
        jToggleButton1.setBounds(300, 400, 251, 67);
        add(jToggleButton1);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PlanSmart.png")));
        jLabel1.setBounds(31, 113, 200, 200);
        add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 36));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Plan Inicio+");
        jLabel2.setBounds(331, 26, 447, 57);
        add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel3.setText("Promoción: Primer mes GRATIS + 50 minutos extra si contratas en línea");
        jLabel3.setBounds(49, 100, 473, 20);
        add(jLabel3);

        jLabel5.setText("🌍 Llamadas internacionales desde $0.15/minuto");
        jLabel5.setBounds(280, 200, 279, 20);
        add(jLabel5);

        jLabel6.setText("💵 $7.99/mes – Conecta lo justo, al precio más bajo del mercado");
        jLabel6.setBounds(280, 250, 378, 20);
        add(jLabel6);

        jLabel11.setText("📞 300 minutos a números locales y nacionales");
        jLabel11.setBounds(280, 150, 264, 20);
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
        jLabel9.setText("✨ Perfecto para personas mayores, líneas de emergencia o negocios que llaman poco.");
        jLabel9.setBounds(100, 480, 561, 20);
        add(jLabel9);

        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO add your handling code here:
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
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
        java.awt.EventQueue.invokeLater(() -> new PInicio().setVisible(true));
    }

    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JToggleButton jToggleButton1;
}