package GUI_CHIDO;

import Clases.Cliente;
import Clases.BackgroundPanel;
import Clases.DatabaseConnection;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Versión completa de User_1 con funcionalidades mejoradas
 * Compatible con NetNexus Ultra v1.5.1
 * @author ASUS & Compañero
 */
public class User_1_Complete extends JFrame {
    
    private static final Logger logger = Logger.getLogger(User_1_Complete.class.getName());
    private Cliente currentUser;
    private BackgroundPanel backgroundPanel;
    
    // Componentes de la interfaz
    private JLabel jLabelWelcome;
    private JLabel jLabelUserInfo;
    private JButton jButtonServicios;
    private JButton jButtonContratos;
    private JButton jButtonPerfil;
    private JButton jButtonFacturacion;
    private JButton jButtonSoporte;
    private JButton jButtonCalificaciones;
    private JButton jButtonLogout;
    
    public User_1_Complete(Cliente cliente) {
        this.currentUser = cliente;
        initComponents();
        setupBackground();
        loadUserData();
    }
    
    /**
     * Constructor alternativo para compatibilidad
     */
    public User_1_Complete() {
        this(new Cliente(1, "Usuario", "Demo", "000-000-0000", "demo@netnexus.com", "demo123"));
    }
    
    private void initComponents() {
        setTitle("NetNexus Ultra - Panel de Usuario");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // Inicializar componentes
        jLabelWelcome = new JLabel();
        jLabelUserInfo = new JLabel();
        jButtonServicios = new JButton();
        jButtonContratos = new JButton();
        jButtonPerfil = new JButton();
        jButtonFacturacion = new JButton();
        jButtonSoporte = new JButton();
        jButtonCalificaciones = new JButton();
        jButtonLogout = new JButton();
        
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
        // Título de bienvenida
        jLabelWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        jLabelWelcome.setForeground(new Color(50, 70, 90));
        jLabelWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelWelcome.setText("¡Bienvenido a NetNexus Ultra!");
        add(jLabelWelcome);
        jLabelWelcome.setBounds(0, 50, 1000, 40);
        
        // Información del usuario
        jLabelUserInfo.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        jLabelUserInfo.setForeground(new Color(70, 90, 110));
        jLabelUserInfo.setHorizontalAlignment(SwingConstants.CENTER);
        if (currentUser != null) {
            jLabelUserInfo.setText("Usuario: " + currentUser.getNombre() + " " + currentUser.getApellido());
        }
        add(jLabelUserInfo);
        jLabelUserInfo.setBounds(0, 100, 1000, 25);
        
        // Configurar botones principales
        setupButton(jButtonServicios, "🌐 Ver Servicios", 150, 180, 220, 70, new Color(70, 130, 180));
        setupButton(jButtonContratos, "📋 Contratos Activos", 400, 180, 220, 70, new Color(76, 175, 80));
        setupButton(jButtonCalificaciones, "⭐ Calificaciones", 650, 180, 220, 70, new Color(255, 193, 7));
        
        setupButton(jButtonPerfil, "👤 Mi Perfil", 150, 280, 220, 70, new Color(255, 152, 0));
        setupButton(jButtonFacturacion, "💳 Facturación", 400, 280, 220, 70, new Color(156, 39, 176));
        setupButton(jButtonSoporte, "🛠️ Soporte Técnico", 650, 280, 220, 70, new Color(244, 67, 54));
        
        setupButton(jButtonLogout, "🚪 Cerrar Sesión", 400, 380, 220, 50, new Color(97, 97, 97));
    }
    
