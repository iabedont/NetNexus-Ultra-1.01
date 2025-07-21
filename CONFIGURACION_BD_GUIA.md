# 🔧 GUÍA DE CONFIGURACIÓN - NetNexus Ultra V1.5.1

## ❗ **PROBLEMA ACTUAL:**
MySQL no está ejecutándose en tu sistema.

## 🎯 **SOLUCIONES DISPONIBLES:**

### **OPCIÓN 1: Instalar y Configurar MySQL (RECOMENDADO)**

#### **Para XAMPP:**
1. Descarga XAMPP desde: https://www.apachefriends.org/
2. Instala XAMPP
3. Abre el Panel de Control de XAMPP
4. Inicia el servicio "MySQL"
5. Ve a "Admin" para abrir phpMyAdmin
6. Ejecuta el script: `NetNexus_Ultra_FINAL_SIN_ERRORES.sql`

#### **Para MySQL Standalone:**
1. Descarga MySQL desde: https://dev.mysql.com/downloads/installer/
2. Instala MySQL con contraseña "root" o sin contraseña
3. Inicia el servicio MySQL
4. Abre MySQL Workbench
5. Ejecuta el script: `NetNexus_Ultra_FINAL_SIN_ERRORES.sql`

### **OPCIÓN 2: Usar Base de Datos en Memoria (TEMPORAL)**

Si quieres probar la aplicación SIN instalar MySQL:

#### **Paso 1:** Descarga H2 Database
```
https://h2database.com/html/download.html
```

#### **Paso 2:** Copia el archivo `h2-x.x.x.jar` a la carpeta `libraries/`

#### **Paso 3:** Modifica `Main.java` para usar el fallback:
```java
// Cambiar esta línea en Main.java:
DatabaseConnection.getConnection();
// Por esta:
DatabaseConnectionFallback.getConnection();
```

### **OPCIÓN 3: Configuración Manual Rápida**

Si tienes MySQL instalado pero no está iniciando:

#### **Windows:**
```cmd
net start mysql80
# o
net start mysql
# o buscar en Servicios de Windows
```

#### **Verificar Puerto:**
```cmd
netstat -an | findstr :3306
```

#### **Contraseñas Comunes a Probar:**
- Sin contraseña (campo vacío)
- "root"
- "admin" 
- "password"
- "123456"

## 🚀 **COMANDOS PARA PROBAR:**

### **Después de configurar MySQL:**
```cmd
# Compilar:
javac -cp "libraries\*;." -d . src\Clases\*.java

# Ejecutar:
java -cp "libraries\*;." Clases.Main
```

### **Para usar H2 (fallback):**
```cmd
# Compilar con H2:
javac -cp "libraries\*;." -d . src\Clases\DatabaseConnectionFallback.java

# Modificar Main.java para usar DatabaseConnectionFallback
# Luego ejecutar:
java -cp "libraries\*;." Clases.Main
```

## 📋 **VERIFICACIÓN:**

### **✅ MySQL Funcionando:**
```
INFO: Driver MySQL cargado exitosamente
INFO: Conexión MySQL establecida con contraseña: [sin contraseña]
```

### **✅ H2 Funcionando (Fallback):**
```
INFO: Conexión H2 (en memoria) establecida como fallback
INFO: Base de datos H2 inicializada con datos de prueba
```

## 🎯 **RECOMENDACIÓN:**

**Para desarrollo:** Usa XAMPP (más fácil)
**Para producción:** Instala MySQL standalone
**Para pruebas rápidas:** Usa H2 fallback

---

**¿Cuál opción prefieres? Te ayudo a configurar la que elijas.** 🚀
