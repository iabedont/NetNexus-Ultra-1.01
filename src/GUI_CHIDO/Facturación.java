package GUI_CHIDO;

import java.awt.*;
import java.awt.event.ActionEvent; // Importar TitledBorder
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Facturación extends JFrame {

    private static final Logger logger = Logger.getLogger(Facturación.class.getName());
    private JFrame parentFrame;

    // Componentes de la interfaz de usuario
    private JLabel jLabelTitulo;
    private JLabel jLabelIdFactura;
    private JTextField jTextFieldIdFactura;
    private JLabel jLabelFecha;
    private JTextField jTextFieldFecha;
    private JLabel jLabelMontoTotal;
    private JTextField jTextFieldMontoTotal;
    private JLabel jLabelMetodoPago;
    private JComboBox<String> jComboBoxMetodoPago;
    private JPanel jPanelTarjeta;
    private JLabel jLabelNumeroTarjeta;
    private JTextField jTextFieldNumeroTarjeta;
    private JLabel jLabelNombreTarjeta;
    private JTextField jTextFieldNombreTarjeta;
    private JLabel jLabelFechaVencimiento;
    private JTextField jTextFieldFechaVencimiento;
    private JLabel jLabelCVV;
    private JPasswordField jPasswordFieldCVV;
    private JButton jButtonGenerarTicket;
    private JButton jButtonGuardarTarjeta;
    private JButton jButtonCancelar;

    public Facturación() {
        initComponents();
        // Inicializar la fecha actual
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        jTextFieldFecha.setText(formatter.format(new Date()));
        // Deshabilitar el panel de tarjeta por defecto
        jPanelTarjeta.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Inicialización de componentes
        jLabelTitulo = new JLabel();
        jLabelIdFactura = new JLabel();
        jTextFieldIdFactura = new JTextField();
        jLabelFecha = new JLabel();
        jTextFieldFecha = new JTextField();
        jLabelMontoTotal = new JLabel();
        jTextFieldMontoTotal = new JTextField();
        jLabelMetodoPago = new JLabel();
        jComboBoxMetodoPago = new JComboBox<>();
        // CORRECCIÓN: Inicializar jPanelTarjeta aquí
        jPanelTarjeta = new JPanel();
        jLabelNumeroTarjeta = new JLabel();
        jTextFieldNumeroTarjeta = new JTextField();
        jLabelNombreTarjeta = new JLabel();
        jTextFieldNombreTarjeta = new JTextField();
        jLabelFechaVencimiento = new JLabel();
        jTextFieldFechaVencimiento = new JTextField();
        jLabelCVV = new JLabel();
        jPasswordFieldCVV = new JPasswordField();
        jButtonGuardarTarjeta = new JButton();
        jButtonGenerarTicket = new JButton();
        jButtonCancelar = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Facturación de Servicios");
        setLayout(null); // Usamos un layout nulo para posicionamiento absoluto

        // Configuración del título principal
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32)); // Fuente más moderna
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("DETALLES DE FACTURA");
        jLabelTitulo.setForeground(new Color(50, 70, 90)); // Color de texto más oscuro
        add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 662, 40);

        // --- Campos de Factura ---
        jLabelIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelIdFactura.setText("ID Factura:");
        add(jLabelIdFactura);
        jLabelIdFactura.setBounds(50, 100, 150, 30);

        jTextFieldIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldIdFactura.setEditable(false);
        jTextFieldIdFactura.setBackground(new Color(240, 240, 240)); // Fondo ligeramente gris
        jTextFieldIdFactura.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldIdFactura);
        jTextFieldIdFactura.setBounds(220, 100, 250, 30);

        jLabelFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelFecha.setText("Fecha:");
        add(jLabelFecha);
        jLabelFecha.setBounds(50, 150, 150, 30);

        jTextFieldFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldFecha.setEditable(false);
        jTextFieldFecha.setBackground(new Color(240, 240, 240));
        jTextFieldFecha.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldFecha);
        jTextFieldFecha.setBounds(220, 150, 250, 30);

        jLabelMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMontoTotal.setText("Monto Total:");
        add(jLabelMontoTotal);
        jLabelMontoTotal.setBounds(50, 200, 150, 30);

        jTextFieldMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldMontoTotal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldMontoTotal);
        jTextFieldMontoTotal.setBounds(220, 200, 250, 30);

        jLabelMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMetodoPago.setText("Método de Pago:");
        add(jLabelMetodoPago);
        jLabelMetodoPago.setBounds(50, 250, 150, 30);

        jComboBoxMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jComboBoxMetodoPago.setModel(new DefaultComboBoxModel<>(new String[] { "Seleccione", "Agencia", "Tarjeta de Crédito", "Tarjeta de Débito" }));
        jComboBoxMetodoPago.setBackground(Color.WHITE);
        jComboBoxMetodoPago.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jComboBoxMetodoPago);
        jComboBoxMetodoPago.setBounds(220, 250, 250, 30);

        // Listener para el cambio de método de pago
        jComboBoxMetodoPago.addActionListener(this::jComboBoxMetodoPagoActionPerformed);

        // --- Panel para la información de la tarjeta ---
        jPanelTarjeta.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Detalles de Tarjeta", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), new Color(50, 70, 90)));
        jPanelTarjeta.setLayout(null);
        jPanelTarjeta.setBackground(new Color(248, 248, 248)); // Fondo ligeramente diferente para el panel
        jPanelTarjeta.setBounds(50, 300, 560, 180);

        jLabelNumeroTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelNumeroTarjeta.setText("Número de Tarjeta:");
        jPanelTarjeta.add(jLabelNumeroTarjeta);
        jLabelNumeroTarjeta.setBounds(30, 35, 150, 25);

        jTextFieldNumeroTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldNumeroTarjeta.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldNumeroTarjeta);
        jTextFieldNumeroTarjeta.setBounds(190, 35, 250, 25);

        jLabelNombreTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelNombreTarjeta.setText("Nombre en Tarjeta:");
        jPanelTarjeta.add(jLabelNombreTarjeta);
        jLabelNombreTarjeta.setBounds(30, 70, 150, 25);

        jTextFieldNombreTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldNombreTarjeta.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldNombreTarjeta);
        jTextFieldNombreTarjeta.setBounds(190, 70, 250, 25);

        jLabelFechaVencimiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelFechaVencimiento.setText("Fecha Vencimiento (MM/AA):");
        jPanelTarjeta.add(jLabelFechaVencimiento);
        jLabelFechaVencimiento.setBounds(30, 105, 200, 25);

        jTextFieldFechaVencimiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldFechaVencimiento.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldFechaVencimiento);
        jTextFieldFechaVencimiento.setBounds(230, 105, 80, 25);

        jLabelCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelCVV.setText("CVV:");
        jPanelTarjeta.add(jLabelCVV);
        jLabelCVV.setBounds(330, 105, 50, 25);

        jPasswordFieldCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jPasswordFieldCVV.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jPasswordFieldCVV);
        jPasswordFieldCVV.setBounds(380, 105, 60, 25);

        jButtonGuardarTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonGuardarTarjeta.setText("Guardar Tarjeta");
        jButtonGuardarTarjeta.setBackground(new Color(100, 180, 250)); // Azul claro
        jButtonGuardarTarjeta.setForeground(Color.WHITE);
        jButtonGuardarTarjeta.setFocusPainted(false);
        jButtonGuardarTarjeta.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonGuardarTarjeta.addActionListener(this::jButtonGuardarTarjetaActionPerformed);
        jPanelTarjeta.add(jButtonGuardarTarjeta);
        jButtonGuardarTarjeta.setBounds(190, 140, 180, 35);


        add(jPanelTarjeta);

        // --- Botones de acción ---
        jButtonGenerarTicket.setBackground(new Color(60, 179, 113)); // Verde mar
        jButtonGenerarTicket.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButtonGenerarTicket.setForeground(Color.WHITE);
        jButtonGenerarTicket.setText("Generar Ticket");
        jButtonGenerarTicket.setFocusPainted(false);
        jButtonGenerarTicket.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(jButtonGenerarTicket);
        jButtonGenerarTicket.setBounds(80, 500, 220, 50);
        jButtonGenerarTicket.addActionListener(this::jButtonGenerarTicketActionPerformed);

        jButtonCancelar.setBackground(new Color(220, 20, 60)); // Rojo carmesí
        jButtonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButtonCancelar.setForeground(Color.WHITE);
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        // Icono de "Atrás" si lo tienes, ajusta la ruta si es necesario
        // jButtonCancelar.setIcon(new ImageIcon(getClass().getResource("/Imagenes/Atras.png")));
        add(jButtonCancelar);
        jButtonCancelar.setBounds(340, 500, 220, 50);
        jButtonCancelar.addActionListener(this::jButtonCancelarActionPerformed);

        setSize(662, 600);
        setLocationRelativeTo(null); // Centrar la ventana
    }

    private void jComboBoxMetodoPagoActionPerformed(ActionEvent evt) {
        String selectedMethod = (String) jComboBoxMetodoPago.getSelectedItem();
        if ("Tarjeta de Crédito".equals(selectedMethod) || "Tarjeta de Débito".equals(selectedMethod)) {
            jPanelTarjeta.setVisible(true);
        } else {
            jPanelTarjeta.setVisible(false);
        }
        // Revalidar y repintar para asegurar que el layout se actualice correctamente
        revalidate();
        repaint();
    }

    private void jButtonGenerarTicketActionPerformed(ActionEvent evt) {
        // Lógica para generar el ticket y guardar la factura
        String idFactura = jTextFieldIdFactura.getText();
        String fecha = jTextFieldFecha.getText();
        String monto = jTextFieldMontoTotal.getText();
        String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();

        if (idFactura.isEmpty() || monto.isEmpty() || "Seleccione".equals(metodoPago)) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios (ID Factura, Monto Total, Método de Pago).", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String mensaje = "Factura ID: " + idFactura + "\n" +
                         "Fecha: " + fecha + "\n" +
                         "Monto Total: $" + monto + "\n" +
                         "Método de Pago: " + metodoPago;

        if (jPanelTarjeta.isVisible()) {
            String numeroTarjeta = jTextFieldNumeroTarjeta.getText();
            String nombreTarjeta = jTextFieldNombreTarjeta.getText();
            String fechaVencimiento = jTextFieldFechaVencimiento.getText();
            String cvv = new String(jPasswordFieldCVV.getPassword());

            if (numeroTarjeta.isEmpty() || nombreTarjeta.isEmpty() || fechaVencimiento.isEmpty() || cvv.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos de la tarjeta.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }

            mensaje += "\n\nDetalles de Tarjeta:" +
                       "\nNúmero: " + numeroTarjeta +
                       "\nNombre: " + nombreTarjeta +
                       "\nFecha Vencimiento: " + fechaVencimiento +
                       "\nCVV: " + (cvv.isEmpty() ? "No ingresado" : "***"); // No mostrar CVV real
        }

        JOptionPane.showMessageDialog(this, mensaje, "Ticket Generado Exitosamente", JOptionPane.INFORMATION_MESSAGE);

        // Aquí iría la lógica para guardar la factura en la base de datos
        // y generar el ticket asociado.
        // try {
        //     // Conectar a la base de datos
        //     // Obtener idMetodoPago de la tabla MetodoPago
        //     // Insertar en la tabla Factura (incluyendo idMetodoPago y datos de tarjeta si aplica)
        //     // Insertar en la tabla Ticket (con referencia a la factura)
        // } catch (SQLException ex) {
        //     logger.log(Level.SEVERE, "Error al guardar la factura o ticket", ex);
        //     JOptionPane.showMessageDialog(this, "Error al procesar la factura.", "Error", JOptionPane.ERROR_MESSAGE);
        // }
    }

    private void jButtonGuardarTarjetaActionPerformed(ActionEvent evt) {
        String numeroTarjeta = jTextFieldNumeroTarjeta.getText();
        String nombreTarjeta = jTextFieldNombreTarjeta.getText();
        String fechaVencimiento = jTextFieldFechaVencimiento.getText();
        String cvv = new String(jPasswordFieldCVV.getPassword());

        if (numeroTarjeta.isEmpty() || nombreTarjeta.isEmpty() || fechaVencimiento.isEmpty() || cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos de la tarjeta para guardarla.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Simulación de guardado seguro. En un entorno real, esto implicaría:
        // 1. Tokenización: Enviar los datos de la tarjeta a un proveedor de pagos (Stripe, PayPal, etc.)
        //    para obtener un token seguro.
        // 2. Almacenamiento del Token: Guardar solo el token (no los datos sensibles) en tu base de datos.
        // 3. Procesamiento de Pagos: Usar el token para futuras transacciones.
        JOptionPane.showMessageDialog(this, "Información de tarjeta guardada de forma segura (simulado).", "Tarjeta Guardada", JOptionPane.INFORMATION_MESSAGE);
    }

    private void jButtonCancelarActionPerformed(ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Facturación().setVisible(true));
    }
}
