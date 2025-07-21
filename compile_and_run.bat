@echo off
echo Limpiando archivos de compilacion previos...
if exist "build\classes\Clases\*.class" del /Q "build\classes\Clases\*.class"
if exist "build\classes\GUI_CHIDO\*.class" del /Q "build\classes\GUI_CHIDO\*.class"

echo Compilando el proyecto...
javac -cp "libraries\mysql-connector-j-9.3.0.jar" -d "build\classes" src\Clases\*.java src\GUI_CHIDO\*.java

echo Verificando compilacion...
if exist "build\classes\Clases\Main.class" (
    echo Compilacion exitosa!
    echo Ejecutando aplicacion...
    java -cp "build\classes;libraries\mysql-connector-j-9.3.0.jar" Clases.Main
) else (
    echo Error en la compilacion!
    pause
)
