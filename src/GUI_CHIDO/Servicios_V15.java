package GUI_CHIDO;

import Clases.BackgroundPanel;
import Clases.DatabaseConnection;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;

/**
 * Clase Servicios mejorada para NetNexus Ultra v1.5.1
 * Integra correctamente con la base de datos y las funcionalidades de ubicación
 */
public class Servicios_V15 extends JFrame {

    private static final Logger logger = Logger.getLogger(Servicios_V15.class.getName());
    private JFrame parentFrame;
    private int currentClienteId;
    private BackgroundPanel backgroundPanel;
    
    // Componentes de la interfaz
    private JLabel jLabelTitulo;
    private JButton jButtonInternet;
    private JButton jButtonTelefonia;
    private JButton jButtonTelevision;
    private JButton jButtonPaquetes;
    private JButton jButtonVolver;
    private JLabel jLabelDescripcion;

    public Servicios_V15(JFrame parentFrame, int clienteId) {
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId;
        initComponents();
        setupBackground();
        loadClientData();
    }
    
    public Servicios_V15() {
        this(null, 1); // Constructor por defecto
    }

    private void initComponents() {
        setTitle("NetNexus Ultra - Servicios Disponibles");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Inicializar componentes
        jLabelTitulo = new JLabel();
        jButtonInternet = new JButton();
        jButtonTelefonia = new JButton();
        jButtonTelevision = new JButton();
        jButtonPaquetes = new JButton();
        jButtonVolver = new JButton();
        jLabelDescripcion = new JLabel();
        
        setupLayout();
        setupEventListeners();
    }
    
    private void setupBackground() {
        try {
            backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
            backgroundPanel.setLayout(null);
            setContentPane(backgroundPanel);
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error cargando imagen de fondo", e);
            getContentPane().setBackground(new Color(240, 248, 255));
        }
    }
    
    private void setupLayout() {
        // Título principal
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        jLabelTitulo.setForeground(new Color(50, 70, 90));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("SERVICIOS NETNEXUS ULTRA");
        add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 50, 1000, 45);
        
        // Descripción
        jLabelDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        jLabelDescripcion.setForeground(new Color(70, 90, 110));
        jLabelDescripcion.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelDescripcion.setText("Seleccione el tipo de servicio que desea contratar");
        add(jLabelDescripcion);
        jLabelDescripcion.setBounds(0, 110, 1000, 25);
        
        // Botones de servicios
        setupServiceButton(jButtonInternet, "🌐 INTERNET", 200, 200, 250, 100, new Color(70, 130, 180));
        setupServiceButton(jButtonTelefonia, "📞 TELEFONÍA", 550, 200, 250, 100, new Color(76, 175, 80));
        setupServiceButton(jButtonTelevision, "📺 TELEVISIÓN", 200, 350, 250, 100, new Color(255, 152, 0));
        setupServiceButton(jButtonPaquetes, "📦 PAQUETES", 550, 350, 250, 100, new Color(156, 39, 176));
        
