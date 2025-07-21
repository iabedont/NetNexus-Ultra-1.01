package GUI_CHIDO;

import javax.swing.JFrame;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Television extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Television.class.getName());
    private JFrame parentFrame;
    private int currentClienteId; // Añadir esta variable

    public Television() {
        initComponents();
        this.setLocationRelativeTo(null); // Centrar la ventana
    }

    public Television(JFrame parentFrame, int clienteId) { // Nuevo constructor
        this(); // Llama al constructor sin argumentos para inicializar componentes
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId; // Asigna el ID del cliente
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // ... (Tu código initComponents existente) ...
        // Asegúrate de que tu botón de regresar (si lo tienes) use parentFrame
        // Por ejemplo, si tienes un jButton2 para regresar:
        // jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        // Y el método:
        // private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        //     this.setVisible(false);
        //     if (parentFrame != null) {
        //         parentFrame.setVisible(true);
        //     }
        // }
    }

    // Asegúrate de tener un método para regresar si tu UI lo requiere
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    // Si tienes botones para contratar planes de TV que abran Facturación,
    // asegúrate de pasar el currentClienteId:
    // private void jButtonContratarTVPlanActionPerformed(java.awt.event.ActionEvent evt) {
    //     Facturación facturacionFrame = new Facturación("Plan TV Básico", 25.00, currentClienteId);
    //     facturacionFrame.setParentFrame(this);
    //     facturacionFrame.setVisible(true);
    //     this.setVisible(false);
    // }

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

        java.awt.EventQueue.invokeLater(() -> new Television().setVisible(true));
    }

    // Declaración de variables (asegúrate de que coincida con tu .form)
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
