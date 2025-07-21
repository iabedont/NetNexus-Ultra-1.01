package GUI_CHIDO;

import Clases.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

public class Facturación extends JFrame {

    private static final Logger logger = Logger.getLogger(Facturación.class.getName());
    private JFrame parentFrame;
    private double basePrice;
    private int currentClienteId; // ID de cliente, ahora no es de prueba, se pasará

    // Clase interna para representar tarjetas guardadas
    private static class TarjetaGuardada {
        private final int idTarjeta;
        private final String tipoTarjeta;
        private final String fechaVencimiento;
        private final String displayName;
        private final String ultimosCuatroDigitos; // Para mostrar solo los últimos 4 dígitos
        private final String nombreTarjeta; // Nombre del titular de la tarjeta

        public TarjetaGuardada(int idTarjeta, String tipoTarjeta, String fechaVencimiento, String ultimosCuatroDigitos, String nombreTarjeta) {
            this.idTarjeta = idTarjeta;
            this.tipoTarjeta = tipoTarjeta;
            this.fechaVencimiento = fechaVencimiento;
            this.ultimosCuatroDigitos = ultimosCuatroDigitos;
            this.nombreTarjeta = nombreTarjeta;
            
            // Crear un nombre descriptivo para mostrar en el ComboBox
            this.displayName = nombreTarjeta + " (**** " + ultimosCuatroDigitos + ") - " + tipoTarjeta.toUpperCase();
        }

        public int getIdTarjeta() { return idTarjeta; }
        public String getTipoTarjeta() { return tipoTarjeta; }
        public String getFechaVencimiento() { return fechaVencimiento; }
        public String getUltimosCuatroDigitos() { return ultimosCuatroDigitos; }
        public String getNombreTarjeta() { return nombreTarjeta; }
        
        @Override
        public String toString() {
            return displayName;
        }
    }

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
    private JLabel jLabelPlanContratado;
    private JTextField jTextFieldPlanContratado;
    private JLabel jLabelTicketId;
    private JTextField jTextFieldTicketId;
    private JButton jButtonFinalizarPago;
    
    // Nuevos componentes para tarjetas guardadas separadas por tipo
    private JLabel jLabelTarjetasCredito;
    private JComboBox<TarjetaGuardada> jComboBoxTarjetasCredito;
    private JButton jButtonUsarTarjetaCredito;
    private JLabel jLabelTarjetasDebito;
    private JComboBox<TarjetaGuardada> jComboBoxTarjetasDebito;
    private JButton jButtonUsarTarjetaDebito;
    
    // Sistema de pestañas
    private JTabbedPane jTabbedPaneMain;
    private JPanel jPanelPago;
    private JPanel jPanelUbicacion;
    
    // Componentes de ubicación
    private JLabel jLabelDireccion;
    private JTextField jTextFieldDireccion;
    private JLabel jLabelCiudad;
    private JTextField jTextFieldCiudad;
    private JLabel jLabelProvincia;
    private JTextField jTextFieldProvincia;
    private JLabel jLabelCodigoPostal;
    private JTextField jTextFieldCodigoPostal;

    private final SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public Facturación() {
        initComponents();
        SimpleDateFormat displayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        jTextFieldFecha.setText(displayFormatter.format(new Date()));
        jPanelTarjeta.setVisible(false);
        generateInvoiceAndTicketIds();
        jTextFieldPlanContratado.setEditable(false);
        jTextFieldMontoTotal.setEditable(false);
        // Inicializar componentes de tarjetas guardadas como ocultos
        jLabelTarjetasGuardadas.setVisible(false);
        jComboBoxTarjetasGuardadas.setVisible(false);
        jButtonUsarTarjetaGuardada.setVisible(false);
    }

    public Facturación(String planName, double basePrice, int clienteId) {
        this();
        this.basePrice = basePrice;
        this.currentClienteId = clienteId; // Asignar el ID del cliente logueado
        jTextFieldPlanContratado.setText(planName);
        calculateAndDisplayTotal();
        // Cargar tarjetas guardadas del cliente
        loadSavedCards();
    }

