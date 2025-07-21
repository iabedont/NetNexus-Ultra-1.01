package Clases;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

/**
 * Utilidad para el manejo seguro de datos de tarjetas de crédito
 */
public class CardSecurityUtil {
    
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES";
    
    // Clave fija para demostración - en producción debería estar en un lugar seguro
    private static final String SECRET_KEY = "NetNexusUltraKey"; // 16 chars for AES-128
    
    /**
     * Genera un hash SHA-256 de un número de tarjeta
     * @param cardNumber El número completo de la tarjeta
     * @return Hash SHA-256 del número de tarjeta
     */
    public static String hashCardNumber(String cardNumber) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(cardNumber.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error al generar hash de tarjeta", e);
        }
    }
    
    /**
     * Encripta información sensible de la tarjeta
     * @param data Datos a encriptar
     * @return Datos encriptados en Base64
     */
    public static String encrypt(String data) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypted = cipher.doFinal(data.getBytes());
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al encriptar datos", e);
        }
    }
    
    /**
     * Desencripta información de la tarjeta
     * @param encryptedData Datos encriptados en Base64
     * @return Datos desencriptados
     */
    public static String decrypt(String encryptedData) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decoded = Base64.getDecoder().decode(encryptedData);
            byte[] decrypted = cipher.doFinal(decoded);
            return new String(decrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error al desencriptar datos", e);
        }
    }
    
    /**
     * Obtiene los últimos 4 dígitos de un número de tarjeta
     * @param cardNumber Número completo de la tarjeta
     * @return Últimos 4 dígitos
     */
    public static String getLastFourDigits(String cardNumber) {
        if (cardNumber == null || cardNumber.length() < 4) {
            return "****";
        }
        return cardNumber.substring(cardNumber.length() - 4);
    }
    
    /**
     * Formatea un número de tarjeta mostrando solo los últimos 4 dígitos
     * @param cardNumber Número completo de la tarjeta
     * @return Formato **** **** **** 1234
     */
    public static String formatCardNumberMasked(String cardNumber) {
        String lastFour = getLastFourDigits(cardNumber);
        return "**** **** **** " + lastFour;
    }
    
    /**
     * Valida el formato básico de un número de tarjeta (solo dígitos y longitud)
     * @param cardNumber Número de tarjeta a validar
     * @return true si el formato es válido
     */
    public static boolean isValidCardNumberFormat(String cardNumber) {
        if (cardNumber == null) return false;
        
        // Remover espacios y guiones
        String cleanNumber = cardNumber.replaceAll("[\\s-]", "");
        
        // Verificar que solo contenga dígitos
        if (!cleanNumber.matches("\\d+")) return false;
        
        // Verificar longitud (13-19 dígitos según estándares de la industria)
        return cleanNumber.length() >= 13 && cleanNumber.length() <= 19;
    }
    
    /**
     * Valida el formato de fecha de vencimiento (MM/YY o MM/YYYY)
     * @param expiryDate Fecha de vencimiento
     * @return true si el formato es válido
     */
    public static boolean isValidExpiryDateFormat(String expiryDate) {
        if (expiryDate == null) return false;
        
        // Formato MM/YY o MM/YYYY
        return expiryDate.matches("^(0[1-9]|1[0-2])/(\\d{2}|\\d{4})$");
    }
    
    /**
     * Valida el formato de CVV (3 o 4 dígitos)
     * @param cvv Código de seguridad
     * @return true si el formato es válido
     */
    public static boolean isValidCVVFormat(String cvv) {
        if (cvv == null) return false;
        return cvv.matches("^\\d{3,4}$");
    }
}
