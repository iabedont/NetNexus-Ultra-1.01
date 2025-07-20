package GUI_CHIDO;

import Clases.BackgroundPanel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.logging.Level;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingConstants;


public class Internet extends JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Internet.class.getName());
    private JFrame parentFrame;

    // Componentes de la interfaz de usuario
    private BackgroundPanel backgroundPanel; // Nuevo panel de fondo
    private javax.swing.JButton jButton1; // Plan Estándar
    private javax.swing.JButton jButton2; // Regresar
    private javax.swing.JButton jButton3; // Plan Básico
    private javax.swing.JButton jButton4; // Plan Ultra
    private javax.swing.JLabel jLabel1; // Título "¡Tu próxima gran experiencia..."
    private javax.swing.JLabel jLabel10; // "Atención personalizada..."
    private javax.swing.JLabel jLabel11; // "Facturación clara..."
    private javax.swing.JLabel jLabel12; // "Infraestructura moderna..."
    private javax.swing.JLabel jLabel2; // "...comienza seleccionando el plan ideal!"
    private javax.swing.JLabel jLabel3; // "INTERNET DE ALTA VELOCIDAD..."
    private javax.swing.JLabel jLabel4; // "Todos nuestros planes incluyen:"
    private javax.swing.JLabel jLabel5; // "Router Wi-Fi GRATIS"
    private javax.swing.JLabel jLabel6; // "Instalación sin costo..."
    private javax.swing.JLabel jLabel7; // "Compatible con Smart TVs..."
    private javax.swing.JLabel jLabel8; // "Control parental..."
    private javax.swing.JLabel jLabel9; // "Cambios de plan sin penalización"

    public Internet() {
        initComponents();
        this.setSize(800, 750); // Ajustar tamaño para que quepan todos los elementos y el fondo
        this.setLocationRelativeTo(null); // Centrar la ventana

        // Añadir ComponentListener para responsividad
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                adjustComponentPositions();
            }
        });
        adjustComponentPositions(); // Ajustar posiciones inicialmente
    }

    public Internet(JFrame parentFrame) {
        this(); // Llama al constructor sin argumentos para inicializar componentes
        this.parentFrame = parentFrame;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {
        // Inicialización del BackgroundPanel como content pane
        backgroundPanel = new BackgroundPanel("/Imagenes/fondo.png");
        backgroundPanel.setLayout(null); // Usamos un layout nulo para posicionamiento absoluto
        this.setContentPane(backgroundPanel); // Establecer como content pane

        // Inicialización de componentes (ahora se añadirán a backgroundPanel)
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Planes de Internet");

        // Botón Regresar
        jButton2.setBackground(new Color(90, 150, 200)); // Azul suave
        jButton2.setFont(new Font("Segoe UI", Font.BOLD, 22)); // Fuente más grande
        jButton2.setForeground(Color.WHITE);
        
        // Cargar y escalar el icono si existe
        try {
            ImageIcon atrasIcon = new ImageIcon(getClass().getResource("/Imagenes/Atras.png"));
            if (atrasIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                Image img = atrasIcon.getImage();
                Image scaledImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH); // Escalar icono más grande
                jButton2.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen 'Atras.png' no encontrada. Usando solo texto para el botón Regresar.");
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen 'Atras.png': " + e.getMessage());
        }
        
        jButton2.setText("Regresar");
        jButton2.setContentAreaFilled(false); // Mantener transparente para ver el fondo
        jButton2.setBorderPainted(false); // No pintar borde por defecto
        jButton2.setFocusPainted(false);
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));
        backgroundPanel.add(jButton2);

        // Títulos principales
        jLabel1.setFont(new Font("Segoe UI", Font.BOLD, 30)); // Fuente más grande y negrita
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("¡Tu próxima gran experiencia ");
        jLabel1.setForeground(new Color(30, 50, 70)); // Azul oscuro-gris
        backgroundPanel.add(jLabel1);

        jLabel2.setFont(new Font("Segoe UI", Font.BOLD, 30));
        jLabel2.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel2.setText("comienza seleccionando el plan ideal!");
        jLabel2.setForeground(new Color(30, 50, 70));
        backgroundPanel.add(jLabel2);

        jLabel3.setFont(new Font("Segoe UI", Font.PLAIN, 20)); // Fuente ligeramente más grande
        jLabel3.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel3.setText("INTERNET DE ALTA VELOCIDAD – ¡Más que solo velocidad!");
        jLabel3.setForeground(new Color(30, 50, 70)); // Azul oscuro-gris para subtítulo
        backgroundPanel.add(jLabel3);

        // Características de los planes
        jLabel4.setFont(new Font("Segoe UI", Font.BOLD, 16));
        jLabel4.setText("💡 Todos nuestros planes incluyen:");
        jLabel4.setForeground(new Color(30, 50, 70)); // Azul oscuro-gris
        backgroundPanel.add(jLabel4);

        jLabel5.setText("📶 Router Wi-Fi GRATIS");
        jLabel5.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel5);

        jLabel6.setText("🛠️ Instalación sin costo y en menos de 24 horas");
        jLabel6.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel6);

        jLabel7.setText("📺 Compatible con Smart TVs, consolas y dispositivos móviles");
        jLabel7.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel7);

        jLabel8.setText("🔒 Control parental y filtros opcionales incluidos");
        jLabel8.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel8);

        jLabel9.setText("✅ Cambios de plan sin penalización");
        jLabel9.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel9);

        jLabel10.setText("✅ Atención personalizada en cada etapa");
        jLabel10.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel10);

        jLabel11.setText("✅ Facturación clara, sin cobros ocultos");
        jLabel11.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel11);

        jLabel12.setText("✅ Infraestructura moderna y cobertura nacional");
        jLabel12.setForeground(new Color(70, 100, 120)); // Azul más claro para legibilidad
        backgroundPanel.add(jLabel12);

        // Botones de planes (con carga de imagen robusta y estilo)
        // Botón Plan Básico
        setupPlanButton(jButton3, "/Imagenes/PBasico-removebg-preview.png", "Plan Básico");
        backgroundPanel.add(jButton3);

        // Botón Plan Estándar
        setupPlanButton(jButton1, "/Imagenes/PEstandar-removebg-preview (1).png", "Plan Estándar");
        backgroundPanel.add(jButton1);

        // Botón Plan Ultra
        setupPlanButton(jButton4, "/Imagenes/PUltra-removebg-preview (1).png", "Plan Ultra");
        backgroundPanel.add(jButton4);

        // Ajustar posiciones inicialmente (se llamará de nuevo en resize)
        adjustComponentPositions();
        pack();
    }

    // Método auxiliar para configurar botones de plan
    private void setupPlanButton(JButton button, String imagePath, String fallbackText) {
        try {
            ImageIcon planIcon = new ImageIcon(getClass().getResource(imagePath));
            if (planIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
                // Escalar la imagen si es necesario (ajusta el tamaño según tu diseño)
                Image img = planIcon.getImage();
                Image scaledImg = img.getScaledInstance(180, 180, Image.SCALE_SMOOTH); // Ejemplo de tamaño
                button.setIcon(new ImageIcon(scaledImg));
            } else {
                logger.log(Level.WARNING, "Imagen '{0}' no encontrada. Usando texto de marcador para el botón.", imagePath);
                button.setText(fallbackText);
            }
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error al cargar la imagen '{0}': {1}", new Object[]{imagePath, e.getMessage()});
            button.setText(fallbackText);
        }
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        // Puedes añadir un ActionListener aquí si cada botón de plan tiene una acción diferente
        // button.addActionListener(e -> JOptionPane.showMessageDialog(this, "Has seleccionado el " + fallbackText));
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        this.setVisible(false);
        if (parentFrame != null) {
            parentFrame.setVisible(true);
        }
    }

    private void adjustComponentPositions() {
        int newWidth = getContentPane().getWidth();
        int newHeight = getContentPane().getHeight();

        // Botón Regresar (más grande y más a la esquina)
        int regresarButtonWidth = 240; // Más grande
        int regresarButtonHeight = 65; // Más grande
        jButton2.setBounds(10, 10, regresarButtonWidth, regresarButtonHeight); // Más cerca de la esquina

        // Títulos principales
        int titleWidth = newWidth - 100; // Ajustar ancho
        jLabel1.setBounds(50, 90, titleWidth, 40); // Mover más abajo
        jLabel2.setBounds(50, jLabel1.getY() + jLabel1.getHeight(), titleWidth, 40);
        jLabel3.setBounds(50, jLabel2.getY() + jLabel2.getHeight() + 10, titleWidth, 30); // Añadir más espacio

        // Características de los planes (ajustar posiciones y espaciado)
        int featureCol1X = 50;
        int featureCol2X = newWidth / 2 + 30; // Segunda columna a la mitad + un poco
        int featureStartY = jLabel3.getY() + jLabel3.getHeight() + 30; // Debajo del subtítulo con más espacio
        int featureLineHeight = 28; // Espacio entre líneas de características

        // Posicionamiento de las etiquetas de características
        jLabel4.setBounds(featureCol1X, featureStartY, 400, 20); // Título de la sección
        jLabel5.setBounds(featureCol1X + 20, featureStartY + featureLineHeight, 300, 20);
        jLabel6.setBounds(featureCol1X + 20, featureStartY + (2 * featureLineHeight), 300, 20);
        jLabel7.setBounds(featureCol1X + 20, featureStartY + (3 * featureLineHeight), 380, 20);
        jLabel8.setBounds(featureCol1X + 20, featureStartY + (4 * featureLineHeight), 300, 20);
        
        // Segunda columna de características
        jLabel9.setBounds(featureCol2X, featureStartY + featureLineHeight, 300, 20);
        jLabel10.setBounds(featureCol2X, featureStartY + (2 * featureLineHeight), 300, 20);
        jLabel11.setBounds(featureCol2X, featureStartY + (3 * featureLineHeight), 300, 20);
        jLabel12.setBounds(featureCol2X, featureStartY + (4 * featureLineHeight), 300, 20);

        // Botones de planes (centrados y espaciados)
        int planButtonWidth = 200;
        int planButtonHeight = 200; // Aumentado para mejor visualización de iconos
        // Calcular la Y de los botones para que estén debajo de todas las características
        int lastFeatureY = Math.max(jLabel8.getY(), jLabel12.getY());
        int planButtonY = lastFeatureY + jLabel8.getHeight() + 50; // Espacio después de la última característica

        int gapBetweenButtons = 40;
        int totalButtonsWidth = (3 * planButtonWidth) + (2 * gapBetweenButtons);
        int startX = (newWidth - totalButtonsWidth) / 2;

        jButton3.setBounds(startX, planButtonY, planButtonWidth, planButtonHeight); // Básico
        jButton1.setBounds(startX + planButtonWidth + gapBetweenButtons, planButtonY, planButtonWidth, planButtonHeight); // Estándar
        jButton4.setBounds(startX + (2 * planButtonWidth) + (2 * gapBetweenButtons), planButtonY, planButtonWidth, planButtonHeight); // Ultra

        backgroundPanel.revalidate();
        backgroundPanel.repaint();
    }

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
        java.awt.EventQueue.invokeLater(() -> new Internet().setVisible(true));
    }
}
