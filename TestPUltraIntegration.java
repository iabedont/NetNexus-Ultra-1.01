package GUI_CHIDO;

import javax.swing.JFrame;

public class TestPUltraIntegration {
    public static void main(String[] args) {
        try {
            System.out.println("Iniciando test de integración PUltra...");
            
            // Crear instancia de PUltra (similar a como se haría en la aplicación real)
            PUltra pUltra = new PUltra();
            System.out.println("✅ PUltra creado exitosamente");
            
            // Crear instancia de Facturación (esto es lo que hace el botón)
            Facturación facturacion = new Facturación("Plan Ultra", 60.00, 1);
            System.out.println("✅ Facturación creado exitosamente");
            
            // Probar la llamada setParentFrame (esto es lo que falla)
            facturacion.setParentFrame(pUltra);
            System.out.println("✅ setParentFrame funcionó correctamente");
            
            // Mostrar información sobre las clases
            System.out.println("PUltra es instancia de JFrame: " + (pUltra instanceof JFrame));
            System.out.println("Método setParentFrame disponible: " + 
                java.util.Arrays.stream(facturacion.getClass().getMethods())
                .anyMatch(m -> m.getName().equals("setParentFrame")));
            
            System.out.println("🎉 Test completado exitosamente - No hay problemas de compatibilidad");
            
        } catch (Exception e) {
            System.err.println("❌ Error en el test:");
            e.printStackTrace();
        }
    }
}
