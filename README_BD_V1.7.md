# NetNexus Ultra - Base de Datos V1.7 
## Instrucciones de Actualización y Uso

### 🔄 **PASOS PARA ACTUALIZAR LA BASE DE DATOS**

#### 1. **Ejecutar el Script SQL**
```sql
-- Ejecutar el archivo: Base_de_datos_actualizada_V1.7.sql
-- Este script recreará completamente la base de datos con los cambios optimizados
```

#### 2. **Verificar la Conexión**
- Asegúrate de que la aplicación Java se conecte a la base de datos `mydb`
- Usuario y contraseña según tu configuración de MySQL

### ✅ **CAMBIOS PRINCIPALES IMPLEMENTADOS**

#### **🛠️ Correcciones de Compatibilidad:**
1. **Métodos de Pago Corregidos**:
   - ❌ Antes: "Tarjeta de Crédito" (con tilde)
   - ✅ Ahora: "Tarjeta de Credito" (sin tilde) - Coincide exactamente con Java

2. **Tabla Factura Simplificada**:
   - ❌ Antes: Campo redundante `idMetodoPago`
   - ✅ Ahora: Solo `metodo_pago` como texto directo

3. **Campos de Hash Optimizados**:
   - `numero_tarjeta`: VARCHAR(64) para hash SHA-256 completo
   - `nombre_tarjeta`: VARCHAR(150) para hash SHA-256 completo
   - `cvv_tarjeta`: VARCHAR(64) para hash SHA-256 completo

#### **📊 Datos de Prueba Ampliados:**
- ✅ 10+ clientes de prueba con datos realistas
- ✅ Contratos existentes para pruebas
- ✅ Técnicos y vehículos funcionales
- ✅ Tipos de servicio completos

### 🎯 **VISTAS SQL CREADAS PARA FACILITAR CONSULTAS**

#### **1. Vista de Contratos Completos**
```sql
SELECT * FROM vista_contratos_completos 
WHERE cliente_email = 'usuario@email.com';
```

#### **2. Vista de Facturas Completas**
```sql
SELECT * FROM vista_facturas_completas 
WHERE cliente_nombre = 'Juan';
```

### 🔧 **USUARIOS DE PRUEBA DISPONIBLES**

| Email | Password | Tipo | ID |
|-------|----------|------|-----|
| admin@admin.admin | 123 | Administrador | 1 |
| gokussjcaicedo@capsulecorp.com | 12345 | Usuario | 1006936479 |
| jcarlos@gmail.com | 123 | Usuario | 123456 |
| maria.garcia@gmail.com | 123 | Usuario | 8563 |
| pedro.lopez@gmail.com | 123 | Usuario | 643986553 |

### 📱 **FLUJO DE PRUEBA RECOMENDADO**

1. **Login**: Usar `gokussjcaicedo@capsulecorp.com` / `12345`
2. **Navegar**: Servicios → Internet → Plan Ultra
3. **Facturar**: Completar proceso de pago
4. **Verificar**: Los datos se guardarán correctamente en la BD

### 🐛 **SOLUCIÓN A PROBLEMAS COMUNES**

#### **Error: "Método de pago no encontrado"**
- ✅ **Solución**: Ya corregido. Los métodos ahora coinciden exactamente

#### **Error: "No se guarda nada en la base de datos"**
- ✅ **Solución**: Estructura simplificada elimina dependencias complejas

#### **Error: "LoadingScreen se cuelga"**
- ✅ **Solución**: Manejo de excepciones mejorado en SwingWorker

### 📋 **VERIFICACIÓN POST-INSTALACIÓN**

#### **Consultar Datos Insertados:**
```sql
-- Ver últimos contratos creados
SELECT * FROM contrato ORDER BY idContrato DESC LIMIT 5;

-- Ver últimas facturas
SELECT * FROM factura ORDER BY idFactura DESC LIMIT 5;

-- Ver clientes disponibles
SELECT idCliente, nombre, apellido, email FROM cliente WHERE tipo = 'Usuario';
```

### 🚀 **FUNCIONALIDADES MEJORADAS**

1. **Inserción de Datos**:
   - Orden correcto: contrato → servicios → ticket → factura
   - Validación de claves foráneas automática
   
2. **Seguridad**:
   - Hashing SHA-256 para datos sensibles de tarjetas
   - Transacciones con rollback automático

3. **Performance**:
   - Índices optimizados
   - Vistas precalculadas para consultas frecuentes

### ⚠️ **NOTAS IMPORTANTES**

- **Backup**: Haz respaldo de tu base de datos actual antes de ejecutar el script
- **Charset**: La nueva BD usa `utf8mb4` para mejor compatibilidad
- **IDs Únicos**: Los IDs se generan automáticamente sin colisiones
- **Hash Seguro**: Los datos de tarjetas se almacenan hasheados (irreversibles)

### 📞 **SOPORTE**

Si encuentras algún problema:
1. Verifica que ejecutaste el script completo
2. Confirma que los métodos de pago coinciden
3. Revisa los logs de la aplicación Java para errores específicos