    private void setupButton(JButton button, String text, int x, int y, int width, int height, Color color) {
        button.setText(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        add(button);
        button.setBounds(x, y, width, height);
    }
    
    private void setupEventListeners() {
        jButtonServicios.addActionListener(e -> abrirServicios());
        jButtonContratos.addActionListener(e -> abrirContratos());
        jButtonPerfil.addActionListener(e -> abrirPerfil());
        jButtonFacturacion.addActionListener(e -> abrirFacturacion());
        jButtonCalificaciones.addActionListener(e -> abrirCalificaciones());
        jButtonSoporte.addActionListener(e -> abrirSoporte());
        jButtonLogout.addActionListener(e -> cerrarSesion());
    }
    
    private void loadUserData() {
        // Verificar conexión a base de datos
        try {
            Connection conn = DatabaseConnection.getConnection();
            if (conn != null) {
                logger.info("Conexión a BD establecida para usuario: " + currentUser.getIdCliente());
                conn.close();
            }
        } catch (SQLException e) {
            logger.log(Level.WARNING, "Error conectando a BD", e);
            JOptionPane.showMessageDialog(this, 
                "Advertencia: No se pudo conectar a la base de datos.\nAlgunas funciones pueden no estar disponibles.", 
                "Conexión BD", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
    
    // Métodos de navegación
    private void abrirServicios() {
        try {
            Servicios servicios = new Servicios(this, currentUser.getIdCliente());
            servicios.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo servicios", e);
            JOptionPane.showMessageDialog(this, "Error al abrir servicios: " + e.getMessage());
        }
    }
    
    private void abrirContratos() {
        try {
            UserContractsWindow contratos = new UserContractsWindow(currentUser);
            contratos.setVisible(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo contratos", e);
            JOptionPane.showMessageDialog(this, "Error al abrir contratos: " + e.getMessage());
        }
    }
    
    private void abrirCalificaciones() {
        try {
            VerCalificaciones calificaciones = new VerCalificaciones(this, currentUser.getIdCliente());
            calificaciones.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo calificaciones", e);
            JOptionPane.showMessageDialog(this, "Error al abrir calificaciones: " + e.getMessage());
        }
    }
    
    private void abrirPerfil() {
        try {
            Perfil_User perfil = new Perfil_User(this, String.valueOf(currentUser.getIdCliente()), currentUser.getPassword());
            perfil.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo perfil", e);
            JOptionPane.showMessageDialog(this, "Error al abrir perfil: " + e.getMessage());
        }
    }
    
    private void abrirFacturacion() {
        try {
            // Mostrar diálogo para seleccionar plan
            String[] planes = {"Plan Básico - $15.99", "Plan Estándar - $29.99", "Plan Premium - $49.99", "Plan Ultra - $69.99"};
            String seleccion = (String) JOptionPane.showInputDialog(this, 
                "Seleccione un plan para contratar:", 
                "Seleccionar Plan", 
                JOptionPane.QUESTION_MESSAGE, 
                null, 
                planes, 
                planes[0]);
            
            if (seleccion != null) {
                String planNombre = seleccion.split(" - ")[0];
                double precio = Double.parseDouble(seleccion.split("\\$")[1]);
                
                Facturación facturacion = new Facturación(planNombre, precio, currentUser.getIdCliente());
                facturacion.setParentFrame(this);
                facturacion.setVisible(true);
                this.setVisible(false);
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo facturación", e);
            JOptionPane.showMessageDialog(this, "Error al abrir facturación: " + e.getMessage());
        }
    }
    
    private void abrirSoporte() {
        JOptionPane.showMessageDialog(this, 
            "Soporte Técnico NetNexus Ultra\n\n" +
            "📞 Teléfono: 1800-NETNEXUS\n" +
            "📧 Email: soporte@netnexus.com\n" +
            "🌐 Web: www.netnexus.com/soporte\n\n" +
            "Horario de atención: 24/7", 
            "Soporte Técnico", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void cerrarSesion() {
        int confirmacion = JOptionPane.showConfirmDialog(this, 
            "¿Está seguro de que desea cerrar sesión?", 
            "Confirmar Cierre", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirmacion == JOptionPane.YES_OPTION) {
            logger.info("Usuario " + currentUser.getNombre() + " cerró sesión");
            this.dispose();
            
            // Abrir ventana de login
            SwingUtilities.invokeLater(() -> {
                try {
                    new Clases.LoginFrame().setVisible(true);
                } catch (Exception e) {
                    logger.log(Level.SEVERE, "Error abriendo login", e);
                    System.exit(0);
                }
            });
        }
    }
    
    /**
     * Método para actualizar información del usuario
     */
    public void updateUserInfo(Cliente nuevoUsuario) {
        this.currentUser = nuevoUsuario;
        if (jLabelUserInfo != null) {
            jLabelUserInfo.setText("Usuario: " + currentUser.getNombre() + " " + currentUser.getApellido());
        }
    }
    
    /**
     * Método para mostrar la ventana desde otras clases
     */
    public void mostrarVentana() {
        this.setVisible(true);
    }
}
