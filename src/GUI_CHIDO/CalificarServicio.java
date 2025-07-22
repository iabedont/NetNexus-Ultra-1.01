package GUI_CHIDO;

import Clases.*;
import java.awt.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Interfaz para que los clientes califiquen los servicios recibidos
 * Permite calificar diferentes aspectos del servicio y agregar comentarios
 * 
 * @author ASUS
 * @version 1.0
 */
public class CalificarServicio extends JFrame {
    
    private static final Logger logger = Logger.getLogger(CalificarServicio.class.getName());
    
    private JFrame parentFrame;
    private int currentClienteId;
    private BackgroundPanel backgroundPanel;
    
    // Componentes de la interfaz
    private JLabel jLabelTitulo;
    private JComboBox<String> jComboBoxServicios;
    private JSlider jSliderTecnico;
    private JSlider jSliderServicio;
    private JSlider jSliderTiempo;
    private JSlider jSliderPrecio;
    private JLabel jLabelCalificacionGeneral;
    private JTextArea jTextAreaComentario;
    private JCheckBox jCheckBoxRecomendaria;
    private JButton jButtonGuardar;
    private JButton jButtonCancelar;
    
    // Labels para mostrar valores de sliders
    private JLabel jLabelValorTecnico;
    private JLabel jLabelValorServicio;
    private JLabel jLabelValorTiempo;
    private JLabel jLabelValorPrecio;
    
    // Datos de servicios disponibles
    private List<String[]> serviciosDisponibles;
    
    public CalificarServicio(JFrame parentFrame, int clienteId) {
        this.parentFrame = parentFrame;
        this.currentClienteId = clienteId;
        
        initComponents();
        setupBackground();
        cargarServiciosDisponibles();
        setupEventListeners();
    }
    
    private void initComponents() {
        setTitle("NetNexus Ultra - Calificar Servicios");
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        // Inicializar componentes
        jLabelTitulo = new JLabel();
        jComboBoxServicios = new JComboBox<>();
        jSliderTecnico = new JSlider(1, 5, 5);
        jSliderServicio = new JSlider(1, 5, 5);
        jSliderTiempo = new JSlider(1, 5, 5);
        jSliderPrecio = new JSlider(1, 5, 5);
        jLabelCalificacionGeneral = new JLabel();
        jTextAreaComentario = new JTextArea(4, 30);
        jCheckBoxRecomendaria = new JCheckBox();
        jButtonGuardar = new JButton();
        jButtonCancelar = new JButton();
        
        // Labels para valores
        jLabelValorTecnico = new JLabel("5");
        jLabelValorServicio = new JLabel("5");
        jLabelValorTiempo = new JLabel("5");
        jLabelValorPrecio = new JLabel("5");
        
        setupLayout();
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
        jLabelTitulo.setFont(new Font("Segoe UI", Font.BOLD, 24));
        jLabelTitulo.setForeground(new Color(50, 70, 90));
        jLabelTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelTitulo.setText("CALIFICAR SERVICIO");
        backgroundPanel.add(jLabelTitulo);
        jLabelTitulo.setBounds(0, 20, 700, 30);
        
        // Panel principal con scroll
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        mainPanel.setOpaque(false);
        
        // Selección de servicio
        JLabel lblServicio = new JLabel("Servicio a calificar:");
        lblServicio.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblServicio.setForeground(Color.WHITE);
        mainPanel.add(lblServicio);
        lblServicio.setBounds(50, 70, 200, 25);
        
        jComboBoxServicios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        mainPanel.add(jComboBoxServicios);
        jComboBoxServicios.setBounds(50, 100, 600, 30);
        
        // Panel de calificaciones
        JPanel panelCalificaciones = createCalificacionesPanel();
        mainPanel.add(panelCalificaciones);
        panelCalificaciones.setBounds(50, 150, 600, 250);
        
        // Calificación general
        jLabelCalificacionGeneral.setFont(new Font("Segoe UI", Font.BOLD, 18));
        jLabelCalificacionGeneral.setForeground(Color.YELLOW);
        jLabelCalificacionGeneral.setHorizontalAlignment(SwingConstants.CENTER);
        jLabelCalificacionGeneral.setText("Calificación General: 5.0 ★★★★★");
        mainPanel.add(jLabelCalificacionGeneral);
        jLabelCalificacionGeneral.setBounds(50, 410, 600, 25);
        
        // Comentario
        JLabel lblComentario = new JLabel("Comentario (opcional):");
        lblComentario.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblComentario.setForeground(Color.WHITE);
        mainPanel.add(lblComentario);
        lblComentario.setBounds(50, 445, 200, 25);
        
        jTextAreaComentario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        jTextAreaComentario.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        jTextAreaComentario.setLineWrap(true);
        jTextAreaComentario.setWrapStyleWord(true);
        JScrollPane scrollComentario = new JScrollPane(jTextAreaComentario);
        mainPanel.add(scrollComentario);
        scrollComentario.setBounds(50, 475, 600, 60);
        
        // Checkbox recomendación
        jCheckBoxRecomendaria.setText("¿Recomendarías este servicio a otros usuarios?");
        jCheckBoxRecomendaria.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jCheckBoxRecomendaria.setForeground(Color.WHITE);
        jCheckBoxRecomendaria.setOpaque(false);
        jCheckBoxRecomendaria.setSelected(true);
        mainPanel.add(jCheckBoxRecomendaria);
        jCheckBoxRecomendaria.setBounds(50, 545, 400, 25);
        
        // Botones
        setupButtons(mainPanel);
        
        backgroundPanel.add(mainPanel);
        mainPanel.setBounds(0, 0, 700, 600);
    }
    
