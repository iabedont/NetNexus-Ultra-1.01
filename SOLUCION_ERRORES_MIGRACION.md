# 🔧 Guía de Solución de Errores - Migración de Datos Ibarra

## 🚨 Errores Comunes y Soluciones

### ❌ **Error 1: "USE mydb" - Syntax Error**
**Problema:** El comando USE no funciona en algunos editores SQL
**Solución:**
```sql
-- En lugar de usar el script con USE mydb
-- Conectarse directamente a la base de datos:
mysql -u root -p mydb < migracion_ibarra_simple.sql
```

### ❌ **Error 2: "Foreign Key Constraint Fails"**
**Problema:** Restricciones de claves foráneas
**Solución:**
```sql
SET FOREIGN_KEY_CHECKS = 0;
-- Ejecutar inserts
SET FOREIGN_KEY_CHECKS = 1;
```

### ❌ **Error 3: "Table doesn't exist"**
**Problema:** Tabla no existe en la base de datos
**Solución:**
1. Verificar que la base de datos mydb existe
2. Verificar que todas las tablas están creadas
3. Ejecutar primero el script de estructura de base de datos

### ❌ **Error 4: "Duplicate entry for key"**
**Problema:** Datos duplicados en campos únicos
**Solución:**
```sql
-- Limpiar datos existentes primero
TRUNCATE TABLE cliente;
-- Luego insertar nuevos datos
```

### ❌ **Error 5: "AUTO_INCREMENT syntax error"**
**Problema:** Sintaxis de AUTO_INCREMENT no compatible
**Solución:** Usar el script simple sin ALTER TABLE AUTO_INCREMENT

## 📋 **Scripts Disponibles (en orden de recomendación)**

### 1. 🥇 **migracion_ibarra_simple.sql** (RECOMENDADO)
- ✅ Sintaxis compatible con todas las versiones de MySQL
- ✅ Sin comandos problemáticos (USE, ALTER AUTO_INCREMENT)
- ✅ Inserción directa con VALUES
- ✅ Limpieza con TRUNCATE

### 2. 🥈 **Base_Datos_Ibarra_CORREGIDO.sql**
- ⚠️ Versión completa con verificaciones
- ⚠️ Puede tener problemas de compatibilidad
- ✅ Incluye verificaciones y estadísticas

### 3. 🥉 **Base_Datos_Ibarra_V2.0.sql** (ORIGINAL)
- ❌ Puede tener errores de sintaxis
- ❌ No recomendado para uso directo

## 🛠️ **Métodos de Ejecución**

### **Método 1: Línea de Comandos (RECOMENDADO)**
```bash
# Navegar al directorio
cd "ruta_del_proyecto"

# Ejecutar script simple
mysql -u root -p mydb < migracion_ibarra_simple.sql

# Verificar resultados
mysql -u root -p mydb -e "SELECT tipo, COUNT(*) FROM cliente GROUP BY tipo;"
```

### **Método 2: MySQL Workbench**
1. Abrir MySQL Workbench
2. Conectar a la base de datos
3. Seleccionar la base de datos `mydb`
4. Copiar y pegar el contenido de `migracion_ibarra_simple.sql`
5. Ejecutar script completo

### **Método 3: phpMyAdmin**
1. Acceder a phpMyAdmin
2. Seleccionar base de datos `mydb`
3. Ir a la pestaña "SQL"
4. Importar archivo `migracion_ibarra_simple.sql`

### **Método 4: Batch Automático**
```bash
# Ejecutar el instalador automático
instalar_datos_corregido.bat

# Seleccionar opción 1 (script simple)
```

## 🔍 **Verificación Post-Instalación**

### **Verificar que los datos se insertaron correctamente:**
```sql
-- Verificar total de clientes
SELECT COUNT(*) as Total_Clientes FROM cliente;

-- Verificar distribución por tipo
SELECT tipo, COUNT(*) as cantidad FROM cliente GROUP BY tipo;

-- Verificar administradores
SELECT nombre, apellido, email FROM administradores;

-- Verificar técnicos
SELECT nombre, apellido, especialidad FROM tecnicos;

-- Verificar usuarios con servicios
SELECT c.nombre, c.apellido, co.tipoServicio, u.direccion 
FROM cliente c
JOIN contrato co ON c.idCliente = co.Cliente_idCliente
JOIN servicios s ON co.idContrato = s.Contrato_idContrato
JOIN ubicacion u ON s.idServicios = u.Servicios_idServicios
WHERE c.tipo = 'Usuario';
```

### **Resultado Esperado:**
```
Total_Clientes: 15
Administrador: 3
Técnico: 5  
Usuario: 7
```

## 🆘 **Solución de Problemas Específicos**

### **Si aparece "Access denied for user":**
```bash
# Verificar credenciales de MySQL
mysql -u root -p

# Si no funciona, resetear contraseña de MySQL
```

### **Si aparece "Unknown database 'mydb'":**
```sql
-- Crear la base de datos primero
CREATE DATABASE mydb;
USE mydb;

-- Luego ejecutar el script de estructura
-- Finalmente ejecutar el script de datos
```

### **Si aparece "Column count doesn't match value count":**
- Usar el script `migracion_ibarra_simple.sql` que especifica todos los valores
- Verificar que la estructura de tablas coincida

### **Si hay problemas con caracteres especiales:**
```sql
-- Configurar codificación
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
```

## 📞 **Contacto de Soporte**

Si persisten los problemas:
1. Verificar versión de MySQL: `SELECT VERSION();`
2. Verificar permisos de usuario en la base de datos
3. Intentar con el script más simple primero
4. Revisar logs de error de MySQL

## ✅ **Datos Finales Confirmados**

Una vez completada la migración tendrás:

### **👨‍💼 Administradores:**
- María Elena Yépez Rueda
- Carlos Alberto Morales Intriago  
- Ana Lucía Santacruz Flores

### **👨‍🔧 Técnicos:**
- Jorge Luis Valenzuela Pozo (Redes y Fibra Óptica)
- Patricia Elizabeth Cadena Guerrero (Instalaciones WiFi)
- Miguel Andrés Rosero Benítez (Telecomunicaciones)
- Silvia Maribel Ruiz Moncayo (Soporte Técnico)
- Edison Ramiro Terán Villarreal (Mantenimiento)

### **👤 Usuarios:**
- Rosa María Chávez Montenegro (La Victoria)
- Fabián Oswaldo Imbaquingo Yar (El Ejido)
- Carmen Dolores Andrade Paredes (Azaya)
- Roberto Carlos Maldonado Enríquez (Alpachaca)
- Luz Marina Bolaños Cevallos (Caranqui)
- Hernán Patricio Vásquez Calderón (La Esperanza)
- Gloria Esperanza Cuasapaz Malte (Yahuarcocha)

**🎯 ¡Todos con datos realistas de Ibarra, Imbabura, Ecuador!**
