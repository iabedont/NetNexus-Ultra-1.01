package GUI_CHIDO;

import Clases.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

/**
 * Interfaz para mostrar calificaciones y recomendaciones de servicios
 * Permite a los usuarios ver opiniones de otros clientes antes de contratar
 * 
 * @author ASUS
 * @version 1.0
 */
public class VerCalificaciones extends JFrame {
    
    private static final Logger logger = Logger.getLogger(VerCalificaciones.class.getName());
    
    private JFrame parentFrame;
    private int currentClienteId;
    private BackgroundPanel backgroundPanel;
    
    // Componentes de la interfaz
    private JLabel jLabelTitulo;
    private JTabbedPane jTabbedPane;
    private JTable jTableMejoresServicios;
    private JTable jTableCalificacionesRecientes;
    private JTable jTableEstadisticas;
    private JButton jButtonVolver;
    private JButton jButtonCalificar;
    
    public VerCalificaciones(JFrame parentFrame, int clienteId) {
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId;
        
        initComponents();
        setupBackground();
        setupLayout();
        cargarDatos();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("NetNexus Ultra - Calificaciones y Recomendaciones");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(true);
        
        // Inicializar componentes
        jLabelTitulo = new JLabel();
        jTabbedPane = new JTabbedPane();
        jTableMejoresServicios = new JTable();
        jTableCalificacionesRecientes = new JTable();
        jTableEstadisticas = new JTable();
        jButtonVolver = new JButton();
        jButtonCalificar = new JButton();
    }
    
