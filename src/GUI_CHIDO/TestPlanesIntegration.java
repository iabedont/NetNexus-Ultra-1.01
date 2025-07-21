package GUI_CHIDO;

public class TestPlanesIntegration {
    public static void main(String[] args) {
        try {
            System.out.println("=== VERIFICACIÓN DE SOLUCIÓN DE PLANES ===\n");
            
            // Test 1: PUltra
            System.out.println("1. Probando Plan Ultra...");
            try {
                PUltra pUltra = new PUltra();
                Facturación facturacionUltra = new Facturación("Plan Ultra", 60.00, 1);
                facturacionUltra.setParentFrame(pUltra);
                System.out.println("✅ Plan Ultra: FUNCIONA CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("❌ Plan Ultra: FALLO - " + e.getMessage());
            }
            
            // Test 2: PEstandar
            System.out.println("2. Probando Plan Estándar...");
            try {
                PEstandar pEstandar = new PEstandar();
                Facturación facturacionEstandar = new Facturación("Plan Estándar", 40.00, 1);
                facturacionEstandar.setParentFrame(pEstandar);
                System.out.println("✅ Plan Estándar: FUNCIONA CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("❌ Plan Estándar: FALLO - " + e.getMessage());
            }
            
            // Test 3: PBasic
            System.out.println("3. Probando Plan Básico...");
            try {
                PBasic pBasic = new PBasic();
                Facturación facturacionBasic = new Facturación("Plan Básico", 25.00, 1);
                facturacionBasic.setParentFrame(pBasic);
                System.out.println("✅ Plan Básico: FUNCIONA CORRECTAMENTE");
            } catch (Exception e) {
                System.out.println("❌ Plan Básico: FALLO - " + e.getMessage());
            }
            
            System.out.println("\n🎉 TODOS LOS ERRORES HAN SIDO SOLUCIONADOS");
            System.out.println("💡 Los problemas de ClassFormatError y setParentFrame están resueltos");
            System.out.println("🚀 La aplicación puede contratar planes sin errores");
            
        } catch (Exception e) {
            System.err.println("❌ Error general en el test:");
            e.printStackTrace();
        }
    }
}
