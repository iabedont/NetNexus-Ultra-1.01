/*
 * EJEMPLO DE INTEGRACIÓN: Cómo agregar recomendaciones a la ventana de servicios
 * Este código muestra cómo modificar Servicios_V15.java para incluir el panel de recomendaciones
 */

// 1. IMPORTAR LA NUEVA CLASE
import GUI_CHIDO.PanelRecomendaciones;

// 2. AGREGAR CAMPO PRIVADO EN LA CLASE
public class Servicios_V15 extends JFrame {
    // ... campos existentes ...
    private PanelRecomendaciones panelRecomendaciones;
    
    // 3. MODIFICAR EL MÉTODO initComponents()
    private void initComponents() {
        setTitle("NetNexus Ultra - Servicios Disponibles");
        setSize(1400, 700); // Aumentar ancho para el panel de recomendaciones
        // ... resto del código existente ...
        
        // Inicializar panel de recomendaciones
        panelRecomendaciones = new PanelRecomendaciones(this, currentClienteId);
    }
    
    // 4. MODIFICAR EL MÉTODO setupLayout()
    private void setupLayout() {
        // Layout existente para servicios (lado izquierdo)
        JPanel panelServicios = new JPanel();
        panelServicios.setLayout(null);
        panelServicios.setOpaque(false);
        panelServicios.setBounds(50, 50, 800, 600);
        
        // Agregar todos los componentes existentes al panel de servicios
        panelServicios.add(jLabelTitulo);
        panelServicios.add(jLabelDescripcion);
        panelServicios.add(jButtonInternet);
        panelServicios.add(jButtonTelefonia);
        panelServicios.add(jButtonTelevision);
        panelServicios.add(jButtonPaquetes);
        panelServicios.add(jButtonVolver);
        
        // Agregar panel de recomendaciones (lado derecho)
        panelRecomendaciones.setBounds(870, 50, 480, 600);
        
        // Agregar ambos paneles al frame principal
        add(panelServicios);
        add(panelRecomendaciones);
    }
    
    // 5. ACTUALIZAR RECOMENDACIONES DESPUÉS DE CONTRATAR UN SERVICIO
    private void abrirInternet() {
        try {
            Internet internet = new Internet(this, currentClienteId);
            internet.setVisible(true);
            this.setVisible(false);
            
            // Actualizar recomendaciones cuando regrese a esta ventana
            // (se puede llamar en el método de callback o en windowActivated)
            panelRecomendaciones.actualizarRecomendaciones();
            
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error abriendo Internet", e);
            mostrarErrorYVolver("Error al abrir servicios de internet");
        }
    }
}

/*
 * EJEMPLO DE USO EN OTRAS VENTANAS
 */

// En cualquier JFrame que quiera mostrar recomendaciones:
public class MiVentana extends JFrame {
    
    private void agregarPanelRecomendaciones() {
        PanelRecomendaciones panel = new PanelRecomendaciones(this, clienteId);
        
        // Opción 1: Como panel lateral
        panel.setBounds(800, 50, 400, 500);
        add(panel);
        
        // Opción 2: Como pestaña en un JTabbedPane
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Servicios", panelServicios);
        tabs.addTab("Recomendaciones", panel);
        
        // Opción 3: Como diálogo modal
        JDialog dialog = new JDialog(this, "Recomendaciones", true);
        dialog.add(panel);
        dialog.setSize(500, 600);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }
}

/*
 * PERSONALIZACIÓN AVANZADA
 */

// Crear panel de recomendaciones con configuración específica
PanelRecomendaciones panel = new PanelRecomendaciones(parentFrame, clienteId) {
    @Override
    protected void mostrarDetallesServicio(String[] servicio) {
        // Comportamiento personalizado al hacer clic en un servicio
        String categoria = servicio[2];
        
        switch (categoria.toLowerCase()) {
            case "internet":
                abrirInternetDirecto(servicio);
                break;
            case "telefonia":
                abrirTelefoniaDirecto(servicio);
                break;
            default:
                super.mostrarDetallesServicio(servicio);
        }
    }
};

/*
 * INTEGRACIÓN CON SISTEMA DE EVENTOS
 */

// Escuchar cuando se actualicen las calificaciones
public class ServiciosConRecomendaciones extends Servicios_V15 {
    
    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        
        if (visible && panelRecomendaciones != null) {
            // Actualizar recomendaciones cada vez que se muestre la ventana
            SwingUtilities.invokeLater(() -> {
                panelRecomendaciones.actualizarRecomendaciones();
            });
        }
    }
    
    // Método para llamar después de contratar un servicio
    public void servicioContratado() {
        if (panelRecomendaciones != null) {
            panelRecomendaciones.actualizarRecomendaciones();
        }
        
        // Mostrar mensaje de agradecimiento
        JOptionPane.showMessageDialog(this,
            "¡Gracias por contratar nuestro servicio!\n\n" +
            "Te invitamos a calificarlo una vez que lo hayas usado.\n" +
            "Tu opinión ayuda a otros usuarios a tomar mejores decisiones.",
            "Servicio Contratado",
            JOptionPane.INFORMATION_MESSAGE);
    }
}

/*
 * NOTAS DE IMPLEMENTACIÓN:
 * 
 * 1. El PanelRecomendaciones es completamente independiente y reutilizable
 * 2. Se puede integrar en cualquier JFrame o JDialog
 * 3. Actualiza automáticamente los datos desde la base de datos
 * 4. Es responsivo y se adapta al tamaño asignado
 * 5. Maneja errores de conexión de forma elegante
 * 
 * BENEFICIOS:
 * - Mejora la experiencia del usuario
 * - Aumenta las ventas al mostrar servicios populares
 * - Proporciona transparencia y confianza
 * - Facilita la toma de decisiones informadas
 */
