/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI_CHIDO;

import javax.swing.JFrame;

public class PConecta extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PConecta.class.getName());
    private JFrame parentFrame;

    public PConecta() {
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
        getContentPane().setLayout(null); // Use null layout

        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/Contratar-removebg-preview (1).png")));
        jToggleButton1.setText("jToggleButton1");
        jToggleButton1.setContentAreaFilled(false);
        jToggleButton1.addActionListener(evt -> jToggleButton1ActionPerformed(evt));
        jToggleButton1.setBounds(300, 400, 251, 67); // Approximate center for toggle button
        add(jToggleButton1);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Imagenes/PlanSmart.png")));
        jLabel1.setBounds(38, 113, 200, 200); // Approximate size for image
        add(jLabel1);

        jLabel2.setFont(new java.awt.Font("Segoe UI Historic", 1, 36));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Plan Conecta+");
        jLabel2.setBounds(331, 26, 447, 57);
        add(jLabel2);

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 18));
        jLabel3.setText("Promoción: Primer mes a solo $7.50 + 100 minutos internacionales de regalo");
        jLabel3.setBounds(38, 100, 636, 25);
        add(jLabel3);

        jLabel5.setText("📢 Buzón de voz + desvío de llamadas incluido");
        jLabel5.setBounds(280, 200, 279, 20);
        add(jLabel5);

        jLabel6.setText("💰 $14.99/mes – Más minutos y funciones por menos dinero");
        jLabel6.setBounds(280, 250, 378, 20);
        add(jLabel6);

        jLabel11.setText("👨‍👩‍👧‍👦 800 minutos a todo el país (fijos y móviles)");
        jLabel11.setBounds(280, 150, 296, 20);
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
        jLabel9.setText("✨ Ideal para familias que desean estar siempre en contacto, sin sorpresas en la factura.");
        jLabel9.setBounds(100, 480, 561, 20);
        add(jLabel9);

        setSize(800, 600); // Set window size
        setLocationRelativeTo(null); // Center the window
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
        java.awt.EventQueue.invokeLater(() -> new PConecta().setVisible(true));
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