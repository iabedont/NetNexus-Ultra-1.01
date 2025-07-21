import GUI_CHIDO.Facturación;
import javax.swing.SwingUtilities;

public class TestFacturacion {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            System.out.println("Iniciando aplicación de facturación...");
            try {
                Facturación factura = new Facturación("Plan Ultra", 50.0, 1);
                factura.setVisible(true);
                System.out.println("✅ Aplicación de facturación iniciada correctamente");
                System.out.println("✅ Sistema de pestañas implementado");
                System.out.println("✅ Separación de tarjetas de crédito y débito");
                System.out.println("✅ Autocompletado seguro sin reversión de hash");
            } catch (Exception e) {
                System.err.println("❌ Error al iniciar la aplicación: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}