    private void setupBackground() {
        try {
            backgroundPanel = new BackgroundPanel("/Imagenes/Fondo.jpg");
        } catch (Exception e) {
            logger.log(Level.WARNING, "No se pudo cargar imagen de fondo", e);
            backgroundPanel = new BackgroundPanel(null);
        }
        
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);
    }
    
    private void setupLayout() {
        // Título
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        jLabelTitulo.setForeground(new Color(50, 70, 90));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("📊 CALIFICACIONES Y RECOMENDACIONES");
        backgroundPanel.add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 1000, 35);
        
        // Configurar tabbed pane
        jTabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jTabbedPane.setBackground(new Color(240, 240, 240));
        
        // Tab 1: Mejores Servicios
        JPanel panelMejores = createMejoresServiciosPanel();
        jTabbedPane.addTab("🏆 Mejores Servicios", panelMejores);
        
        // Tab 2: Calificaciones Recientes
        JPanel panelRecientes = createCalificacionesRecientesPanel();
        jTabbedPane.addTab("🕒 Opiniones Recientes", panelRecientes);
        
        // Tab 3: Estadísticas
        JPanel panelEstadisticas = createEstadisticasPanel();
        jTabbedPane.addTab("📈 Estadísticas", panelEstadisticas);
        
        backgroundPanel.add(jTabbedPane);
        jTabbedPane.setBounds(20, 70, 960, 530);
        
        // Botones
        setupButtons();
    }
    
    private JPanel createMejoresServiciosPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Descripción
        JLabel lblDescripcion = new JLabel("<html><center>" +
            "<h3>🌟 Servicios Mejor Calificados</h3>" +
            "Servicios recomendados por nuestros clientes con las mejores calificaciones" +
            "</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblDescripcion, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"Servicio", "Categoría", "Precio", "Calificaciones", 
                           "Promedio", "% Recomendación", "Clasificación"};
        
        jTableMejoresServicios = new JTable();
        jTableMejoresServicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTableMejoresServicios.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        jTableMejoresServicios.setRowHeight(25);
        jTableMejoresServicios.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Renderer personalizado para las clasificaciones
        jTableMejoresServicios.setDefaultRenderer(Object.class, new CalificacionTableRenderer());
        
        JScrollPane scrollMejores = new JScrollPane(jTableMejoresServicios);
        scrollMejores.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(scrollMejores, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createCalificacionesRecientesPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Descripción
        JLabel lblDescripcion = new JLabel("<html><center>" +
            "<h3>💬 Opiniones Recientes</h3>" +
            "Comentarios y calificaciones más recientes de nuestros clientes" +
            "</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblDescripcion, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"Cliente", "Servicio", "Calificación", "Comentario", 
                           "Recomendado", "Fecha", "Útil"};
        
        jTableCalificacionesRecientes = new JTable();
        jTableCalificacionesRecientes.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jTableCalificacionesRecientes.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        jTableCalificacionesRecientes.setRowHeight(35);
        jTableCalificacionesRecientes.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Permitir ajuste automático de columnas
        jTableCalificacionesRecientes.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        JScrollPane scrollRecientes = new JScrollPane(jTableCalificacionesRecientes);
        scrollRecientes.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        panel.add(scrollRecientes, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createEstadisticasPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        
        // Descripción
        JLabel lblDescripcion = new JLabel("<html><center>" +
            "<h3>📊 Estadísticas Detalladas</h3>" +
            "Análisis completo de calificaciones por categoría de servicio" +
            "</center></html>");
        lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblDescripcion.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(lblDescripcion, BorderLayout.NORTH);
        
        // Tabla
        String[] columnas = {"Servicio", "Categoría", "Precio Base", "Total Calificaciones", 
                           "Promedio General", "Técnico", "Servicio", "Tiempo", "Precio", "% Recomendación"};
        
        jTableEstadisticas = new JTable();
        jTableEstadisticas.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        jTableEstadisticas.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));
        jTableEstadisticas.setRowHeight(25);
        jTableEstadisticas.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        jTableEstadisticas.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        JScrollPane scrollEstadisticas = new JScrollPane(jTableEstadisticas);
        scrollEstadisticas.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        scrollEstadisticas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        panel.add(scrollEstadisticas, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void setupButtons() {
        // Botón Calificar
        jButtonCalificar.setText("⭐ Calificar Servicio");
        jButtonCalificar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonCalificar.setBackground(new Color(255, 193, 7));
        jButtonCalificar.setForeground(Color.BLACK);
        jButtonCalificar.setFocusPainted(false);
        jButtonCalificar.setBorder(BorderFactory.createRaisedBevelBorder());
        jButtonCalificar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backgroundPanel.add(jButtonCalificar);
        jButtonCalificar.setBounds(700, 620, 180, 35);
        
        // Botón Volver
        jButtonVolver.setText("← Volver");
        jButtonVolver.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonVolver.setBackground(new Color(108, 117, 125));
        jButtonVolver.setForeground(Color.WHITE);
        jButtonVolver.setFocusPainted(false);
        jButtonVolver.setBorder(BorderFactory.createRaisedBevelBorder());
        jButtonVolver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        backgroundPanel.add(jButtonVolver);
        jButtonVolver.setBounds(50, 620, 100, 35);
    }
    
    private void setupEventListeners() {
        jButtonVolver.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });
        
        jButtonCalificar.addActionListener(e -> abrirCalificarServicio());
    }
    
    private void cargarDatos() {
        cargarMejoresServicios();
        cargarCalificacionesRecientes();
        cargarEstadisticas();
    }
    
    private void cargarMejoresServicios() {
        try {
            List<String[]> mejoresServicios = CalificacionDAO.obtenerMejoresServicios(20);
            
            String[] columnas = {"Servicio", "Categoría", "Precio", "Calificaciones", 
                               "Promedio", "% Recomendación", "Clasificación"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (String[] servicio : mejoresServicios) {
                Object[] row = {
                    servicio[0], // nombre
                    servicio[2].toUpperCase(), // categoria
                    "$" + servicio[3], // precio
                    servicio[4], // total calificaciones
                    String.format("%.1f %s", 
                                Double.parseDouble(servicio[5]), 
                                CalificacionServicio.generarEstrellas(Double.parseDouble(servicio[5]))),
                    servicio[6] + "%", // porcentaje recomendacion
                    servicio[7] // clasificacion
                };
                model.addRow(row);
            }
            
            jTableMejoresServicios.setModel(model);
            
            // Ajustar anchos de columna
            jTableMejoresServicios.getColumnModel().getColumn(0).setPreferredWidth(200); // Servicio
            jTableMejoresServicios.getColumnModel().getColumn(1).setPreferredWidth(100); // Categoría
            jTableMejoresServicios.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
            jTableMejoresServicios.getColumnModel().getColumn(3).setPreferredWidth(100); // Calificaciones
            jTableMejoresServicios.getColumnModel().getColumn(4).setPreferredWidth(150); // Promedio
            jTableMejoresServicios.getColumnModel().getColumn(5).setPreferredWidth(120); // % Recomendación
            jTableMejoresServicios.getColumnModel().getColumn(6).setPreferredWidth(130); // Clasificación
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar mejores servicios", e);
        }
    }
    
    private void cargarCalificacionesRecientes() {
        try {
            List<String[]> calificaciones = CalificacionDAO.obtenerCalificacionesDetalladas(-1, 50);
            
            String[] columnas = {"Cliente", "Servicio", "Calificación", "Comentario", 
                               "Recomendado", "Fecha", "Útil"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (String[] calificacion : calificaciones) {
                // Truncar nombre del cliente para privacidad
                String clienteAnonimo = calificacion[0].length() > 0 ? 
                    calificacion[0].charAt(0) + "***" + 
                    (calificacion[0].length() > 3 ? calificacion[0].substring(calificacion[0].length()-1) : "") 
                    : "Usuario";
                
                Object[] row = {
                    clienteAnonimo,
                    calificacion[1], // servicio
                    String.format("%.1f %s", 
                                Double.parseDouble(calificacion[3]), 
                                CalificacionServicio.generarEstrellas(Double.parseDouble(calificacion[3]))),
                    calificacion[4] != null && !calificacion[4].isEmpty() ? 
                        (calificacion[4].length() > 100 ? 
                         calificacion[4].substring(0, 97) + "..." : calificacion[4]) : "Sin comentarios",
                    calificacion[5], // recomendado
                    calificacion[6].substring(0, 10), // fecha (solo fecha, sin hora)
                    calificacion[7] + " personas" // util
                };
                model.addRow(row);
            }
            
            jTableCalificacionesRecientes.setModel(model);
            
            // Ajustar anchos de columna
            jTableCalificacionesRecientes.getColumnModel().getColumn(0).setPreferredWidth(80);  // Cliente
            jTableCalificacionesRecientes.getColumnModel().getColumn(1).setPreferredWidth(150); // Servicio
            jTableCalificacionesRecientes.getColumnModel().getColumn(2).setPreferredWidth(120); // Calificación
            jTableCalificacionesRecientes.getColumnModel().getColumn(3).setPreferredWidth(300); // Comentario
            jTableCalificacionesRecientes.getColumnModel().getColumn(4).setPreferredWidth(80);  // Recomendado
            jTableCalificacionesRecientes.getColumnModel().getColumn(5).setPreferredWidth(80);  // Fecha
            jTableCalificacionesRecientes.getColumnModel().getColumn(6).setPreferredWidth(80);  // Útil
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar calificaciones recientes", e);
        }
    }
    
    private void cargarEstadisticas() {
        try {
            List<String[]> estadisticas = CalificacionDAO.obtenerEstadisticasServicios();
            
            String[] columnas = {"Servicio", "Categoría", "Precio Base", "Total Calificaciones", 
                               "Promedio General", "Técnico", "Servicio", "Tiempo", "Precio", "% Recomendación"};
            DefaultTableModel model = new DefaultTableModel(columnas, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };
            
            for (String[] estadistica : estadisticas) {
                Object[] row = {
                    estadistica[0], // servicio
                    estadistica[1].toUpperCase(), // categoria
                    "$" + estadistica[2], // precio
                    estadistica[3], // total calificaciones
                    estadistica[4], // promedio general
                    estadistica[5], // tecnico
                    estadistica[6], // servicio
                    estadistica[7], // tiempo
                    estadistica[8], // precio
                    estadistica[9] + "%" // recomendacion
                };
                model.addRow(row);
            }
            
            jTableEstadisticas.setModel(model);
            
            // Ajustar anchos de columna
            for (int i = 0; i < jTableEstadisticas.getColumnCount(); i++) {
                jTableEstadisticas.getColumnModel().getColumn(i).setPreferredWidth(100);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al cargar estadísticas", e);
        }
    }
    
    private void abrirCalificarServicio() {
        try {
            CalificarServicio calificarFrame = new CalificarServicio(this, currentClienteId);
            calificarFrame.setVisible(true);
            this.setVisible(false);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al abrir ventana de calificación", e);
            JOptionPane.showMessageDialog(this, 
                "Error al abrir la ventana de calificación: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Renderer personalizado para la tabla de calificaciones
     */
    private static class CalificacionTableRenderer extends JLabel implements TableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            setText(value != null ? value.toString() : "");
            setOpaque(true);
            
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
                
                // Colorear según la clasificación en la última columna
                if (column == table.getColumnCount() - 1 && value != null) {
                    String clasificacion = value.toString();
                    switch (clasificacion) {
                        case "EXCELENTE":
                            setBackground(new Color(76, 175, 80, 50));
                            break;
                        case "MUY BUENO":
                            setBackground(new Color(139, 195, 74, 50));
                            break;
                        case "BUENO":
                            setBackground(new Color(255, 193, 7, 50));
                            break;
                        case "REGULAR":
                            setBackground(new Color(255, 152, 0, 50));
                            break;
                        case "NECESITA MEJORAS":
                            setBackground(new Color(244, 67, 54, 50));
                            break;
                    }
                }
            }
            
            setFont(table.getFont());
            setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
            
            return this;
        }
    }
}
