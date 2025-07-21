package GUI_CHIDO;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class PagoExitosoFrame extends JFrame {

    private JFrame parentFrame; // Para regresar a User_1
    
    private JLabel jLabelTitulo;
    private JLabel jLabelMensaje;
    private JLabel jLabelFacturaID;
    private JLabel jLabelTicketID;
    private JLabel jLabelPlan;
    private JLabel jLabelFecha;
    private JLabel jLabelMonto;
    private JLabel jLabelMetodo;
    private JButton jButtonIrPrincipal;
    private JButton jButtonElegirOtroPlan;

    public PagoExitosoFrame(JFrame mainUserFrame, String facturaId, String ticketId, String plan, String fecha, String monto, String metodoPago) {
        this.parentFrame = mainUserFrame; // Guardar referencia a User_1
        initComponents();
        displayInvoiceDetails(facturaId, ticketId, plan, fecha, monto, metodoPago);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        jLabelTitulo = new JLabel();
        jLabelMensaje = new JLabel();
        jLabelFacturaID = new JLabel();
        jLabelTicketID = new JLabel();
        jLabelPlan = new JLabel();
        jLabelFecha = new JLabel();
        jLabelMonto = new JLabel();
        jLabelMetodo = new JLabel();
        jButtonIrPrincipal = new JButton();
        jButtonElegirOtroPlan = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Pago Exitoso");
        setLayout(null);
        setSize(550, 500); // Tamaño de la ventana de éxito
        setLocationRelativeTo(null); // Centrar

        // Título
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("¡Pago Realizado Exitosamente!");
        jLabelTitulo.setForeground(new Color(60, 179, 113)); // Verde
        add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 550, 40);

        // Mensaje
        jLabelMensaje.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMensaje.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelMensaje.setText("Tu transacción ha sido procesada con éxito. Aquí están los detalles:");
        jLabelMensaje.setForeground(Color.BLACK);
        add(jLabelMensaje);
        jLabelMensaje.setBounds(0, 70, 550, 20);

        // Detalles de la factura
        int startY = 120;
        int lineHeight = 30;
        int labelX = 50;
        int valueX = 200;
        int width = 250;

        setupDetailLabel(jLabelFacturaID, "ID Factura:", startY);
        setupDetailLabel(jLabelTicketID, "ID Ticket:", startY + lineHeight);
        setupDetailLabel(jLabelPlan, "Plan Contratado:", startY + 2 * lineHeight);
        setupDetailLabel(jLabelFecha, "Fecha:", startY + 3 * lineHeight);
        setupDetailLabel(jLabelMonto, "Monto Total:", startY + 4 * lineHeight);
        setupDetailLabel(jLabelMetodo, "Método de Pago:", startY + 5 * lineHeight);

        // Botones
        jButtonIrPrincipal.setBackground(new Color(70, 130, 180)); // Acero azul
        jButtonIrPrincipal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonIrPrincipal.setForeground(Color.WHITE);
        jButtonIrPrincipal.setText("Ir a Página Principal");
        jButtonIrPrincipal.setFocusPainted(false);
        jButtonIrPrincipal.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonIrPrincipal.addActionListener(this::jButtonIrPrincipalActionPerformed);
        add(jButtonIrPrincipal);
        jButtonIrPrincipal.setBounds(60, startY + 7 * lineHeight, 200, 45);

        jButtonElegirOtroPlan.setBackground(new Color(60, 179, 113)); // Verde mar
        jButtonElegirOtroPlan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonElegirOtroPlan.setForeground(Color.WHITE);
        jButtonElegirOtroPlan.setText("Elegir Otro Plan");
        jButtonElegirOtroPlan.setFocusPainted(false);
        jButtonElegirOtroPlan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonElegirOtroPlan.addActionListener(this::jButtonElegirOtroPlanActionPerformed);
        add(jButtonElegirOtroPlan);
        jButtonElegirOtroPlan.setBounds(290, startY + 7 * lineHeight, 200, 45);
    }

    private void setupDetailLabel(JLabel label, String text, int yPos) {
        label.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        label.setForeground(Color.DARK_GRAY);
        add(label);
        label.setBounds(50, yPos, 450, 25);
        label.setText(text); // Se actualizará con los datos reales
    }

    private void displayInvoiceDetails(String facturaId, String ticketId, String plan, String fecha, String monto, String metodoPago) {
        jLabelFacturaID.setText("ID Factura: " + facturaId);
        jLabelTicketID.setText("ID Ticket: " + ticketId);
        jLabelPlan.setText("Plan Contratado: " + plan);
        jLabelFecha.setText("Fecha: " + fecha);
        jLabelMonto.setText("Monto Total: $" + monto);
        jLabelMetodo.setText("Método de Pago: " + metodoPago);
    }

    private void jButtonIrPrincipalActionPerformed(ActionEvent evt) {
        this.dispose(); // Cerrar esta ventana
        if (parentFrame != null) {
            parentFrame.setVisible(true); // Mostrar la ventana principal (User_1)
        }
    }

    private void jButtonElegirOtroPlanActionPerformed(ActionEvent evt) {
        this.dispose(); // Cerrar esta ventana
        if (parentFrame != null) {
            // Abrir la ventana de Servicios
            Servicios serviciosFrame = new Servicios(parentFrame); // Necesita un constructor que acepte parentFrame
            serviciosFrame.setVisible(true);
            parentFrame.setVisible(false); // Ocultar User_1 si Servicios no lo hace automáticamente
        } else {
            // Si no hay parentFrame, simplemente abrir Servicios como nueva ventana principal
            new Servicios(null).setVisible(true);
        }
    }
}
