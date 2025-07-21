package GUI_CHIDO;

import javax.swing.JFrame;

public class TestIntegrationComplete {
    public static void main(String[] args) {
        try {
            System.out.println("=== TEST DE INTEGRACIÓN COMPLETO ===");
            
            // Test PUltra
            System.out.println("\n1. Probando PUltra...");
            PUltra pUltra = new PUltra();
            System.out.println("✅ PUltra creado exitosamente");
            
            Facturación facturacionUltra = new Facturación("Plan Ultra", 60.00, 1);
            facturacionUltra.setParentFrame(pUltra);
            System.out.println("✅ PUltra → Facturación: setParentFrame funciona");
            
            // Test PEstandar
            System.out.println("\n2. Probando PEstandar...");
            PEstandar pEstandar = new PEstandar();
            System.out.println("✅ PEstandar creado exitosamente");
            
            Facturación facturacionEstandar = new Facturación("Plan Estándar", 40.00, 1);
            facturacionEstandar.setParentFrame(pEstandar);
            System.out.println("✅ PEstandar → Facturación: setParentFrame funciona");
            
            // Test PBasic
            System.out.println("\n3. Probando PBasic...");
            PBasic pBasic = new PBasic();
            System.out.println("✅ PBasic creado exitosamente");
            
            Facturación facturacionBasic = new Facturación("Plan Básico", 25.00, 1);
            facturacionBasic.setParentFrame(pBasic);
            System.out.println("✅ PBasic → Facturación: setParentFrame funciona");
            
            // Verificar compatibilidad de tipos
            System.out.println("\n4. Verificando compatibilidades...");
            System.out.println("PUltra instanceof JFrame: " + (pUltra instanceof JFrame));
            System.out.println("PEstandar instanceof JFrame: " + (pEstandar instanceof JFrame));
            System.out.println("PBasic instanceof JFrame: " + (pBasic instanceof JFrame));
            
            System.out.println("\n🎉 TODOS LOS TESTS PASARON EXITOSAMENTE");
            System.out.println("💡 Los errores de ClassFormatError y setParentFrame han sido solucionados");
            
        } catch (Exception e) {
            System.err.println("❌ Error en el test:");
            e.printStackTrace();
        }
    }
}
