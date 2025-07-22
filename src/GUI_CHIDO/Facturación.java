package GUI_CHIDO;

import Clases.BackgroundPanel;
import Clases.CardSecurityUtil;
import Clases.DatabaseConnection;
import java.awt.*;
import java.awt.event.ActionEvent;
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
    private int currentClienteId;
    private BackgroundPanel backgroundPanel;

    // Clase interna para representar tarjetas guardadas
    private static class TarjetaGuardada {
        private final int idTarjeta;
        private final String tipoTarjeta;
        private final String fechaVencimiento;
        private final String displayName;
        private final String ultimosCuatroDigitos;
        private final String nombreTarjeta;

        public TarjetaGuardada(int idTarjeta, String tipoTarjeta, String fechaVencimiento, String ultimosCuatroDigitos, String nombreTarjeta) {
            this.idTarjeta = idTarjeta;
            this.tipoTarjeta = tipoTarjeta;
            this.fechaVencimiento = fechaVencimiento;
            this.ultimosCuatroDigitos = ultimosCuatroDigitos;
            this.nombreTarjeta = nombreTarjeta;
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

    // Clase interna para representar ubicaciones guardadas
    private static class UbicacionGuardada {
        private final int idUbicacion;
        private final String direccion;
        private final String ciudad;
        private final String provincia;
        private final String codigoPostal;
        private final String displayName;

        public UbicacionGuardada(int idUbicacion, String direccion, String ciudad, String provincia, String codigoPostal) {
            this.idUbicacion = idUbicacion;
            this.direccion = direccion;
            this.ciudad = ciudad;
            this.provincia = provincia;
            this.codigoPostal = codigoPostal;
            this.displayName = direccion + ", " + ciudad + ", " + provincia + " (" + codigoPostal + ")";
        }

        public int getIdUbicacion() { return idUbicacion; }
        public String getDireccion() { return direccion; }
        public String getCiudad() { return ciudad; }
        public String getProvincia() { return provincia; }
        public String getCodigoPostal() { return codigoPostal; }

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
    
    // Componentes para duración y descuentos
    private JLabel jLabelDuracion;
    private JComboBox<String> jComboBoxDuracion;
    private JLabel jLabelPrecioBase;
    private JTextField jTextFieldPrecioBase;
    private JLabel jLabelDescuento;
    private JTextField jTextFieldDescuento;
    
    // Componentes para tarjetas guardadas separadas por tipo
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
    
    // Componentes para gestión de ubicaciones guardadas
    private JLabel jLabelUbicacionesGuardadas;
    private JComboBox<UbicacionGuardada> jComboBoxUbicacionesGuardadas;
    private JButton jButtonUsarUbicacion;
    private JButton jButtonGuardarUbicacion;
    private JButton jButtonLimpiarUbicacion;

    private final SimpleDateFormat dbFormatter = new SimpleDateFormat("yyyy-MM-dd");

    public Facturación() {
        this.currentClienteId = 1; // Default para pruebas
        
        setTitle("NetNexus Ultra - Facturación");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(900, 700);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Usar BackgroundPanel como content pane
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        backgroundPanel.setLayout(null);
        this.setContentPane(backgroundPanel);
        
        initComponents();
        generateInvoiceAndTicketIds();
        
        SimpleDateFormat displayFormatter = new SimpleDateFormat("dd/MM/yyyy");
        jTextFieldFecha.setText(displayFormatter.format(new Date()));
        jTextFieldPlanContratado.setEditable(false);
        jTextFieldMontoTotal.setEditable(false);
        
        // Configurar campos de precio y descuento como no editables
        jTextFieldPrecioBase.setEditable(false);
        jTextFieldDescuento.setEditable(false);
        
        // Ocultar inicialmente los componentes de tarjetas guardadas y panel de tarjeta
        setTarjetasGuardadasVisible(false);
        jPanelTarjeta.setVisible(false);
    }

    /**
     * Maneja los cambios en la duración del contrato
     */
    private void jComboBoxDuracionActionPerformed(ActionEvent evt) {
        calculateAndDisplayTotal();
    }

    /**
     * Carga la ubicación existente del cliente si ya tiene una registrada
     */
    private void cargarUbicacionExistente() {
        if (currentClienteId <= 0) {
            return;
        }

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT u.direccion, u.ciudad, u.provincia, u.codigo_postal " +
                        "FROM ubicacion u " +
                        "INNER JOIN servicios s ON u.Servicios_idServicios = s.idServicios " +
                        "INNER JOIN contrato c ON s.Contrato_idContrato = c.idContrato " +
                        "WHERE c.Cliente_idCliente = ? " +
                        "ORDER BY c.fecha_inicio DESC LIMIT 1";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentClienteId);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                final String direccion = rs.getString("direccion");
                final String ciudad = rs.getString("ciudad");
                final String provincia = rs.getString("provincia");
                final String codigoPostal = rs.getString("codigo_postal");
                
                SwingUtilities.invokeLater(() -> {
                    jTextFieldDireccion.setText(direccion);
                    jTextFieldCiudad.setText(ciudad);
                    jTextFieldProvincia.setText(provincia);
                    jTextFieldCodigoPostal.setText(codigoPostal);
                    
                    JOptionPane.showMessageDialog(this, 
                        "✅ Se ha cargado su última dirección registrada.\nPuede modificarla si es necesario.", 
                        "Ubicación Cargada", 
                        JOptionPane.INFORMATION_MESSAGE);
                });
            }

        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error al cargar ubicación existente: " + e.getMessage(), e);
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

    public Facturación(String planName, double basePrice, int clienteId) {
        this();
        this.basePrice = basePrice;
        this.currentClienteId = clienteId;
        jTextFieldPlanContratado.setText(planName);
        calculateAndDisplayTotal();
        loadSavedCards();
        
        // Cargar ubicación existente después de establecer el cliente
        cargarUbicacionExistente();
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
        
        // Inicializar componentes de duración y descuentos
        jLabelDuracion = new JLabel();
        jComboBoxDuracion = new JComboBox<>();
        jLabelPrecioBase = new JLabel();
        jTextFieldPrecioBase = new JTextField();
        jLabelDescuento = new JLabel();
        jTextFieldDescuento = new JTextField();
        
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
        
        // Inicializar componentes para gestión de ubicaciones guardadas
        jLabelUbicacionesGuardadas = new JLabel();
        jComboBoxUbicacionesGuardadas = new JComboBox<>();
        jButtonUsarUbicacion = new JButton();
        jButtonGuardarUbicacion = new JButton();
        jButtonLimpiarUbicacion = new JButton();

        setupMainLayout();
        setupPaymentTab();
        setupLocationTab();
        
        // Configurar las pestañas
        jTabbedPaneMain.addTab("Detalles de Pago", jPanelPago);
        jTabbedPaneMain.addTab("Información de Ubicación", jPanelUbicacion);
        jTabbedPaneMain.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jTabbedPaneMain.setBackground(new Color(255, 255, 255, 200));
        jTabbedPaneMain.setBounds(20, 160, 840, 450);
        backgroundPanel.add(jTabbedPaneMain);
    }

    /**
     * Configura el layout principal con información de factura
     */
    private void setupMainLayout() {
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("DETALLES DE FACTURA");
        jLabelTitulo.setForeground(Color.WHITE); // Texto blanco para mejor contraste
        jLabelTitulo.setOpaque(true);
        jLabelTitulo.setBackground(new Color(50, 70, 90, 180)); // Fondo semi-transparente
        backgroundPanel.add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 880, 40);

        // ID Factura
        jLabelIdFactura.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelIdFactura.setText("ID Factura:");
        jLabelIdFactura.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelIdFactura);
        jLabelIdFactura.setBounds(50, 80, 150, 30);

        jTextFieldIdFactura.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldIdFactura.setEditable(false);
        jTextFieldIdFactura.setBackground(new Color(255, 255, 255, 220));
        jTextFieldIdFactura.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        backgroundPanel.add(jTextFieldIdFactura);
        jTextFieldIdFactura.setBounds(220, 80, 150, 30);
        
        // Plan Contratado
        jLabelPlanContratado.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelPlanContratado.setText("Plan:");
        jLabelPlanContratado.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelPlanContratado);
        jLabelPlanContratado.setBounds(400, 80, 80, 30);

        jTextFieldPlanContratado.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldPlanContratado.setEditable(false);
        jTextFieldPlanContratado.setBackground(new Color(255, 255, 255, 220));
        jTextFieldPlanContratado.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        backgroundPanel.add(jTextFieldPlanContratado);
        jTextFieldPlanContratado.setBounds(500, 80, 150, 30);

        // Duración del contrato
        jLabelDuracion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelDuracion.setText("Duración:");
        jLabelDuracion.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelDuracion);
        jLabelDuracion.setBounds(50, 115, 80, 30);

        jComboBoxDuracion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jComboBoxDuracion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { 
            "1 mes", "6 meses (5% descuento)", "12 meses (12% descuento)" 
        }));
        jComboBoxDuracion.setBackground(new Color(255, 255, 255, 220));
        jComboBoxDuracion.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        jComboBoxDuracion.addActionListener(evt -> jComboBoxDuracionActionPerformed(evt));
        backgroundPanel.add(jComboBoxDuracion);
        jComboBoxDuracion.setBounds(140, 115, 200, 30);

        // Precio Base
        jLabelPrecioBase.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelPrecioBase.setText("Precio:");
        jLabelPrecioBase.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelPrecioBase);
        jLabelPrecioBase.setBounds(360, 115, 80, 30);

        jTextFieldPrecioBase.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldPrecioBase.setEditable(false);
        jTextFieldPrecioBase.setBackground(new Color(255, 255, 255, 220));
        jTextFieldPrecioBase.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        backgroundPanel.add(jTextFieldPrecioBase);
        jTextFieldPrecioBase.setBounds(440, 115, 100, 30);

        // Descuento
        jLabelDescuento.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelDescuento.setText("Desc:");
        jLabelDescuento.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelDescuento);
        jLabelDescuento.setBounds(560, 115, 50, 30);

        jTextFieldDescuento.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldDescuento.setEditable(false);
        jTextFieldDescuento.setBackground(new Color(255, 255, 255, 220));
        jTextFieldDescuento.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2));
        backgroundPanel.add(jTextFieldDescuento);
        jTextFieldDescuento.setBounds(610, 115, 70, 30);

        // Monto Total
        jLabelMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabelMontoTotal.setText("Total:");
        jLabelMontoTotal.setForeground(Color.WHITE);
        backgroundPanel.add(jLabelMontoTotal);
        jLabelMontoTotal.setBounds(700, 115, 80, 30);

        jTextFieldMontoTotal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jTextFieldMontoTotal.setEditable(false);
        jTextFieldMontoTotal.setBackground(new Color(144, 238, 144, 220)); // Verde claro
        jTextFieldMontoTotal.setBorder(BorderFactory.createLineBorder(new Color(34, 139, 34), 3));
        backgroundPanel.add(jTextFieldMontoTotal);
        jTextFieldMontoTotal.setBounds(760, 115, 100, 30);

        // Botones principales
        jButtonFinalizarPago.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonFinalizarPago.setText("Finalizar Pago");
        jButtonFinalizarPago.setBackground(new Color(76, 175, 80));
        jButtonFinalizarPago.setForeground(Color.WHITE);
        jButtonFinalizarPago.setFocusPainted(false);
        jButtonFinalizarPago.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonFinalizarPago.addActionListener(evt -> jButtonFinalizarPagoActionPerformed(evt));
        backgroundPanel.add(jButtonFinalizarPago);
        jButtonFinalizarPago.setBounds(600, 590, 150, 40);

        jButtonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jButtonCancelar.setText("Cancelar");
        jButtonCancelar.setBackground(new Color(244, 67, 54));
        jButtonCancelar.setForeground(Color.WHITE);
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonCancelar.addActionListener(evt -> jButtonCancelarActionPerformed(evt));
        backgroundPanel.add(jButtonCancelar);
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
        jComboBoxMetodoPago.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Seleccione", "Agencia", "Tarjeta de Credito", "Tarjeta de Debito" }));
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

        // Ubicaciones guardadas
        jLabelUbicacionesGuardadas.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelUbicacionesGuardadas.setText("Ubicaciones Guardadas:");
        jPanelUbicacion.add(jLabelUbicacionesGuardadas);
        jLabelUbicacionesGuardadas.setBounds(50, 30, 200, 30);

        jComboBoxUbicacionesGuardadas.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        jComboBoxUbicacionesGuardadas.setBackground(Color.WHITE);
        jComboBoxUbicacionesGuardadas.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jComboBoxUbicacionesGuardadas);
        jComboBoxUbicacionesGuardadas.setBounds(250, 30, 350, 30);

        jButtonUsarUbicacion.setFont(new Font("Segoe UI", Font.BOLD, 12));
        jButtonUsarUbicacion.setText("Usar");
        jButtonUsarUbicacion.setBackground(new Color(70, 130, 180));
        jButtonUsarUbicacion.setForeground(Color.WHITE);
        jButtonUsarUbicacion.setFocusPainted(false);
        jButtonUsarUbicacion.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonUsarUbicacion.addActionListener(evt -> jButtonUsarUbicacionActionPerformed(evt));
        jPanelUbicacion.add(jButtonUsarUbicacion);
        jButtonUsarUbicacion.setBounds(620, 30, 80, 30);

        // Dirección
        jLabelDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelDireccion.setText("Dirección:");
        jPanelUbicacion.add(jLabelDireccion);
        jLabelDireccion.setBounds(50, 80, 150, 30);

        jTextFieldDireccion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldDireccion.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldDireccion);
        jTextFieldDireccion.setBounds(200, 80, 400, 30);

        // Ciudad
        jLabelCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelCiudad.setText("Ciudad:");
        jPanelUbicacion.add(jLabelCiudad);
        jLabelCiudad.setBounds(50, 130, 150, 30);

        jTextFieldCiudad.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldCiudad.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCiudad);
        jTextFieldCiudad.setBounds(200, 130, 200, 30);

        // Provincia
        jLabelProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelProvincia.setText("Provincia:");
        jPanelUbicacion.add(jLabelProvincia);
        jLabelProvincia.setBounds(420, 130, 100, 30);

        jTextFieldProvincia.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldProvincia.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldProvincia);
        jTextFieldProvincia.setBounds(520, 130, 200, 30);

        // Código Postal
        jLabelCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelCodigoPostal.setText("Código Postal:");
        jPanelUbicacion.add(jLabelCodigoPostal);
        jLabelCodigoPostal.setBounds(50, 180, 150, 30);

        jTextFieldCodigoPostal.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jTextFieldCodigoPostal.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));
        jPanelUbicacion.add(jTextFieldCodigoPostal);
        jTextFieldCodigoPostal.setBounds(200, 180, 150, 30);

        // Botones de gestión de ubicaciones
        jButtonGuardarUbicacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonGuardarUbicacion.setText("Guardar Ubicación");
        jButtonGuardarUbicacion.setBackground(new Color(76, 175, 80));
        jButtonGuardarUbicacion.setForeground(Color.WHITE);
        jButtonGuardarUbicacion.setFocusPainted(false);
        jButtonGuardarUbicacion.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonGuardarUbicacion.addActionListener(evt -> jButtonGuardarUbicacionActionPerformed(evt));
        jPanelUbicacion.add(jButtonGuardarUbicacion);
        jButtonGuardarUbicacion.setBounds(450, 230, 150, 35);

        jButtonLimpiarUbicacion.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonLimpiarUbicacion.setText("Limpiar");
        jButtonLimpiarUbicacion.setBackground(new Color(255, 152, 0));
        jButtonLimpiarUbicacion.setForeground(Color.WHITE);
        jButtonLimpiarUbicacion.setFocusPainted(false);
        jButtonLimpiarUbicacion.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        jButtonLimpiarUbicacion.addActionListener(evt -> jButtonLimpiarUbicacionActionPerformed(evt));
        jPanelUbicacion.add(jButtonLimpiarUbicacion);
        jButtonLimpiarUbicacion.setBounds(620, 230, 100, 35);

        // Cargar ubicaciones guardadas
        loadSavedLocations();
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

    private void generateInvoiceAndTicketIds() {
        Random rand = new Random();
        int invoiceId = 10000 + rand.nextInt(90000);
        jTextFieldIdFactura.setText(String.valueOf(invoiceId));

        int ticketId = 10000 + rand.nextInt(90000);
        jTextFieldTicketId.setText(String.valueOf(ticketId));
    }

    private void calculateAndDisplayTotal() {
        if (basePrice > 0) {
            String duracionSeleccionada = (String) jComboBoxDuracion.getSelectedItem();
            int meses = 1;
            double descuentoPorcentaje = 0.0;
            
            if (duracionSeleccionada != null) {
                if (duracionSeleccionada.contains("6 meses")) {
                    meses = 6;
                    descuentoPorcentaje = 5.0;
                } else if (duracionSeleccionada.contains("12 meses")) {
                    meses = 12;
                    descuentoPorcentaje = 12.0;
                }
            }
            
            // Calcular precio base por la duración
            double precioBase = basePrice * meses;
            jTextFieldPrecioBase.setText("$" + new DecimalFormat("#.##").format(precioBase));
            
            // Primero aplicar IVA al precio base
            double ivaRate = 0.15;
            double precioConIva = precioBase * (1 + ivaRate);
            
            // Luego aplicar descuento al total (precio + IVA)
            double montoDescuento = precioConIva * (descuentoPorcentaje / 100.0);
            jTextFieldDescuento.setText(descuentoPorcentaje > 0 ? 
                "-$" + new DecimalFormat("#.##").format(montoDescuento) + " (" + descuentoPorcentaje + "%)" : 
                "$0.00");
            
            // Total final = (Precio base + IVA) - Descuento
            double totalAmount = precioConIva - montoDescuento;
            
            DecimalFormat df = new DecimalFormat("#.##");
            jTextFieldMontoTotal.setText("$" + df.format(totalAmount));
        } else {
            jTextFieldPrecioBase.setText("$0.00");
            jTextFieldDescuento.setText("$0.00");
            jTextFieldMontoTotal.setText("$0.00");
        }
    }

    private void jComboBoxMetodoPagoActionPerformed(ActionEvent evt) {
        String selectedMethod = (String) jComboBoxMetodoPago.getSelectedItem();
        if ("Tarjeta de Credito".equals(selectedMethod)) {
            setTarjetasGuardadasVisible(false);
            jLabelTarjetasCredito.setVisible(true);
            jComboBoxTarjetasCredito.setVisible(true);
            jButtonUsarTarjetaCredito.setVisible(true);
            jPanelTarjeta.setVisible(true);
        } else if ("Tarjeta de Debito".equals(selectedMethod)) {
            setTarjetasGuardadasVisible(false);
            jLabelTarjetasDebito.setVisible(true);
            jComboBoxTarjetasDebito.setVisible(true);
            jButtonUsarTarjetaDebito.setVisible(true);
            jPanelTarjeta.setVisible(true);
        } else {
            setTarjetasGuardadasVisible(false);
            jPanelTarjeta.setVisible(false);
        }
        revalidate();
        repaint();
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
        jTextFieldNombreTarjeta.setText(tarjeta.getNombreTarjeta());
        jTextFieldFechaVencimiento.setText(tarjeta.getFechaVencimiento());
        
        String numeroMostrar = "**** **** **** " + tarjeta.getUltimosCuatroDigitos();
        jTextFieldNumeroTarjeta.setText(numeroMostrar);
        jTextFieldNumeroTarjeta.setEditable(false);
        
        jPasswordFieldCVV.setText("");
        jPasswordFieldCVV.requestFocus();
        
        JOptionPane.showMessageDialog(this, 
            "Tarjeta seleccionada. Por favor, ingrese el CVV para continuar.", 
            "Tarjeta Cargada", 
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Carga las tarjetas guardadas del cliente actual en comboboxes separados por tipo
     */
    private void loadSavedCards() {
        if (currentClienteId <= 0) {
            return;
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
                
                TarjetaGuardada tarjeta = new TarjetaGuardada(idTarjeta, tipoTarjeta, fechaVencimiento, ultimosCuatroDigitos, nombreTitularVisible);
                
                if ("credito".equalsIgnoreCase(tipoTarjeta)) {
                    jComboBoxTarjetasCredito.addItem(tarjeta);
                    countCredito++;
                } else if ("debito".equalsIgnoreCase(tipoTarjeta)) {
                    jComboBoxTarjetasDebito.addItem(tarjeta);
                    countDebito++;
                }
            }

            logger.info("Cargadas " + countCredito + " tarjetas de crédito y " + countDebito + " tarjetas de débito para cliente " + currentClienteId);

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

    private void jButtonFinalizarPagoActionPerformed(ActionEvent evt) {
        // Validar campos obligatorios
        if (!validarCamposObligatorios()) {
            return;
        }
        
        // Procesar el pago y crear registros en base de datos
        if (procesarPagoCompleto()) {
            // Mostrar confirmación de éxito
            JOptionPane.showMessageDialog(this, 
                "¡Pago procesado exitosamente!\n" +
                "Contrato creado: " + jTextFieldIdFactura.getText() + "\n" +
                "Ticket generado: " + jTextFieldTicketId.getText() + "\n" +
                "Plan contratado: " + jTextFieldPlanContratado.getText(), 
                "Pago Exitoso", 
                JOptionPane.INFORMATION_MESSAGE);
                
            // Cerrar ventana y volver al frame padre
            this.setVisible(false);
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
        }
    }

    /**
     * Valida que todos los campos obligatorios estén completados
     */
    private boolean validarCamposObligatorios() {
        // Validar método de pago seleccionado
        String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
        if ("Seleccione".equals(metodoPago)) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un método de pago.", 
                "Campo Obligatorio", 
                JOptionPane.WARNING_MESSAGE);
            jTabbedPaneMain.setSelectedIndex(0); // Ir a pestaña de pago
            return false;
        }
        
        // Validar campos de tarjeta si se seleccionó tarjeta
        if ("Tarjeta de Credito".equals(metodoPago) || "Tarjeta de Debito".equals(metodoPago)) {
            if (jTextFieldNumeroTarjeta.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese el número de tarjeta.", 
                    "Campo Obligatorio", 
                    JOptionPane.WARNING_MESSAGE);
                jTabbedPaneMain.setSelectedIndex(0);
                jTextFieldNumeroTarjeta.requestFocus();
                return false;
            }
            
            if (jTextFieldNombreTarjeta.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese el nombre del titular.", 
                    "Campo Obligatorio", 
                    JOptionPane.WARNING_MESSAGE);
                jTabbedPaneMain.setSelectedIndex(0);
                jTextFieldNombreTarjeta.requestFocus();
                return false;
            }
            
            if (jTextFieldFechaVencimiento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese la fecha de vencimiento.", 
                    "Campo Obligatorio", 
                    JOptionPane.WARNING_MESSAGE);
                jTabbedPaneMain.setSelectedIndex(0);
                jTextFieldFechaVencimiento.requestFocus();
                return false;
            }
            
            if (jPasswordFieldCVV.getPassword().length == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Por favor ingrese el CVV.", 
                    "Campo Obligatorio", 
                    JOptionPane.WARNING_MESSAGE);
                jTabbedPaneMain.setSelectedIndex(0);
                jPasswordFieldCVV.requestFocus();
                return false;
            }
        }
        
        // Validar campos de ubicación
        if (jTextFieldDireccion.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese la dirección.", 
                "Campo Obligatorio", 
                JOptionPane.WARNING_MESSAGE);
            jTabbedPaneMain.setSelectedIndex(1); // Ir a pestaña de ubicación
            jTextFieldDireccion.requestFocus();
            return false;
        }
        
        if (jTextFieldCiudad.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese la ciudad.", 
                "Campo Obligatorio", 
                JOptionPane.WARNING_MESSAGE);
            jTabbedPaneMain.setSelectedIndex(1);
            jTextFieldCiudad.requestFocus();
            return false;
        }
        
        if (jTextFieldProvincia.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese la provincia.", 
                "Campo Obligatorio", 
                JOptionPane.WARNING_MESSAGE);
            jTabbedPaneMain.setSelectedIndex(1);
            jTextFieldProvincia.requestFocus();
            return false;
        }
        
        if (jTextFieldCodigoPostal.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor ingrese el código postal.", 
                "Campo Obligatorio", 
                JOptionPane.WARNING_MESSAGE);
            jTabbedPaneMain.setSelectedIndex(1);
            jTextFieldCodigoPostal.requestFocus();
            return false;
        }
        
        return true;
    }

    /**
     * Procesa el pago completo creando todos los registros necesarios en la base de datos
     */
    private boolean procesarPagoCompleto() {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Iniciar transacción
            
            // 1. Crear el contrato
            int contratoId = crearContrato(conn);
            if (contratoId == -1) {
                conn.rollback();
                return false;
            }
            
            // 2. Crear el servicio asociado
            int servicioId = crearServicio(conn, contratoId);
            if (servicioId == -1) {
                conn.rollback();
                return false;
            }
            
            // 3. Guardar la ubicación
            if (!guardarUbicacion(conn, servicioId)) {
                conn.rollback();
                return false;
            }
            
            // 4. Crear factura
            if (!crearFactura(conn, contratoId)) {
                conn.rollback();
                return false;
            }
            
            // 5. Generar el ticket
            String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
            if (!generarTicket(conn, servicioId, metodoPago)) {
                conn.rollback();
                return false;
            }
            
            // 6. Guardar tarjeta si se solicita y es nueva
            if (("Tarjeta de Credito".equals(metodoPago) || "Tarjeta de Debito".equals(metodoPago)) 
                && !jTextFieldNumeroTarjeta.getText().contains("****")) {
                // Solo guardar si no es una tarjeta existente (que tendría ****)
                guardarTarjetaSiSolicitado(conn, metodoPago);
            }
            
            conn.commit(); // Confirmar transacción
            logger.info("Pago procesado exitosamente para cliente " + currentClienteId + 
                       ", contrato " + contratoId + ", servicio " + servicioId);
            return true;
            
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException ex) {
                logger.log(Level.SEVERE, "Error en rollback", ex);
            }
            logger.log(Level.SEVERE, "Error procesando pago: " + e.getMessage(), e);
            JOptionPane.showMessageDialog(this, 
                "Error procesando el pago: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            return false;
        } finally {
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "Error cerrando conexión", ex);
            }
        }
    }

    /**
     * Crea un contrato en la base de datos
     */
    private int crearContrato(Connection conn) throws SQLException {
        String sql = "INSERT INTO contrato (idContrato, Cliente_idCliente, fecha_inicio, fecha_fin, monto_total, tiposervicio) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            int contratoId = Integer.parseInt(jTextFieldIdFactura.getText());
            Date fechaInicio = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(fechaInicio);
            cal.add(Calendar.YEAR, 1); // Contrato por 1 año
            Date fechaFin = cal.getTime();
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, contratoId);
            pstmt.setInt(2, currentClienteId);
            pstmt.setDate(3, new java.sql.Date(fechaInicio.getTime()));
            pstmt.setDate(4, new java.sql.Date(fechaFin.getTime()));
            // Limpiar el texto del monto antes de parsearlo
            String montoText = jTextFieldMontoTotal.getText().replace("$", "").replace(",", "").trim();
            pstmt.setDouble(5, Double.parseDouble(montoText));
            pstmt.setString(6, jTextFieldPlanContratado.getText());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Contrato creado exitosamente: " + contratoId);
                return contratoId;
            }
            return -1;
            
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en formato de número al crear contrato", e);
            return -1;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Crea un servicio asociado al contrato
     */
    private int crearServicio(Connection conn, int contratoId) throws SQLException {
        String sql = "INSERT INTO servicios (idServicios, Contrato_idContrato, Tecnicos_idTecnicos, TipoServicio_idTipoServicio, fecha_servicio, descripcion, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            int servicioId = contratoId; // Usar mismo ID para simplicidad
            Date fechaServicio = new Date();
            String descripcion = "Instalación y configuración de " + jTextFieldPlanContratado.getText() + 
                                " para cliente ID " + currentClienteId;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, servicioId);
            pstmt.setInt(2, contratoId);
            pstmt.setInt(3, 1); // ID de técnico por defecto
            pstmt.setInt(4, 1); // ID de tipo de servicio por defecto (instalación)
            pstmt.setDate(5, new java.sql.Date(fechaServicio.getTime()));
            pstmt.setString(6, descripcion);
            pstmt.setString(7, "pendiente");
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Servicio creado exitosamente: " + servicioId);
                return servicioId;
            }
            return -1;
            
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Guarda la información de ubicación
     */
    private boolean guardarUbicacion(Connection conn, int servicioId) throws SQLException {
        String sql = "INSERT INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigo_postal) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            int ubicacionId = servicioId; // Usar mismo ID para simplicidad
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ubicacionId);
            pstmt.setInt(2, servicioId);
            pstmt.setString(3, jTextFieldDireccion.getText().trim());
            pstmt.setString(4, jTextFieldCiudad.getText().trim());
            pstmt.setString(5, jTextFieldProvincia.getText().trim());
            pstmt.setString(6, jTextFieldCodigoPostal.getText().trim());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Ubicación guardada exitosamente: " + ubicacionId);
                return true;
            }
            return false;
            
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Crea una factura en la base de datos
     */
    private boolean crearFactura(Connection conn, int contratoId) throws SQLException {
        String sql = "INSERT INTO factura (idFactura, Cliente_idCliente, fecha_emision, monto_total, estado_pago, metodo_pago, numero_tarjeta_oculto) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            int facturaId = Integer.parseInt(jTextFieldIdFactura.getText());
            Date fechaEmision = new Date();
            String metodoPago = (String) jComboBoxMetodoPago.getSelectedItem();
            String estadoPago = "Agencia".equals(metodoPago) ? "pendiente" : "pagado";
            String numeroTarjetaOculto = null;
            
            // Si es tarjeta, guardar número oculto
            if (("Tarjeta de Credito".equals(metodoPago) || "Tarjeta de Debito".equals(metodoPago)) 
                && !jTextFieldNumeroTarjeta.getText().trim().isEmpty()) {
                String numeroTarjeta = jTextFieldNumeroTarjeta.getText().trim();
                if (numeroTarjeta.contains("****")) {
                    numeroTarjetaOculto = numeroTarjeta;
                } else {
                    // Ocultar número de tarjeta nueva
                    numeroTarjetaOculto = "**** **** **** " + numeroTarjeta.substring(numeroTarjeta.length() - 4);
                }
            }
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, facturaId);
            pstmt.setInt(2, currentClienteId);
            pstmt.setDate(3, new java.sql.Date(fechaEmision.getTime()));
            // Limpiar el texto del monto antes de parsearlo
            String montoText = jTextFieldMontoTotal.getText().replace("$", "").replace(",", "").trim();
            pstmt.setDouble(4, Double.parseDouble(montoText));
            pstmt.setString(5, estadoPago);
            pstmt.setString(6, metodoPago);
            pstmt.setString(7, numeroTarjetaOculto);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Factura creada exitosamente: " + facturaId + ", Estado: " + estadoPago);
                return true;
            }
            return false;
            
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en formato de número al crear factura", e);
            return false;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Genera un ticket de soporte/instalación
     */
    private boolean generarTicket(Connection conn, int servicioId, String metodoPago) throws SQLException {
        String sql = "INSERT INTO ticket (idTicket, Servicios_idServicios, fecha_creacion, descripcion, prioridad, estado) VALUES (?, ?, ?, ?, ?, ?)";
        PreparedStatement pstmt = null;
        
        try {
            int ticketId = Integer.parseInt(jTextFieldTicketId.getText());
            Date fechaCreacion = new Date();
            String estadoTicket = "Agencia".equals(metodoPago) ? "pendiente_pago" : "abierto";
            String prioridad = "Agencia".equals(metodoPago) ? "baja" : "media";
            
            String descripcion = "Agencia".equals(metodoPago) ? 
                "PENDIENTE DE PAGO - " + jTextFieldPlanContratado.getText() + 
                "\nCliente debe acudir a agencia para completar el pago" +
                "\nCliente ID: " + currentClienteId + 
                "\nDirección de instalación: " + jTextFieldDireccion.getText() + 
                "\nCiudad: " + jTextFieldCiudad.getText() + ", " + jTextFieldProvincia.getText() +
                "\nMonto: " + jTextFieldMontoTotal.getText() :
                "Ticket de instalación para " + jTextFieldPlanContratado.getText() + 
                "\nCliente ID: " + currentClienteId + 
                "\nDirección: " + jTextFieldDireccion.getText() + 
                "\nCiudad: " + jTextFieldCiudad.getText() + ", " + jTextFieldProvincia.getText() +
                "\nMonto: " + jTextFieldMontoTotal.getText() +
                "\nMétodo de pago: " + metodoPago;
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, ticketId);
            pstmt.setInt(2, servicioId);
            pstmt.setDate(3, new java.sql.Date(fechaCreacion.getTime()));
            pstmt.setString(4, descripcion);
            pstmt.setString(5, prioridad);
            pstmt.setString(6, estadoTicket);
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                logger.info("Ticket generado exitosamente: " + ticketId + ", Estado: " + estadoTicket);
                return true;
            }
            return false;
            
        } catch (NumberFormatException e) {
            logger.log(Level.SEVERE, "Error en formato de número al generar ticket", e);
            return false;
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /**
     * Guarda la tarjeta si se solicita y no es una tarjeta existente
     */
    private void guardarTarjetaSiSolicitado(Connection conn, String tipoTarjeta) {
        // Por ahora, esta funcionalidad está implementada en el botón "Guardar Tarjeta"
        // En el futuro se puede mejorar para preguntar automáticamente
        logger.info("Opción de guardar tarjeta disponible. Tipo: " + tipoTarjeta);
    }

    /**
     * Limpia todos los campos del formulario después de un pago exitoso
     */
    private void limpiarCamposPago() {
        // Limpiar campos de pago
        jTextFieldNumeroTarjeta.setText("");
        jTextFieldFechaVencimiento.setText("");
        jPasswordFieldCVV.setText("");
        jTextFieldNombreTarjeta.setText("");
        
        // Limpiar campos de ubicación
        jTextFieldDireccion.setText("");
        jTextFieldCiudad.setText("");
        jTextFieldProvincia.setText("");
        jTextFieldCodigoPostal.setText("");
        
        // Resetear combo box
        jComboBoxMetodoPago.setSelectedIndex(0);
        
        logger.info("Campos de pago limpiados después de transacción exitosa");
    }

    private void jButtonGuardarTarjetaActionPerformed(ActionEvent evt) {
        // Validar campos de entrada
        String numeroTarjeta = jTextFieldNumeroTarjeta.getText().trim();
        String fechaVencimiento = jTextFieldFechaVencimiento.getText().trim();
        String cvv = new String(jPasswordFieldCVV.getPassword()).trim();
        String nombreTarjeta = jTextFieldNombreTarjeta.getText().trim();
        
        // Validaciones básicas
        if (numeroTarjeta.isEmpty() || numeroTarjeta.contains("****")) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese un número de tarjeta válido.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!CardSecurityUtil.isValidCardNumberFormat(numeroTarjeta)) {
            JOptionPane.showMessageDialog(this, 
                "Formato de número de tarjeta inválido.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!CardSecurityUtil.isValidExpiryDateFormat(fechaVencimiento)) {
            JOptionPane.showMessageDialog(this, 
                "Formato de fecha de vencimiento inválido (MM/YY).", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (!CardSecurityUtil.isValidCVVFormat(cvv)) {
            JOptionPane.showMessageDialog(this, 
                "Formato de CVV inválido.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if (nombreTarjeta.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Por favor, ingrese el nombre del titular de la tarjeta.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            // Verificar si ya existe una tarjeta con el mismo hash
            String hashTarjeta = CardSecurityUtil.hashCardNumber(numeroTarjeta);
            
            String checkQuery = "SELECT COUNT(*) FROM tarjetas_usuario WHERE Cliente_idCliente = ? AND numero_tarjeta_hash = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, currentClienteId);
                checkStmt.setString(2, hashTarjeta);
                
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        JOptionPane.showMessageDialog(this, 
                            "Esta tarjeta ya está registrada para el cliente.", 
                            "Tarjeta Duplicada", 
                            JOptionPane.INFORMATION_MESSAGE);
                        return;
                    }
                }
            }
            
            // Guardar la tarjeta con datos hasheados
            String insertQuery = "INSERT INTO tarjetas_usuario (Cliente_idCliente, numero_tarjeta_hash, " +
                               "fecha_vencimiento_enc, cvv_enc, nombre_titular, ultimos_cuatro_digitos, " +
                               "fecha_registro) VALUES (?, ?, ?, ?, ?, ?, NOW())";
            
            try (PreparedStatement insertStmt = conn.prepareStatement(insertQuery)) {
                insertStmt.setInt(1, currentClienteId);
                insertStmt.setString(2, hashTarjeta);
                insertStmt.setString(3, CardSecurityUtil.encrypt(fechaVencimiento));
                insertStmt.setString(4, CardSecurityUtil.encrypt(cvv));
                insertStmt.setString(5, nombreTarjeta); // Nombre no encriptado para facilitar búsquedas
                insertStmt.setString(6, CardSecurityUtil.getLastFourDigits(numeroTarjeta));
                
                int rowsInserted = insertStmt.executeUpdate();
                
                if (rowsInserted > 0) {
                    JOptionPane.showMessageDialog(this, 
                        "Tarjeta guardada exitosamente de forma segura.\n" +
                        "Los datos están protegidos con encriptación.", 
                        "Tarjeta Guardada", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    // Recargar las tarjetas guardadas
                    cargarTarjetasGuardadas();
                    
                    logger.info("Tarjeta guardada exitosamente para cliente ID: " + currentClienteId);
                } else {
                    JOptionPane.showMessageDialog(this, 
                        "Error al guardar la tarjeta. Intente nuevamente.", 
                        "Error", 
                        JOptionPane.ERROR_MESSAGE);
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al guardar tarjeta", e);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar la tarjeta: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Carga las tarjetas guardadas del cliente para mostrar en el combo box
     */
    private void cargarTarjetasGuardadas() {
        if (currentClienteId <= 0) {
            logger.warning("ID de cliente no válido para cargar tarjetas guardadas");
            return;
        }
        
        // Limpiar los ComboBox existentes
        jComboBoxTarjetasCredito.removeAllItems();
        jComboBoxTarjetasDebito.removeAllItems();
        
        // Agregar elementos por defecto
        jComboBoxTarjetasCredito.addItem(new TarjetaGuardada(0, "credito", "", "", "-- Seleccionar Tarjeta de Crédito --"));
        jComboBoxTarjetasDebito.addItem(new TarjetaGuardada(0, "debito", "", "", "-- Seleccionar Tarjeta de Débito --"));
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "SELECT id_tarjeta, numero_tarjeta_hash, nombre_titular, ultimos_cuatro_digitos, " +
                          "fecha_vencimiento_enc, fecha_registro, tipo_tarjeta " +
                          "FROM tarjetas_usuario WHERE Cliente_idCliente = ? AND activa = true " +
                          "ORDER BY fecha_registro DESC";
            
            try (PreparedStatement pstmt = conn.prepareStatement(query)) {
                pstmt.setInt(1, currentClienteId);
                
                try (ResultSet rs = pstmt.executeQuery()) {
                    int countCredito = 0;
                    int countDebito = 0;
                    
                    while (rs.next()) {
                        int idTarjeta = rs.getInt("id_tarjeta");
                        String nombreTitular = rs.getString("nombre_titular");
                        String ultimosCuatro = rs.getString("ultimos_cuatro_digitos");
                        String fechaVencimientoEnc = rs.getString("fecha_vencimiento_enc");
                        String tipoTarjeta = rs.getString("tipo_tarjeta");
                        
                        // Desencriptar la fecha de vencimiento para mostrar
                        String fechaVencimiento = "";
                        try {
                            fechaVencimiento = CardSecurityUtil.decrypt(fechaVencimientoEnc);
                        } catch (Exception e) {
                            logger.warning("Error al desencriptar fecha de vencimiento para tarjeta ID: " + idTarjeta);
                            fechaVencimiento = "**/**";
                        }
                        
                        // Crear objeto TarjetaGuardada
                        TarjetaGuardada tarjeta = new TarjetaGuardada(
                            idTarjeta,
                            tipoTarjeta != null ? tipoTarjeta : "credito",
                            fechaVencimiento,
                            ultimosCuatro,
                            nombreTitular
                        );
                        
                        // Agregar al ComboBox correspondiente
                        if ("debito".equalsIgnoreCase(tipoTarjeta)) {
                            jComboBoxTarjetasDebito.addItem(tarjeta);
                            countDebito++;
                        } else {
                            jComboBoxTarjetasCredito.addItem(tarjeta);
                            countCredito++;
                        }
                    }
                    
                    logger.info("Cargadas " + countCredito + " tarjetas de crédito y " + countDebito + " tarjetas de débito para cliente " + currentClienteId);
                }
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar tarjetas guardadas", e);
            JOptionPane.showMessageDialog(this, 
                "Error al cargar tarjetas guardadas: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
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

    // Métodos para gestión de ubicaciones
    
    /**
     * Carga las ubicaciones guardadas del cliente desde la base de datos
     */
    private void loadSavedLocations() {
        if (currentClienteId <= 0) return;
        
        jComboBoxUbicacionesGuardadas.removeAllItems();
        jComboBoxUbicacionesGuardadas.addItem(new UbicacionGuardada(0, "-- Seleccionar Ubicación Guardada --", "", "", ""));
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            String sql = "SELECT DISTINCT u.idUbicacion, u.direccion, u.ciudad, u.provincia, u.codigo_postal " +
                        "FROM ubicacion u " +
                        "INNER JOIN servicios s ON u.Servicios_idServicios = s.idServicios " +
                        "INNER JOIN contrato c ON s.Contrato_idContrato = c.idContrato " +
                        "WHERE c.Cliente_idCliente = ? " +
                        "UNION " +
                        "SELECT u.idUbicacion, u.direccion, u.ciudad, u.provincia, u.codigo_postal " +
                        "FROM ubicacion u " +
                        "WHERE u.Servicios_idServicios = ? " +
                        "ORDER BY idUbicacion DESC";
            
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentClienteId);
            pstmt.setInt(2, -currentClienteId); // Ubicaciones favoritas guardadas
            rs = pstmt.executeQuery();
            
            int count = 0;
            while (rs.next()) {
                int idUbicacion = rs.getInt("idUbicacion");
                String direccion = rs.getString("direccion");
                String ciudad = rs.getString("ciudad");
                String provincia = rs.getString("provincia");
                String codigoPostal = rs.getString("codigo_postal");
                
                UbicacionGuardada ubicacion = new UbicacionGuardada(idUbicacion, direccion, ciudad, provincia, codigoPostal);
                jComboBoxUbicacionesGuardadas.addItem(ubicacion);
                count++;
            }
            
            logger.info("Cargadas " + count + " ubicaciones guardadas para cliente " + currentClienteId);
            
        } catch (SQLException ex) {
            logger.log(Level.WARNING, "Error al cargar ubicaciones guardadas: " + ex.getMessage(), ex);
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
     * Maneja el evento de usar una ubicación guardada seleccionada
     */
    private void jButtonUsarUbicacionActionPerformed(ActionEvent evt) {
        UbicacionGuardada ubicacionSeleccionada = (UbicacionGuardada) jComboBoxUbicacionesGuardadas.getSelectedItem();
        
        if (ubicacionSeleccionada != null && ubicacionSeleccionada.getIdUbicacion() > 0) {
            jTextFieldDireccion.setText(ubicacionSeleccionada.getDireccion());
            jTextFieldCiudad.setText(ubicacionSeleccionada.getCiudad());
            jTextFieldProvincia.setText(ubicacionSeleccionada.getProvincia());
            jTextFieldCodigoPostal.setText(ubicacionSeleccionada.getCodigoPostal());
            
            JOptionPane.showMessageDialog(this, 
                "Ubicación cargada exitosamente.\nPuede modificar los campos si es necesario.", 
                "Ubicación Cargada", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Por favor, seleccione una ubicación válida de la lista.", 
                "Selección Requerida", 
                JOptionPane.WARNING_MESSAGE);
        }
    }

    /**
     * Maneja el evento de guardar la ubicación actual como favorita
     */
    private void jButtonGuardarUbicacionActionPerformed(ActionEvent evt) {
        // Validar que todos los campos estén llenos
        if (jTextFieldDireccion.getText().trim().isEmpty() ||
            jTextFieldCiudad.getText().trim().isEmpty() ||
            jTextFieldProvincia.getText().trim().isEmpty() ||
            jTextFieldCodigoPostal.getText().trim().isEmpty()) {
            
            JOptionPane.showMessageDialog(this, 
                "Por favor, complete todos los campos de ubicación antes de guardar.", 
                "Campos Incompletos", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Confirmar con el usuario
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Desea guardar esta ubicación para uso futuro?\n\n" +
            "Dirección: " + jTextFieldDireccion.getText() + "\n" +
            "Ciudad: " + jTextFieldCiudad.getText() + "\n" +
            "Provincia: " + jTextFieldProvincia.getText() + "\n" +
            "Código Postal: " + jTextFieldCodigoPostal.getText(), 
            "Confirmar Guardado", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            if (guardarUbicacionFavorita()) {
                JOptionPane.showMessageDialog(this, 
                    "Ubicación guardada exitosamente.\nPodrá seleccionarla en futuras transacciones.", 
                    "Ubicación Guardada", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Recargar la lista de ubicaciones
                SwingUtilities.invokeLater(() -> loadSavedLocations());
            }
        }
    }

    /**
     * Guarda la ubicación actual como favorita en la base de datos
     */
    private boolean guardarUbicacionFavorita() {
        if (currentClienteId <= 0) return false;
        
        Connection conn = null;
        PreparedStatement pstmt = null;
        
        try {
            conn = DatabaseConnection.getConnection();
            
            // Verificar si ya existe esta ubicación exacta para el cliente
            String checkSql = "SELECT COUNT(*) FROM ubicacion u " +
                             "INNER JOIN servicios s ON u.Servicios_idServicios = s.idServicios " +
                             "INNER JOIN contrato c ON s.Contrato_idContrato = c.idContrato " +
                             "WHERE c.Cliente_idCliente = ? AND u.direccion = ? AND u.ciudad = ? AND u.provincia = ? AND u.codigo_postal = ?";
            
            pstmt = conn.prepareStatement(checkSql);
            pstmt.setInt(1, currentClienteId);
            pstmt.setString(2, jTextFieldDireccion.getText().trim());
            pstmt.setString(3, jTextFieldCiudad.getText().trim());
            pstmt.setString(4, jTextFieldProvincia.getText().trim());
            pstmt.setString(5, jTextFieldCodigoPostal.getText().trim());
            
            ResultSet rs = pstmt.executeQuery();
            rs.next();
            int count = rs.getInt(1);
            rs.close();
            pstmt.close();
            
            if (count > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Esta ubicación ya está guardada en su lista de ubicaciones.", 
                    "Ubicación Duplicada", 
                    JOptionPane.INFORMATION_MESSAGE);
                return true; // No es un error, simplemente ya existe
            }
            
            // Crear un registro temporal en la tabla de ubicaciones para referencia futura
            // Usaremos un servicio temporal con ID especial para ubicaciones favoritas
            String insertSql = "INSERT INTO ubicacion (idUbicacion, Servicios_idServicios, direccion, ciudad, provincia, codigo_postal) " +
                              "VALUES (?, ?, ?, ?, ?, ?)";
            
            // Generar un ID único para la ubicación
            Random rand = new Random();
            int ubicacionId = 900000 + rand.nextInt(99999); // IDs especiales para ubicaciones favoritas
            
            pstmt = conn.prepareStatement(insertSql);
            pstmt.setInt(1, ubicacionId);
            pstmt.setInt(2, -currentClienteId); // Usar ID negativo del cliente como servicio temporal
            pstmt.setString(3, jTextFieldDireccion.getText().trim());
            pstmt.setString(4, jTextFieldCiudad.getText().trim());
            pstmt.setString(5, jTextFieldProvincia.getText().trim());
            pstmt.setString(6, jTextFieldCodigoPostal.getText().trim());
            
            int result = pstmt.executeUpdate();
            
            if (result > 0) {
                logger.info("Ubicación favorita guardada exitosamente para cliente " + currentClienteId);
                return true;
            }
            
        } catch (SQLException ex) {
            logger.log(Level.SEVERE, "Error al guardar ubicación favorita: " + ex.getMessage(), ex);
            JOptionPane.showMessageDialog(this, 
                "Error al guardar la ubicación: " + ex.getMessage(), 
                "Error de Base de Datos", 
                JOptionPane.ERROR_MESSAGE);
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ex) {
                logger.log(Level.WARNING, "Error al cerrar recursos", ex);
            }
        }
        
        return false;
    }

    /**
     * Maneja el evento de limpiar todos los campos de ubicación
     */
    private void jButtonLimpiarUbicacionActionPerformed(ActionEvent evt) {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea limpiar todos los campos de ubicación?", 
            "Confirmar Limpieza", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            jTextFieldDireccion.setText("");
            jTextFieldCiudad.setText("");
            jTextFieldProvincia.setText("");
            jTextFieldCodigoPostal.setText("");
            jComboBoxUbicacionesGuardadas.setSelectedIndex(0);
            
            JOptionPane.showMessageDialog(this, 
                "Campos de ubicación limpiados.", 
                "Campos Limpiados", 
                JOptionPane.INFORMATION_MESSAGE);
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
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(Level.SEVERE, null, ex);
        }
        java.awt.EventQueue.invokeLater(() -> new Facturación().setVisible(true));
    }
}