        // Botón volver
        jButtonVolver.setText("← Volver al Menú Principal");
        jButtonVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonVolver.setBackground(new Color(97, 97, 97));
        jButtonVolver.setForeground(Color.WHITE);
        jButtonVolver.setFocusPainted(false);
        jButtonVolver.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        jButtonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(jButtonVolver);
        jButtonVolver.setBounds(375, 550, 250, 50);
    }
    
    private void setupServiceButton(JButton button, String text, int x, int y, int width, int height, Color color) {
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(15, 25, 15, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Efecto hover
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            Color originalColor = color;
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor.brighter());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(originalColor);
            }
        });
        
        add(button);
        button.setBounds(x, y, width, height);
    }
    
    private void setupEventListeners() {
        jButtonInternet.addActionListener(e -> abrirInternet());
        jButtonTelefonia.addActionListener(e -> abrirTelefonia());
        jButtonTelevision.addActionListener(e -> abrirTelevision());
        jButtonPaquetes.addActionListener(e -> abrirPaquetes());
        jButtonVolver.addActionListener(e -> volverMenuPrincipal());
    }
    
    private void loadClientData() {
        // Cargar información del cliente desde la base de datos
        try {
            Connection conn = DatabaseConnection.getConnection();
            String sql = "SELECT nombre, apellido FROM cliente WHERE idCliente = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, currentClienteId);
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                jLabelDescripcion.setText("Cliente: " + nombreCompleto + " | Seleccione el servicio deseado");
            }
            
            rs.close();
            pstmt.close();
            conn.close();
            
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error cargando datos del cliente", e);
        }
    }
    
    // Métodos de navegación a servicios específicos
    private void abrirInternet() {
        try {
            Internet internet = new Internet(this, currentClienteId);
            internet.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo Internet", e);
            mostrarErrorYVolver("Error al abrir servicios de Internet");
        }
    }
    
    private void abrirTelefonia() {
        // Mostrar opciones de telefonía
        String[] opciones = {"Telefonía Fija", "Telefonía Móvil"};
        String seleccion = (String) JOptionPane.showInputDialog(this,
            "Seleccione el tipo de telefonía:",
            "Servicios de Telefonía",
            JOptionPane.QUESTION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion != null) {
            try {
                if (seleccion.equals("Telefonía Fija")) {
                    TeleFija teleFija = new TeleFija(this, currentClienteId);
                    teleFija.setVisible(true);
                } else {
                    TeleMovil teleMovil = new TeleMovil(this, currentClienteId);
                    teleMovil.setVisible(true);
                }
                this.setVisible(false);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error abriendo telefonía", e);
                mostrarErrorYVolver("Error al abrir servicios de telefonía");
            }
        }
    }
    
    private void abrirTelevision() {
        try {
            Television television = new Television(this, currentClienteId);
            television.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo Televisión", e);
            mostrarErrorYVolver("Error al abrir servicios de televisión");
        }
    }
    
    private void abrirPaquetes() {
        // Mostrar opciones de paquetes combinados
        String mensaje = "Paquetes Disponibles:\n\n" +
                        "📦 PAQUETE BÁSICO\n" +
                        "Internet 50MB + Telefonía Fija\n" +
                        "Precio: $29.99/mes\n\n" +
                        "📦 PAQUETE COMPLETO\n" +
                        "Internet 100MB + Telefonía + TV\n" +
                        "Precio: $59.99/mes\n\n" +
                        "📦 PAQUETE ULTRA\n" +
                        "Internet 500MB + Telefonía + TV Premium\n" +
                        "Precio: $99.99/mes";
        
        String[] opciones = {"Paquete Básico", "Paquete Completo", "Paquete Ultra", "Cancelar"};
        int seleccion = JOptionPane.showOptionDialog(this,
            mensaje,
            "Paquetes NetNexus Ultra",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            opciones,
            opciones[0]);
        
        if (seleccion >= 0 && seleccion < 3) {
            String planNombre = opciones[seleccion];
            double precio = (seleccion == 0) ? 29.99 : (seleccion == 1) ? 59.99 : 99.99;
            
            try {
                Facturación facturacion = new Facturación(planNombre, precio, currentClienteId);
                facturacion.setParentFrame(this);
                facturacion.setVisible(true);
                this.setVisible(false);
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error abriendo facturación de paquetes", e);
                mostrarErrorYVolver("Error al procesar el paquete seleccionado");
            }
        }
    }
    
    private void volverMenuPrincipal() {
        if (parentFrame != null) {
            parentFrame.setVisible(true);
            this.dispose();
        } else {
            // Si no hay parent frame, crear uno nuevo
            try {
                User_1_Complete mainFrame = new User_1_Complete();
                mainFrame.setVisible(true);
                this.dispose();
            } catch (Exception e) {
                logger.log(Level.SEVERE, "Error volviendo al menú principal", e);
                System.exit(0);
            }
        }
    }
    
    private void mostrarErrorYVolver(String mensaje) {
        JOptionPane.showMessageDialog(this, 
            mensaje + "\n\nVolviendo al menú de servicios.", 
            "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Método para verificar la conectividad con la base de datos
     */
    public boolean verificarConexionBD() {
        try {
            Connection conn = DatabaseConnection.getConnection();
            boolean activa = conn != null && !conn.isClosed();
            if (conn != null) conn.close();
            return activa;
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error verificando conexión BD", e);
            return false;
        }
    }
}