    private void initComponents() {
        // Inicializar componentes principales
        jLabelTitulo = new JLabel();
        jLabelIdFactura = new JLabel();
        jTextFieldIdFactura = new JTextField();
        jLabelFecha = new JLabel();
        jTextFieldFecha = new JTextField();
        jLabelMontoTotal = new JLabel();
        jTextFieldMontoTotal = new JTextField();
        jLabelPlanContratado = new JLabel();
        jTextFieldPlanContratado = new JTextField();
        jLabelTicketId = new JLabel();
        jTextFieldTicketId = new JTextField();
        jButtonGenerarTicket = new JButton();
        jButtonCancelar = new JButton();
        jButtonFinalizarPago = new JButton();
        
        // Crear sistema de pestañas
        jTabbedPaneMain = new JTabbedPane();
        jPanelPago = new JPanel();
        jPanelUbicacion = new JPanel();
        
        // Inicializar componentes de pago
        jLabelMetodoPago = new JLabel();
        jComboBoxMetodoPago = new JComboBox<>();
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
        
        // Componentes para tarjetas guardadas separadas por tipo
        jLabelTarjetasCredito = new JLabel();
        jComboBoxTarjetasCredito = new JComboBox<>();
        jButtonUsarTarjetaCredito = new JButton();
        jLabelTarjetasDebito = new JLabel();
        jComboBoxTarjetasDebito = new JComboBox<>();
        jButtonUsarTarjetaDebito = new JButton();
        
        // Inicializar componentes de ubicación
        jLabelDireccion = new JLabel();
        jTextFieldDireccion = new JTextField();
        jLabelCiudad = new JLabel();
        jTextFieldCiudad = new JTextField();
        jLabelProvincia = new JLabel();
        jTextFieldProvincia = new JTextField();
        jLabelCodigoPostal = new JLabel();
        jTextFieldCodigoPostal = new JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Facturación de Servicios");
        setLayout(null);
        
        setupMainLayout();
        setupPaymentTab();
        setupLocationTab();
        
        // Configurar las pestañas
        jTabbedPaneMain.addTab("Detalles de Pago", jPanelPago);
        jTabbedPaneMain.addTab("Información de Ubicación", jPanelUbicacion);
        jTabbedPaneMain.setBounds(20, 120, 840, 450);
        add(jTabbedPaneMain);
        
        // Configurar fecha automáticamente
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        jTextFieldFecha.setText(dateFormat.format(new Date()));
    }

        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("DETALLES DE FACTURA");
        jLabelTitulo.setForeground(new Color(50, 70, 90));
        add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 662, 40);

        jLabelIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelIdFactura.setText("ID Factura:");
        add(jLabelIdFactura);
        jLabelIdFactura.setBounds(50, 80, 150, 30);

        jTextFieldIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldIdFactura.setEditable(false);
        jTextFieldIdFactura.setBackground(new Color(240, 240, 240));
        jTextFieldIdFactura.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldIdFactura);
        jTextFieldIdFactura.setBounds(220, 80, 250, 30);
        
        jLabelTicketId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelTicketId.setText("ID Ticket:");
        add(jLabelTicketId);
        jLabelTicketId.setBounds(50, 120, 150, 30);

        jTextFieldTicketId.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldTicketId.setEditable(false);
        jTextFieldTicketId.setBackground(new Color(240, 240, 240));
        jTextFieldTicketId.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldTicketId);
        jTextFieldTicketId.setBounds(220, 120, 250, 30);

        jLabelPlanContratado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelPlanContratado.setText("Plan Contratado:");
        add(jLabelPlanContratado);
        jLabelPlanContratado.setBounds(50, 160, 150, 30);

        jTextFieldPlanContratado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldPlanContratado.setEditable(false);
        jTextFieldPlanContratado.setBackground(new Color(240, 240, 240));
        jTextFieldPlanContratado.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldPlanContratado);
        jTextFieldPlanContratado.setBounds(220, 160, 250, 30);

        jLabelFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelFecha.setText("Fecha:");
        add(jLabelFecha);
        jLabelFecha.setBounds(50, 200, 150, 30);

        jTextFieldFecha.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldFecha.setEditable(false);
        jTextFieldFecha.setBackground(new Color(240, 240, 240));
        jTextFieldFecha.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldFecha);
        jTextFieldFecha.setBounds(220, 200, 250, 30);

        jLabelMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMontoTotal.setText("Monto Total (IVA 15%):");
        add(jLabelMontoTotal);
        jLabelMontoTotal.setBounds(50, 240, 150, 30);

        jTextFieldMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldMontoTotal.setEditable(false);
        jTextFieldMontoTotal.setBackground(new Color(240, 240, 240));
        jTextFieldMontoTotal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldMontoTotal);
        jTextFieldMontoTotal.setBounds(220, 240, 250, 30);

        jLabelMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMetodoPago.setText("Método de Pago:");
        add(jLabelMetodoPago);
        jLabelMetodoPago.setBounds(50, 280, 150, 30);

        jComboBoxMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jComboBoxMetodoPago.setModel(new DefaultComboBoxModel<>(new String[] { "Seleccione", "Agencia", "Tarjeta de Credito", "Tarjeta de Debito" }));
        jComboBoxMetodoPago.setBackground(Color.WHITE);
        jComboBoxMetodoPago.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jComboBoxMetodoPago);
        jComboBoxMetodoPago.setBounds(220, 280, 250, 30);

        jComboBoxMetodoPago.addActionListener(this::jComboBoxMetodoPagoActionPerformed);

        // ============= TARJETAS GUARDADAS =============
        jLabelTarjetasGuardadas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelTarjetasGuardadas.setText("Tarjetas Guardadas:");
        add(jLabelTarjetasGuardadas);
        jLabelTarjetasGuardadas.setBounds(50, 320, 150, 30);

        jComboBoxTarjetasGuardadas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jComboBoxTarjetasGuardadas.setBackground(Color.WHITE);
        jComboBoxTarjetasGuardadas.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jComboBoxTarjetasGuardadas);
        jComboBoxTarjetasGuardadas.setBounds(220, 320, 200, 30);

        jButtonUsarTarjetaGuardada.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonUsarTarjetaGuardada.setText("Usar Tarjeta");
        jButtonUsarTarjetaGuardada.setBackground(new Color(70, 130, 180));
        jButtonUsarTarjetaGuardada.setForeground(Color.WHITE);
        jButtonUsarTarjetaGuardada.setFocusPainted(false);
        jButtonUsarTarjetaGuardada.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonUsarTarjetaGuardada.addActionListener(evt -> jButtonUsarTarjetaGuardadaActionPerformed(evt));
        add(jButtonUsarTarjetaGuardada);
        jButtonUsarTarjetaGuardada.setBounds(440, 320, 120, 30);

        // ============= PANEL DE TARJETA =============
        jPanelTarjeta.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Detalles de Tarjeta", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), new Color(50, 70, 90)));
        jPanelTarjeta.setLayout(null);
        jPanelTarjeta.setBackground(new Color(248, 248, 248));
        jPanelTarjeta.setBounds(50, 370, 560, 180);

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
        jButtonGuardarTarjeta.setBackground(new Color(100, 180, 250));
        jButtonGuardarTarjeta.setForeground(Color.WHITE);
        jButtonGuardarTarjeta.setFocusPainted(false);
        jButtonGuardarTarjeta.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonGuardarTarjeta.addActionListener(this::jButtonGuardarTarjetaActionPerformed);
        jPanelTarjeta.add(jButtonGuardarTarjeta);
        jButtonGuardarTarjeta.setBounds(190, 140, 180, 35);

        add(jPanelTarjeta);

        // ============= PANEL DE UBICACIÓN =============
        jPanelUbicacion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Información de Ubicación", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), new Color(50, 70, 90)));
        jPanelUbicacion.setLayout(null);
        jPanelUbicacion.setBackground(new Color(248, 248, 248));
        jPanelUbicacion.setBounds(50, 570, 560, 140);

        jLabelDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelDireccion.setText("Dirección:");
        jPanelUbicacion.add(jLabelDireccion);
        jLabelDireccion.setBounds(30, 30, 100, 25);

        jTextFieldDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldDireccion.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldDireccion);
        jTextFieldDireccion.setBounds(140, 30, 380, 25);

        jLabelCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelCiudad.setText("Ciudad:");
        jPanelUbicacion.add(jLabelCiudad);
        jLabelCiudad.setBounds(30, 65, 100, 25);

        jTextFieldCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldCiudad.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCiudad);
        jTextFieldCiudad.setBounds(140, 65, 150, 25);

        jLabelProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelProvincia.setText("Provincia:");
        jPanelUbicacion.add(jLabelProvincia);
        jLabelProvincia.setBounds(310, 65, 80, 25);

        jTextFieldProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldProvincia.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldProvincia);
        jTextFieldProvincia.setBounds(390, 65, 130, 25);

        jLabelCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelCodigoPostal.setText("Código Postal:");
        jPanelUbicacion.add(jLabelCodigoPostal);
        jLabelCodigoPostal.setBounds(30, 100, 100, 25);

        jTextFieldCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldCodigoPostal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCodigoPostal);
        jTextFieldCodigoPostal.setBounds(140, 100, 150, 25);

        add(jPanelUbicacion);

        // ============= BOTONES =============
        jButtonGenerarTicket.setBackground(new Color(60, 179, 113));
        jButtonGenerarTicket.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButtonGenerarTicket.setForeground(Color.WHITE);
        jButtonGenerarTicket.setText("Generar Resumen");
        jButtonGenerarTicket.setFocusPainted(false);
        jButtonGenerarTicket.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(jButtonGenerarTicket);
        jButtonGenerarTicket.setBounds(80, 730, 220, 50);
        jButtonGenerarTicket.addActionListener(this::jButtonGenerarTicketActionPerformed);

        jButtonFinalizarPago.setBackground(new Color(0, 128, 0));
        jButtonFinalizarPago.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButtonFinalizarPago.setForeground(Color.WHITE);
        jButtonFinalizarPago.setText("Finalizar Pago");
        jButtonFinalizarPago.setFocusPainted(false);
        jButtonFinalizarPago.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(jButtonFinalizarPago);
        jButtonFinalizarPago.setBounds(340, 730, 220, 50);
        jButtonFinalizarPago.addActionListener(this::jButtonFinalizarPagoActionPerformed);

        jButtonCancelar.setBackground(new Color(220, 20, 60));
        jButtonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jButtonCancelar.setForeground(Color.WHITE);
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(jButtonCancelar);
        jButtonCancelar.setBounds(210, 790, 220, 50);
        jButtonCancelar.addActionListener(this::jButtonCancelarActionPerformed);

        setSize(662, 880);
        setLocationRelativeTo(null);
    }

    private void generateInvoiceAndTicketIds() {
        Random rand = new Random();
        int invoiceId = 10000 + rand.nextInt(90000);
        jTextFieldIdFactura.setText(String.valueOf(invoiceId));

        int ticketId = 10000 + rand.nextInt(90000);
        jTextFieldTicketId.setText(String.valueOf(ticketId));
    }

    private void calculateAndDisplayTotal() {
        if (basePrice > 0) {
            double ivaRate = 0.15;
            double totalAmount = basePrice * (1 + ivaRate);
            DecimalFormat df = new DecimalFormat("#.##");
            jTextFieldMontoTotal.setText(df.format(totalAmount));
        } else {
            jTextFieldMontoTotal.setText("0.00");
        }
    }

    @SuppressWarnings("unused")
    private void jComboBoxMetodoPagoActionPerformed(ActionEvent evt) {
        String selectedMethod = (String) jComboBoxMetodoPago.getSelectedItem();
        if ("Tarjeta de Credito".equals(selectedMethod)) {
            // Mostrar solo las tarjetas de crédito y panel de tarjeta
            setTarjetasGuardadasVisible(false);
            jLabelTarjetasCredito.setVisible(true);
            jComboBoxTarjetasCredito.setVisible(true);
            jButtonUsarTarjetaCredito.setVisible(true);
            jPanelTarjeta.setVisible(true);
        } else if ("Tarjeta de Debito".equals(selectedMethod)) {
            // Mostrar solo las tarjetas de débito y panel de tarjeta
            setTarjetasGuardadasVisible(false);
            jLabelTarjetasDebito.setVisible(true);
            jComboBoxTarjetasDebito.setVisible(true);
            jButtonUsarTarjetaDebito.setVisible(true);
            jPanelTarjeta.setVisible(true);
        } else {
            // Ocultar todas las secciones de tarjeta
            setTarjetasGuardadasVisible(false);
            jPanelTarjeta.setVisible(false);
        }
        revalidate();
        repaint();
    }

    @SuppressWarnings("unused")
    private void jButtonGenerarTicketActionPerformed(ActionEvent evt) {
        String idFacturaStr = jTextFieldIdFactura.getText();
        String idTicketStr = jTextFieldTicketId.getText();
        String fechaDisplay = jTextFieldFecha.getText();
        String montoStr = jTextFieldMontoTotal.getText();
        String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
        String planContratado = jTextFieldPlanContratado.getText();

        if (idFacturaStr.isEmpty() || idTicketStr.isEmpty() || montoStr.isEmpty() || "Seleccione".equals(metodoPago) || planContratado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return;
        }

        String mensaje = "Factura ID: " + idFacturaStr + "\n" +
                         "Ticket ID: " + idTicketStr + "\n" +
                         "Plan: " + planContratado + "\n" +
                         "Fecha: " + fechaDisplay + "\n" +
                         "Monto Total (IVA 15%): $" + montoStr + "\n" +
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
            
            mensaje += """
                    
                    Detalles de Tarjeta (Solo para verificación):
                    Número: """ + numeroTarjeta + """
                    
                    Nombre: """ + nombreTarjeta + """
                    
                    Fecha Vencimiento: """ + fechaVencimiento + """
                    
                    CVV: """ + (cvv.isEmpty() ? "No ingresado" : "***");
        }

        JOptionPane.showMessageDialog(this, mensaje, "Resumen de Factura", JOptionPane.INFORMATION_MESSAGE);
    }

    @SuppressWarnings("unused")
    private void jButtonFinalizarPagoActionPerformed(ActionEvent evt) {
        // Validación completa antes de proceder
        if (!validateAllFields()) {
            return; // validateAllFields() ya muestra el mensaje de error apropiado
        }

        final String idFacturaStr = jTextFieldIdFactura.getText();
        final String idTicketStr = jTextFieldTicketId.getText();
        final String fechaDisplay = jTextFieldFecha.getText();
        final String fechaDB = dbFormatter.format(new Date());
        final String montoStr = jTextFieldMontoTotal.getText();
        final String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
        final String planContratado = jTextFieldPlanContratado.getText();

        // Declarar e inicializar las variables finales para su uso en SwingWorker
        final String numeroTarjeta;
        final String nombreTarjeta;
        final String fechaVencimiento;
        final String cvv;
        final String hashedNumeroTarjeta;
        final String hashedNombreTarjeta;
        final String hashedCVV;
        final String tipoTarjeta;

        try {
            final int idFactura = Integer.parseInt(idFacturaStr);
            final int idTicket = Integer.parseInt(idTicketStr);
            final double monto = Double.parseDouble(montoStr);

            if (jPanelTarjeta.isVisible()) {
                numeroTarjeta = jTextFieldNumeroTarjeta.getText().trim();
                nombreTarjeta = jTextFieldNombreTarjeta.getText().trim();
                fechaVencimiento = formatAndValidateExpiryDate(jTextFieldFechaVencimiento.getText().trim());
                cvv = new String(jPasswordFieldCVV.getPassword()).trim();
                
                // Determinar tipo de tarjeta basado en el método de pago
                tipoTarjeta = metodoPago.equals("Tarjeta de Credito") ? "Credito" : "Debito";
                
                hashedNumeroTarjeta = sha256(numeroTarjeta);
                hashedNombreTarjeta = sha256(nombreTarjeta);
                hashedCVV = sha256(cvv);
            } else {
                numeroTarjeta = null;
                nombreTarjeta = null;
                fechaVencimiento = null;
                cvv = null;
                tipoTarjeta = null;
                hashedNumeroTarjeta = null;
                hashedNombreTarjeta = null;
                hashedCVV = null;
            }

            LoadingScreen loading = new LoadingScreen(this, true);
            loading.setVisible(true);

            SwingWorker<Boolean, Void> worker = new SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    try {
                        logger.log(Level.INFO, "Iniciando proceso de pago en background...");
                        Thread.sleep(1000); // Reducir tiempo de simulación
                        
                        logger.log(Level.INFO, "Intentando guardar en la base de datos...");
                        // Intentar guardar en la base de datos
                        savePaymentAndContractToDatabase(idFactura, idTicket, fechaDB, monto, metodoPago, planContratado, 
                                                       hashedNumeroTarjeta, hashedNombreTarjeta, fechaVencimiento, hashedCVV, 
                                                       tipoTarjeta, currentClienteId);
                        
                        logger.log(Level.INFO, "Datos guardados exitosamente en la base de datos");
                        return true; // Si llegamos aquí, todo salió bien
                    } catch (Exception e) {
                        // Log del error y re-lanzar para manejarlo en done()
                        logger.log(Level.SEVERE, "Error al guardar en la base de datos: " + e.getMessage(), e);
                        throw e; // Re-lanzar para que done() pueda manejarlo
                    }
                }

                @Override
                protected void done() {
                    logger.log(Level.INFO, "SwingWorker done() ejecutándose...");
                    
                    // Cerrar loading en el EDT (Event Dispatch Thread)
                    SwingUtilities.invokeLater(() -> {
                        if (loading.isVisible()) {
                            loading.dispose();
                            logger.log(Level.INFO, "LoadingScreen cerrado");
                        }
                    });
                    
                    try {
                        if (get()) {
                            logger.log(Level.INFO, "Proceso completado exitosamente, mostrando ventana de éxito");
                            // Éxito: mostrar ventana de éxito
                            SwingUtilities.invokeLater(() -> {
                                JOptionPane.showMessageDialog(Facturación.this, 
                                    "¡Pago procesado exitosamente!\n" +
                                    "Los datos se han guardado correctamente en la base de datos.\n" +
                                    "Factura ID: " + idFacturaStr, 
                                    "Pago Exitoso", JOptionPane.INFORMATION_MESSAGE);
                                
                                PagoExitosoFrame pagoExitoso = new PagoExitosoFrame(
                                    parentFrame,
                                    idFacturaStr,
                                    idTicketStr,
                                    planContratado,
                                    fechaDisplay,
                                    montoStr,
                                    metodoPago
                                );
                                pagoExitoso.setVisible(true);
                                Facturación.this.dispose();
                            });
                        }
                    } catch (java.util.concurrent.ExecutionException e) {
                        logger.log(Level.SEVERE, "Error en ExecutionException", e);
                        // Error durante la ejecución
                        Throwable cause = e.getCause();
                        String errorMessage;
                        if (cause instanceof SQLException) {
                            errorMessage = "Error de base de datos: " + (cause.getMessage() != null ? cause.getMessage() : "Error desconocido");
                        } else {
                            errorMessage = "Error inesperado: " + (cause != null && cause.getMessage() != null ? cause.getMessage() : "Error desconocido");
                        }
                        
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(Facturación.this, errorMessage, "Error al Procesar Pago", JOptionPane.ERROR_MESSAGE);
                        });
                        logger.log(Level.SEVERE, "Error en SwingWorker: " + errorMessage, e);
                    } catch (InterruptedException e) {
                        logger.log(Level.WARNING, "SwingWorker interrumpido", e);
                        Thread.currentThread().interrupt();
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(Facturación.this, "La operación fue interrumpida.", "Operación Interrumpida", JOptionPane.WARNING_MESSAGE);
                        });
                    } catch (Exception e) {
                        logger.log(Level.SEVERE, "Error inesperado en done()", e);
                        SwingUtilities.invokeLater(() -> {
                            JOptionPane.showMessageDialog(Facturación.this, "Error inesperado: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                        });
                    }
                }
            };
            worker.execute();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el formato de números.", "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error de formato de número", e);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error general", e);
        }
    }

    private void savePaymentAndContractToDatabase(int idFactura, int idTicket, String fecha, double monto, String metodoPago, String planContratado, String hashedNumeroTarjeta, String hashedNombreTarjeta, String fechaVencimiento, String hashedCVV, String tipoTarjeta, int clienteId) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmtContrato = null;
        PreparedStatement pstmtServicio = null;
        PreparedStatement pstmtTicket = null;
        PreparedStatement pstmtFactura = null;
        PreparedStatement pstmtTarjeta = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Obtener el ID del tipo de servicio
            int idTipoServicio = getTipoServicioId(planContratado);
            if (idTipoServicio == -1) {
                throw new SQLException("No se pudo encontrar el ID de tipo de servicio para el plan: " + planContratado);
            }

            // 2. Crear el contrato primero
            Random rand = new Random();
            int idContrato = 10000 + rand.nextInt(90000);
            
            // Verificar que el ID del contrato no existe
            while (contratoExists(conn, idContrato)) {
                idContrato = 10000 + rand.nextInt(90000);
            }
            
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            String fechaInicio = dbFormatter.format(cal.getTime());

            cal.add(Calendar.YEAR, 1);
            String fechaFin = dbFormatter.format(cal.getTime());

            String sqlInsertContrato = "INSERT INTO contrato (idContrato, Cliente_idCliente, fecha_inicio, fecha_fin, monto_total, tiposervicio) VALUES (?, ?, ?, ?, ?, ?)";
            pstmtContrato = conn.prepareStatement(sqlInsertContrato);
            pstmtContrato.setInt(1, idContrato);
            pstmtContrato.setInt(2, clienteId);
            pstmtContrato.setString(3, fechaInicio);
            pstmtContrato.setString(4, fechaFin);
            pstmtContrato.setDouble(5, monto);
            pstmtContrato.setString(6, planContratado);
            pstmtContrato.executeUpdate();
            pstmtContrato.close();

            // 3. Crear un servicio asociado al contrato
            int idServicio = 10000 + rand.nextInt(90000);
            while (servicioExists(conn, idServicio)) {
                idServicio = 10000 + rand.nextInt(90000);
            }
            
            // Usar el primer técnico disponible
            int idTecnicoDefault = getFirstAvailableTechnician(conn);
            if (idTecnicoDefault == -1) {
                throw new SQLException("No hay técnicos disponibles en la base de datos");
            }
            
            String sqlInsertServicio = "INSERT INTO servicios (idServicios, Contrato_idContrato, Tecnicos_idTecnicos, TipoServicio_idTipoServicio, fecha_servicio, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
            pstmtServicio = conn.prepareStatement(sqlInsertServicio);
            pstmtServicio.setInt(1, idServicio);
            pstmtServicio.setInt(2, idContrato);
            pstmtServicio.setInt(3, idTecnicoDefault);
            pstmtServicio.setInt(4, idTipoServicio);
            pstmtServicio.setString(5, fecha);
            pstmtServicio.setString(6, "Contratación de plan " + planContratado);
            pstmtServicio.setString(7, "completado");
            pstmtServicio.executeUpdate();
            pstmtServicio.close();

            // 4. Crear el ticket asociado al servicio
            String sqlInsertTicket = "INSERT INTO ticket (idTicket, Servicios_idServicios, fecha_creacion, descripcion, prioridad, estado) VALUES (?, ?, ?, ?, ?, ?)";
            pstmtTicket = conn.prepareStatement(sqlInsertTicket);
            pstmtTicket.setInt(1, idTicket);
            pstmtTicket.setInt(2, idServicio);
            pstmtTicket.setString(3, fecha);
            pstmtTicket.setString(4, "Contratación de plan " + planContratado);
            pstmtTicket.setString(5, "media");
            pstmtTicket.setString(6, "cerrado");
            pstmtTicket.executeUpdate();
            pstmtTicket.close();

            // 5. Gestionar tarjeta si es necesario
            Integer idTarjetaVinculada = null;
            if (hashedNumeroTarjeta != null && hashedNombreTarjeta != null && hashedCVV != null) {
                // Verificar si la tarjeta ya existe para este usuario
                idTarjetaVinculada = getTarjetaExistente(conn, clienteId, hashedNumeroTarjeta);
                
                if (idTarjetaVinculada == null) {
                    // Registrar nueva tarjeta
                    idTarjetaVinculada = registrarNuevaTarjeta(conn, clienteId, hashedNumeroTarjeta, 
                                                              hashedNombreTarjeta, fechaVencimiento, hashedCVV, tipoTarjeta);
                    logger.log(Level.INFO, "Nueva tarjeta registrada para cliente " + clienteId + " con ID: " + idTarjetaVinculada);
                } else {
                    logger.log(Level.INFO, "Usando tarjeta existente con ID: " + idTarjetaVinculada);
                }
            }

            // 6. Crear la factura asociada al ticket (ESTRUCTURA MEJORADA)
            String sqlInsertFactura = "INSERT INTO factura (idFactura, Ticket_idTicket, fecha_factura, monto, estado_pago, metodo_pago, numero_tarjeta, nombre_tarjeta, fecha_vencimiento_tarjeta, cvv_tarjeta, Tarjeta_idTarjeta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            pstmtFactura = conn.prepareStatement(sqlInsertFactura);
            pstmtFactura.setInt(1, idFactura);
            pstmtFactura.setInt(2, idTicket);
            pstmtFactura.setString(3, fecha);
            pstmtFactura.setDouble(4, monto);
            pstmtFactura.setString(5, "pagado");
            pstmtFactura.setString(6, metodoPago);
            
            // Los hashes ya están en formato SHA-256 (64 caracteres) que caben en VARCHAR(64)
            pstmtFactura.setString(7, hashedNumeroTarjeta);
            pstmtFactura.setString(8, hashedNombreTarjeta);
            pstmtFactura.setString(9, fechaVencimiento);
            pstmtFactura.setString(10, hashedCVV);
            
            // Vincular tarjeta si existe
            if (idTarjetaVinculada != null) {
                pstmtFactura.setInt(11, idTarjetaVinculada);
            } else {
                pstmtFactura.setNull(11, java.sql.Types.INTEGER);
            }
            pstmtFactura.executeUpdate();
            pstmtFactura.close();

            // 7. Guardar información de ubicación
            saveLocationToDatabase(conn, idServicio);

            conn.commit();
            logger.log(Level.INFO, "Contrato, Servicio, Ticket, Factura y Ubicación guardados exitosamente.");

        } catch (SQLException ex) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rbEx) {
                logger.log(Level.SEVERE, "Error al hacer rollback", rbEx);
            }
            logger.log(Level.SEVERE, "Error al guardar en la base de datos: " + ex.getMessage(), ex);
            throw ex; // Re-lanzar para que el SwingWorker pueda manejarlo
        } finally {
            try {
                if (pstmtContrato != null) pstmtContrato.close();
                if (pstmtServicio != null) pstmtServicio.close();
                if (pstmtTicket != null) pstmtTicket.close();
                if (pstmtFactura != null) pstmtFactura.close();
                if (pstmtTarjeta != null) pstmtTarjeta.close();
                if (conn != null) conn.setAutoCommit(true);
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                logger.log(Level.SEVERE, "Error al cerrar recursos de la base de datos", closeEx);
            }
        }
    }

    private int getTipoServicioId(String planName) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int idTipoServicio = -1;
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT idTipoServicio FROM tiposervicio WHERE nombre = ?";
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, planName);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                idTipoServicio = rs.getInt("idTipoServicio");
            }
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al obtener idTipoServicio para el plan: " + planName, ex);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                logger.log(Level.SEVERE, "Error al cerrar recursos de la base de datos", closeEx);
            }
        }
        return idTipoServicio;
    }

    private boolean contratoExists(Connection conn, int idContrato) throws SQLException {
        String sql = "SELECT COUNT(*) FROM contrato WHERE idContrato = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idContrato);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private boolean servicioExists(Connection conn, int idServicio) throws SQLException {
        String sql = "SELECT COUNT(*) FROM servicios WHERE idServicios = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idServicio);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private int getFirstAvailableTechnician(Connection conn) throws SQLException {
        String sql = "SELECT idTecnicos FROM tecnicos LIMIT 1";
        try (PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("idTecnicos");
            }
            return -1; // No hay técnicos disponibles
        }
    }

    private String sha256(String base) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(base.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    // ============= MÉTODOS DE VALIDACIÓN COMPLETA =============
    
    private boolean validateAllFields() {
        // 1. Validar cliente
        if (currentClienteId <= 0) {
            JOptionPane.showMessageDialog(this, "Error: No se ha seleccionado un cliente válido.", "Cliente No Válido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 2. Validar campos básicos
        String idFacturaStr = jTextFieldIdFactura.getText().trim();
        String idTicketStr = jTextFieldTicketId.getText().trim();
        String montoStr = jTextFieldMontoTotal.getText().trim();
        String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
        String planContratado = jTextFieldPlanContratado.getText().trim();

        if (idFacturaStr.isEmpty() || idTicketStr.isEmpty() || montoStr.isEmpty() || 
            "Seleccione".equals(metodoPago) || planContratado.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, complete todos los campos obligatorios.", "Campos Incompletos", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // 3. Validar números
        try {
            int idFactura = Integer.parseInt(idFacturaStr);
            int idTicket = Integer.parseInt(idTicketStr);
            double monto = Double.parseDouble(montoStr);
            
            if (idFactura < 10000 || idFactura > 99999) {
                JOptionPane.showMessageDialog(this, "ID de factura debe estar entre 10000 y 99999.", "ID Factura Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (idTicket < 10000 || idTicket > 99999) {
                JOptionPane.showMessageDialog(this, "ID de ticket debe estar entre 10000 y 99999.", "ID Ticket Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            
            if (monto <= 0 || monto > 10000) {
                JOptionPane.showMessageDialog(this, "El monto debe estar entre $0.01 y $10,000.", "Monto Inválido", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Error en el formato de números. Verifique los campos numéricos.", "Formato Inválido", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 4. Validar campos de tarjeta si es necesario
        if (jPanelTarjeta.isVisible()) {
            if (!validateCardFields()) {
                return false;
            }
        }

        // 5. Validar campos de ubicación
        if (!validateLocationFields()) {
            return false;
        }

        return true;
    }

    private boolean validateCardFields() {
        String numeroTarjeta = jTextFieldNumeroTarjeta.getText().trim();
        String nombreTarjeta = jTextFieldNombreTarjeta.getText().trim();
        String fechaVencimiento = jTextFieldFechaVencimiento.getText().trim();
        String cvv = new String(jPasswordFieldCVV.getPassword()).trim();

        // Validar número de tarjeta
        if (numeroTarjeta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El número de tarjeta es obligatorio.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldNumeroTarjeta.requestFocus();
            return false;
        }

        if (!validateCardNumber(numeroTarjeta)) {
            JOptionPane.showMessageDialog(this, 
                "El número de tarjeta no es válido.\n" +
                "- Debe tener entre 13 y 19 dígitos\n" +
                "- Debe pasar la validación de Luhn", 
                "Número de Tarjeta Inválido", JOptionPane.ERROR_MESSAGE);
            jTextFieldNumeroTarjeta.requestFocus();
            return false;
        }

        // Validar nombre en la tarjeta
        if (nombreTarjeta.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El nombre en la tarjeta es obligatorio.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldNombreTarjeta.requestFocus();
            return false;
        }

        if (!validateCardHolderName(nombreTarjeta)) {
            JOptionPane.showMessageDialog(this, 
                "El nombre en la tarjeta no es válido.\n" +
                "- Debe tener entre 2 y 50 caracteres\n" +
                "- Solo se permiten letras y espacios", 
                "Nombre Inválido", JOptionPane.ERROR_MESSAGE);
            jTextFieldNombreTarjeta.requestFocus();
            return false;
        }

        // Validar fecha de vencimiento
        if (fechaVencimiento.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La fecha de vencimiento es obligatoria.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldFechaVencimiento.requestFocus();
            return false;
        }

        try {
            String fechaFormateada = formatAndValidateExpiryDate(fechaVencimiento);
            jTextFieldFechaVencimiento.setText(fechaFormateada); // Actualizar el campo con el formato correcto
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, 
                "Error en fecha de vencimiento: " + e.getMessage(), 
                "Fecha Inválida", JOptionPane.ERROR_MESSAGE);
            jTextFieldFechaVencimiento.requestFocus();
            return false;
        }

        // Validar CVV
        if (cvv.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código CVV es obligatorio.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jPasswordFieldCVV.requestFocus();
            return false;
        }

        if (!validateCVV(cvv)) {
            JOptionPane.showMessageDialog(this, 
                "El código CVV no es válido.\n" +
                "Debe tener 3 o 4 dígitos numéricos.", 
                "CVV Inválido", JOptionPane.ERROR_MESSAGE);
            jPasswordFieldCVV.requestFocus();
            return false;
        }

        return true;
    }

    private String formatAndValidateExpiryDate(String input) throws Exception {
        if (input == null || input.trim().isEmpty()) {
            throw new Exception("La fecha de vencimiento es obligatoria.");
        }

        // Remover espacios y caracteres no numéricos excepto /
        String cleaned = input.replaceAll("[^\\d/]", "");

        // Patrones aceptados: MM/YY, MM/YYYY, MMYY, MMYYYY
        String month = "";
        String year = "";

        if (cleaned.matches("\\d{2}/\\d{2}")) {
            // MM/YY
            String[] parts = cleaned.split("/");
            month = parts[0];
            year = "20" + parts[1]; // Asumimos 20XX
        } else if (cleaned.matches("\\d{2}/\\d{4}")) {
            // MM/YYYY
            String[] parts = cleaned.split("/");
            month = parts[0];
            year = parts[1];
        } else if (cleaned.matches("\\d{4}")) {
            // MMYY
            month = cleaned.substring(0, 2);
            year = "20" + cleaned.substring(2, 4);
        } else if (cleaned.matches("\\d{6}")) {
            // MMYYYY
            month = cleaned.substring(0, 2);
            year = cleaned.substring(2, 6);
        } else {
            throw new Exception("Formato de fecha inválido. Use MM/YY, MM/YYYY, MMYY o MMYYYY.");
        }

        // Validar mes
        int monthInt = Integer.parseInt(month);
        if (monthInt < 1 || monthInt > 12) {
            throw new Exception("El mes debe estar entre 01 y 12.");
        }

        // Validar año
        int yearInt = Integer.parseInt(year);
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        if (yearInt < currentYear || yearInt > currentYear + 20) {
            throw new Exception("El año debe estar entre " + currentYear + " y " + (currentYear + 20) + ".");
        }

        // Validar que no esté vencida
        Calendar current = Calendar.getInstance();
        Calendar expiry = Calendar.getInstance();
        expiry.set(yearInt, monthInt - 1, 1); // Mes es 0-based en Calendar
        expiry.set(Calendar.DAY_OF_MONTH, expiry.getActualMaximum(Calendar.DAY_OF_MONTH)); // Último día del mes

        if (expiry.before(current)) {
            throw new Exception("La tarjeta está vencida.");
        }

        // Retornar en formato MM/YY
        return String.format("%02d/%02d", monthInt, yearInt % 100);
    }

    // ============= MÉTODOS DE GESTIÓN DE TARJETAS =============

    private Integer getTarjetaExistente(Connection conn, int clienteId, String hashedNumero) throws SQLException {
        String sql = "SELECT idTarjeta FROM tarjetas_usuario WHERE Cliente_idCliente = ? AND numero_tarjeta_hash = ? AND activa = true";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, clienteId);
            pstmt.setString(2, hashedNumero);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("idTarjeta");
                }
                return null;
            }
        }
    }

    private Integer registrarNuevaTarjeta(Connection conn, int clienteId, String hashedNumero, 
                                         String hashedNombre, String fechaVencimiento, 
                                         String hashedCVV, String tipoTarjeta) throws SQLException {
        String sql = "INSERT INTO tarjetas_usuario (Cliente_idCliente, numero_tarjeta_hash, nombre_tarjeta_hash, fecha_vencimiento, cvv_hash, tipo_tarjeta) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, clienteId);
            pstmt.setString(2, hashedNumero);
            pstmt.setString(3, hashedNombre);
            pstmt.setString(4, fechaVencimiento);
            pstmt.setString(5, hashedCVV);
            pstmt.setString(6, tipoTarjeta);
            
            int result = pstmt.executeUpdate();
            if (result > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
            throw new SQLException("No se pudo registrar la tarjeta.");
        }
    }

    @SuppressWarnings("unused")
    private void jButtonGuardarTarjetaActionPerformed(ActionEvent evt) {
        // Validar que el usuario esté logueado
        if (currentClienteId <= 0) {
            JOptionPane.showMessageDialog(this, "Error: No se ha identificado un usuario válido para guardar la tarjeta.", "Usuario No Válido", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Validar campos de tarjeta
        if (!validateCardFields()) {
            return; // validateCardFields() ya muestra el mensaje de error
        }

        String numeroTarjeta = jTextFieldNumeroTarjeta.getText().trim();
        String nombreTarjeta = jTextFieldNombreTarjeta.getText().trim();
        String fechaVencimiento = jTextFieldFechaVencimiento.getText().trim();
        String cvv = new String(jPasswordFieldCVV.getPassword()).trim();
        String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
        String tipoTarjeta = metodoPago.equals("Tarjeta de Credito") ? "Credito" : "Debito";

        try {
            String hashedNumeroTarjeta = sha256(numeroTarjeta);
            String hashedNombreTarjeta = sha256(nombreTarjeta);
            String hashedCVV = sha256(cvv);

            // Guardar tarjeta en la base de datos
            Connection conn = null;
            try {
                conn = DatabaseConnection.getConnection();
                
                // Verificar si la tarjeta ya existe
                Integer idTarjetaExistente = getTarjetaExistente(conn, currentClienteId, hashedNumeroTarjeta);
                
                if (idTarjetaExistente != null) {
                    JOptionPane.showMessageDialog(this, 
                        "Esta tarjeta ya está registrada para su cuenta.\n" +
                        "ID de Tarjeta: " + idTarjetaExistente, 
                        "Tarjeta Ya Registrada", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Registrar nueva tarjeta
                    Integer nuevaTarjetaId = registrarNuevaTarjeta(conn, currentClienteId, hashedNumeroTarjeta, 
                                                                   hashedNombreTarjeta, fechaVencimiento, hashedCVV, tipoTarjeta);
                    
                    JOptionPane.showMessageDialog(this, 
                        "¡Tarjeta guardada exitosamente!\n" +
                        "ID de Tarjeta: " + nuevaTarjetaId + "\n" +
                        "Tipo: " + tipoTarjeta + "\n" +
                        "Vencimiento: " + fechaVencimiento + "\n\n" +
                        "NOTA: Los datos están encriptados con SHA-256 para su seguridad.", 
                        "Tarjeta Registrada", JOptionPane.INFORMATION_MESSAGE);
                    
                    logger.log(Level.INFO, "Tarjeta registrada exitosamente. ID: " + nuevaTarjetaId + ", Cliente: " + currentClienteId);
                }
                
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar la tarjeta en la base de datos:\n" + e.getMessage(), 
                    "Error de Base de Datos", JOptionPane.ERROR_MESSAGE);
                logger.log(Level.SEVERE, "Error al guardar tarjeta en BD", e);
            } finally {
                if (conn != null) {
                    try {
                        conn.close();
                    } catch (SQLException e) {
                        logger.log(Level.WARNING, "Error al cerrar conexión", e);
                    }
                }
            }

        } catch (NoSuchAlgorithmException e) {
            JOptionPane.showMessageDialog(this, "Error al encriptar la información de la tarjeta.", "Error de Encriptación", JOptionPane.ERROR_MESSAGE);
            logger.log(Level.SEVERE, "Error al hashear la tarjeta", e);
        }
    }

    @SuppressWarnings("unused")
    private void jButtonCancelarActionPerformed(ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    public void setParentFrame(JFrame parentFrame) {
        this.parentFrame = parentFrame;
    }

    // ============= MÉTODOS DE VALIDACIÓN AUXILIARES =============

    /**
     * Valida un número de tarjeta usando el algoritmo de Luhn
     */
    private boolean validateCardNumber(String cardNumber) {
        if (cardNumber == null || cardNumber.trim().isEmpty()) {
            return false;
        }
        
        String clean = cardNumber.trim().replaceAll("[^0-9]", "");
        
        // Verificar longitud (13-19 dígitos para la mayoría de tarjetas)
        if (clean.length() < 13 || clean.length() > 19) {
            return false;
        }
        
        // Algoritmo de Luhn
        int sum = 0;
        boolean alternate = false;
        for (int i = clean.length() - 1; i >= 0; i--) {
            int digit = Character.getNumericValue(clean.charAt(i));
            
            if (alternate) {
                digit *= 2;
                if (digit > 9) {
                    digit = (digit % 10) + 1;
                }
            }
            
            sum += digit;
            alternate = !alternate;
        }
        
        return (sum % 10) == 0;
    }
    
    /**
     * Valida un código CVV
     */
    private boolean validateCVV(String cvv) {
        if (cvv == null || cvv.trim().isEmpty()) {
            return false;
        }
        
        String clean = cvv.trim().replaceAll("[^0-9]", "");
        return clean.length() >= 3 && clean.length() <= 4;
    }
    
    /**
     * Valida que el nombre en la tarjeta sea válido
     */
    private boolean validateCardHolderName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        
        String trimmed = name.trim();
        return trimmed.length() >= 2 && trimmed.length() <= 50 && 
               trimmed.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$");
    }

    // ============= MÉTODOS DE TARJETAS GUARDADAS =============

    /**
     * Carga las tarjetas guardadas del cliente actual en comboboxes separados por tipo
     */
    private void loadSavedCards() {
        if (currentClienteId <= 0) {
            return; // No hay cliente válido
        }

        jComboBoxTarjetasCredito.removeAllItems();
        jComboBoxTarjetasDebito.removeAllItems();
        
        jComboBoxTarjetasCredito.addItem(new TarjetaGuardada(0, "credito", "", "", "-- Seleccionar Tarjeta de Crédito --"));
        jComboBoxTarjetasDebito.addItem(new TarjetaGuardada(0, "debito", "", "", "-- Seleccionar Tarjeta de Débito --"));

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            // Usar la nueva estructura de base de datos con campos visibles
            String sql = "SELECT idTarjeta, tipo_tarjeta, fecha_vencimiento, ultimos_cuatro_digitos, nombre_titular_visible " +
                        "FROM tarjetas_usuario WHERE Cliente_idCliente = ? AND activa = true ORDER BY fecha_registro DESC";
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentClienteId);
            rs = pstmt.executeQuery();

            int countCredito = 0, countDebito = 0;
            while (rs.next()) {
                int idTarjeta = rs.getInt("idTarjeta");
                String tipoTarjeta = rs.getString("tipo_tarjeta");
                String fechaVencimiento = rs.getString("fecha_vencimiento");
                String ultimosCuatroDigitos = rs.getString("ultimos_cuatro_digitos");
                String nombreTitularVisible = rs.getString("nombre_titular_visible");
                
                // Crear display name con información visible
                String displayName = nombreTitularVisible + " (**** " + ultimosCuatroDigitos + ") - Vence: " + fechaVencimiento;
                
                TarjetaGuardada tarjeta = new TarjetaGuardada(idTarjeta, tipoTarjeta, fechaVencimiento, ultimosCuatroDigitos, nombreTitularVisible);
                
                // Agregar a combobox correspondiente según tipo
                if ("credito".equalsIgnoreCase(tipoTarjeta)) {
                    jComboBoxTarjetasCredito.addItem(tarjeta);
                    countCredito++;
                } else if ("debito".equalsIgnoreCase(tipoTarjeta)) {
                    jComboBoxTarjetasDebito.addItem(tarjeta);
                    countDebito++;
                }
            }

            logger.log(Level.INFO, "Cargadas " + countCredito + " tarjetas de crédito y " + countDebito + " tarjetas de débito para cliente " + currentClienteId);

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al cargar tarjetas guardadas: " + e.getMessage(), e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "Error al cerrar recursos", ex);
            }
        }
    }

    /**
     * Maneja el uso de una tarjeta guardada seleccionada
     */
    @SuppressWarnings("unused")
    private void jButtonUsarTarjetaGuardadaActionPerformed(ActionEvent evt) {
        TarjetaGuardada tarjetaSeleccionada = (TarjetaGuardada) jComboBoxTarjetasGuardadas.getSelectedItem();
        
        if (tarjetaSeleccionada == null || tarjetaSeleccionada.getIdTarjeta() == 0) {
            JOptionPane.showMessageDialog(this, "Por favor, seleccione una tarjeta válida.", "Tarjeta No Seleccionada", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Limpiar campos actuales
        jTextFieldNumeroTarjeta.setText("");
        jTextFieldNombreTarjeta.setText("");
        jPasswordFieldCVV.setText("");
        
        // Establecer fecha de vencimiento de la tarjeta guardada
        jTextFieldFechaVencimiento.setText(tarjetaSeleccionada.getFechaVencimiento());
        
        // Ajustar el método de pago según el tipo de tarjeta
        String metodoPago = tarjetaSeleccionada.getTipoTarjeta().equals("Credito") ? "Tarjeta de Credito" : "Tarjeta de Debito";
        jComboBoxMetodoPago.setSelectedItem(metodoPago);

        // Mostrar mensaje informativo
        JOptionPane.showMessageDialog(this, 
            "Tarjeta seleccionada: " + tarjetaSeleccionada.toString() + "\n\n" +
            "NOTA: Por seguridad, deberá ingresar nuevamente:\n" +
            "- Número de tarjeta\n" +
            "- Nombre en la tarjeta\n" +
            "- Código CVV\n\n" +
            "La fecha de vencimiento se ha rellenado automáticamente.", 
            "Tarjeta Guardada Seleccionada", JOptionPane.INFORMATION_MESSAGE);

        // Enfocar en el campo de número de tarjeta
        jTextFieldNumeroTarjeta.requestFocus();
    }

    // ============= MÉTODOS DE VALIDACIÓN DE UBICACIÓN =============

    /**
     * Valida los campos de ubicación
     */
    private boolean validateLocationFields() {
        String direccion = jTextFieldDireccion.getText().trim();
        String ciudad = jTextFieldCiudad.getText().trim();
        String provincia = jTextFieldProvincia.getText().trim();
        String codigoPostal = jTextFieldCodigoPostal.getText().trim();

        if (direccion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La dirección es obligatoria.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldDireccion.requestFocus();
            return false;
        }

        if (direccion.length() < 10 || direccion.length() > 100) {
            JOptionPane.showMessageDialog(this, "La dirección debe tener entre 10 y 100 caracteres.", "Dirección Inválida", JOptionPane.ERROR_MESSAGE);
            jTextFieldDireccion.requestFocus();
            return false;
        }

        if (ciudad.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La ciudad es obligatoria.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldCiudad.requestFocus();
            return false;
        }

        if (ciudad.length() < 2 || ciudad.length() > 45) {
            JOptionPane.showMessageDialog(this, "La ciudad debe tener entre 2 y 45 caracteres.", "Ciudad Inválida", JOptionPane.ERROR_MESSAGE);
            jTextFieldCiudad.requestFocus();
            return false;
        }

        if (provincia.isEmpty()) {
            JOptionPane.showMessageDialog(this, "La provincia es obligatoria.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldProvincia.requestFocus();
            return false;
        }

        if (provincia.length() < 2 || provincia.length() > 45) {
            JOptionPane.showMessageDialog(this, "La provincia debe tener entre 2 y 45 caracteres.", "Provincia Inválida", JOptionPane.ERROR_MESSAGE);
            jTextFieldProvincia.requestFocus();
            return false;
        }

        if (codigoPostal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "El código postal es obligatorio.", "Campo Requerido", JOptionPane.WARNING_MESSAGE);
            jTextFieldCodigoPostal.requestFocus();
            return false;
        }

        if (!codigoPostal.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "El código postal debe tener 6 dígitos numéricos.", "Código Postal Inválido", JOptionPane.ERROR_MESSAGE);
            jTextFieldCodigoPostal.requestFocus();
            return false;
        }

        return true;
    }

    /**
     * Guarda la información de ubicación en la base de datos
     */
    private void saveLocationToDatabase(Connection conn, int servicioId) throws SQLException {
        String direccion = jTextFieldDireccion.getText().trim();
        String ciudad = jTextFieldCiudad.getText().trim();
        String provincia = jTextFieldProvincia.getText().trim();
        String codigoPostal = jTextFieldCodigoPostal.getText().trim();

        // Generar ID único para ubicación
        Random rand = new Random();
        int idUbicacion = 10000 + rand.nextInt(90000);
        
        // Verificar que el ID no existe
        while (ubicacionExists(conn, idUbicacion)) {
            idUbicacion = 10000 + rand.nextInt(90000);
        }

        String sql = "INSERT INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigo_postal) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUbicacion);
            pstmt.setInt(2, servicioId);
            pstmt.setString(3, direccion);
            pstmt.setString(4, ciudad);
            pstmt.setString(5, provincia);
            pstmt.setString(6, codigoPostal);
            pstmt.executeUpdate();
            
            logger.log(Level.INFO, "Ubicación guardada con ID: " + idUbicacion + " para servicio: " + servicioId);
        }
    }

    /**
     * Verifica si una ubicación ya existe
     */
    private boolean ubicacionExists(Connection conn, int idUbicacion) throws SQLException {
        String sql = "SELECT COUNT(*) FROM ubicacion WHERE idUbicacion = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idUbicacion);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    // ============= MÉTODO MAIN =============

    /**
     * Configura el layout principal con información de factura
     */
    private void setupMainLayout() {
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("DETALLES DE FACTURA");
        jLabelTitulo.setForeground(new Color(50, 70, 90));
        add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 880, 40);

        // ID Factura
        jLabelIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelIdFactura.setText("ID Factura:");
        add(jLabelIdFactura);
        jLabelIdFactura.setBounds(50, 80, 150, 30);

        jTextFieldIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldIdFactura.setEditable(false);
        jTextFieldIdFactura.setBackground(new Color(240, 240, 240));
        jTextFieldIdFactura.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldIdFactura);
        jTextFieldIdFactura.setBounds(220, 80, 150, 30);
        
        // Plan Contratado
        jLabelPlanContratado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelPlanContratado.setText("Plan:");
        add(jLabelPlanContratado);
        jLabelPlanContratado.setBounds(400, 80, 80, 30);

        jTextFieldPlanContratado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldPlanContratado.setEditable(false);
        jTextFieldPlanContratado.setBackground(new Color(240, 240, 240));
        jTextFieldPlanContratado.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldPlanContratado);
        jTextFieldPlanContratado.setBounds(500, 80, 150, 30);

        // Monto Total
        jLabelMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMontoTotal.setText("Total:");
        add(jLabelMontoTotal);
        jLabelMontoTotal.setBounds(680, 80, 80, 30);

        jTextFieldMontoTotal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldMontoTotal.setEditable(false);
        jTextFieldMontoTotal.setBackground(new Color(240, 240, 240));
        jTextFieldMontoTotal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        add(jTextFieldMontoTotal);
        jTextFieldMontoTotal.setBounds(760, 80, 100, 30);

        // Botones principales
        jButtonFinalizarPago.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonFinalizarPago.setText("Finalizar Pago");
        jButtonFinalizarPago.setBackground(new Color(76, 175, 80));
        jButtonFinalizarPago.setForeground(Color.WHITE);
        jButtonFinalizarPago.setFocusPainted(false);
        jButtonFinalizarPago.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonFinalizarPago.addActionListener(evt -> jButtonFinalizarPagoActionPerformed(evt));
        add(jButtonFinalizarPago);
        jButtonFinalizarPago.setBounds(600, 590, 150, 40);

        jButtonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setBackground(new Color(244, 67, 54));
        jButtonCancelar.setForeground(Color.WHITE);
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonCancelar.addActionListener(evt -> jButtonCancelarActionPerformed(evt));
        add(jButtonCancelar);
        jButtonCancelar.setBounds(760, 590, 100, 40);
    }

    /**
     * Configura la pestaña de detalles de pago
     */
    private void setupPaymentTab() {
        jPanelPago.setLayout(null);
        jPanelPago.setBackground(new Color(248, 248, 248));

        // Método de Pago
        jLabelMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelMetodoPago.setText("Método de Pago:");
        jPanelPago.add(jLabelMetodoPago);
        jLabelMetodoPago.setBounds(30, 30, 150, 30);

        jComboBoxMetodoPago.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jComboBoxMetodoPago.setModel(new DefaultComboBoxModel<>(new String[] { "Seleccione", "Agencia", "Tarjeta de Credito", "Tarjeta de Debito" }));
        jComboBoxMetodoPago.setBackground(Color.WHITE);
        jComboBoxMetodoPago.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jComboBoxMetodoPago.addActionListener(this::jComboBoxMetodoPagoActionPerformed);
        jPanelPago.add(jComboBoxMetodoPago);
        jComboBoxMetodoPago.setBounds(200, 30, 200, 30);

        // ============= TARJETAS DE CRÉDITO =============
        jLabelTarjetasCredito.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelTarjetasCredito.setText("Tarjetas de Crédito:");
        jPanelPago.add(jLabelTarjetasCredito);
        jLabelTarjetasCredito.setBounds(30, 80, 150, 30);

        jComboBoxTarjetasCredito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jComboBoxTarjetasCredito.setBackground(Color.WHITE);
        jComboBoxTarjetasCredito.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelPago.add(jComboBoxTarjetasCredito);
        jComboBoxTarjetasCredito.setBounds(200, 80, 300, 30);

        jButtonUsarTarjetaCredito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButtonUsarTarjetaCredito.setText("Usar");
        jButtonUsarTarjetaCredito.setBackground(new Color(70, 130, 180));
        jButtonUsarTarjetaCredito.setForeground(Color.WHITE);
        jButtonUsarTarjetaCredito.setFocusPainted(false);
        jButtonUsarTarjetaCredito.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonUsarTarjetaCredito.addActionListener(evt -> jButtonUsarTarjetaCreditoActionPerformed(evt));
        jPanelPago.add(jButtonUsarTarjetaCredito);
        jButtonUsarTarjetaCredito.setBounds(520, 80, 80, 30);

        // ============= TARJETAS DE DÉBITO =============
        jLabelTarjetasDebito.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelTarjetasDebito.setText("Tarjetas de Débito:");
        jPanelPago.add(jLabelTarjetasDebito);
        jLabelTarjetasDebito.setBounds(30, 120, 150, 30);

        jComboBoxTarjetasDebito.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jComboBoxTarjetasDebito.setBackground(Color.WHITE);
        jComboBoxTarjetasDebito.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelPago.add(jComboBoxTarjetasDebito);
        jComboBoxTarjetasDebito.setBounds(200, 120, 300, 30);

        jButtonUsarTarjetaDebito.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButtonUsarTarjetaDebito.setText("Usar");
        jButtonUsarTarjetaDebito.setBackground(new Color(70, 130, 180));
        jButtonUsarTarjetaDebito.setForeground(Color.WHITE);
        jButtonUsarTarjetaDebito.setFocusPainted(false);
        jButtonUsarTarjetaDebito.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonUsarTarjetaDebito.addActionListener(evt -> jButtonUsarTarjetaDebitoActionPerformed(evt));
        jPanelPago.add(jButtonUsarTarjetaDebito);
        jButtonUsarTarjetaDebito.setBounds(520, 120, 80, 30);

        // ============= PANEL DE TARJETA MANUAL =============
        jPanelTarjeta.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Nueva Tarjeta", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 14), new Color(50, 70, 90)));
        jPanelTarjeta.setLayout(null);
        jPanelTarjeta.setBackground(new Color(255, 255, 255));
        jPanelTarjeta.setBounds(30, 170, 750, 200);

        jLabelNumeroTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelNumeroTarjeta.setText("Número de Tarjeta:");
        jPanelTarjeta.add(jLabelNumeroTarjeta);
        jLabelNumeroTarjeta.setBounds(30, 35, 150, 25);

        jTextFieldNumeroTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldNumeroTarjeta.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldNumeroTarjeta);
        jTextFieldNumeroTarjeta.setBounds(200, 35, 250, 25);

        jLabelNombreTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelNombreTarjeta.setText("Nombre del Titular:");
        jPanelTarjeta.add(jLabelNombreTarjeta);
        jLabelNombreTarjeta.setBounds(30, 75, 150, 25);

        jTextFieldNombreTarjeta.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldNombreTarjeta.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldNombreTarjeta);
        jTextFieldNombreTarjeta.setBounds(200, 75, 250, 25);

        jLabelFechaVencimiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelFechaVencimiento.setText("Fecha Vencimiento:");
        jPanelTarjeta.add(jLabelFechaVencimiento);
        jLabelFechaVencimiento.setBounds(30, 115, 150, 25);

        jTextFieldFechaVencimiento.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jTextFieldFechaVencimiento.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jTextFieldFechaVencimiento);
        jTextFieldFechaVencimiento.setBounds(200, 115, 100, 25);

        jLabelCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jLabelCVV.setText("CVV:");
        jPanelTarjeta.add(jLabelCVV);
        jLabelCVV.setBounds(350, 115, 50, 25);

        jPasswordFieldCVV.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jPasswordFieldCVV.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelTarjeta.add(jPasswordFieldCVV);
        jPasswordFieldCVV.setBounds(400, 115, 80, 25);

        jButtonGuardarTarjeta.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonGuardarTarjeta.setText("Guardar Tarjeta");
        jButtonGuardarTarjeta.setBackground(new Color(76, 175, 80));
        jButtonGuardarTarjeta.setForeground(Color.WHITE);
        jButtonGuardarTarjeta.setFocusPainted(false);
        jButtonGuardarTarjeta.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonGuardarTarjeta.addActionListener(evt -> jButtonGuardarTarjetaActionPerformed(evt));
        jPanelTarjeta.add(jButtonGuardarTarjeta);
        jButtonGuardarTarjeta.setBounds(500, 155, 130, 30);

        jPanelPago.add(jPanelTarjeta);

        // Ocultar inicialmente los componentes de tarjetas guardadas y panel de tarjeta
        setTarjetasGuardadasVisible(false);
        jPanelTarjeta.setVisible(false);
    }

    /**
     * Configura la pestaña de información de ubicación
     */
    private void setupLocationTab() {
        jPanelUbicacion.setLayout(null);
        jPanelUbicacion.setBackground(new Color(248, 248, 248));
        jPanelUbicacion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)), "Información de Ubicación", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Segoe UI", Font.BOLD, 16), new Color(50, 70, 90)));

        // Dirección
        jLabelDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelDireccion.setText("Dirección:");
        jPanelUbicacion.add(jLabelDireccion);
        jLabelDireccion.setBounds(50, 50, 150, 30);

        jTextFieldDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldDireccion.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldDireccion);
        jTextFieldDireccion.setBounds(200, 50, 400, 30);

        // Ciudad
        jLabelCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelCiudad.setText("Ciudad:");
        jPanelUbicacion.add(jLabelCiudad);
        jLabelCiudad.setBounds(50, 100, 150, 30);

        jTextFieldCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldCiudad.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCiudad);
        jTextFieldCiudad.setBounds(200, 100, 200, 30);

        // Provincia
        jLabelProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelProvincia.setText("Provincia:");
        jPanelUbicacion.add(jLabelProvincia);
        jLabelProvincia.setBounds(420, 100, 100, 30);

        jTextFieldProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldProvincia.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldProvincia);
        jTextFieldProvincia.setBounds(520, 100, 200, 30);

        // Código Postal
        jLabelCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelCodigoPostal.setText("Código Postal:");
        jPanelUbicacion.add(jLabelCodigoPostal);
        jLabelCodigoPostal.setBounds(50, 150, 150, 30);

        jTextFieldCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldCodigoPostal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCodigoPostal);
        jTextFieldCodigoPostal.setBounds(200, 150, 150, 30);
    }

    /**
     * Controla la visibilidad de los componentes de tarjetas guardadas
     */
    private void setTarjetasGuardadasVisible(boolean visible) {
        jLabelTarjetasCredito.setVisible(visible);
        jComboBoxTarjetasCredito.setVisible(visible);
        jButtonUsarTarjetaCredito.setVisible(visible);
        jLabelTarjetasDebito.setVisible(visible);
        jComboBoxTarjetasDebito.setVisible(visible);
        jButtonUsarTarjetaDebito.setVisible(visible);
    }

    /**
     * Maneja el uso de una tarjeta de crédito guardada seleccionada
     */
    private void jButtonUsarTarjetaCreditoActionPerformed(ActionEvent evt) {
        TarjetaGuardada tarjetaSeleccionada = (TarjetaGuardada) jComboBoxTarjetasCredito.getSelectedItem();
        if (tarjetaSeleccionada != null && tarjetaSeleccionada.getIdTarjeta() > 0) {
            autocompletarCamposTarjeta(tarjetaSeleccionada);
        }
    }

    /**
     * Maneja el uso de una tarjeta de débito guardada seleccionada
     */
    private void jButtonUsarTarjetaDebitoActionPerformed(ActionEvent evt) {
        TarjetaGuardada tarjetaSeleccionada = (TarjetaGuardada) jComboBoxTarjetasDebito.getSelectedItem();
        if (tarjetaSeleccionada != null && tarjetaSeleccionada.getIdTarjeta() > 0) {
            autocompletarCamposTarjeta(tarjetaSeleccionada);
        }
    }

    /**
     * Autocompleta los campos de tarjeta con la información visible de la tarjeta guardada
     */
    private void autocompletarCamposTarjeta(TarjetaGuardada tarjeta) {
        // Autocompletar con información visible (no sensible)
        jTextFieldNombreTarjeta.setText(tarjeta.getNombreTarjeta());
        jTextFieldFechaVencimiento.setText(tarjeta.getFechaVencimiento());
        
        // Mostrar solo los últimos 4 dígitos con asteriscos
        String numeroMostrar = "**** **** **** " + tarjeta.getUltimosCuatroDigitos();
        jTextFieldNumeroTarjeta.setText(numeroMostrar);
        jTextFieldNumeroTarjeta.setEditable(false); // No permitir edición cuando es tarjeta guardada
        
        // El CVV debe ingresarse nuevamente por seguridad
        jPasswordFieldCVV.setText("");
        jPasswordFieldCVV.requestFocus();
        
        JOptionPane.showMessageDialog(this, 
            "Tarjeta seleccionada. Por favor, ingrese el CVV para continuar.", 
            "Tarjeta Cargada", 
            JOptionPane.INFORMATION_MESSAGE);
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
