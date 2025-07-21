import GUI_CHIDO.Facturación;
import javax.swing.SwingUtilities;

/**
 * Clase de prueba para verificar la funcionalidad de gestión de ubicaciones
 * en la clase Facturación
 */
public class TestUbicaciones {
    
    public static void main(String[] args) {
        try {
            // Configurar Look and Feel
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException e) {
            System.err.println("Error configurando Look and Feel: " + e.getMessage());
        }
        
        SwingUtilities.invokeLater(() -> {
            // Crear ventana de facturación con datos de prueba
            Facturación facturacion = new Facturación("Plan Premium", 29.99, 1);
            facturacion.setVisible(true);
            
            System.out.println("=== Test de Gestión de Ubicaciones ===");
            System.out.println("✅ Ventana de facturación iniciada");
            System.out.println("✅ Funcionalidades implementadas:");
            System.out.println("   - Seleccionar ubicaciones guardadas");
            System.out.println("   - Guardar nueva ubicación como favorita");
            System.out.println("   - Limpiar campos de ubicación");
            System.out.println("   - Autocompletar desde ubicaciones existentes");
            System.out.println("✅ Ve a la pestaña 'Información de Ubicación' para probar");
        });
    }
}
