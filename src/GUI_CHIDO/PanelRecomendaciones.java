package GUI_CHIDO;

import Clases.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Panel de recomendaciones integrado que se muestra junto a los servicios
 * Ayuda a los usuarios a tomar decisiones informadas basadas en calificaciones
 * 
 * @author ASUS
 * @version 1.0
 */
public class PanelRecomendaciones extends JPanel {
    
    private static final Logger logger = Logger.getLogger(PanelRecomendaciones.class.getName());
    
    private JFrame parentFrame;
    private int currentClienteId;
    
    // Componentes
    private JLabel jLabelTitulo;
    private JPanel jPanelMejoresServicios;
    private JPanel jPanelCalificacionesRecientes;
    private JScrollPane jScrollPane;
    
    public PanelRecomendaciones(JFrame parentFrame, int clienteId) {
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId;
        
        initComponents();
        cargarRecomendaciones();
    }
    
    private void initComponents() {
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 245));
        setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(70, 130, 180), 2),
            "🌟 Recomendaciones Basadas en Calificaciones",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 14),
            new Color(70, 130, 180)
        ));
        
        setupComponents();
        setupLayout();
    }
    
    private void setupComponents() {
        // Título
        jLabelTitulo = new JLabel("<html><center>" +
            "<h3>🔥 Servicios Más Populares</h3>" +
            "<small>Basado en calificaciones reales de nuestros clientes</small>" +
            "</center></html>");
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Panel principal con scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        
        jPanelMejoresServicios = new JPanel();
        jPanelMejoresServicios.setLayout(new BoxLayout(jPanelMejoresServicios, BoxLayout.Y_AXIS));
        jPanelMejoresServicios.setBackground(Color.WHITE);
        jPanelMejoresServicios.setBorder(BorderFactory.createTitledBorder("🏆 Top 5 Servicios"));
        
        jPanelCalificacionesRecientes = new JPanel();
        jPanelCalificacionesRecientes.setLayout(new BoxLayout(jPanelCalificacionesRecientes, BoxLayout.Y_AXIS));
        jPanelCalificacionesRecientes.setBackground(Color.WHITE);
        jPanelCalificacionesRecientes.setBorder(BorderFactory.createTitledBorder("💬 Opiniones Recientes"));
        
        mainPanel.add(jPanelMejoresServicios);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(jPanelCalificacionesRecientes);
        
        jScrollPane = new JScrollPane(mainPanel);
        jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane.setBorder(null);
    }
    
    private void setupLayout() {
        add(jLabelTitulo, BorderLayout.NORTH);
        add(jScrollPane, BorderLayout.CENTER);
        
        // Botón para ver todas las calificaciones
        JButton btnVerTodas = new JButton("🔍 Ver Todas las Calificaciones");
        btnVerTodas.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnVerTodas.setBackground(new Color(70, 130, 180));
        btnVerTodas.setForeground(Color.WHITE);
        btnVerTodas.setFocusPainted(false);
        btnVerTodas.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnVerTodas.addActionListener(e -> abrirCalificacionesCompletas());
        
        add(btnVerTodas, BorderLayout.SOUTH);
    }
    
    private void cargarRecomendaciones() {
        SwingUtilities.invokeLater(() -> {
            cargarMejoresServicios();
            cargarCalificacionesRecientes();
        });
    }
    
    private void cargarMejoresServicios() {
        try {
            List<String[]> mejoresServicios = CalificacionDAO.obtenerMejoresServicios(5);
            
            jPanelMejoresServicios.removeAll();
            
            if (mejoresServicios.isEmpty()) {
                JLabel lblSinDatos = new JLabel("No hay suficientes calificaciones aún");
                lblSinDatos.setHorizontalAlignment(SwingConstants.CENTER);
                lblSinDatos.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                jPanelMejoresServicios.add(lblSinDatos);
            } else {
                for (int i = 0; i < mejoresServicios.size(); i++) {
                    String[] servicio = mejoresServicios.get(i);
                    JPanel servicioPanel = crearPanelServicio(servicio, i + 1);
                    jPanelMejoresServicios.add(servicioPanel);
                    jPanelMejoresServicios.add(Box.createVerticalStrut(5));
                }
            }
            
            revalidate();
            repaint();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando mejores servicios", e);
        }
    }
    
    private void cargarCalificacionesRecientes() {
        try {
            List<String[]> calificaciones = CalificacionDAO.obtenerCalificacionesDetalladas(-1, 3);
            
            jPanelCalificacionesRecientes.removeAll();
            
            if (calificaciones.isEmpty()) {
                JLabel lblSinDatos = new JLabel("No hay calificaciones recientes");
                lblSinDatos.setHorizontalAlignment(SwingConstants.CENTER);
                lblSinDatos.setFont(new Font("Segoe UI", Font.ITALIC, 11));
                jPanelCalificacionesRecientes.add(lblSinDatos);
            } else {
                for (String[] calificacion : calificaciones) {
                    JPanel calificacionPanel = crearPanelCalificacion(calificacion);
                    jPanelCalificacionesRecientes.add(calificacionPanel);
                    jPanelCalificacionesRecientes.add(Box.createVerticalStrut(5));
                }
            }
            
            revalidate();
            repaint();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error cargando calificaciones recientes", e);
        }
    }
    
    private JPanel crearPanelServicio(String[] servicio, int posicion) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Número de posición
        JLabel lblPosicion = new JLabel(String.valueOf(posicion));
        lblPosicion.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblPosicion.setForeground(new Color(255, 193, 7));
        lblPosicion.setPreferredSize(new Dimension(25, 25));
        lblPosicion.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Información del servicio
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblNombre = new JLabel(servicio[0]); // nombre
        lblNombre.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        double calificacion = Double.parseDouble(servicio[5]); // promedio_calificacion
        String estrellas = CalificacionServicio.generarEstrellas(calificacion);
        
        JLabel lblCalificacion = new JLabel(String.format("%.1f %s (%s calificaciones)", 
                                          calificacion, estrellas, servicio[4]));
        lblCalificacion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        lblCalificacion.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(lblNombre, BorderLayout.NORTH);
        infoPanel.add(lblCalificacion, BorderLayout.SOUTH);
        
        // Precio
        JLabel lblPrecio = new JLabel("$" + servicio[3]);
        lblPrecio.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblPrecio.setForeground(new Color(76, 175, 80));
        
        panel.add(lblPosicion, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        panel.add(lblPrecio, BorderLayout.EAST);
        
        // Efecto hover
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                panel.setBackground(new Color(240, 248, 255));
                panel.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                panel.setBackground(Color.WHITE);
            }
            
            @Override
            public void mouseClicked(MouseEvent e) {
                mostrarDetallesServicio(servicio);
            }
        });
        
        return panel;
    }
    
    private JPanel crearPanelCalificacion(String[] calificacion) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(Color.LIGHT_GRAY),
            BorderFactory.createEmptyBorder(8, 8, 8, 8)
        ));
        
        // Cliente anónimo
        String clienteAnonimo = calificacion[0].length() > 0 ? 
            calificacion[0].charAt(0) + "***" : "Usuario";
        
        JLabel lblCliente = new JLabel(clienteAnonimo);
        lblCliente.setFont(new Font("Segoe UI", Font.BOLD, 11));
        lblCliente.setPreferredSize(new Dimension(60, 20));
        
        // Información de la calificación
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBackground(Color.WHITE);
        
        JLabel lblServicio = new JLabel(calificacion[1]); // servicio
        lblServicio.setFont(new Font("Segoe UI", Font.BOLD, 11));
        
        double calificacionNum = Double.parseDouble(calificacion[3]);
        String estrellas = CalificacionServicio.generarEstrellas(calificacionNum);
        
        JLabel lblCalificacion = new JLabel(String.format("%.1f %s", calificacionNum, estrellas));
        lblCalificacion.setFont(new Font("Segoe UI", Font.PLAIN, 10));
        
        // Comentario truncado
        String comentario = calificacion[4];
        if (comentario.length() > 50) {
            comentario = comentario.substring(0, 47) + "...";
        }
        JLabel lblComentario = new JLabel("\"" + comentario + "\"");
        lblComentario.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        lblComentario.setForeground(new Color(100, 100, 100));
        
        infoPanel.add(lblServicio, BorderLayout.NORTH);
        infoPanel.add(lblCalificacion, BorderLayout.CENTER);
        infoPanel.add(lblComentario, BorderLayout.SOUTH);
        
        panel.add(lblCliente, BorderLayout.WEST);
        panel.add(infoPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void mostrarDetallesServicio(String[] servicio) {
        String mensaje = String.format(
            "📋 DETALLES DEL SERVICIO\n\n" +
            "🏷️ Nombre: %s\n" +
            "📂 Categoría: %s\n" +
            "💰 Precio: $%s\n" +
            "⭐ Calificación: %.1f/5.0\n" +
            "📊 Total calificaciones: %s\n" +
            "👍 Recomendación: %s%%\n" +
            "🏆 Clasificación: %s\n\n" +
            "¿Te gustaría contratar este servicio?",
            servicio[0], // nombre
            servicio[2].toUpperCase(), // categoria
            servicio[3], // precio
            Double.parseDouble(servicio[5]), // calificacion
            servicio[4], // total calificaciones
            servicio[6], // % recomendacion
            servicio[7] // clasificacion
        );
        
        int option = JOptionPane.showConfirmDialog(
            this,
            mensaje,
            "Información del Servicio",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.INFORMATION_MESSAGE
        );
        
        if (option == JOptionPane.YES_OPTION) {
            // Aquí se podría abrir la ventana de contratación
            JOptionPane.showMessageDialog(this,
                "Redirigiendo a la página de contratación...\n" +
                "(Esta funcionalidad se integraría con el sistema de servicios)",
                "Contratar Servicio",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void abrirCalificacionesCompletas() {
        try {
            VerCalificaciones verCalificaciones = new VerCalificaciones(parentFrame, currentClienteId);
            verCalificaciones.setVisible(true);
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo calificaciones completas", e);
            JOptionPane.showMessageDialog(this,
                "Error al abrir las calificaciones: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Actualiza las recomendaciones (útil para llamar después de que el usuario califique algo)
     */
    public void actualizarRecomendaciones() {
        cargarRecomendaciones();
    }
}
