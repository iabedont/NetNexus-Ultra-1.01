package GUI_CHIDO;

import javax.swing.JFrame;

public class Internet extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Internet.class.getName());
    private JFrame parentFrame;

    public Internet() {
        initComponents();
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
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
        setLayout(null);

        jButton2.setBackground(new java.awt.Color(248, 243, 243));
        jButton2.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false);
        jButton2.setBounds(10, 10, 263, 60);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        add(jButton2);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("¡Tu próxima gran experiencia ");
        jLabel1.setBounds(26, 80, 557, 40);
        add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 36));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("comienza seleccionando el plan ideal!");
        jLabel2.setBounds(41, 120, 622, 40);
        add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText(" INTERNET DE ALTA VELOCIDAD – ¡Más que solo velocidad!");
        jLabel3.setBounds(97, 170, 507, 30);
        add(jLabel3);

        jLabel4.setText("💡 Todos nuestros planes incluyen:");
        jLabel4.setBounds(26, 210, 192, 20);
        add(jLabel4);

        jLabel5.setText("📶 Router Wi-Fi GRATIS");
        jLabel5.setBounds(26, 238, 148, 20);
        add(jLabel5);

        jLabel6.setText("🛠️ Instalación sin costo y en menos de 24 horas");
        jLabel6.setBounds(26, 266, 261, 20);
        add(jLabel6);

        jLabel7.setText("📺 Compatible with Smart TVs, consolas y dispositivos móviles");
        jLabel7.setBounds(26, 294, 347, 20);
        add(jLabel7);

        jLabel8.setText("🔒 Control parental y filtros opcionales incluidos");
        jLabel8.setBounds(26, 322, 274, 20);
        add(jLabel8);

        jLabel9.setText("✅ Cambios de plan sin penalización");
        jLabel9.setBounds(400, 238, 299, 20);
        add(jLabel9);

        jLabel10.setText("✅ Atención personalizada en cada etapa");
        jLabel10.setBounds(400, 266, 299, 20);
        add(jLabel10);

        jLabel11.setText("✅ Facturación clara, sin cobros ocultos");
        jLabel11.setBounds(400, 294, 299, 20);
        add(jLabel11);

        jLabel12.setText("✅ Infraestructura moderna y cobertura nacional");
        jLabel12.setBounds(400, 322, 299, 20);
        add(jLabel12);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PBasico-removebg-preview.png")));
        jButton3.setBorderPainted(false);
        jButton3.setContentAreaFilled(false);
        jButton3.setBounds(246, 350, 214, 79);
        add(jButton3);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PEstandar-removebg-preview (1).png")));
        jButton1.setContentAreaFilled(false);
        jButton1.setBounds(47, 440, 213, 84);
        add(jButton1);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PUltra-removebg-preview (1).png")));
        jButton4.setContentAreaFilled(false);
        jButton4.setBounds(400, 440, 218, 90);
        add(jButton4);

        setSize(700, 600);
        setLocationRelativeTo(null);
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
        java.awt.EventQueue.invokeLater(() -> new Internet().setVisible(true));
    }

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
}