    private JPanel createCalificacionesPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1, 5, 5));
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.WHITE, 2),
            "Califica cada aspecto del servicio (1-5)",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            new Font("Segoe UI", Font.BOLD, 12),
            Color.WHITE
        ));
        
        // Configurar sliders
        panel.add(createSliderPanel("👨‍🔧 Técnico:", jSliderTecnico, jLabelValorTecnico));
        panel.add(createSliderPanel("🛠️ Calidad del Servicio:", jSliderServicio, jLabelValorServicio));
        panel.add(createSliderPanel("⏱️ Tiempo de Respuesta:", jSliderTiempo, jLabelValorTiempo));
        panel.add(createSliderPanel("💰 Relación Calidad-Precio:", jSliderPrecio, jLabelValorPrecio));
        
        return panel;
    }
    
    private JPanel createSliderPanel(String label, JSlider slider, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setOpaque(false);
        
        JLabel lblText = new JLabel(label);
        lblText.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblText.setForeground(Color.WHITE);
        lblText.setPreferredSize(new Dimension(200, 25));
        
        slider.setOpaque(false);
        slider.setMajorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setForeground(Color.WHITE);
        
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        valueLabel.setForeground(Color.YELLOW);
        valueLabel.setPreferredSize(new Dimension(30, 25));
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(lblText, BorderLayout.WEST);
        panel.add(slider, BorderLayout.CENTER);
        panel.add(valueLabel, BorderLayout.EAST);
        
        return panel;
    }
    
    private void setupButtons(JPanel mainPanel) {
        // Botón Guardar
        jButtonGuardar.setText("💾 Guardar Calificación");
        jButtonGuardar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonGuardar.setBackground(new Color(76, 175, 80));
        jButtonGuardar.setForeground(Color.WHITE);
        jButtonGuardar.setFocusPainted(false);
        jButtonGuardar.setBorder(BorderFactory.createRaisedBevelBorder());
        jButtonGuardar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(jButtonGuardar);
        jButtonGuardar.setBounds(450, 545, 200, 35);
        
        // Botón Cancelar
        jButtonCancelar.setText("❌ Cancelar");
        jButtonCancelar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jButtonCancelar.setBackground(new Color(244, 67, 54));
        jButtonCancelar.setForeground(Color.WHITE);
        jButtonCancelar.setFocusPainted(false);
        jButtonCancelar.setBorder(BorderFactory.createRaisedBevelBorder());
        jButtonCancelar.setCursor(new Cursor(Cursor.HAND_CURSOR));
        mainPanel.add(jButtonCancelar);
        jButtonCancelar.setBounds(50, 580, 120, 30);
    }
    
    private void setupEventListeners() {
        // Listeners para sliders
        jSliderTecnico.addChangeListener(e -> {
            jLabelValorTecnico.setText(String.valueOf(jSliderTecnico.getValue()));
            actualizarCalificacionGeneral();
        });
        
        jSliderServicio.addChangeListener(e -> {
            jLabelValorServicio.setText(String.valueOf(jSliderServicio.getValue()));
            actualizarCalificacionGeneral();
        });
        
        jSliderTiempo.addChangeListener(e -> {
            jLabelValorTiempo.setText(String.valueOf(jSliderTiempo.getValue()));
            actualizarCalificacionGeneral();
        });
        
        jSliderPrecio.addChangeListener(e -> {
            jLabelValorPrecio.setText(String.valueOf(jSliderPrecio.getValue()));
            actualizarCalificacionGeneral();
        });
        
        // Botón guardar
        jButtonGuardar.addActionListener(e -> guardarCalificacion());
        
        // Botón cancelar
        jButtonCancelar.addActionListener(e -> {
            if (parentFrame != null) {
                parentFrame.setVisible(true);
            }
            dispose();
        });
    }
    
    private void cargarServiciosDisponibles() {
        serviciosDisponibles = CalificacionDAO.obtenerServiciosParaCalificar(currentClienteId);
        
        jComboBoxServicios.removeAllItems();
        
        if (serviciosDisponibles.isEmpty()) {
            jComboBoxServicios.addItem("No hay servicios disponibles para calificar");
            jButtonGuardar.setEnabled(false);
        } else {
            for (String[] servicio : serviciosDisponibles) {
                String item = String.format("%s - %s (Fecha: %s)", 
                                          servicio[1], servicio[2], servicio[3]);
                jComboBoxServicios.addItem(item);
            }
            jButtonGuardar.setEnabled(true);
        }
    }
    
    private void actualizarCalificacionGeneral() {
        double promedio = (jSliderTecnico.getValue() + jSliderServicio.getValue() + 
                          jSliderTiempo.getValue() + jSliderPrecio.getValue()) / 4.0;
        
        String estrellas = CalificacionServicio.generarEstrellas(promedio);
        jLabelCalificacionGeneral.setText(String.format("Calificación General: %.1f %s", 
                                                        promedio, estrellas));
    }
    
    private void guardarCalificacion() {
        if (serviciosDisponibles.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "No hay servicios disponibles para calificar.", 
                "Sin servicios", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int selectedIndex = jComboBoxServicios.getSelectedIndex();
        if (selectedIndex < 0) {
            JOptionPane.showMessageDialog(this, 
                "Por favor seleccione un servicio.", 
                "Servicio requerido", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String[] servicioSeleccionado = serviciosDisponibles.get(selectedIndex);
            int servicioId = Integer.parseInt(servicioSeleccionado[0]);
            int tipoServicioId = Integer.parseInt(servicioSeleccionado[4]);
            
            CalificacionServicio calificacion = new CalificacionServicio(
                currentClienteId,
                servicioId,
                tipoServicioId,
                jSliderTecnico.getValue(),
                jSliderServicio.getValue(),
                jSliderTiempo.getValue(),
                jSliderPrecio.getValue(),
                jTextAreaComentario.getText().trim(),
                jCheckBoxRecomendaria.isSelected()
            );
            
            if (!calificacion.validarCalificaciones()) {
                JOptionPane.showMessageDialog(this, 
                    "Las calificaciones deben estar entre 1 y 5.", 
                    "Calificaciones inválidas", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            boolean guardado = CalificacionDAO.guardarCalificacion(calificacion);
            
            if (guardado) {
                JOptionPane.showMessageDialog(this, 
                    "¡Gracias por tu calificación!\n\n" +
                    "Tu opinión ayuda a otros usuarios a tomar mejores decisiones.", 
                    "Calificación guardada", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                // Volver al frame anterior
                if (parentFrame != null) {
                    parentFrame.setVisible(true);
                }
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Error al guardar la calificación.\nPor favor intenta nuevamente.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error al guardar calificación", e);
            JOptionPane.showMessageDialog(this, 
                "Error inesperado: " + e.getMessage(), 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
